/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.ifsp.spo.pds2016glybif.rest;

import br.edu.ifsp.spo.pds2016glybif.banco.Conexao.Status;
import br.edu.ifsp.spo.pds2016glybif.funcionalidades.FuncoesGerais;
import static br.edu.ifsp.spo.pds2016glybif.funcionalidades.FuncoesGerais.objectToXML;
import br.edu.ifsp.spo.pds2016glybif.modelo.Usuario;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Classe de teste da classe RestUsuario.
 *
 * @author Lorhan Sohaky
 */
public final class RestUsuarioTest {

    private static final Logger LOGGER = Logger.getLogger(RestUsuarioTest.class.getName());
    final String url;

    /**
     * Método construtor da classe.
     *
     * @param url URL do site.
     */
    public RestUsuarioTest(String url) {
        this.url = url;
    }

    /**
     * Método que executa todos os outros métodos de teste
     */
    public void execute() {
        this.testCadastroUsuario();
        this.testEnviarEmail();
        this.testGetUsuario();
        this.testMudarSenha();
    }

    /**
     * Teste do método cadastroUsuario da classe RestUsuario.
     */
    @Test
    public void testCadastroUsuario() {
        System.out.println("cadastroUsuario");
        try {
            String data = "11/11/1999";
            DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
            Date dataNascimento;
            dataNascimento = formatter.parse(data);
            Usuario user = new Usuario("Usuario Teste", "Teste", "usuarioteste@teste.com", "senhateste", dataNascimento);
            Status retorno = Status.parseStatus(FuncoesGerais.callSet(url, objectToXML(user, Usuario.class), "/usuario/cadastro", "POST", "TEXT/XML", "TEXT/PLAIN"));
            assertNotEquals(null, retorno);
            assertNotEquals(Status.ERRO, retorno);
        } catch (Exception ex) {
            LOGGER.log(Level.INFO, "Erro ao testar cadastroUsuario.", ex);
            fail();
        }
    }

    /**
     * Teste do método getUsuario da classe RestUsuario.
     */
    @Test
    public void testGetUsuario() {
        System.out.println("getUsuario");
        String email = "usuarioteste@teste.com";
        String senha = "senhateste";
        try {
            String retorno = FuncoesGerais.callGet(url + "funcoes/usuario/login/" + email + "/" + senha, "GET", "APPLICATION/XML");
            assertNotEquals(null, retorno);
        } catch (Exception ex) {
            LOGGER.log(Level.INFO, "Erro ao testar getUsuario.", ex);
            fail();
        }
    }

    /**
     * Teste do método enviarEmail da classe RestUsuario.
     */
    @Test
    public void testEnviarEmail() {
        System.out.println("enviarEmail");
        String email = "lorhan123@gmail.com";
        String data = "11-11-1999";
        try {
            Status retorno = Status.parseStatus(FuncoesGerais.callSet(url, url, "/usuario/mandaremail/" + email + "/" + data, "POST", "TEXT/PLAIN", "TEXT/PLAIN"));
            assertNotEquals(null, retorno);
            assertNotEquals(Status.ERRO, retorno);
        } catch (Exception ex) {
            LOGGER.log(Level.INFO, "Erro ao testar enviarEmail.", ex);
            fail();
        }
    }

    /**
     * Teste do método mudarSenha da classe RestUsuario.
     */
    @Test
    public void testMudarSenha() {
        System.out.println("mudarSenha");
        long id = 1;
        String senha = "123";
        try {
            Status retorno = Status.parseStatus(FuncoesGerais.callGet(url + "funcoes/usuario/mudarsenha/" + id + "/" + senha, "PUT", "TEXT/PLAIN"));
            assertNotEquals(null, retorno);
            assertNotEquals(Status.ERRO, retorno);
        } catch (Exception ex) {
            LOGGER.log(Level.INFO, "Erro ao testar mudarSenha.", ex);
            fail();
        }
    }

}
