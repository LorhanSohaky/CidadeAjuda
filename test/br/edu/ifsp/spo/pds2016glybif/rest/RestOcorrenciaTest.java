/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.ifsp.spo.pds2016glybif.rest;

import br.edu.ifsp.spo.pds2016glybif.banco.Conexao.Status;
import br.edu.ifsp.spo.pds2016glybif.funcionalidades.FuncoesGerais;
import static br.edu.ifsp.spo.pds2016glybif.funcionalidades.FuncoesGerais.objectToXML;
import br.edu.ifsp.spo.pds2016glybif.modelo.Imagem;
import br.edu.ifsp.spo.pds2016glybif.modelo.Ocorrencia;
import br.edu.ifsp.spo.pds2016glybif.modelo.Ponto;
import br.edu.ifsp.spo.pds2016glybif.modelo.Usuario;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Classe de teste da classe RestOcorrencia.
 *
 * @author Lorhan Sohaky
 */
public final class RestOcorrenciaTest {

    private static final Logger LOGGER = Logger.getLogger(RestOcorrenciaTest.class.getName());
    final String url;

    /**
     * Método construtor da classe.
     *
     * @param url URL do site.
     */
    public RestOcorrenciaTest(String url) {
        this.url = url;
    }

    /**
     * Método que executa todos os outros métodos de teste
     */
    public void execute() {
        this.testCadastroOcorrencia();
        this.testGetInfo();
        this.testListaTodosPontos();
        this.testSetMapa();
    }

    /**
     * Teste do método cadastroOcorrencia da classe RestOcorrencia.
     */
    @Test
    public void testCadastroOcorrencia() {
        System.out.println("cadastroOcorrencia");
        Usuario user = new Usuario(1);
        Ocorrencia oco = new Ocorrencia(new Ponto(1, 1), true, true, "Descrição Teste", user);
        oco.getLocal().setTipo(1);
        try {
            Status retorno = Status.parseStatus(FuncoesGerais.callSet(url, objectToXML(oco, Ocorrencia.class), "/ocorrencia/cadastro", "POST", "TEXT/XML", "TEXT/PLAIN"));
            assertNotEquals(null, retorno);
        } catch (Exception ex) {
            LOGGER.log(Level.INFO, "Erro ao testar cadastroOcorrencia.", ex);
            fail();
        }
    }

    /**
     * Teste do método setMapa da classe RestOcorrencia.
     */
    @Test
    public void testSetMapa() {
        System.out.println("setMapa");
        float latN = 1;
        float lonN = 1;
        float latS = 30;
        float lonS = 30;
        try {
            String retorno = FuncoesGerais.callGet(url + "funcoes/ocorrencia/mapa/" + latN + "/" + lonN + "/" + latS + "/" + lonS, "GET", "APPLICATION/XML");
            assertNotEquals(null, retorno);
        } catch (Exception ex) {
            LOGGER.log(Level.INFO, "Erro ao testar setMapa.", ex);
            fail();
        }
    }

    /**
     * Teste do método listaTodosPontos da classe RestOcorrencia.
     */
    @Test
    public void testListaTodosPontos() {
        System.out.println("listaTodosPontos");
        try {
            String retorno = FuncoesGerais.callGet(url + "funcoes/ocorrencia/listapontos", "GET", "APPLICATION/XML");
            assertNotEquals(null, retorno);
        } catch (Exception ex) {
            LOGGER.log(Level.INFO, "Erro ao testar listaTodosPontos.", ex);
            fail();
        }
    }

    /**
     * Teste do método getInfo da classe RestOcorrencia.
     */
    @Test
    public void testGetInfo() {
        System.out.println("getInfo");
        long id = 1;
        try {
            String retorno = FuncoesGerais.callGet(url + "funcoes/ocorrencia/getinfo/" + id, "GET", "APPLICATION/XML");
            assertNotEquals(null, retorno);
        } catch (Exception ex) {
            LOGGER.log(Level.INFO, "Erro ao testar getInfo.", ex);
            fail();
        }
    }

}
