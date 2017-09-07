/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.ifsp.spo.pds2016glybif.rest;

import br.edu.ifsp.spo.pds2016glybif.funcionalidades.FuncoesGerais;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Classe para teste da classe RestTipo.
 *
 * @author Lorhan Sohaky
 */
public final class RestTipoTest {

    private static final Logger LOGGER = Logger.getLogger(RestTipoTest.class.getName());
    final String url;

    /**
     * Método construtor da classe.
     *
     * @param url URL do site.
     */
    public RestTipoTest(String url) {
        this.url = url;
    }

    /**
     * Método que executa todos os outros métodos de teste
     */
    public void execute() {
        this.testListaTipos();
    }

    /**
     * Teste do método listaTipos da classe RestTipo.
     */
    @Test
    public void testListaTipos() {
        System.out.println("listaTipos");
        try {
            String retorno = FuncoesGerais.callGet(url + "funcoes/tipo/listatipos", "GET", "APPLICATION/XML");
            assertNotEquals("", retorno);
        } catch (Exception ex) {
            LOGGER.log(Level.INFO, "Erro ao testar listarTipos.", ex);
            fail();
        }
    }

}
