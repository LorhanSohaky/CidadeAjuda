/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.ifsp.spo.pds2016glybif.modelo;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * Classe modelo do tipo.
 *
 * @author Lorhan Sohaky
 */
@XmlRootElement
public class Tipo {

    private long pkTipo;
    private String nome;
    private String sugestaoDeDescricao;
    private String prazo;

    /**
     * Getter prazo.
     *
     * @return Prazo do tipo.
     */
    public String getPrazo() {
        return prazo;
    }

    /**
     * Setter prazo.
     *
     * @param prazo Prazo do tipo.
     */
    public void setPrazo(String prazo) {
        this.prazo = prazo;
    }

    /**
     * Método construtor da classe Tipo.
     */
    public Tipo() {
    }

    /**
     * Método construtor da classe Tipo.
     *
     * @param pkTipo Número de identificação do tipo.
     */
    public Tipo(long pkTipo) {
        this.pkTipo = pkTipo;
    }

    /**
     * Método construtor da classe Tipo.
     *
     * @param nome Nome do tipo.
     * @param pkTipo Número de identificação do tipo.
     */
    public Tipo(long pkTipo, String nome) {
        this.pkTipo = pkTipo;
        this.nome = nome;
    }

    /**
     * Método construtor da classe Tipo.
     *
     * @param nome Nome do tipo.
     * @param pkTipo Número de identificação do tipo.
     * @param sugestaoDeDescricao Sugestão de descrição de ocorrência desse
     * tipo.
     */
    public Tipo(long pkTipo, String nome, String sugestaoDeDescricao) {
        this.pkTipo = pkTipo;
        this.nome = nome;
        this.sugestaoDeDescricao = sugestaoDeDescricao;
    }

    /**
     * Método construtor da classe Tipo.
     *
     * @param nome Nome do tipo.
     */
    public Tipo(String nome) {
        this.nome = nome;
    }

    /**
     * Método que retorna a identificação do tipo de ocorrência.
     *
     * @return Número de identificação do tipo de ocorrência.
     */
    public long getPkTipo() {
        return pkTipo;
    }

    /**
     * Método que informa a identificação do tipo de ocorrência.
     *
     * @param pkTipo Número de identificação do tipo de ocorrência.
     */
    public void setPkTipo(long pkTipo) {
        this.pkTipo = pkTipo;
    }

    /**
     * Getter nome.
     *
     * @return Nome do tipo.
     */
    public String getNome() {
        return nome;
    }

    /**
     * Setter nome.
     *
     * @param nome Nome do tipo.
     */
    public void setNome(String nome) {
        this.nome = nome;
    }

    /**
     * Getter sugestaoDeDescricao.
     *
     * @return Sugestão de descrição para ocorrências desse mesmo tipo.
     */
    public String getSugestaoDeDescricao() {
        return sugestaoDeDescricao;
    }

    /**
     * Setter sugestaoDeDescricao.
     *
     * @param sugestaoDeDescricao Sugestão de descrição para ocorrências desse
     * mesmo tipo.
     */
    public void setSugestaoDeDescricao(String sugestaoDeDescricao) {
        this.sugestaoDeDescricao = sugestaoDeDescricao;
    }
}
