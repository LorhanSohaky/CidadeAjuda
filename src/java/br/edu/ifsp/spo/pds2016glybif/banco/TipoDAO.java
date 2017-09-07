package br.edu.ifsp.spo.pds2016glybif.banco;

import br.edu.ifsp.spo.pds2016glybif.banco.Conexao.Status;
import br.edu.ifsp.spo.pds2016glybif.modelo.Tipo;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Classe que faz intermédio do tipo com o banco de dados.
 *
 * @author Lorhan Sohaky
 */
public class TipoDAO {

    private static final Logger LOGGER = Logger.getLogger(TipoDAO.class.getName());
    private final Connection conexao;
    private PreparedStatement stmt;
    private ResultSet rs;

    /**
     * Método construtor da classe TipoDAO.
     *
     * @param con Objeto de conexão.
     */
    public TipoDAO(Connection con) {
        this.conexao = con;
    }

    /**
     * Insere o cadastro do tipo no banco de dados.
     *
     * @param tipo Dados do tipo de ocorrência encapsulado pela classe Tipo.
     * @return Status da execução.
     * @throws java.sql.SQLException Erro SQL.
     */
    public Status inserirTipo(Tipo tipo) throws SQLException {
        Status existe = this.existeTipo(tipo.getNome());
        if (existe != Status.OK) {
            return existe;
        }
        this.stmt = this.conexao.prepareStatement("INSERT INTO tb_tipo (nome,sugestao_de_descricao,prazo"
                + ") VALUES (?,?,INTERVAL ?)");
        this.stmt.setString(1, tipo.getNome());
        this.stmt.setString(2, tipo.getSugestaoDeDescricao());
        this.stmt.setString(3, tipo.getPrazo());
        this.stmt.executeUpdate();
        return Status.OK;

    }

    /**
     * Verifica se o tipo já está cadastrado no banco de dados.
     *
     * @param nome String que contém o nome do tipo de ocorrência.
     * @return Status da execução.
     * @throws java.sql.SQLException Erro SQL.
     */
    public Status existeTipo(String nome) throws SQLException {
        this.stmt = this.conexao.prepareStatement("SELECT pk_tipo FROM tb_tipo WHERE nome=?");
        this.stmt.setString(1, nome);
        this.rs = this.stmt.executeQuery();
        if (rs.next()) {
            return Status.EXISTE;
        } else {
            return Status.OK;
        }
    }

    /**
     * Método que lista os tipos de ocorrências.
     *
     * @return Lista com os tipos de ocorrências.
     * @throws java.sql.SQLException Erro SQL.
     */
    public List<Tipo> listaTipos() throws SQLException {
        List<Tipo> list = new ArrayList<>();
        this.stmt = this.conexao.prepareStatement("SELECT pk_tipo, nome, sugestao_de_descricao FROM tb_tipo ORDER BY pk_tipo;");
        this.rs = this.stmt.executeQuery();
        while (rs.next()) {
            list.add(new Tipo(rs.getInt(1), rs.getString(2), rs.getString(3)));
        }
        return list;
    }

    /**
     * Método para pegar o prazo do tipo.
     * @param pkTipo Númer de identificação do tipo.
     * @return Prazo.
     * @throws SQLException Erro SQL.
     */
    public String getPrazo(long pkTipo) throws SQLException {
        this.stmt = this.conexao.prepareStatement("SELECT prazo FROM tb_tipo WHERE pk_tipo=?;");
        this.stmt.setLong(1, pkTipo);
        this.rs = this.stmt.executeQuery();
        if (rs.next()) {
            return rs.getString(1);
        }
        return "";
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
