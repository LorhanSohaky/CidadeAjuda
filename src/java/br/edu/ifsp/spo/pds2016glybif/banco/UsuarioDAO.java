package br.edu.ifsp.spo.pds2016glybif.banco;

import br.edu.ifsp.spo.pds2016glybif.banco.Conexao.Status;
import br.edu.ifsp.spo.pds2016glybif.funcionalidades.FuncoesGerais;
import br.edu.ifsp.spo.pds2016glybif.modelo.Ocorrencia;
import br.edu.ifsp.spo.pds2016glybif.modelo.Usuario;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.mail.MessagingException;

/**
 * Classe que faz intermédio do usuário com o banco de dados.
 *
 * @author Lorhan Sohaky
 */
public class UsuarioDAO {

    private static final Logger LOGGER = Logger.getLogger(UsuarioDAO.class.getName());
    private final Connection conexao;
    private PreparedStatement stmt;
    private ResultSet rs;

    /**
     * Método construtor da classe UsuarioDAO.
     *
     * @param con Objeto de conexão.
     */
    public UsuarioDAO(Connection con) {
        this.conexao = con;
    }

    /**
     * Insere o cadastro do usuário no banco de dados.
     *
     * @param user Dados do usuário encapsulado pela classe Usuario.
     * @return Status da execução.
     * @throws java.sql.SQLException Erro SQL.
     */
    public Status inserirUsuario(Usuario user) throws SQLException {
        Status existe = this.existeEmail(user.getEmail());
        if (existe != Status.OK) {
            return existe;
        }
        java.sql.Date data = new java.sql.Date(user.getDataNascimento().getTime());

        this.stmt = this.conexao.prepareStatement("INSERT INTO tb_usuario (nome,apelido,email,"
                + "senha,data_nascimento) VALUES (?,?,?,MD5(?),?)");
        this.stmt.setString(1, user.getNome());
        this.stmt.setString(2, user.getApelido());
        this.stmt.setString(3, user.getEmail());
        this.stmt.setString(4, user.getSenha());
        this.stmt.setDate(5, data);
        this.stmt.executeUpdate();
        return Status.OK;
    }

    /**
     * Verifica se o e-mail e senha confere com o que há no banco de dados.
     *
     * @param email String e-mail do usuário.
     * @param senha String senha do usuário.
     * @return Usuario Objeto Usuario.
     * @throws java.sql.SQLException Erro SQL.
     */
    public Usuario login(String email, String senha) throws SQLException {
        Usuario user = null;
        this.stmt = this.conexao.prepareStatement("SELECT pk_usuario, nome, apelido FROM tb_usuario WHERE email=? AND "
                + "senha=MD5(?)");
        this.stmt.setString(1, email);
        this.stmt.setString(2, senha);
        this.rs = this.stmt.executeQuery();
        if (rs.next()) {
            user = new Usuario(rs.getLong("pk_usuario"), rs.getString("nome"), rs.getString("apelido"));
        }
        return user;
    }

    /**
     * Verifica se o e-mail já está cadastrado no banco de dados.
     *
     * @param email E-mail do usuário.
     * @return Status da execução.
     * @throws java.sql.SQLException Erro SQL.
     */
    public Status existeEmail(String email) throws SQLException {
        this.stmt = this.conexao.prepareStatement("SELECT pk_usuario FROM tb_usuario WHERE email=?");
        this.stmt.setString(1, email);
        this.rs = this.stmt.executeQuery();
        if (rs.next()) {
            return Status.EXISTE;
        } else {
            return Status.OK;
        }
    }

    /**
     * Método para alterar a senha do usuário que esqueceu a senha.
     *
     * @param id Número de indentificação do usuário.
     * @param senha Nova senha do usuário.
     * @throws SQLException Erro SQL.s
     */
    public void mudarSenha(long id, String senha) throws SQLException {
        this.stmt = this.conexao.prepareStatement("UPDATE tb_usuario SET senha=MD5(?) WHERE pk_usuario=?;");
        this.stmt.setString(1, senha);
        this.stmt.setLong(2, id);
        this.stmt.executeUpdate();
    }

