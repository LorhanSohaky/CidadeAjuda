package br.edu.ifsp.spo.pds2016glybif.modelo;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Lorhan Sohaky
 */
@XmlRootElement(name = "imagem")
@XmlAccessorType(XmlAccessType.FIELD)
public class Imagem {

    private long pkImagem;
    private byte[] imagem;
    private long fkOcorrencia;
    private long fkComentario;

    /**
     * Método construtor da classe Imagem.
     */
    public Imagem() {
    }

    /**
     * Método construtor da classe Imagem.
     *
     * @param imagem Imagem do local.
     */
    public Imagem(byte[] imagem) {
        this.imagem = (byte[]) imagem.clone();
    }

    /**
     * Método construtor da classe Imagem.
     *
     * @param pkImagem Número de identificação das imagem.
     * @param imagem Imagem do local.
     */
    public Imagem(long pkImagem, byte[] imagem) {
        this.pkImagem = pkImagem;
        this.imagem = (byte[]) imagem.clone();
    }

    /**
     * Método construtor da classe Imagem.
     *
     * @param pkImagem Número de identificação da imagem do local.
     */
    public Imagem(long pkImagem) {
        this.pkImagem = pkImagem;
    }

    /**
     * Getter imagem.
     *
     * @return Imagem do local.
     */
    public byte[] getImagem() {
        return imagem;
    }

    /**
     * Setter imagem.
     *
     * @param imagem Imagem do local.
     */
    public void setImagem(byte[] imagem) {
        this.imagem = imagem;
    }

    /**
     * Getter pkImagem.
     *
     * @return Número de identificação da imagem.
     */
    public long getPkImagem() {
        return pkImagem;
    }

    /**
     * Setter imagem.
     *
     * @param pkImagem Número de identificação da imagem.
     */
    public void setPkImagem(long pkImagem) {
        this.pkImagem = pkImagem;
    }

    /**
     * Getter fkOcorrencia.
     *
     * @return Número de identificação da ocorrência.
     */
    public long getFkOcorrencia() {
        return fkOcorrencia;
    }

    /**
     * Setter fkOcorrencia.
     *
     * @param fkOcorrencia Número de identificação da ocorrência.
     */
    public void setFkOcorrencia(long fkOcorrencia) {
        this.fkOcorrencia = fkOcorrencia;
    }

    /**
     * Getter fkComentario.
     *
     * @return Número de identificação do comentário.
     */
    public long getFkComentario() {
        return fkComentario;
    }

    /**
     * Setter fkComentario.
     *
     * @param fkComentario Número de identificação do comentário.
     */
    public void setFkComentario(long fkComentario) {
        this.fkComentario = fkComentario;
    }

}
