/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.ifsp.spo.pds2016glybif.servlet;

import br.edu.ifsp.spo.pds2016glybif.banco.Conexao;
import br.edu.ifsp.spo.pds2016glybif.banco.Conexao.Status;
import br.edu.ifsp.spo.pds2016glybif.funcionalidades.FuncoesGerais;
import static br.edu.ifsp.spo.pds2016glybif.funcionalidades.FuncoesGerais.objectToXML;
import br.edu.ifsp.spo.pds2016glybif.modelo.Interacao;
import br.edu.ifsp.spo.pds2016glybif.modelo.Usuario;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Classe Servlet interação.
 *
 * @author Lorhan Sohaky
 */
public class ChamadasInteracao extends HttpServlet {

    private static final Logger LOGGER = Logger.getLogger(ChamadasInteracao.class.getName());
    private static final long serialVersionUID = 1;

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
        String acao = request.getParameter("acao");
        Conexao.Status retorno = null;
        switch (acao) {
            case "CadastrarInteracao":
                retorno = registrarInteracao(request);
                if (retorno == Status.EXISTE) {
                    response.sendRedirect(getServletContext().getInitParameter("host") + "erros.jsp?id=2");
                }
                break;
            default:
                response.sendRedirect(getServletContext().getInitParameter("host") + "erros.jsp?id=5");
                break;
        }
        if (retorno != Status.OK && retorno != null) {
            response.sendRedirect(getServletContext().getInitParameter("host") + "erros.jsp?id=4");
        } else if (retorno == null) {
            response.sendRedirect(getServletContext().getInitParameter("host") + "erros.jsp?id=3");
        }
    }

    private Status registrarInteracao(HttpServletRequest request) {
        Usuario user = (Usuario) request.getSession().getAttribute("usuario");
        String resposta = request.getParameter("resposta");
        long ocorrencia = Long.parseLong(request.getParameter("ocorrencia"));
        Interacao inte = new Interacao(resposta, ocorrencia, user.getPkUsuario());
        try {
            return Status.parseStatus(FuncoesGerais.callSet(getServletContext().getInitParameter("host"), objectToXML(inte, Interacao.class), "/interacao/registrar", "POST", "TEXT/XML", "TEXT/PLAIN"));
        } catch (Exception ex) {
            LOGGER.log(Level.INFO, "Erro ao registrar interação.", ex);
        }
        return Status.ERRO;
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
