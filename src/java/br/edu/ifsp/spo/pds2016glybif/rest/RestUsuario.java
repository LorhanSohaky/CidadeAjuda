package br.edu.ifsp.spo.pds2016glybif.rest;

import br.edu.ifsp.spo.pds2016glybif.banco.Conexao;
import br.edu.ifsp.spo.pds2016glybif.banco.Conexao.Status;
import br.edu.ifsp.spo.pds2016glybif.banco.UsuarioDAO;
import br.edu.ifsp.spo.pds2016glybif.modelo.Usuario;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.mail.MessagingException;
import javax.naming.NamingException;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/**
 * Classe Rest do usuário.
 *
 * @author Lorhan Sohaky
 */
@Path("/usuario")
public class RestUsuario {

    private final Conexao BDD;
    private final UsuarioDAO DAO;
    private static final Logger LOGGER = Logger.getLogger(RestUsuario.class.getName());

    /**
     * Método construtor de RestUsuario.
     *
     * @throws IOException Erro.
     * @throws NamingException Erro.
     * @throws SQLException Erro SQL.
     */
    public RestUsuario() throws IOException, NamingException, SQLException {
        BDD = new Conexao();
        DAO = new UsuarioDAO(BDD.getConexao());
    }

    /**
     * Rest para cadastro de usuário.
     *
     * @param user Objeto usuário.
     * @return Status da execução.
     */
    @POST
    @Path("/cadastro")
    @Consumes(MediaType.TEXT_XML)
    @Produces(MediaType.TEXT_PLAIN)
    public String cadastroUsuario(Usuario user) {
        user.setEmail(user.getEmail().toLowerCase(Locale.getDefault()));
        Status resultado = null;
        try {
            resultado = DAO.inserirUsuario(user);
        } catch (SQLException ex) {
            LOGGER.log(Level.INFO, "Erro ao cadastrar usuário.", ex);
        } finally {
            BDD.close();
            DAO.close();
        }

        if (resultado != Status.OK) {
            return String.valueOf(resultado);
        }
        return String.valueOf(Status.OK);
    }

    /**
     * Rest para fazer login.
     *
     * @param email E-mail do usuário.
     * @param senha Senha do usuário.
     * @return Objeto usuário.
     */
    @GET
    @Produces(MediaType.APPLICATION_XML)
    @Path("/login/{email}/{senha}")
    public Usuario getUsuario(@PathParam("email") String email, @PathParam("senha") String senha) {
        email = email.toLowerCase(Locale.getDefault());
        Usuario user = null;
        try {
            user = DAO.login(email, senha);
        } catch (SQLException ex) {
            LOGGER.log(Level.INFO, "Erro ao pegar informações do usuário.", ex);
        }
        BDD.close();
        DAO.close();
        if (user != null) {
            return user;
        }
        return null;
    }

    /**
     * Rest para enviar uma mensagem ao e-mail do usuário.
     *
     * @param email Endereço de e-mail do usuário.
     * @param data Data de nascimento do usuário. Utilizada para trazer maior
     * segurança.
     * @param url URL na qual o usuário irá clicar para poder alterar a senha.
     * @return Resultado da execução.
     */
    @POST
    @Produces(MediaType.TEXT_PLAIN)
    @Consumes(MediaType.TEXT_PLAIN)
    @Path("/mandaremail/{email}/{data}")
    public String enviarEmail(@PathParam("email") String email, @PathParam("data") String data, String url) {
        Status stu = Status.ERRO;
        try {
            Status verificador = DAO.verificaEmilEDataNascimento(email, data);
            if (verificador == Status.DIFERENTE) {
                return String.valueOf(Status.DIFERENTE);
            }
            stu = DAO.enviarEmail(email, url);
        } catch (SQLException | MessagingException ex) {
            LOGGER.log(Level.INFO, "Erro ao enviar e-mail.", ex);
        } finally {
            BDD.close();
            DAO.close();
        }
        return String.valueOf(stu);
    }

    /**
     * Rest para mudar a senha do usuário.
     *
     * @param id Número de identificação do usuário.
     * @param senha Nova senha a ser alterada no banco de dados.
     * @return Resultado da execução.
     */
    @PUT
    @Produces(MediaType.TEXT_PLAIN)
    @Path("/mudarsenha/{id}/{senha}")
    public String mudarSenha(@PathParam("id") long id, @PathParam("senha") String senha) {
        try {
            DAO.mudarSenha(id, senha);
            BDD.close();
            return String.valueOf(Status.OK);
        } catch (SQLException ex) {
            LOGGER.log(Level.INFO, "Erro ao mudar a senha.", ex);
            return String.valueOf(Status.ERRO);
        }
    }
}
