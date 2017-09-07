/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.ifsp.spo.pds2016glybif.modelo;

import java.util.Date;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Classe modelo da ocorrência.
 *
 * @author Lorhan Sohaky
 */
@XmlRootElement(name = "ocorrencia")
@XmlAccessorType(XmlAccessType.FIELD)
public class Ocorrencia {

    private long pkOcorrencia;
    @XmlElement(type = Ponto.class)
    private Ponto local;
    private Date dataHora;
    @XmlElement(type = Imagem.class)
    private List<Imagem> listaImagens;
    private boolean transitavelVeiculo;
    private boolean transitavelAPe;
    private String descricao;
    private long qtdExistente;
    private long qtdInexistente;
    private long qtdCasoEncerrado;
    private Date prazo;
    @XmlElement(type = Usuario.class)
    private Usuario usuario;
    private boolean ocorrenciaAtiva;
    @XmlElement(type = Tipo.class)
    private Tipo tipo;

    /**
     * Getter de ocorrenciaAtiva.
     *
     * @return Ocorrência ativa.
     */
    public boolean isOcorrenciaAtiva() {
        return ocorrenciaAtiva;
    }

    /**
     * Setter de ocorrenciaAtiva.
     *
     * @param ocorrenciaAtiva Atributo de ocorrência ativa.
     */
    public void setOcorrenciaAtiva(boolean ocorrenciaAtiva) {
        this.ocorrenciaAtiva = ocorrenciaAtiva;
    }

    /**
     * Método construtor de Ocorrencia.
     */
    public Ocorrencia() {
    }

    /**
     * Método construtor de Ocorrencia.
     *
     * @param pkOcorrencia Número de identificação da ocorrência.
     */
    public Ocorrencia(long pkOcorrencia) {
        this.pkOcorrencia = pkOcorrencia;
    }

    /**
     * Método construtor de Ocorrencia.
     *
     * @param dataHora Data e hora da ocorrência.
     * @param transitavelVeiculo Transitável por meio de Veículo.
     * @param transitavelAPe Transitável a pé.
     * @param descricao Descrição da ocorrência.
     * @param qtdExistente Quantidade de existente.
     * @param qtdInexistente Quantidade de inexistente.
     * @param qtdCasoEncerrado Quantidade de caso encerrado.
     * @param pkOcorrencia Número de identificação da ocorrência
     * @param tipo Tipo da ocorrência.
     * @param user Objeto Usuario.
     */
    public Ocorrencia(Date dataHora, boolean transitavelVeiculo, boolean transitavelAPe, String descricao, long qtdExistente, long qtdInexistente, long qtdCasoEncerrado, long pkOcorrencia, Tipo tipo, Usuario user) {
        this.dataHora = (Date) dataHora.clone();
        this.transitavelVeiculo = transitavelVeiculo;
        this.transitavelAPe = transitavelAPe;
        this.descricao = descricao;
        this.pkOcorrencia = pkOcorrencia;
        this.qtdExistente = qtdExistente;
        this.qtdInexistente = qtdInexistente;
        this.qtdCasoEncerrado = qtdCasoEncerrado;
        this.tipo = tipo;
        this.usuario = user;
    }

    /**
     * Método construtor de Ocorrencia.
     *
     * @param dataHora Data e hora da ocorrência.
     * @param transitavelVeiculo Transitável por meio de Veículo.
     * @param transitavelAPe Transitável a pé.
     * @param descricao Descrição da ocorrência.
     * @param pkOcorrencia Número de identificação da ocorrência.
     * @param tipo Tipo da ocorrência.
     * @param user Objeto Usuario.
     */
    public Ocorrencia(Date dataHora, boolean transitavelVeiculo, boolean transitavelAPe, String descricao, long pkOcorrencia, Tipo tipo, Usuario user) {
        this.pkOcorrencia=pkOcorrencia;
        this.dataHora = (Date) dataHora.clone();
        this.transitavelVeiculo = transitavelVeiculo;
        this.transitavelAPe = transitavelAPe;
        this.descricao = descricao;
        this.tipo = tipo;
        this.usuario = user;
    }

    /**
     * Método construtor de Ocorrencia.
     *
     * @param ponto Local onde se deu a ocorrência.
     * @param transitavelVeiculo Atributo que informa se tem como passar com
     * veículo.
     * @param transitavelAPe Atributo que informa se tem como passar a pé.
     * @param descricao Descrição da ocorrência pelo usuário.
     * @param user Objeto Usuario.
     */
    public Ocorrencia(Ponto ponto, boolean transitavelVeiculo, boolean transitavelAPe, String descricao, Usuario user) {
        this.local = ponto;
        this.transitavelVeiculo = transitavelVeiculo;
        this.transitavelAPe = transitavelAPe;
        this.descricao = descricao;
        this.usuario = user;
    }

    /**
     * Getter PkOcorrencia.
     *
     * @return Número de identificação da ocorrência.
     */
    public long getPkOcorrencia() {
        return pkOcorrencia;
    }

