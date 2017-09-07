package br.edu.ifsp.spo.pds2016glybif.banco;

import br.edu.ifsp.spo.pds2016glybif.banco.Conexao.Status;
import br.edu.ifsp.spo.pds2016glybif.modelo.Ocorrencia;
import br.edu.ifsp.spo.pds2016glybif.modelo.Ponto;
import br.edu.ifsp.spo.pds2016glybif.modelo.Tipo;
import br.edu.ifsp.spo.pds2016glybif.modelo.Usuario;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Classe que faz intermédio da ocorrência com o banco de dados.
 *
 * @author Lorhan Sohaky
 */
public class OcorrenciaDAO {

    private static final Logger LOGGER = Logger.getLogger(OcorrenciaDAO.class.getName());
    private final Connection conexao;
    private PreparedStatement stmt;
    private ResultSet rs;

    /**
     * Método construtor da classe OcorrenciaDAO.
     *
     * @param con Objeto de conexão.
     */
    public OcorrenciaDAO(Connection con) {
        this.conexao = con;
    }

    /**
     * Insere o cadastro da ocorrencia no banco de dados.
     *
     * @param oco Dados da ocorrência encapsulado pela classe Ocorrencia.
     * @return Status da execução.
     * @throws java.sql.SQLException Erro SQL.
     */
    public Status inserirOcorrencia(Ocorrencia oco) throws SQLException {
        TipoDAO tipo = new TipoDAO(conexao);

        this.stmt = this.conexao.prepareStatement("INSERT INTO tb_ocorrencia (ponto,data_hora,transitavel_veiculo,transitavel_a_pe,descricao,fk_tipo,fk_usuario, prazo,qtd_existente)"
                + "VALUES(POINT(?,?),now(),?,?,?,?,?, now()+INTERVAL'" + tipo.getPrazo(oco.getLocal().getTipo()) + "',1) RETURNING pk_ocorrencia;");
        this.stmt.setDouble(1, oco.getLocal().getLon());
        this.stmt.setDouble(2, oco.getLocal().getLat());
        this.stmt.setBoolean(3, oco.getTransitavelVeiculo());
        this.stmt.setBoolean(4, oco.getTransitavelAPe());
        this.stmt.setString(5, oco.getDescricao());
        this.stmt.setLong(6, oco.getLocal().getTipo());
        this.stmt.setLong(7, oco.getUsuario().getPkUsuario());
        this.rs = this.stmt.executeQuery();
        tipo.close();
        if (this.rs.next()) {
            long pkOcorrencia = this.rs.getLong(1);
            UsuarioDAO user = new UsuarioDAO(conexao);
            user.incrementarQtdResposta(oco.getUsuario().getPkUsuario());
            this.stmt = this.conexao.prepareStatement("INSERT INTO tb_interacao (resposta,data_hora,fk_usuario,fk_ocorrencia) VALUES('E', now(),?,?);");
            this.stmt.setLong(1, oco.getUsuario().getPkUsuario());
            this.stmt.setLong(2, pkOcorrencia);
            this.stmt.execute();
            if (oco.getListaImagens() != null && !oco.getListaImagens().isEmpty()) {
                ImagemDAO img = new ImagemDAO(conexao);
                img.inserirImagem(oco.getListaImagens(), "fk_ocorrencia", pkOcorrencia);
            }
            return Status.OK;
        } else {
            return Status.ERRO;
        }
    }

    /**
     * Método que lista todos os pontos do mapa.
     *
     * @return PesqMap Lista de pontos.
     * @throws SQLException Erro SQL.
     */
    public List<Ponto> listaTodosPontos() throws SQLException {
        List<Ponto> list = new ArrayList<>();
        this.stmt = conexao.prepareStatement("SELECT pk_ocorrencia,ponto[0] AS x,ponto[1] AS y,fk_tipo FROM tb_ocorrencia AS tb_o WHERE ocorrencia_ativa=true ORDER BY tb_o.fk_tipo ASC;");
        this.rs = stmt.executeQuery();
        while (rs.next()) {
            list.add(new Ponto(rs.getInt(1), rs.getDouble(2), rs.getDouble(3), rs.getInt(4)));
        }
        return list;
    }

