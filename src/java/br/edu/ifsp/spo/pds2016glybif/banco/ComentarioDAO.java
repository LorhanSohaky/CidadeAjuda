package br.edu.ifsp.spo.pds2016glybif.banco;

import br.edu.ifsp.spo.pds2016glybif.banco.Conexao.Status;
import br.edu.ifsp.spo.pds2016glybif.modelo.Comentario;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Classe que faz intermédio da classe Comentario com o banco de dados.
 *
 * @author Lorhan Sohaky
 */
public class ComentarioDAO {

    private static final Logger LOGGER = Logger.getLogger(ComentarioDAO.class.getName());
    private final Connection conexao;
    private PreparedStatement stmt;
    private ResultSet rs;

    /**
     * Método construtor da classe ComentarioDAO.
     *
     * @param con Atributo para ter acesso ao banco de dados.
     */
    public ComentarioDAO(Connection con) {
        this.conexao = con;
    }

    /**
     * Insere o comentario no banco de dados.
     *
     * @param com Dados do comentario encapsulado pela classe Comentario.
     * @return Status da execução.
     * @throws java.sql.SQLException Erro SQL.
     */
    public Status inserirComentario(Comentario com) throws SQLException {
        this.stmt = this.conexao.prepareStatement("INSERT INTO tb_comentario (comentario,fk_usuario,fk_ocorrencia,data_hora) VALUES (?,?,?,now());", new String[]{"pk_comentario"});
        this.stmt.setString(1, com.getComentario());
        this.stmt.setLong(2, com.getUsuario().getPkUsuario());
        this.stmt.setLong(3, com.getOcorrencia().getPkOcorrencia());
        this.stmt.executeUpdate();
        if (!com.getListaImagens().isEmpty()) {
            ImagemDAO img = new ImagemDAO(conexao);
            img.inserirImagem(com.getListaImagens(), "fk_comentario", this.stmt.getGeneratedKeys().getLong(1));
        }
        return Status.OK;
    }

    /**
     * Método para listar comentários de uma determinada ocorrência.
     *
     * @param pkOcorrencia Número de indentificação da ocorrência.
     * @param qtdLidos Quantidade de comentários lidos.
     * @return Retorna uma lista de comentátios.
     * @throws SQLException Erro SQL.
     */
    public List<Comentario> listaComentario(long pkOcorrencia, int qtdLidos) throws SQLException {
        List<Comentario> list = new ArrayList<>();
        ImagemDAO img = new ImagemDAO(conexao);
        this.stmt = this.conexao.prepareStatement("SELECT pk_comentario,comentario,apelido,data_hora FROM tb_comentario, tb_usuario WHERE fk_ocorrencia=? AND fk_usuario=pk_usuario ORDER BY data_hora DESC OFFSET ? LIMIT 10;");
        this.stmt.setLong(1, pkOcorrencia);
        this.stmt.setInt(2, qtdLidos);
        this.rs = this.stmt.executeQuery();
        while (rs.next()) {
            Comentario com = new Comentario(rs.getLong("pk_comentario"), rs.getString("comentario"), rs.getString("apelido"), rs.getTimestamp("data_hora"));
            com.setListaImagens(img.listaImagens("fk_comentario", com.getPkComentario()));
            list.add(com);
        }
        img.close();
        return list;
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
