package br.edu.ifsp.spo.pds2016glybif.rest;

import br.edu.ifsp.spo.pds2016glybif.banco.Conexao;
import br.edu.ifsp.spo.pds2016glybif.banco.ImagemDAO;
import br.edu.ifsp.spo.pds2016glybif.modelo.Imagem;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.naming.NamingException;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/**
 * Classe que faz intermédio dos dados recebidos via HTTP com a imagem.
 *
 * @author Lorhan Sohaky
 */
@Path("/imagem")
public class RestImagem {

    private final Conexao BDD;
    private final ImagemDAO DAO;
    private static final Logger LOGGER = Logger.getLogger(RestImagem.class.getName());

    /**
     * Método construtor da RestImagem.
     *
     * @throws NamingException Erro
     * @throws SQLException Erro
     */
    public RestImagem() throws NamingException, SQLException {
        BDD = new Conexao();
        DAO = new ImagemDAO(BDD.getConexao());
    }
    
    /**
     * Rest para pegar imagem.
     * @param pkImagem Número de identificação da imagem.
     * @return Objeto imagem.
     */
    @GET
    @Path("/getImagem/{pkImagem}")
    @Produces(MediaType.TEXT_XML)
    public Imagem getImagem(@PathParam("pkImagem") long pkImagem){
        try {
            return DAO.getImagem(pkImagem);
        } catch (SQLException ex) {
            LOGGER.log(Level.INFO, "Erro ao listar os comentários.", ex);
        }finally{
            DAO.close();
            BDD.close();
        }
        return null;
    }
}
