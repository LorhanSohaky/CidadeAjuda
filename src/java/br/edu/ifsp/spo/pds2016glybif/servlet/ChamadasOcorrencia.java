/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.ifsp.spo.pds2016glybif.servlet;

import br.edu.ifsp.spo.pds2016glybif.banco.Conexao.Status;
import br.edu.ifsp.spo.pds2016glybif.funcionalidades.FuncoesGerais;
import static br.edu.ifsp.spo.pds2016glybif.funcionalidades.FuncoesGerais.objectToXML;
import br.edu.ifsp.spo.pds2016glybif.modelo.Ocorrencia;
import br.edu.ifsp.spo.pds2016glybif.modelo.Ponto;
import br.edu.ifsp.spo.pds2016glybif.modelo.Usuario;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Classe Servlet ocorrência.
 *
 * @author Lorhan Sohaky
 */
public class ChamadasOcorrencia extends HttpServlet {

    private static final Logger LOGGER = Logger.getLogger(ChamadasOcorrencia.class.getName());
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
        request.setCharacterEncoding("UTF-8");
        String acao = request.getParameter("acao");
        Status retorno = null;
        switch (acao) {
            case "CadastrarOcorrencia":
                retorno = cadastrarOcorrencia(request);
                break;
            default:
                response.sendRedirect(getServletContext().getInitParameter("host") + "erros.jsp?id=5");
                break;
        }
        if (retorno != Status.OK && retorno != null) {
            response.sendRedirect(getServletContext().getInitParameter("host") + "erros.jsp?id=3");
        } else if (retorno == null) {
            response.sendRedirect(getServletContext().getInitParameter("host") + "erros.jsp?id=1");
        } else {
            switch (acao) {
                case "CadastrarOcorrencia":
                    response.sendRedirect(getServletContext().getInitParameter("host") + "index.jsp");
                    break;
                default:
                    response.sendRedirect(getServletContext().getInitParameter("host") + "erros.jsp?id=5");
                    break;
            }
        }

    }

    private Status cadastrarOcorrencia(HttpServletRequest request) {
        Usuario user = (Usuario) request.getSession().getAttribute("usuario");
        if (user == null) {
            return null;
        }
        Status retorno;
        double lon = Double.parseDouble(request.getParameter("x"));
        double lat = Double.parseDouble(request.getParameter("y"));

        boolean transitavelPe = Boolean.parseBoolean(request.getParameter("pe"));
        boolean transitavelVeiculo = Boolean.parseBoolean(request.getParameter("veiculo"));
        long tipo = Long.parseLong(request.getParameter("tipo"));
        String descricao = request.getParameter("descricao");
        Ponto po = new Ponto(lon, lat, tipo);
        Ocorrencia oco = new Ocorrencia(po, transitavelVeiculo, transitavelPe, descricao, user);
        try {
            retorno = Status.parseStatus(FuncoesGerais.callSet(getServletContext().getInitParameter("host"), objectToXML(oco, Ocorrencia.class), "/ocorrencia/cadastro", "POST", "TEXT/XML", "TEXT/PLAIN"));
            return retorno;
        } catch (Exception ex) {
            LOGGER.log(Level.INFO, "Erro ao cadastrar ocorrência.", ex);
            return Status.ERRO;
        }
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
