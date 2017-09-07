package br.edu.ifsp.spo.pds2016glybif.rest;

import br.edu.ifsp.spo.pds2016glybif.banco.Conexao;
import br.edu.ifsp.spo.pds2016glybif.banco.TipoDAO;
import br.edu.ifsp.spo.pds2016glybif.modelo.Tipo;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.naming.NamingException;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/**
 * Classe Rest do tipo.
 *
 * @author Lorhan Sohaky
 */
@Path("/tipo")
public class RestTipo {

    private final Conexao BDD;
    private final TipoDAO DAO;
    private static final Logger LOGGER = Logger.getLogger(RestTipo.class.getName());

    /**
     * Método construtor da RestTipo.
     *
     * @throws NamingException Erro.
     * @throws SQLException Erro SQL.
     * @throws IOException Erro.
     */
    public RestTipo() throws NamingException, SQLException, IOException {
        BDD = new Conexao();
        DAO = new TipoDAO(BDD.getConexao());
    }

    /**
     * Método que lista todos os tipos de ocorrência.
     *
     * @return Lista de tipo de ocorrências.
     */
    @GET
    @Path("/listatipos")
    @Produces(MediaType.APPLICATION_XML)
    public List<Tipo> listaTipos() {
        List<Tipo> list = null;
        try {
            list = DAO.listaTipos();
        } catch (SQLException ex) {
            LOGGER.log(Level.INFO, "Erro ao listar tipos de ocorrência.", ex);
        } finally {
            BDD.close();
            DAO.close();
        }
        return list;
    }
}
