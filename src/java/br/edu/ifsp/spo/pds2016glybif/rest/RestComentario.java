package br.edu.ifsp.spo.pds2016glybif.rest;

import br.edu.ifsp.spo.pds2016glybif.banco.ComentarioDAO;
import br.edu.ifsp.spo.pds2016glybif.banco.Conexao;
import br.edu.ifsp.spo.pds2016glybif.banco.Conexao.Status;
import br.edu.ifsp.spo.pds2016glybif.modelo.Comentario;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.naming.NamingException;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/**
 * Classe que faz intermédio dos dados recebidos via HTTP com o comentário.
 *
 * @author Lorhan Sohaky
 */
@Path("/comentario")
public class RestComentario {

    private final Conexao BDD;
    private final ComentarioDAO DAO;
    private static final Logger LOGGER = Logger.getLogger(RestComentario.class.getName());

    /**
     * Método construtor da RestComentario.
     *
     * @throws NamingException Erro
     * @throws SQLException Erro
     */
    public RestComentario() throws NamingException, SQLException {
        BDD = new Conexao();
        DAO = new ComentarioDAO(BDD.getConexao());
    }

    /**
     * Rest para registro do comentário.
     *
     * @param com Objeto Comentario
     * @return Status da execução.
     */
    @POST
    @Path("/registrar")
    @Consumes(MediaType.TEXT_XML)
    @Produces(MediaType.TEXT_PLAIN)
    public String registrar(Comentario com) {

        Status stu = Status.ERRO;
        try {
            stu = DAO.inserirComentario(com);
        } catch (SQLException ex) {
            LOGGER.log(Level.INFO, "Erro ao registrar o comentário.", ex);
        } finally {
            DAO.close();
            BDD.close();
        }
        return String.valueOf(stu);
    }

    /**
     * Rest para listar os comentários de uma ocorrência.
     *
     * @param pkOcorrencia Número de identificação da ocorrência.
     * @param qtdLidos Quantidade de comentários lidos.
     * @return Lista de comentários.
     */
    @GET
    @Path("/lista/{pkOcorrencia}/{qtdLidos}")
    @Produces(MediaType.TEXT_XML)
    public List<Comentario> lista(@PathParam("pkOcorrencia") long pkOcorrencia, @PathParam("qtdLidos") int qtdLidos) {
        try {
            return DAO.listaComentario(pkOcorrencia, qtdLidos);
        } catch (SQLException ex) {
            LOGGER.log(Level.INFO, "Erro ao listar os comentários.", ex);
        } finally {
            DAO.close();
            BDD.close();
        }
        return null;
    }
}