    /**
     * Método que procura os pontos que estão dentro do retângulo formado pelas
     * coordenadas para serem colocados no mapa.
     *
     * @param latN Latitude Nordeste.
     * @param lonN Longitude Nordeste.
     * @param latS Latitude Sudoeste.
     * @param lonS Longitude Sudoeste.
     * @return PesqMap Lista de pontos.
     * @throws SQLException Erro SQL.
     */
    public List<Ponto> pontoNoMapa(float latN, float lonN, float latS, float lonS) throws SQLException {
        List<Ponto> list = new ArrayList<>();
        String str = "(" + lonN + "," + latN + "),(" + lonS + "," + latS + ")";
        this.stmt = conexao.prepareStatement("SELECT pk_ocorrencia,ponto[0] AS x,ponto[1] AS y,fk_tipo FROM tb_ocorrencia AS tb_o WHERE BOX (?) @> tb_o.ponto AND ocorrencia_ativa=true ORDER BY tb_o.fk_tipo ASC;");
        this.stmt.setString(1, str);
        this.rs = stmt.executeQuery();
        while (rs.next()) {
            list.add(new Ponto(rs.getInt(1), rs.getDouble(2), rs.getDouble(3), rs.getInt(4)));
        }
        return list;
    }

    /**
     * Método que pega as informações de uma ocorrência.
     *
     * @param pkOcorrencia Número de identificação da ocorrência.
     * @return Objeto Ocorrencia.
     * @throws SQLException Erro SQL.
     */
    public Ocorrencia getInfo(long pkOcorrencia) throws SQLException {
        Ocorrencia oco = null;
        ImagemDAO img = new ImagemDAO(conexao);
        this.stmt = conexao.prepareStatement("SELECT data_hora, transitavel_veiculo, transitavel_a_pe,"
                + "descricao, qtd_existente, qtd_inexistente, qtd_caso_encerrado,pk_Ocorrencia, a.nome AS tipo, apelido FROM("
                + "	SELECT * FROM tb_ocorrencia WHERE pk_Ocorrencia=?"
                + ") AS b, tb_usuario AS u, tb_tipo AS a WHERE b.fk_Tipo = a.pk_Tipo AND b.fk_usuario=u.pk_usuario;");
        this.stmt.setLong(1, pkOcorrencia);
        this.rs = stmt.executeQuery();
        while (rs.next()) {
            oco = new Ocorrencia(rs.getTimestamp(1), rs.getBoolean(2), rs.getBoolean(3), rs.getString(4), rs.getInt(5), rs.getInt(6), rs.getInt(7), rs.getInt(8), new Tipo(rs.getString(9)), new Usuario(rs.getString(10)));
            oco.setListaImagens(img.listaImagens("fk_ocorrencia", pkOcorrencia));
        }
        img.close();
        return oco;
    }

