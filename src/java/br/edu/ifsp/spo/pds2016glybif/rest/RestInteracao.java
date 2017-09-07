package br.edu.ifsp.spo.pds2016glybif.rest;

import br.edu.ifsp.spo.pds2016glybif.banco.Conexao;
import br.edu.ifsp.spo.pds2016glybif.banco.InteracaoDAO;
import br.edu.ifsp.spo.pds2016glybif.modelo.Interacao;
import java.sql.SQLException;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.naming.NamingException;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/**
 * Classe que faz intermédio dos dados recebidos via HTTP com a interação.
 *
 * @author Lorhan Sohaky
 */
@Path("/interacao")
public class RestInteracao {

    private final Conexao BDD;
    private final InteracaoDAO DAO;
    private static final Logger LOGGER = Logger.getLogger(RestInteracao.class.getName());

    /**
     * Método construtor da RestInteracao.
     *
     * @throws NamingException Erro
     * @throws SQLException Erro
     */
    public RestInteracao() throws NamingException, SQLException {
        BDD = new Conexao();
        DAO = new InteracaoDAO(BDD.getConexao());
    }

    /**
     * Rest para registro da interação.
     *
     * @param resposta Resposta do usuário.
     * @param fkOcorrencia Número de identificação da ocorrência.
     * @param fkUsuario Número de identificação do usuário.
     * @return Status da execução.
     */
    @PUT
    @Path("/registrar/{resposta}/{usuario}/{ocorrencia}")
    @Produces(MediaType.TEXT_PLAIN)
    public String registrar(@PathParam("resposta") String resposta, @PathParam("ocorrencia") long fkOcorrencia, @PathParam("usuario") long fkUsuario) {
        Interacao inte = new Interacao(resposta.toUpperCase(Locale.getDefault()), fkOcorrencia, fkUsuario);
        String stu = "";
        try {
            stu = String.valueOf(DAO.inserirInteracao(inte));
        } catch (SQLException ex) {
            LOGGER.log(Level.INFO, "Erro ao registrar a interação.", ex);
        } finally {
            BDD.close();
            DAO.close();
        }
        return stu;
    }

    /**
     * Rest para pegar as informações da útima interação de determinado usuário
     * para uma determinada ocorrencia.
     *
     * @param fkOcorrencia Número de identificação da ocorrência.
     * @param fkUsuario Número de identificação do usuário.
     * @return Última interação do usuário.
     */
    @GET
    @Path("/getultimainteracao/{fkOcorrencia}/{fkUsuario}")
    @Produces(MediaType.TEXT_PLAIN)
    public String getUltimaInteracao(@PathParam("fkOcorrencia") long fkOcorrencia, @PathParam("fkUsuario") long fkUsuario) {
        String ret = "";
        try {
            ret = DAO.getUltimaInteracao(fkOcorrencia, fkUsuario);
        } catch (SQLException ex) {
            LOGGER.log(Level.INFO, "Erro ao pegar a última interação.", ex);
        } finally {
            BDD.close();
            DAO.close();
        }
        return ret;
    }
}
