package br.edu.ifsp.spo.pds2016glybif.rest;

import br.edu.ifsp.spo.pds2016glybif.banco.Conexao;
import br.edu.ifsp.spo.pds2016glybif.banco.RelatorioDAO;
import static br.edu.ifsp.spo.pds2016glybif.funcionalidades.FuncoesGerais.callGet;
import br.edu.ifsp.spo.pds2016glybif.modelo.Ocorrencia;
import br.edu.ifsp.spo.pds2016glybif.modelo.Relatorio;
import java.sql.SQLException;
import java.util.List;
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
 * Classe Rest do relatório.
 *
 * @author Lorhan Sohaky
 */
@Path("/relatorio")
public class RestRelatorio {

    private final Conexao BDD;
    private final RelatorioDAO DAO;
    private static final Logger LOGGER = Logger.getLogger(RestRelatorio.class.getName());

    /**
     * Método construtor da RestOcorrencia.
     *
     * @throws NamingException Erro.
     * @throws SQLException Erro SQL.
     */
    public RestRelatorio() throws NamingException, SQLException {
        BDD = new Conexao();
        DAO = new RelatorioDAO(BDD.getConexao());
    }

    /**
     * Rest para gerar relatório.
     *
     * @param tipo Tipo da pesquisa.
     * @param ativo Atributo para listar ocorrências ativas e não ativas.
     * @param local Local.
     * @return Lista de relatório.
     */
    @PUT
    @Path("/gerar/{tipo}/{ativo}/{local}")
    @Produces(MediaType.APPLICATION_XML)
    public List<Relatorio> gerarRelatorio(@PathParam("tipo") String tipo, @PathParam("ativo") boolean ativo, @PathParam("local") String local) {
        int pk;
        List<Relatorio> resultado = null;
        try {
            pk = DAO.getPoligono(local, tipo);
            if (pk == 0) {
                String poly = callGet("http://nominatim.openstreetmap.org/search?" + tipo + "=" + local + "&format=xml&polygon=1&addressdetails=1", "GET", "TEXT/XML");
                if (!poly.contains("polygonpoints=")) {
                    return null;
                }
                poly = poly.substring(poly.indexOf("polygonpoints=") + "polygonpoints=".length(), poly.indexOf("lat=") - 1);
                poly = poly.replaceAll("\"", "");
                poly = poly.replaceAll("\'", "");
                poly = poly.replaceAll("\\[", "(");
                poly = poly.replaceAll("]", ")");
                DAO.inserirLocal(local, tipo, poly);
                pk = DAO.getPoligono(poly, tipo);
            }
            resultado = DAO.pontoNoPoligono(pk, ativo);
        } catch (Exception ex) {
            LOGGER.log(Level.INFO, "Erro ao gerar relatório.", ex);
        } finally {
            BDD.close();
            DAO.close();
        }
        return resultado;
    }

    /**
     * Rest para gerar relatório específico de um tipo de ocorrência numa dada
     * região
     *
     * @param pkTipo Número de identificação do tipo
     * @param tipoLocal Tipo de local (city/state, etc)
     * @param ativo Booleano ativo/não ativo
     * @param local Nome do local (São Paulo, por exemplo)
     * @return Lista de ocorrências.
     */
    @GET
    @Path("/especifico/{pkTipo}/{tipoLocal}/{ativo}/{local}")
    @Produces(MediaType.TEXT_XML)
    public List<Ocorrencia> relatorioEspecifico(@PathParam("pkTipo") long pkTipo, @PathParam("tipoLocal") String tipoLocal, @PathParam("ativo") boolean ativo, @PathParam("local") String local) {
        long pkLocal;
        List<Ocorrencia> resultado = null;
        try {
            pkLocal = DAO.getPoligono(local, tipoLocal);
            if (pkLocal == 0) {
                return null;
            }
            resultado = DAO.listaOcorrenciaNoPoligono(pkTipo, pkLocal, ativo);
        } catch (SQLException ex) {
            LOGGER.log(Level.INFO, "Erro ao gerar relatório.", ex);
        } finally {
            BDD.close();
            DAO.close();
        }
        return resultado;
        //TODO: fazer o ponto no poligono retornar o id do local? Já que desse modo dá para utilizar o getInfo para pegar informações mais detalhadas.
    }
}
