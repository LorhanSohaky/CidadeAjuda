/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.ifsp.spo.pds2016glybif.modelo;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * Classe modelo do relatório.
 *
 * @author Lorhan Sohaky
 */
@XmlRootElement
public class Relatorio {

    private String tipo;
    private int total;
    private long pk_tipo;

    /**
     * Método construtor da classe Relatorio.
     */
    public Relatorio() {
    }

    /**
     * Método construtor da classe Relatorio.
     *
     * @param tipo Tipo de ocorrência.
     * @param pk_tipo Número de identificação do tipo.
     * @param total Total de ocorrências do mesmo tipo.
     */
    public Relatorio(String tipo,long pk_tipo, int total) {
        this.tipo = tipo;
        this.pk_tipo=pk_tipo;
        this.total = total;
    }

    /**
     * Getter tipo.
     *
     * @return Tipo de ocorrência.
     */
    public String getTipo() {
        return tipo;
    }

    /**
     * Setter tipo.
     *
     * @param tipo Tipo de ocorrência.
     */
    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    /**
     * Getter total.
     *
     * @return Total de ocorrências do mesmo tipo.
     */
    public int getTotal() {
        return total;
    }

    /**
     * Setter total.
     *
     * @param total Total de ocorrências do mesmo tipo.
     */
    public void setTotal(int total) {
        this.total = total;
    }

    /**
     * Getter pk_tipo.
     * @return Número de identificação do tipo.
     */
    public long getPk_tipo() {
        return pk_tipo;
    }

    /**
     * Setter pk_tipo.
     * @param pk_tipo Número de identificação do tipo.
     */
    public void setPk_tipo(long pk_tipo) {
        this.pk_tipo = pk_tipo;
    }

}
