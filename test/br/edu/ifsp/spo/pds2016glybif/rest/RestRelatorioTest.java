/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.ifsp.spo.pds2016glybif.rest;

import br.edu.ifsp.spo.pds2016glybif.funcionalidades.FuncoesGerais;
import java.net.URLEncoder;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Classe para teste da classe RestRelatorio.
 *
 * @author Lorhan Sohaky
 */
public final class RestRelatorioTest {
    
    private static final Logger LOGGER = Logger.getLogger(RestRelatorioTest.class.getName());
    final String url;

    /**
     * Método construtor da classe.
     *
     * @param url URL do site.
     */
    public RestRelatorioTest(String url) {
        this.url = url;
    }

    /**
     * Método que executa todos os outros métodos de teste
     */
    public void execute() {
        this.testGerarRelatorio();
    }

    /**
     * Teste do método gerarRelatorio da classe RestRelatorio.
     */
    @Test
    public void testGerarRelatorio() {
        System.out.println("gerarRelatorio");
        String tipo = "city";
        String local = "São Paulo";
        boolean ativo = true;
        try {
            String retorno = FuncoesGerais.callGet(url + "funcoes/relatorio/gerar/" + tipo + "/" + String.valueOf(ativo) + "/" + URLEncoder.encode(local, "UTF-8"), "PUT", "APPLICATION/XML");
            assertNotEquals(null, retorno);
        } catch (Exception ex) {
            LOGGER.log(Level.INFO, "Erro ao testar gerarRelatorio.", ex);
            fail();
        }
    }
    
}
