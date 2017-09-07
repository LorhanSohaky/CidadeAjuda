package br.edu.ifsp.spo.pds2016glybif.banco;

import br.edu.ifsp.spo.pds2016glybif.banco.Conexao.Status;
import br.edu.ifsp.spo.pds2016glybif.modelo.Ocorrencia;
import br.edu.ifsp.spo.pds2016glybif.modelo.Relatorio;
import br.edu.ifsp.spo.pds2016glybif.modelo.Tipo;
import br.edu.ifsp.spo.pds2016glybif.modelo.Usuario;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Classe que faz intermédio do relatório com o banco de dados.
 *
 * @author Lorhan Sohaky
 */
public class RelatorioDAO {

    private static final Logger LOGGER = Logger.getLogger(RelatorioDAO.class.getName());
    private final Connection conexao;
    private PreparedStatement stmt;
    private ResultSet rs;

    /**
     * Método construtor da classe OcorrenciaDAO.
     *
     * @param con Objeto de conexão.
     */
    public RelatorioDAO(Connection con) {
        this.conexao = con;
    }

    /**
     * Método que informa a quantidade de pontos dentro de um local.
     *
     * @param pkLocal número de identificação do local.
     * @param ativo Atributo para listar ocorrências ativas e não ativas.
     * @return lista com os locais que estão dentro da região pretendida.
     * @throws SQLException Erro SQL.
     */
    public List<Relatorio> pontoNoPoligono(long pkLocal, boolean ativo) throws SQLException {
        List<Relatorio> list = new ArrayList<>();
        this.stmt = conexao.prepareStatement("SELECT tb_tipo.nome,tb_tipo.pk_tipo, COUNT(*) AS total FROM tb_local,tb_ocorrencia,tb_tipo WHERE pk_local=? AND ocorrencia_ativa=? AND polygono@> ponto AND pk_tipo=fk_tipo GROUP BY tb_tipo.nome, tb_tipo.pk_tipo ORDER BY tb_tipo.nome;");
        this.stmt.setLong(1, pkLocal);
        this.stmt.setBoolean(2, ativo);
        this.rs = stmt.executeQuery();
        while (rs.next()) {
            list.add(new Relatorio(rs.getString(1), rs.getLong(2), rs.getInt(3)));
        }
        return list;
    }

    /**
     * Método para listar as ocorrências que estão no poligono (região)
     *
     * @param pkTipo Número de identificação do tipo
     * @param pkLocal Número de identificação do local
     * @param ativo Booleano ativo/não ativo
     * @return Lista de ocorrências
     * @throws SQLException Erro SQL
     */
    public List<Ocorrencia> listaOcorrenciaNoPoligono(long pkTipo, long pkLocal, boolean ativo) throws SQLException {
        List<Ocorrencia> list = new ArrayList<>();
        this.stmt = conexao.prepareStatement("SELECT data_hora, transitavel_veiculo, transitavel_a_pe,descricao,"
                + "pk_Ocorrencia, ti.nome AS tipo, apelido FROM tb_local,tb_usuario,tb_ocorrencia,tb_tipo as ti WHERE pk_local=? "
                + "AND ocorrencia_ativa=? AND polygono@> ponto AND fk_tipo=? AND fk_Tipo = pk_Tipo AND fk_usuario=pk_usuario ORDER BY data_hora DESC;");
        this.stmt.setLong(1, pkLocal);
        this.stmt.setBoolean(2, ativo);
        this.stmt.setLong(3, pkTipo);
        this.rs = stmt.executeQuery();
        while (rs.next()) {
            list.add(new Ocorrencia(rs.getTimestamp(1), rs.getBoolean(2), rs.getBoolean(3), rs.getString(4), rs.getInt(5), new Tipo(rs.getString(6)), new Usuario(rs.getString(7))));
        }
        return list;
    }

    /**
     * Método para pegar o número de identificação do local.
     *
     * @param nome Nome do local.
     * @param tipo Tipo de local.
     * @return Número de identificação do local.
     * @throws SQLException Erro SQL.
     */
    public int getPoligono(String nome, String tipo) throws SQLException {
        this.stmt = conexao.prepareStatement("SELECT pk_local FROM tb_local WHERE nome=UPPER(?) AND tipo=UPPER(?);");
        this.stmt.setString(1, nome);
        this.stmt.setString(2, tipo);
        this.rs = stmt.executeQuery();
        if (!rs.next()) {
            return 0;
        }
        return rs.getInt(1);
    }

    /**
     * Método para inserir local.
     *
     * @param nome Nome do local.
     * @param tipo Tipo de local
     * @param polygon Poligono da região.
     * @return Status da execução.
     * @throws SQLException Erro SQL.
     */
    public Status inserirLocal(String nome, String tipo, String polygon) throws SQLException {
        this.stmt = conexao.prepareStatement("INSERT INTO tb_local (nome,tipo,polygono) VALUES(UPPER(?),UPPER(?),POLYGON(?));");
        this.stmt.setString(1, nome);
        this.stmt.setString(2, tipo);
        this.stmt.setObject(3, polygon);
        stmt.executeUpdate();
        return Status.OK;
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
