package br.edu.ifsp.spo.pds2016glybif.servlet;

import static br.edu.ifsp.spo.pds2016glybif.banco.Conexao.Status;
import br.edu.ifsp.spo.pds2016glybif.funcionalidades.FuncoesGerais;
import static br.edu.ifsp.spo.pds2016glybif.funcionalidades.FuncoesGerais.objectToXML;
import java.io.IOException;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.logging.Level;
import br.edu.ifsp.spo.pds2016glybif.modelo.Usuario;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.servlet.http.HttpSession;

/**
 * Classe Servlet usuário.
 *
 * @author Lorhan Sohaky
 */
public class ChamadasUsuario extends HttpServlet {

    private static final long serialVersionUID = 1;
    private static final Logger LOGGER = Logger.getLogger(ChamadasUsuario.class.getName());

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request.
     * @param response servlet response.
     * @throws ServletException if a servlet-specific error occurs.
     * @throws IOException if an I/O error occurs.
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        request.setCharacterEncoding("UTF-8");
        String acao = request.getParameter("acao");
        Status retorno;
        switch (acao) {
            case "CadastrarUsuario":
                retorno = cadastrarUsuario(request);
                break;
            case "Login":
                retorno = login(request);
                break;
            case "Logout":
                retorno = logout(request);
                break;
            case "EsqueciSenha":
                retorno = esqueciSenha(request);
                break;
            case "MudarSenha":
                retorno = mudarSenha(request);
                break;
            default:
                retorno = null;
                break;
        }
        if (retorno == Status.EXISTE) {
            response.sendRedirect(getServletContext().getInitParameter("host") + "erros.jsp?id=7");
        } else if (acao.equals("Login") && retorno == Status.ERRO) {
            response.sendRedirect(getServletContext().getInitParameter("host") + "erros.jsp?id=6");
        } else if (retorno == Status.ERRO) {
            response.sendRedirect(getServletContext().getInitParameter("host") + "erros.jsp?id=3");
        } else if (retorno == null) {
            response.sendRedirect(getServletContext().getInitParameter("host") + "erros.jsp?id=6");
        }
        if (acao.equals("CadastrarUsuario") || acao.equals("Logout") || acao.equals("EsqueciSenha") || acao.equals("MudarSenha")) {
            response.sendRedirect(getServletContext().getInitParameter("host") + "index.jsp");
        } else if (acao.equals("Login") && retorno == Status.OK) {
            response.sendRedirect(getServletContext().getInitParameter("host") + "menu.jsp");
        }
    }

    private Status login(HttpServletRequest request) {
        String email = request.getParameter("email");
        String senha = request.getParameter("pass");
        Status res;
        try {
            String xml = FuncoesGerais.callGet(getServletContext().getInitParameter("host") + "funcoes/usuario/login/" + email + "/" + senha, "GET", "APPLICATION/XML");
            if (!xml.equals("")) {
                Usuario user = FuncoesGerais.xmlToObject(xml, Usuario.class);
                HttpSession session = request.getSession(true);
                session.setAttribute("usuario", user);
                return Status.OK;
            }
        } catch (Exception ex) {
            LOGGER.log(Level.INFO, "Erro ao entrar.", ex);
        }
        return Status.ERRO;
    }

    private Status logout(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        session.invalidate();
        return Status.OK;
    }

    private Status cadastrarUsuario(HttpServletRequest request) {
        Status retorno;
        String nome = request.getParameter("nome");
        String apelido = request.getParameter("apelido");
        String email = request.getParameter("email");
        email = email.toLowerCase(Locale.getDefault());
        String emailc = request.getParameter("confirmeemail");
        String senhac = request.getParameter("confirmesenha");
        String senha = request.getParameter("senha");
        retorno = validaCadastro(nome, senha, senhac, email, emailc);
        if (retorno != Status.OK) {
            return retorno;
        }
        String data = request.getParameter("dataNascimento");
        DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        Date dataNascimento;
        try {
            dataNascimento = formatter.parse(data);
            Usuario user = new Usuario(nome, apelido, email, senha, dataNascimento);
            Status resposta = Status.parseStatus(FuncoesGerais.callSet(getServletContext().getInitParameter("host"), objectToXML(user, Usuario.class), "/usuario/cadastro", "POST", "TEXT/XML", "TEXT/PLAIN"));
            return resposta;
        } catch (Exception ex) {
            LOGGER.log(Level.INFO, "Erro ao inserir usuário", ex);
            return Status.ERRO;
        }
    }

    /**
     * Método que valida os dados fornecidos pelo usuário.
     *
     * @param nome Nome do usuário.
     * @param senha Senha do usuário.
     * @param confirmaSenha Senha confirmada pelo usuário.
     * @param email E-mail do usuário.
     * @param confirmaEmail E-mail confirmado pelo usuário.
     * @return Status Status da execução.
     */
    public Status validaCadastro(String nome, String senha, String confirmaSenha, String email, String confirmaEmail) {
        String emailPattern = "\\b(^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@([A-Za-z0-9-])+(\\.[A-Za-z0-9-]+)*((\\.[A-Za-z0-9]{2,})|(\\.[A-Za-z0-9]{2,}\\.[A-Za-z0-9]{2,}))$)\\b";
        Pattern pattern = Pattern.compile(emailPattern, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(email);
        if (matcher.matches() == true) {
            return Status.OK;
        }
        return null;
    }

    private Status esqueciSenha(HttpServletRequest request) {
        Status retorno = Status.ERRO;
        String dataNascimento = request.getParameter("data");
        String email = request.getParameter("email");
        dataNascimento = dataNascimento.replaceAll("/", "-");
        try {
            retorno = Status.parseStatus(FuncoesGerais.callSet(getServletContext().getInitParameter("host"), getServletContext().getInitParameter("externalURL"), "/usuario/mandaremail/" + email + "/" + dataNascimento, "POST", "TEXT/PLAIN", "TEXT/PLAIN"));
        } catch (Exception ex) {
            LOGGER.log(Level.INFO, "Erro ao enviar e-mail!", ex);
        }
        return retorno;
    }

    private Status mudarSenha(HttpServletRequest request) {
        Status retorno = Status.ERRO;
        long id = Long.parseLong(request.getParameter("id"));
        String senha = request.getParameter("senha");
        String confirme = request.getParameter("confirme");
        if (senha == null || confirme == null || id <= 0 || !senha.equals(confirme) || senha.length() == 0) {
            return retorno;
        }
        try {
            retorno = Status.parseStatus(FuncoesGerais.callGet(getServletContext().getInitParameter("host") + "funcoes/usuario/mudarsenha/" + id + "/" + senha, "PUT", "TEXT/PLAIN"));
        } catch (Exception ex) {
            LOGGER.log(Level.INFO, "Erro ao enviar e-mail", ex);
        }
        return retorno;
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