    /**
     * Método para validar se o usuário entrou no site dentro do prazo de 30
     * minutos.
     *
     * @param id Número de identificação da mudança.
     * @return Número de identificação do usuário.
     * @throws SQLException Erro SQL;
     */
    public long validaMudancaSenha(long id) throws SQLException {
        this.stmt = this.conexao.prepareStatement("SELECT fk_usuario FROM tb_muda_senha WHERE pk_muda_senha=? AND prazo>=now();");
        this.stmt.setLong(1, id);
        this.rs = this.stmt.executeQuery();
        if (rs.next()) {
            return rs.getLong("fk_usuario");
        }
        return 0;
    }

    /**
     * Método para enviar e-mail ao usuário.
     *
     * @param email E-mail para qual será mandada a mensagem.
     * @param url Endereço do site.
     * @return Resultado da execução.
     * @throws SQLException Erro SQL.
     * @throws MessagingException Erro.
     */
    public Status enviarEmail(String email, String url) throws SQLException, MessagingException {
        this.stmt = this.conexao.prepareStatement("SELECT pk_usuario, nome, email FROM tb_usuario WHERE email=?;");
        this.stmt.setString(1, email);
        this.rs = this.stmt.executeQuery();
        if (rs.next()) {
            long id = rs.getLong("pk_usuario");
            String nome = rs.getString("nome");
            String para = rs.getString("email");
            this.stmt = this.conexao.prepareStatement("INSERT INTO tb_muda_senha (prazo,fk_usuario) VALUES (now()+INTERVAL '30 minute',?);");
            this.stmt.setLong(1, id);
            this.stmt.executeUpdate();
            this.stmt = this.conexao.prepareStatement("SELECT pk_muda_senha FROM tb_muda_senha WHERE fk_usuario=? ORDER BY pk_muda_senha DESC LIMIT 1;");
            this.stmt.setLong(1, id);
            this.rs = this.stmt.executeQuery();
            if (rs.next()) {
                long pk = rs.getLong("pk_muda_senha");
                FuncoesGerais.enviarEmail(nome, para, url + "mudarsenha.jsp?id=" + pk);
                return Status.OK;
            }
        }
        return Status.ERRO;
    }

    /**
     * Método que verifica se o e-mail e data de nascimento fornecidos pelo
     * usuário constam no banco de dados.
     *
     * @param email E-mail do usuário.
     * @param dataNascimento Data de nascimento do usuário.
     * @return Resultado da execução.
     * @throws SQLException Erro SQL.
     */
    public Status verificaEmilEDataNascimento(String email, String dataNascimento) throws SQLException {
        this.stmt = this.conexao.prepareStatement("SELECT pk_usuario FROM tb_usuario WHERE email=? AND data_nascimento=TO_DATE(?,'DD-MM-YYYY');");
        this.stmt.setString(1, email);
        this.stmt.setString(2, dataNascimento);
        this.rs = this.stmt.executeQuery();
        if (rs.next()) {
            return Status.OK;
        }
        return Status.DIFERENTE;
    }

    /**
     * Método para imcrementar o atributo quantidade de respostas.
     *
     * @param pkUsuario Número de identificação do usuário.
     * @return Status da execução.
     * @throws SQLException Erro SQL.
     */
    public Status incrementarQtdResposta(long pkUsuario) throws SQLException {
        this.stmt = this.conexao.prepareStatement("UPDATE tb_usuario SET qtd_respostas=qtd_respostas+1 WHERE pk_usuario=?;");
        this.stmt.setLong(1, pkUsuario);
        this.stmt.executeUpdate();
        return Status.OK;
    }

    /**
     * Método para pontuar os usuários que forneceram a resposta com mais
     * credibilidade.
     *
     * @param oco Objeto Ocorrencia para pontuar usuários.
     * @param resposta Resposta do usuário.
     * @return Status da execução.
     * @throws SQLException Erro SQL.
     */
    public Status pontuaUsuarios(Ocorrencia oco, String resposta) throws SQLException {
        this.stmt = conexao.prepareStatement("UPDATE tb_usuario SET qtd_resp_confiaveis=qtd_resp_confiaveis+1 FROM ("
                + "SELECT fk_usuario FROM ("
                + "SELECT DISTINCT ON (fk_usuario) fk_usuario, resposta FROM tb_interacao WHERE fk_ocorrencia=1 ORDER BY fk_usuario,data_hora DESC"
                + ") AS tb WHERE resposta=?"
                + ") AS tb WHERE tb.fk_usuario=pk_usuario;");
        this.stmt.setString(1, resposta);
        this.stmt.executeUpdate();
        return Status.OK;
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
