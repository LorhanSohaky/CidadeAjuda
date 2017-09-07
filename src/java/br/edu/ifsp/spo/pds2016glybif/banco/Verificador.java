package br.edu.ifsp.spo.pds2016glybif.banco;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.naming.NamingException;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

/**
 * Classe que verifica o fim da validade de uma ocorrência ativa.
 *
 * @author Lorhan Sohaky
 */
public class Verificador implements ServletContextListener {

    private static final Logger LOGGER = Logger.getLogger(Verificador.class.getName());
    private Connection conexao;
    private PreparedStatement stmt;
    private Thread th;

    /**
     * Método construtor do Agendador.
     *
     */
    public Verificador() {
    }

    private void verifica() {
        try {
            this.stmt = this.conexao.prepareStatement("UPDATE tb_ocorrencia SET ocorrencia_ativa=false WHERE ocorrencia_ativa=true AND prazo<=now();");
            this.stmt.executeUpdate();
        } catch (SQLException ex) {
            LOGGER.log(Level.INFO, "Erro ao verificar ocorrências ativas.", ex);
        }
    }

    /**
     * Inicializador de contexto.
     *
     * @param sce Contexto da Servlet.
     */
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        LOGGER.log(Level.INFO, "Verificador Ativo.");
        this.th = new Thread() {
            @Override
            public void run() {
                Conexao con;
                try {
                    while (true) {
                        con = new Conexao();
                        conexao = con.getConexao();
                        verifica();
                        con.close();
                        Thread.sleep(TimeUnit.MINUTES.toMillis(30));
                    }
                } catch (NamingException | SQLException | InterruptedException ex) {
                    LOGGER.log(Level.INFO, "Erro ao executar verificador", ex);
                }

            }
        };
        th.start();
    }

    /**
     * Finalizador de contexto.
     *
     * @param sce Contexto da Servlet.
     */
    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        try {
            this.th.join(1);
        } catch (InterruptedException ex) {
            LOGGER.log(Level.INFO, "Erro ao finalizar Thread.");
        }
        LOGGER.log(Level.INFO, "Verificador Desativado.");
    }
}
