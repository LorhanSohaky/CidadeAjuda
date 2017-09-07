<%@page import="br.edu.ifsp.spo.pds2016glybif.banco.UsuarioDAO"%>
<%@page import="br.edu.ifsp.spo.pds2016glybif.banco.Conexao"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%
    long id = Long.parseLong(request.getParameter("id"));
    if (id == 0) {
        response.sendRedirect(getServletContext().getInitParameter("host") + "erros.jsp?id=5");
    }
    Conexao con = new Conexao();
    UsuarioDAO dao = new UsuarioDAO(con.getConexao());
    long pk = dao.validaMudancaSenha(id);
    if (pk == 0) {
        response.sendRedirect(getServletContext().getInitParameter("host") + "erros.jsp?id=8");
    }
%>
<!DOCTYPE html>
<html lang="pt-br">
    <head>
        <meta charset="utf-8">
        <link rel="icon" href="assets/img/icon.png">
        <title>Cidade Ajuda: Mudar senha</title>
        <link rel="stylesheet" href="assets/css/base.css" type="text/css">
        <link rel="stylesheet" href="assets/css/mudarsenha.css" type="text/css">
    </head>
    <body>
        <header>
            <div id="logo">
                <img src="assets/img/logo.png" alt="Cidade Ajuda" width="200">
            </div>
            <nav id="menu">
                <ul>
                    <li><a href="index.jsp">Início</a></li>
                    <li><a href="cadusuario.html">Cadastrar-se</a></li>
                    <li><a href="relatorio.jsp">Relatório</a></li>
                </ul>
            </nav>
        </header>
        <section>
            <header id="titulo"><h1>Mudando a senha</h1>
            </header>
            <article id="conteudo">
                <p>Último etapa para a mudança da senha!
                <form action="ChamadasUsuario" method="POST">
                    <input type="hidden" name="acao" value="MudarSenha">
                    <input type="hidden" name="id" value="<%=pk%>">
                    <input type="password" name="senha" placeholder="Digite aqui a nova senha"><br>
                    <input type="password" name="confirme" placeholder="Confirme a nova senha"><br>
                    <input type="submit" value="Atualizar senha">
                </form>
            </article>
        </section>
        <footer>
            <p>Desenvolvido pela equipe GLYBIF</p>
        </footer>
    </body>
</html>