    /**
     * Método para verificar o resultado das interações.
     *
     * @param oco Objeto Ocorrencia para ser feita a análise.
     * @return Status da consulta.
     * @throws java.sql.SQLException Erro SQL.
     */
    public Status analiseDasInteracoes(Ocorrencia oco) throws SQLException {
        int porcentagem = 70;
        int credmin = 60;
        this.stmt = conexao.prepareStatement("SELECT tb1.resposta,tb1.avg AS credibilidade, tb2.media AS porcentagem_qtd FROM (SELECT AVG(media),resposta FROM \n"
                + "	(\n"
                + "		SELECT * FROM (\n"
                + "			SELECT pk_usuario,tb.resposta, CAST(qtd_resp_confiaveis AS FLOAT)/CAST(qtd_respostas AS FLOAT)*100*1.2 AS media FROM tb_usuario, \n"
                + "			(\n"
                + "				SELECT tb1.fk_usuario,tb1.resposta FROM (\n"
                + "					SELECT DISTINCT ON (fk_usuario) fk_usuario, resposta, data_hora FROM tb_interacao WHERE fk_ocorrencia=? ORDER BY fk_usuario,data_hora DESC\n"
                + "				) AS tb1, (\n"
                + "					SELECT DISTINCT ON (fk_usuario) ti.fk_usuario,resposta FROM (\n"
                + "						SELECT DISTINCT ON (fk_usuario) fk_usuario, data_hora FROM tb_interacao WHERE fk_ocorrencia=? ORDER BY fk_usuario,data_hora DESC\n"
                + "					) AS tb, tb_interacao AS ti WHERE tb.fk_usuario=ti.fk_usuario AND fk_ocorrencia=? AND ti.data_hora<tb.data_hora ORDER BY ti.fk_usuario,ti.data_hora DESC\n"
                + "				) AS tb2 WHERE tb1.resposta='C' AND tb2.resposta='E' AND tb1.fk_usuario=tb2.fk_usuario\n"
                + "			) AS tb WHERE tb.fk_usuario=pk_usuario\n"
                + "		) AS tb UNION (\n"
                + "				SELECT pk_usuario,ti.resposta, CAST(qtd_resp_confiaveis AS FLOAT)/CAST(qtd_respostas AS FLOAT)*100 AS media FROM tb_usuario as tu,(\n"
                + "					SELECT DISTINCT ON (fk_usuario) fk_usuario, pk_interacao, resposta FROM tb_interacao WHERE fk_ocorrencia=? ORDER BY fk_usuario,data_hora DESC\n"
                + "				) AS tbu, tb_interacao AS ti LEFT JOIN\n"
                + "				(\n"
                + "					SELECT fk_usuario FROM \n"
                + "					(\n"
                + "						SELECT tb1.fk_usuario,tb1.resposta FROM (\n"
                + "							SELECT DISTINCT ON (fk_usuario) fk_usuario, resposta, data_hora FROM tb_interacao WHERE fk_ocorrencia=? ORDER BY fk_usuario,data_hora DESC\n"
                + "						) AS tb1, (\n"
                + "							SELECT DISTINCT ON (fk_usuario) ti.fk_usuario,resposta FROM (\n"
                + "								SELECT DISTINCT ON (fk_usuario) fk_usuario, data_hora FROM tb_interacao WHERE fk_ocorrencia=? ORDER BY fk_usuario,data_hora DESC\n"
                + "							) AS tb, tb_interacao AS ti WHERE tb.fk_usuario=ti.fk_usuario AND fk_ocorrencia=? AND ti.data_hora<tb.data_hora ORDER BY ti.fk_usuario,ti.data_hora DESC\n"
                + "						) AS tb2 WHERE tb1.resposta='C' AND tb2.resposta='E' AND tb1.fk_usuario=tb2.fk_usuario\n"
                + "					) AS tb WHERE tb.fk_usuario=fk_usuario\n"
                + "				) AS tb ON ti.fk_usuario=tb.fk_usuario WHERE ti.fk_ocorrencia=? AND tu.pk_usuario=ti.fk_usuario AND tbu.pk_interacao=ti.pk_interacao AND tb.fk_usuario IS NULL\n"
                + "		)\n"
                + "	) as tb GROUP BY tb.resposta) AS tb1,(\n"
                + "		(SELECT 'E' AS resposta, CAST (Qtd_Existente AS FLOAT)/(CAST (Qtd_Existente + Qtd_Inexistente + Qtd_Caso_Encerrado AS FLOAT))*100 AS media FROM tb_ocorrencia WHERE pk_ocorrencia=?)\n"
                + "		UNION\n"
                + "		(SELECT 'I' AS resposta, CAST (Qtd_Inexistente AS FLOAT)/(CAST (Qtd_Existente + Qtd_Inexistente + Qtd_Caso_Encerrado AS FLOAT))*100 AS media FROM tb_ocorrencia WHERE pk_ocorrencia=?)\n"
                + "		UNION\n"
                + "		(SELECT 'C' AS resposta, CAST(Qtd_Caso_Encerrado AS FLOAT)/(CAST (Qtd_Existente + Qtd_Inexistente + Qtd_Caso_Encerrado AS FLOAT))*100 AS media FROM tb_ocorrencia WHERE pk_ocorrencia=?)) AS tb2 WHERE\n"
                + "	tb1.resposta=tb2.resposta ORDER BY tb1.resposta ASC;");
        this.stmt.setLong(1, oco.getPkOcorrencia());
        this.stmt.setLong(2, oco.getPkOcorrencia());
        this.stmt.setLong(3, oco.getPkOcorrencia());
        this.stmt.setLong(4, oco.getPkOcorrencia());
        this.stmt.setLong(5, oco.getPkOcorrencia());
        this.stmt.setLong(6, oco.getPkOcorrencia());
        this.stmt.setLong(7, oco.getPkOcorrencia());
        this.stmt.setLong(8, oco.getPkOcorrencia());
        this.stmt.setLong(9, oco.getPkOcorrencia());
        this.stmt.setLong(10, oco.getPkOcorrencia());
        this.stmt.setLong(11, oco.getPkOcorrencia());
        this.rs = stmt.executeQuery();
        List<Analise> list = new ArrayList<>();
        int e = -1, i = -1, c = -1;
        while (rs.next()) {
            Analise an = new Analise(rs.getString("resposta"), rs.getDouble("credibilidade"), rs.getDouble("porcentagem_qtd"));
            switch (an.getResposta()) {
                case "E":
                    e = list.size();
                    break;
                case "I":
                    i = list.size();
                    break;
                case "C":
                    c = list.size();
                    break;
                default:

            }
            list.add(an);
        }
        UsuarioDAO user = new UsuarioDAO(conexao);
        if (i != -1 && e != -1 && list.get(i).getCredibilidade() > list.get(e).getCredibilidade() && list.get(i).getMediaQtd() >= list.get(e).getMediaQtd()) {
            finalizaOcorrencia(oco);
            user.pontuaUsuarios(oco, list.get(i).getResposta());
        } else if (c != -1 && e != -1 && list.get(c).getCredibilidade() > list.get(e).getCredibilidade() && list.get(c).getMediaQtd() >= list.get(e).getMediaQtd()) {
            finalizaOcorrencia(oco);
            user.pontuaUsuarios(oco, list.get(c).getResposta());
        } else if (i != -1 && e != -1 && list.get(i).getCredibilidade() >= credmin && list.get(i).getMediaQtd() >= porcentagem) {
            finalizaOcorrencia(oco);
            user.pontuaUsuarios(oco, list.get(i).getResposta());
        } else if (c != -1 && e != -1 && list.get(c).getCredibilidade() >= credmin && list.get(c).getMediaQtd() >= porcentagem) {
            finalizaOcorrencia(oco);
            user.pontuaUsuarios(oco, list.get(c).getResposta());
        }
        return Status.OK;
    }

