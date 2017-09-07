/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.ifsp.spo.pds2016glybif.rest;

import br.edu.ifsp.spo.pds2016glybif.banco.Conexao;
import br.edu.ifsp.spo.pds2016glybif.funcionalidades.FuncoesGerais;
import br.edu.ifsp.spo.pds2016glybif.modelo.Comentario;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Lorhan Sohaky
 */
public class RestComentarioTest {

    private static final Logger LOGGER = Logger.getLogger(RestComentarioTest.class.getName());
    final String url;

    /**
     * Método construtor da classe.
     *
     * @param url URL do site.
     */
    public RestComentarioTest(String url) {
        this.url = url + "funcoes/comentario/";
    }

    /**
     * Método que executa todos os outros métodos de teste
     */
    public void execute() {
        this.testRegistrar();
        this.testLista();
    }

    /**
     * Teste do método registrar da classe RestComentario.
     */
    @Test
    public void testRegistrar() {
        System.out.println("registrarComentario");
        long fkOcorrencia = 1;
        long fkUsuario = 1;
        try {
            String comentario = "Comentário de teste";
            Comentario com = new Comentario(comentario, fkUsuario, fkOcorrencia);
            Conexao.Status retorno = Conexao.Status.parseStatus(FuncoesGerais.callSet(url,FuncoesGerais.objectToXML(com, Comentario.class),"registrar/", "POST","TEXT/XML", "TEXT/PLAIN"));
            assertNotEquals("", retorno);
        } catch (Exception ex) {
            LOGGER.log(Level.INFO, "Erro ao testar registrarComentario.", ex);
            fail();
        }
    }

    /**
     * Teste do método lista da classe RestComentario.
     */
    @Test
    public void testLista() {
        System.out.println("listaComentarios");
        long pkOcorrencia = 1;
        int qtdLidos = 0;
        try {
            Conexao.Status retorno = Conexao.Status.parseStatus(FuncoesGerais.callGet(url + "lista/" + pkOcorrencia + "/" + qtdLidos, "GET", "TEXT/XML"));
            assertNotEquals("", retorno);
        } catch (Exception ex) {
            LOGGER.log(Level.INFO, "Erro ao testar listaComentarios.", ex);
            fail();
        }
    }

}
