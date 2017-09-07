package br.edu.ifsp.spo.pds2016glybif.rest;

import br.edu.ifsp.spo.pds2016glybif.banco.Conexao;
import br.edu.ifsp.spo.pds2016glybif.banco.OcorrenciaDAO;
import br.edu.ifsp.spo.pds2016glybif.modelo.Ocorrencia;
import br.edu.ifsp.spo.pds2016glybif.modelo.Ponto;
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
 * Classe Rest da ocorrência.
 *
 * @author Lorhan Sohaky
 */
@Path("/ocorrencia")
public class RestOcorrencia {

    private final Conexao BDD;
    private final OcorrenciaDAO DAO;
    private static final Logger LOGGER = Logger.getLogger(RestOcorrencia.class.getName());

    /**
     * Método construtor da RestOcorrencia.
     *
     * @throws NamingException Erro.
     * @throws SQLException Erro SQL.
     */
    public RestOcorrencia() throws NamingException, SQLException {
        BDD = new Conexao();
        DAO = new OcorrenciaDAO(BDD.getConexao());
    }

    /**
     * Rest para cadastro de ocorrência.
     *
     * @param oco Ocorrência.
     * @return Status da execução.
     */
    @POST
    @Path("/cadastro")
    @Consumes(MediaType.TEXT_XML)
    @Produces(MediaType.TEXT_PLAIN)
    public String cadastroOcorrencia(Ocorrencia oco) {
        String resultado = "";
        try {
            resultado = String.valueOf(DAO.inserirOcorrencia(oco));
        } catch (SQLException ex) {
            LOGGER.log(Level.INFO, "Erro ao cadastrar ocorrência.", ex);
        } finally {
            BDD.close();
            DAO.close();
        }
        return resultado;

    }

    /**
     * Rest para pegar todas as ocorrência que estão dentro da view do mapa.
     *
     * @param latN Latitude Nordeste.
     * @param lonN Longitude Nordeste.
     * @param latS Latitude Sudoeste.
     * @param lonS Longitude Sudoeste.
     * @return Lista de pontos.
     */
    @GET
    @Path("/mapa/{latN}/{lonN}/{latS}/{lonS}")
    @Produces(MediaType.APPLICATION_XML)
    public List<Ponto> setMapa(@PathParam("latN") float latN, @PathParam("lonN") float lonN,
            @PathParam("latS") float latS, @PathParam("lonS") float lonS) {
        List<Ponto> list;
        try {
            list = DAO.pontoNoMapa(latN, lonN, latS, lonS);
        } catch (SQLException ex) {
            LOGGER.log(Level.INFO, "Erro ao procurar ponto.", ex);
            return null;
        } finally {
            BDD.close();
            DAO.close();
        }
        if (list.isEmpty()) {
            return null;
        }
        return list;
    }

    /**
     * Método que lista todas as ocorrências ativas.
     *
     * @return Lista com os pontos.
     */
    @GET
    @Path("/listapontos")
    @Produces(MediaType.APPLICATION_XML)
    public List<Ponto> listaTodosPontos() {
        List<Ponto> list;
        try {
            list = DAO.listaTodosPontos();
        } catch (SQLException ex) {
            LOGGER.log(Level.INFO, "Erro ao listar ponto.", ex);
            return null;
        } finally {
            BDD.close();
            DAO.close();
        }
        if (list.isEmpty()) {
            return null;
        }
        return list;
    }

    /**
     * Rest para pegar as informações da ocorrência.
     *
     * @param id Número de identificação da ocorrência.
     * @return Objeto Ocorrencia.
     */
    @GET
    @Path("/getinfo/{id}")
    @Produces(MediaType.APPLICATION_XML)
    public Ocorrencia getInfo(@PathParam("id") long id) {
        Ocorrencia oco=null;
        try {
            oco = DAO.getInfo(id);
        } catch (SQLException ex) {
            LOGGER.log(Level.INFO, "Erro ao procurar informações da ocorrência.", ex);
        } finally {
            BDD.close();
            DAO.close();
        }
        return oco;
    }
}
