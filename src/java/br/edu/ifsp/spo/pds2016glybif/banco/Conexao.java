package br.edu.ifsp.spo.pds2016glybif.banco;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

/**
 *
 * Classe que faz a conexão da aplicação com o banco de dados.
 *
 * @author Lorhan Sohaky
 */
public class Conexao {

    /**
     * Enumeração para determinar o resultado de um método.
     */
    public static enum Status {
        /**
         * Tipo de enumeração que informa que o dado no banco de dados é
         * diferente do fornecido pelo usuário.
         */
        DIFERENTE,
        /**
         * Tipo de enumeração que informa que o método foi realizado com
         * sucesso.
         */
        OK,
        /**
         * Tipo de enumeração que informa que o dado informado via parametro já
         * existe no banco de dados.
         */
        EXISTE,
        /**
         * Tipo de enumeração que informa que ocorreu um erro no método que foi
         * realizado.
         */
        ERRO;

        /**
         * Método que transforma String em Status.
         *
         * @param str String a ser transformada em Status
         * @return Status.
         */
        public static Status parseStatus(String str) {
            switch (str) {
                case "OK":
                    return Status.OK;
                case "EXISTE":
                    return Status.EXISTE;
                case "ERRO":
                    return Status.ERRO;
                default:
                    return null;
            }
        }
    }

    private static final Logger LOGGER = Logger.getLogger(Conexao.class.getName());
    private final Connection conexao;

    /**
     * Realiza a conexão com banco de dados.
     *
     * @throws NamingException Erro ao iniciar contexto.
     * @throws SQLException Erro ao executarS SQL contexto.
     */
    public Conexao() throws NamingException, SQLException {
        Context initCtx = new InitialContext();
        DataSource ds = (DataSource) initCtx.lookup("java:app/cidadeAjuda");
        this.conexao = ds.getConnection();
        initCtx.close();
    }

    /**
     * Fecha a conexão com o banco de dados.
     *
     * @return Status da execução.
     */
    public Status close() {
        try {
            this.conexao.close();
            return Status.OK;
        } catch (SQLException ex) {
            LOGGER.log(Level.INFO, "Erro ao fechar a conexão", ex);
            return Status.ERRO;
        }
    }

    /**
     * Método que devolve o objeto Connection.
     *
     * @return Retorna a conexão.
     */
    public Connection getConexao() {
        return conexao;
    }

}
