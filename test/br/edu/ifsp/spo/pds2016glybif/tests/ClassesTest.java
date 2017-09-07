/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.ifsp.spo.pds2016glybif.tests;

import br.edu.ifsp.spo.pds2016glybif.funcionalidades.FuncoesGeraisTest;
import br.edu.ifsp.spo.pds2016glybif.rest.RestComentarioTest;
import br.edu.ifsp.spo.pds2016glybif.rest.RestInteracaoTest;
import br.edu.ifsp.spo.pds2016glybif.rest.RestOcorrenciaTest;
import br.edu.ifsp.spo.pds2016glybif.rest.RestRelatorioTest;
import br.edu.ifsp.spo.pds2016glybif.rest.RestTipoTest;
import br.edu.ifsp.spo.pds2016glybif.rest.RestUsuarioTest;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

/**
 * Classe de teste geral.
 *
 * @author Lorhan Sohaky
 */
@RunWith(JUnit4.class)
public class ClassesTest {

    /**
     * URL do site.
     */
    public final static String URL = "http://localhost:8080/";

    /**
     * Método para executar todos os testes existentes.
     */
    @Test
    public void test() {
        RestInteracaoTest inte = new RestInteracaoTest(URL);
        FuncoesGeraisTest fun = new FuncoesGeraisTest(URL);
        RestOcorrenciaTest oco = new RestOcorrenciaTest(URL);
        RestRelatorioTest rel = new RestRelatorioTest(URL);
        RestTipoTest tipo = new RestTipoTest(URL);
        RestUsuarioTest usu = new RestUsuarioTest(URL);
        RestComentarioTest com = new RestComentarioTest(URL);

        inte.execute();
        fun.execute();
        oco.execute();
        rel.execute();
        tipo.execute();
        usu.execute();
        com.execute();
    }

}
