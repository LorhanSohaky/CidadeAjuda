/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.ifsp.spo.pds2016glybif.funcionalidades;

import br.edu.ifsp.spo.pds2016glybif.modelo.Usuario;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Classe de teste das funções gerais.
 *
 * @author Lorhan Sohaky
 */
public final class FuncoesGeraisTest {

    private final String url;
    private static final Logger LOGGER = Logger.getLogger(FuncoesGeraisTest.class.getName());

    /**
     * Método construtor da Classe de teste de FuncoesGerais.
     *
     * @param url URL do site.
     */
    public FuncoesGeraisTest(String url) {
        this.url = url;
    }

    /**
     * Método que executa todos os outros métodos de teste
     */
    public void execute() {
        this.testCallGet();
        this.testCallSet();
    }

    /**
     * Teste do método callSet da clase FuncoesGerais.
     *
     */
    @Test
    public void testCallSet() {
        System.out.println("callSet");
        String host = url;
        Usuario user = new Usuario("Lor", "A", "A", "C", new Date());
        String urlParameters;
        try {
            urlParameters = FuncoesGerais.objectToXML(user, Usuario.class);
            String path = "funcoes/usuario/cadastro";
            String method = "POST";
            String contentType = "TEXT/XML";
            String accept = "TEXT/PLAIN";
            String expResult = "";
            String result = FuncoesGerais.callSet(host, urlParameters, path, method, contentType, accept);
            assertNotEquals(expResult, result);
        } catch (Exception ex) {
            LOGGER.log(Level.INFO, "Erro ao testar callSet.", ex);
            fail();
        }

    }

    /**
     * Teste do método callGet da classe FuncoesGerais.
     *
     */
    @Test
    public void testCallGet() {
        System.out.println("callGet");
        try {
            String host = url + "funcoes/usuario/login/lorhan123@gmail.com/123";
            String accept = "APPLICATION/XML";
            String expResult = "";
            String result;
            result = FuncoesGerais.callGet(host, "GET", accept);
            assertNotEquals(expResult, result);
        } catch (Exception ex) {
            LOGGER.log(Level.INFO, "Erro ao testar callGet.", ex);
            fail();
        }

    }

}