    /**
     * Método para finalizar ocorrencia.
     *
     * @param oco Objeto Ocorrencia para finalizar ocorrência.
     * @return Status da execução.
     * @throws SQLException Erro SQL.
     */
    public Status finalizaOcorrencia(Ocorrencia oco) throws SQLException {
        this.stmt = conexao.prepareStatement("UPDATE tb_ocorrencia SET ocorrencia_ativa=false WHERE pk_ocorrencia=?;");
        this.stmt.setLong(1, oco.getPkOcorrencia());
        this.stmt.executeUpdate();
        return Status.OK;
    }

    private static class Analise {

        private final String resposta;
        private final double credibilidade;
        private final double mediaQtd;

        public Analise(String resposta, double credibilidade, double mediaQtd) {
            this.resposta = resposta;
            this.credibilidade = credibilidade;
            this.mediaQtd = mediaQtd;
        }

        public String getResposta() {
            return resposta;
        }

        public double getCredibilidade() {
            return credibilidade;
        }

        public double getMediaQtd() {
            return mediaQtd;
        }
    }

    /**
     * Método para fechar o PreparedStatement e ResultSet
     */
    public void close() {
        try {
            if (rs != null) {
                this.rs.close();
            }
            if (stmt != null) {
                this.stmt.close();
            }
        } catch (SQLException ex) {
            LOGGER.log(Level.INFO, "Erro ao fechar.", ex);
        }
    }
}
