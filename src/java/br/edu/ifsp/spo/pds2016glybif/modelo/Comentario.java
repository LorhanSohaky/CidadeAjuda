package br.edu.ifsp.spo.pds2016glybif.modelo;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Classe modelo Comentário.
 *
 * @author Lorhan Sohaky
 */
@XmlRootElement(name = "comentario")
@XmlAccessorType(XmlAccessType.FIELD)
public class Comentario {

    private long pkComentario;
    private String comentario;
    private Date dataHora;
    @XmlElement(type = Usuario.class)
    private Usuario usuario;
    @XmlElement(type = Ocorrencia.class)
    private Ocorrencia ocorrencia;
    @XmlElement(type = Imagem.class)
    private List<Imagem> listaImagens = new ArrayList<>();

    /**
     * Método contrutor da classe Comentario.
     */
    public Comentario() {
    }

    /**
     * Método contrutor da classe Comentario.
     *
     * @param pkComentario Número de identificação do comentário.
     * @param comentario Texto do comentário.
     * @param apelido Apelido do usuário.
     * @param dataHora Data e hora.
     */
    public Comentario(long pkComentario, String comentario, String apelido, Date dataHora) {
        this.pkComentario = pkComentario;
        this.comentario = comentario;
        this.usuario = new Usuario(apelido);
        this.dataHora = (Date) dataHora.clone();
    }

    /**
     * Método contrutor da classe Comentario.
     *
     * @param pkComentario Número de identificação do comentário.
     * @param comentario Texto do comentário.
     * @param dataHora Data e hora.
     */
    public Comentario(long pkComentario, String comentario, Date dataHora) {
        this.pkComentario = pkComentario;
        this.comentario = comentario;
        this.dataHora = (Date) dataHora.clone();
    }

    /**
     * Método contrutor da classe Comentario.
     *
     * @param pkComentario Número de identificação do comentário.
     * @param comentario Texto do comentário.
     */
    public Comentario(long pkComentario, String comentario) {
        this.pkComentario = pkComentario;
        this.comentario = comentario;
    }

    /**
     * Método contrutor da classe Comentario.
     *
     * @param pkComentario Número de identificação do comentário.
     * @param comentario Texto do comentário.
     * @param pkUsuario Número de identificação do usuário.
     */
    public Comentario(long pkComentario, String comentario, long pkUsuario) {
        this.pkComentario = pkComentario;
        this.comentario = comentario;
        this.usuario = new Usuario(pkUsuario);
    }

    /**
     * Método contrutor da classe Comentario.
     *
     * @param comentario Texto do comentário.
     * @param pkUsuario Número de identificação do usuário.
     * @param pkOcorrencia Número de identificação da ocorrência.
     */
    public Comentario(String comentario, long pkUsuario, long pkOcorrencia) {
        this.comentario = comentario;
        this.ocorrencia = new Ocorrencia(pkOcorrencia);
        this.usuario = new Usuario(pkUsuario);
    }

    /**
     * Método contrutor da classe Comentario.
     *
     * @param comentario Texto do comentário.
     */
    public Comentario(String comentario) {
        this.comentario = comentario;
    }

    /**
     * Getter pkComentario.
     *
     * @return Número de identifiação do comentário.
     */
    public long getPkComentario() {
        return pkComentario;
    }

    /**
     * Setter pkComentário.
     *
     * @param pkComentario Número de identificação do comentário.
     */
    public void setPkComentario(long pkComentario) {
        this.pkComentario = pkComentario;
    }

    /**
     * Getter comentario.
     *
     * @return Texto do comentário.
     */
    public String getComentario() {
        return comentario;
    }

    /**
     * Setter comentario.
     *
     * @param comentario Texto do comentário.
     */
    public void setComentario(String comentario) {
        this.comentario = comentario;
    }

    /**
     * Getter listaImagens.
     *
     * @return Lista de objetos Imagem.
     */
    public List<Imagem> getListaImagens() {
        return listaImagens;
    }

    /**
     * Setter listaImagens.
     *
     * @param listaImagens Lista de objetos Imagem.
     */
    public void setListaImagens(List<Imagem> listaImagens) {
        this.listaImagens = listaImagens;
    }

    /**
     * Getter usuario.
     *
     * @return Objeto Usuario.
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
     * Getter ocorrencia.
     *
     * @return Objeto ocorrencia.
     */
    public Ocorrencia getOcorrencia() {
        return ocorrencia;
    }

    /**
     * Setter ocorrencia.
     *
     * @param ocorrencia Objeto Ocorrencia.
     */
    public void setOcorrencia(Ocorrencia ocorrencia) {
        this.ocorrencia = ocorrencia;
    }

    /**
     * Getter dataHora.
     *
     * @return Data e hora em que foi realizado o comentário.
     */
    public Date getDataHora() {
        return (Date) dataHora.clone();
    }

    /**
     * Setter dataHora.
     *
     * @param dataHora Data e hora em que foi realizado o comentário.
     */
    public void setDataHora(Date dataHora) {
        this.dataHora = (Date) dataHora.clone();
    }

}