    /**
     * Setter PkOcorrencia.
     *
     * @param pkOcorrencia Número de identificação da ocorrência.
     */
    public void setPkOcorrencia(long pkOcorrencia) {
        this.pkOcorrencia = pkOcorrencia;
    }

    /**
     * Getter local.
     *
     * @return Local da ocorrência.
     */
    public Ponto getLocal() {
        return local;
    }

    /**
     * Setter local.
     *
     * @param local Local da ocorrência.
     */
    public void setLocal(Ponto local) {
        this.local = local;
    }

    /**
     * Getter ocorrenciaAtiva.
     *
     * @return Ocorrência ativa
     */
    public Boolean getOcorrenciaAtiva() {
        return ocorrenciaAtiva;
    }

    /**
     * Setter ocorrenciaAtiva.
     *
     * @param ocorrenciaAtiva Ocorrência ativa.
     */
    public void setOcorrenciaAtiva(Boolean ocorrenciaAtiva) {
        this.ocorrenciaAtiva = ocorrenciaAtiva;
    }

    /**
     * Getter dataHora.
     *
     * @return data e hora da ocorrência.
     */
    public Date getDataHora() {
        if (dataHora == null) {
            return null;
        }
        return (Date) dataHora.clone();
    }

    /**
     * Setter dataHora.
     *
     * @param dataHora Data e hora da ocorrência.
     */
    public void setDataHora(Date dataHora) {
        this.dataHora = (Date) dataHora.clone();
    }

    /**
     * Getter transitavelVeiculo.
     *
     * @return Transitável por meio de veículo.
     */
    public boolean getTransitavelVeiculo() {
        return transitavelVeiculo;
    }

    /**
     * Setter transitavelVeiculo.
     *
     * @param transitavelVeiculo Transitável por meio de veículo.
     */
    public void setTransitavelVeiculo(boolean transitavelVeiculo) {
        this.transitavelVeiculo = transitavelVeiculo;
    }

    /**
     * Getter transitavelAPe.
     *
     * @return Transitável a pé.
     */
    public boolean getTransitavelAPe() {
        return transitavelAPe;
    }

    /**
     * Setter transitavelAPe.
     *
     * @param transitavelAPe Transitável a pé.
     */
    public void setTransitavelAPe(boolean transitavelAPe) {
        this.transitavelAPe = transitavelAPe;
    }

    /**
     * Getter descricao.
     *
     * @return Descrição da ocorrência.
     */
    public String getDescricao() {
        return descricao;
    }

    /**
     * Setter descricao.
     *
     * @param descricao Descrição da ocorrência.
     */
    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    /**
     * Getter qtdExistente.
     *
     * @return Quantidade de existente.
     */
    public long getQtdExistente() {
        return qtdExistente;
    }

    /**
     * Setter qtdExistente.
     *
     * @param qtdExistente Quantidade de existente.
     */
    public void setQtdExistente(long qtdExistente) {
        this.qtdExistente = qtdExistente;
    }

    /**
     * Getter qtdInexistente.
     *
     * @return Quantidade de inexistente.
     */
    public long getQtdInexistente() {
        return qtdInexistente;
    }

    /**
     * Setter qtdInexistente.
     *
     * @param qtdInexistente Quantidade de inexistente.
     */
    public void setQtdInexistente(long qtdInexistente) {
        this.qtdInexistente = qtdInexistente;
    }

    /**
     * Getter qtdCasoEncerrado.
     *
     * @return Quantidade de encerrado.
     */
    public long getQtdCasoEncerrado() {
        return qtdCasoEncerrado;
    }

    /**
     * Setter qtdCasoEncerrado.
     *
     * @param qtdCasoEncerrado Quantidade de encerrado.
     */
    public void setQtdCasoEncerrado(long qtdCasoEncerrado) {
        this.qtdCasoEncerrado = qtdCasoEncerrado;
    }

    /**
     * Getter prazo.
     *
     * @return Prazo da ocorrência.
     */
    public Date getPrazo() {
        if (prazo == null) {
            return null;
        }
        return (Date) prazo.clone();
    }

    /**
     * Setter prazo.
     *
     * @param prazo Prazo da ocorrência.
     */
    public void setPrazo(Date prazo) {
        this.prazo = (Date) prazo.clone();
    }

    /**
     * Getter usuario.
     *
     * @return Objeto Usuário.
     */
    public Usuario getUsuario() {
        return usuario;
    }

    /**
     * Setter usuario.
     *
     * @param usuario Objeto Usuario.
     */
    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    /**
     * Getter lista de imagens.
     *
     * @return lista de imagens.
     */
    public List<Imagem> getListaImagens() {
        return listaImagens;
    }

    /**
     * Setter lista de imagens.
     *
     * @param listaIdImagens Lista de imagens.
     */
    public void setListaImagens(List<Imagem> listaIdImagens) {
        this.listaImagens = listaIdImagens;
    }
}
