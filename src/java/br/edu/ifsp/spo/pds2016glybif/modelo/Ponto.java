/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.ifsp.spo.pds2016glybif.modelo;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Classe modelo do ponto.
 *
 * @author Lorhan Sohaky
 */
@XmlRootElement(name = "ponto")
@XmlAccessorType(XmlAccessType.FIELD)
public class Ponto {

    private long pkPonto;
    private double lon;
    private double lat;
    private long tipo;

    /**
     * Método construtor da classe Ponto.
     *
     * @param pkPonto Número de identificação do ponto.
     * @param lon Longitude.
     * @param lat Latitude.
     * @param tipo Tipo de ocorrência.
     */
    public Ponto(long pkPonto, double lon, double lat, long tipo) {
        this.pkPonto = pkPonto;
        this.lon = lon;
        this.lat = lat;
        this.tipo = tipo;
    }

    /**
     * Método construtor da classe Ponto.
     *
     * @param lon Longitude.
     * @param lat Latitude.
     */
    public Ponto(double lon, double lat) {
        this.lon = lon;
        this.lat = lat;
    }

    /**
     * Método construtor da classe Ponto.
     *
     * @param lon Longitude.
     * @param lat Latitude.
     * @param tipo Tipo de ocorrência.
     */
    public Ponto(double lon, double lat, long tipo) {
        this.lon = lon;
        this.lat = lat;
        this.tipo = tipo;
    }

    /**
     * Método construtor da classe Ponto.
     */
    public Ponto() {
        this.lon = 0;
        this.lat = 0;
        this.tipo = 0;
    }

    /**
     * Método que retorna a longitude.
     *
     * @return Longitude.
     */
    public double getLon() {
        return lon;
    }

    /**
     * Método que informa a longitude.
     *
     * @param lon Longitude.
     */
    public void setLon(double lon) {
        this.lon = lon;
    }

    /**
     * Método que retorna a identificação do ponto.
     *
     * @return Número de identificação do ponto.
     */
    public long getPkPonto() {
        return pkPonto;
    }

    /**
     * Método que informa a identificação do ponto.
     *
     * @param pkPonto Número de identificação do ponto.
     */
    public void setPkPonto(long pkPonto) {
        this.pkPonto = pkPonto;
    }

    /**
     * Método que retorna a latitude.
     *
     * @return Latitude.
     */
    public double getLat() {
        return lat;
    }

    /**
     * Método que informa a latitude.
     *
     * @param lat Latitude.
     */
    public void setLat(double lat) {
        this.lat = lat;
    }

    /**
     * Método que retorna o tipo de ocorrência.
     *
     * @return Tipo de ocorrência.
     */
    public long getTipo() {
        return tipo;
    }

    /**
     * Método que informa o tipo de ocorrência.
     *
     * @param tipo Tipo de ocorrência.
     */
    public void setTipo(long tipo) {
        this.tipo = tipo;
    }

}
