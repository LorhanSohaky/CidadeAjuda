/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.ifsp.spo.pds2016glybif.modelo;

import java.util.Date;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Classe modelo da interação.
 *
 * @author Lorhan Sohaky
 */
@XmlRootElement
public class Interacao {

    private String resposta;
    private Date dataHora;
    private long fkOcorrencia;
    private long fkUsuario;

    /**
     * Método construtor
     */
    public Interacao() {
    }

    /**
     * Método construtor da classe Interacao.
     *
     * @param resposta Resposta da interação.
     * @param dataHora Data e hora da interação.
     * @param ocorrencia Número de identificação da ocorrência.
     * @param fkUsuario Número de identificação do usuário.
     */
    public Interacao(String resposta, Date dataHora, long ocorrencia, long fkUsuario) {
        this.resposta = resposta;
        this.dataHora = (Date) dataHora.clone();
        this.fkOcorrencia = ocorrencia;
        this.fkUsuario = fkUsuario;
    }

    /**
     * Método construtor da classe Interacao.
     *
     * @param resposta Resposta da interação.
     * @param ocorrencia Número de identificação da ocorrência.
     * @param fkUsuario Número de identificação do usuário.
     */
    public Interacao(String resposta, long ocorrencia, long fkUsuario) {
        this.resposta = resposta;
        this.fkOcorrencia = ocorrencia;
        this.fkUsuario = fkUsuario;
    }

    /**
     * Método que retorna a resposta da interação.
     *
     * @return Retorno da resposta.
     */
    public String getResposta() {
        return resposta;
    }

    /**
     * Método que retorna a identificação do usuário.
     *
     * @return Retorno do número de identificação do usuário.
     */
    public long getFkUsuario() {
        return fkUsuario;
    }

    /**
     * Método que fornece a identificação do usuário.
     *
     * @param fkUsuario Número de identificação do usuário.
     */
    public void setFkUsuario(long fkUsuario) {
        this.fkUsuario = fkUsuario;
    }

    /**
     * Método que informa a resposta do usuário.
     *
     * @param resposta Resposta do usuário.
     */
    public void setResposta(String resposta) {
        this.resposta = resposta;
    }

    /**
     * Método que retorna a Data e Hora.
     *
     * @return Retorno da data da interação.
     */
    public Date getDataHora() {
        if (dataHora == null) {
            return null;
        }
        return (Date) dataHora.clone();
    }

    /**
     * Método que informa a Data e Hora.
     *
     * @param dataHora Data da interação.
     */
    public void setDataHora(Date dataHora) {
        this.dataHora = (Date) dataHora.clone();
    }

    /**
     * Método que retorna a identificação da ocorrência.
     *
     * @return Retorno da identificação da ocorrência.
     */
    public long getFkOcorrencia() {
        return fkOcorrencia;
    }

    /**
     * Método que informa a identificação da ocorrência.
     *
     * @param fkOcorrencia Número de identificação da ocorrência.
     */
    public void setFkOcorrencia(long fkOcorrencia) {
        this.fkOcorrencia = fkOcorrencia;
    }

}
