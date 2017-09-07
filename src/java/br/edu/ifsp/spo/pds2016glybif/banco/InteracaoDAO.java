package br.edu.ifsp.spo.pds2016glybif.banco;

import br.edu.ifsp.spo.pds2016glybif.banco.Conexao.Status;
import br.edu.ifsp.spo.pds2016glybif.modelo.Interacao;
import br.edu.ifsp.spo.pds2016glybif.modelo.Ocorrencia;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Classe que faz intermédio da classe Interacao com o banco de dados.
 *
 * @author Lorhan Sohaky
 */
public class InteracaoDAO {

    private static final Logger LOGGER = Logger.getLogger(InteracaoDAO.class.getName());
    private final Connection conexao;
    private PreparedStatement stmt;
    private ResultSet rs;

    /**
     * Método construtor da classe InteracaoDAO.
     *
     * @param con Atributo para ter acesso ao banco de dados.
     */
    public InteracaoDAO(Connection con) {
        this.conexao = con;
    }

    /**
     * Insere a interação no banco de dados.
     *
     * @param inte Dados da interação encapsulado pela classe Interacao.
     * @return Status da execução.
     * @throws java.sql.SQLException Erro SQL.
     */
    public Status inserirInteracao(Interacao inte) throws SQLException {
        String resp = getUltimaInteracao(inte.getFkOcorrencia(), inte.getFkUsuario());
        String cmp = "";
        String cmp2 = "";
        if (resp == null) {
            cmp2 = "UPDATE tb_usuario SET qtd_respostas=qtd_respostas+1 WHERE pk_usuario=" + inte.getFkUsuario() + ";";
        }
        if (resp != null && resp.equals(inte.getResposta())) {
            return Status.EXISTE;
        } else if (resp != null && !resp.equals(inte.getResposta())) {
            switch (resp) {
                case "E":
                    cmp = "qtd_existente=qtd_existente-1, ";
                    break;
                case "I":
                    cmp = "qtd_inexistente=qtd_inexistente-1, ";
                    break;
                case "C":
                    cmp = "qtd_caso_encerrado=qtd_caso_encerrado-1, ";
                    break;
                default:
                    cmp = "";
            }
        }
        switch (inte.getResposta()) {
            case "E":
                cmp += "qtd_existente=qtd_existente+1";
                break;
            case "I":
                cmp += "qtd_inexistente=qtd_inexistente+1";
                break;
            case "C":
                cmp += "qtd_caso_encerrado=qtd_caso_encerrado+1";
                break;
            default:
                return Status.ERRO;
        }
        this.stmt = this.conexao.prepareStatement("BEGIN;"
                + "INSERT INTO tb_interacao (resposta,data_hora,fk_usuario,fk_ocorrencia) VALUES (?,now(),?,?);"
                + "UPDATE tb_ocorrencia SET " + cmp + ", prazo=now()+tb_tipo.prazo FROM tb_tipo WHERE pk_ocorrencia=? AND pk_tipo=fk_tipo;"
                + cmp2
                + "COMMIT;");
        this.stmt.setString(1, inte.getResposta());
        this.stmt.setLong(2, inte.getFkUsuario());
        this.stmt.setLong(3, inte.getFkOcorrencia());
        this.stmt.setLong(4, inte.getFkOcorrencia());
        this.stmt.executeUpdate();
        OcorrenciaDAO ocoD = new OcorrenciaDAO(conexao);
        Ocorrencia oco = new Ocorrencia(inte.getFkOcorrencia());
        ocoD.analiseDasInteracoes(oco);
        return Status.OK;
    }

    /**
     * Método para pegar a resposta da última interação de um determinado
     * usuário para uma determinada ocorrência.
     *
     * @param fkOcorrencia Número de identificação da ocorrência.
     * @param fkUsuario Número de identificação do usuário.
     * @return String com a resposta da última interação.
     * @throws java.sql.SQLException Erro SQL.
     */
    public String getUltimaInteracao(long fkOcorrencia, long fkUsuario) throws SQLException {
        this.stmt = this.conexao.prepareStatement("SELECT resposta FROM tb_interacao WHERE fk_ocorrencia=? AND fk_usuario=? ORDER BY data_hora DESC LIMIT 1;");
        this.stmt.setLong(1, fkOcorrencia);
        this.stmt.setLong(2, fkUsuario);
        this.rs = this.stmt.executeQuery();
        if (rs.next()) {
            return rs.getString("resposta");
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
