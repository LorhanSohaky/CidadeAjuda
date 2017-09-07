/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.ifsp.spo.pds2016glybif.modelo;

import java.util.Date;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Classe modelo do usuário.
 *
 * @author Lorhan Sohaky
 */
@XmlRootElement
public class Usuario {

    private long pkUsuario;
    private String nome;
    private String apelido;
    private String email;
    private String senha;
    private Date dataNascimento;
    private long qtdRespostas;
    private long qtdRespConfiaveis;

    /**
     * Método construtor do Usuario.
     *
     * @param nome Nome do usuário.
     * @param apelido Apelido do usuário.
     * @param email E-mail do usuário.
     * @param senha Senha do usuário.
     * @param dataNascimento Data de nascimento do usuário.
     */
    public Usuario(String nome, String apelido, String email, String senha, Date dataNascimento) {
        this.nome = nome;
        this.apelido = apelido;
        this.email = email;
        this.senha = senha;
        this.dataNascimento = (Date) dataNascimento.clone();
    }

    /**
     * Método construtor do Usuario.
     *
     * @param pkUsuario Número de identificação do usuário.
     */
    public Usuario(long pkUsuario) {
        this.pkUsuario = pkUsuario;
    }

    /**
     * Método construtor do Usuario.
     *
     * @param apelido Apelido do usuário
     */
    public Usuario(String apelido) {
        this.apelido = apelido;
    }

    /**
     * Método construtor do Usuario.
     *
     * @param pkUsuario Número de identificação do usuário.
     * @param nome Nome do usuário.
     * @param apelido Apelido do usuário.
     */
    public Usuario(long pkUsuario, String nome, String apelido) {
        this.pkUsuario = pkUsuario;
        this.nome = nome;
        this.apelido = apelido;
    }

    /**
     * Construtor Usuario.
     */
    public Usuario() {
    }

    /**
     * Getter pkUsuario.
     *
     * @return Número de identificação do usuário
     */
    public long getPkUsuario() {
        return pkUsuario;
    }

    /**
     * Setter pkUsuario.
     *
     * @param pkUsuario Número de identificação do usuário.
     */
    public void setPkUsuario(long pkUsuario) {
        this.pkUsuario = pkUsuario;
    }

    /**
     * Getter nome.
     *
     * @return Nome do usuário.
     */
    public String getNome() {
        return nome;
    }

    /**
     * Setter nome.
     *
     * @param nome Nome do usuário.
     */
    public void setNome(String nome) {
        this.nome = nome;
    }

    /**
     * Getter apelido.
     *
     * @return Apelido do usuário.
     */
    public String getApelido() {
        return apelido;
    }

    /**
     * Setter apelido.
     *
     * @param apelido Apelido do usuário.
     */
    public void setApelido(String apelido) {
        this.apelido = apelido;
    }

    /**
     * Getter email.
     *
     * @return E-mail do usuário.
     */
    public String getEmail() {
        return email;
    }

    /**
     * Setter email.
     *
     * @param email E-mail do usuário.
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Getter senha.
     *
     * @return Senha do usuário.
     */
    public String getSenha() {
        return senha;
    }

    /**
     * Setter senha.
     *
     * @param senha Senha do usuário.
     */
    public void setSenha(String senha) {
        this.senha = senha;
    }

    /**
     * Getter dataNascimento.
     *
     * @return Data de nascimento do usuário.
     */
    public Date getDataNascimento() {
        if (dataNascimento == null) {
            return null;
        } else {
            return (Date) dataNascimento.clone();
        }
    }

    /**
     * Setter dataNascimento.
     *
     * @param dataNascimento Data de nascimento do usuário.
     */
    public void setDataNascimento(Date dataNascimento) {
        this.dataNascimento = (Date) dataNascimento.clone();
    }

    /**
     * Getter qtdRespostas.
     *
     * @return Quantidade de respostas do usuário.
     */
    public long getQtdRespostas() {
        return qtdRespostas;
    }

    /**
     * Setter qtdRespostas.
     *
     * @param qtdRespostas Quantidade de respostas do usuário.
     */
    public void setQtdRespostas(long qtdRespostas) {
        this.qtdRespostas = qtdRespostas;
    }

    /**
     * Getter qtdRespConfiaveis.
     *
     * @return Quantidade de respostas confiáveis do usuário.
     */
    public long getQtdRespConfiaveis() {
        return qtdRespConfiaveis;
    }

    /**
     * Setter qtdRespConfiaveis.
     *
     * @param qtdRespConfiaveis Quantidade de respostas confiáveis do usuário.
     */
    public void setQtdRespConfiaveis(long qtdRespConfiaveis) {
        this.qtdRespConfiaveis = qtdRespConfiaveis;
    }
}
