package br.edu.ifsp.spo.pds2016glybif.banco;

import br.edu.ifsp.spo.pds2016glybif.banco.Conexao.Status;
import br.edu.ifsp.spo.pds2016glybif.modelo.Imagem;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Lorhan Sohaky
 */
public class ImagemDAO {

    private static final Logger LOGGER = Logger.getLogger(ImagemDAO.class.getName());
    private final Connection conexao;
    private PreparedStatement stmt;
    private ResultSet rs;

    /**
     * Método construtor da classe ImagemDAO.
     *
     * @param con Objeto de conexão.
     */
    public ImagemDAO(Connection con) {
        this.conexao = con;
    }

    /**
     * Método para inserir imagens no banco de dados.
     *
     * @param list Lista de imagens.
     * @param fk Fk determinando se as imagens estão relacionadas com ocorrencia
     * ou comentário (fk_ocorrencia/fk_comentario)
     * @param pk Número de identificação.
     * @return Retorna o Status da execução.
     * @throws SQLException Erro SQL.
     */
    public Status inserirImagem(List<Imagem> list, String fk, long pk) throws SQLException {
        this.stmt = this.conexao.prepareStatement("INSERT INTO tb_imagem (imagem," + fk + ") VALUES (?,?);");
        for (Imagem img : list) {
            this.stmt.setBytes(1, img.getImagem());
            this.stmt.setLong(2, pk);
            this.stmt.executeUpdate();
        }
        return Status.OK;
    }

    /**
     * Método para listar as imagens do comentário ou da ocorrência, dependendo
     * do valor do parâmetro fk.
     *
     * @param fk Fk determinando se as imagens estão relacionadas com ocorrencia
     * ou comentário (fk_ocorrencia/fk_comentario)
     * @param pk Número de identificação.
     * @return Retorna o Status da execução.
     * @throws SQLException Erro SQL.
     */
    public List<Imagem> listaImagens(String fk, long pk) throws SQLException {
        List<Imagem> list = new ArrayList<>();
        this.stmt = this.conexao.prepareStatement("SELECT pk_imagem, imagem FROM tb_imagem WHERE " + fk + "=? AND imagem<>'';");
        this.stmt.setLong(1, pk);
        this.rs = this.stmt.executeQuery();
        while (rs.next()) {
            list.add(new Imagem(rs.getLong("pk_imagem"), rs.getBytes("imagem")));
        }
        return list;
    }

    /**
     * Método para pegar a imagem.
     *
     * @param pkImagem Número de identificação da imagem.
     * @return Retorna objeto Imagem.
     * @throws SQLException Erro SQL.
     */
    public Imagem getImagem(long pkImagem) throws SQLException {
        this.stmt = this.conexao.prepareStatement("SELECT imagem FROM tb_imagem WHERE pk_imagem=?;");
        this.stmt.setLong(1, pkImagem);
        this.rs = this.stmt.executeQuery();
        if (rs.next()) {
            return new Imagem(rs.getBytes(1));
        }
        return null;
    }

    /**
     * Método para fechar o PreparedStatement e ResultSet
     */
    public void close() {
        try {
            if (rs != null) {
                this.rs.close();
            }
            if (stmt != null) {
                this.stmt.close();
            }
        } catch (SQLException ex) {
            LOGGER.log(Level.INFO, "Erro ao fechar.", ex);
        }
    }
}
