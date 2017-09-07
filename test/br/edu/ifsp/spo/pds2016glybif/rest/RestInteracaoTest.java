/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.ifsp.spo.pds2016glybif.rest;

import br.edu.ifsp.spo.pds2016glybif.banco.Conexao.Status;
import br.edu.ifsp.spo.pds2016glybif.funcionalidades.FuncoesGerais;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Classe de teste para a rest interação.
 *
 * @author Lorhan Sohaky
 */
public final class RestInteracaoTest {

    private static final Logger LOGGER = Logger.getLogger(RestInteracaoTest.class.getName());
    final String url;

    /**
     * Método construtor da classe.
     *
     * @param url URL do site.
     */
    public RestInteracaoTest(String url) {
        this.url = url;
    }

    /**
     * Método que executa todos os outros métodos de teste
     */
    public void execute() {
        this.testGetUltimaInteracao();
        this.testRegistrar();
    }

    /**
     * Teste do método registrar da classe RestInteracao.
     */
    @Test
    public void testRegistrar() {
        System.out.println("registrarInteracao");
        String resposta = "E";
        long fkOcorrencia = 1;
        long fkUsuario = 1;
        try {
            Status retorno = Status.parseStatus(FuncoesGerais.callGet(url + "funcoes/interacao/registrar/" + resposta + "/" + fkUsuario + "/" + fkOcorrencia, "PUT", "TEXT/PLAIN"));
            assertNotEquals("", retorno);
        } catch (Exception ex) {
            LOGGER.log(Level.INFO, "Erro ao testar registrar.", ex);
            fail();
        }

    }

    /**
     * Teste do método getUltimaInteracao da classe RestInteracao.
     */
    @Test
    public void testGetUltimaInteracao() {
        System.out.println("getUltimaInteracao");
        long fkOcorrencia = 1;
        long fkUsuario = 1;
        try {
            Status retorno = Status.parseStatus(FuncoesGerais.callGet(url + "funcoes/interacao/getultimainteracao/" + fkOcorrencia + "/" + fkUsuario, "GET", "TEXT/PLAIN"));
            assertNotEquals("", retorno);
        } catch (Exception ex) {
            LOGGER.log(Level.INFO, "Erro ao testar getUltimaInteracao.", ex);
            fail();
        }
    }

}
