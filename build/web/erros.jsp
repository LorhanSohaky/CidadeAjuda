<%@ page contentType="text/html; charset=UTF-8" %>
<%
    String erros[] = {"Código de erro inválido!",
        "Acesso não autorizado. Faça login antes!",
        "Resposta igual a anterior.",
        "Ocorreu um erro interno.",
        "Erro ao registrar interação",
        "Opção inválida!",
        "Verifique se as informações fornecidas estão corretas.",
        "E-mail já cadastrado",
        "Prazo para mudança da senha esgotado!"
    };
    int id = 0;
    if (request.getParameter("id") != null) {
        id = Integer.parseInt(request.getParameter("id"));
    }
%>
<!DOCTYPE html>
<html lang="pt-br">
    <head>
        <meta charset="utf-8">
        <link rel="icon" href="assets/img/icon.png">
        <title>Cidade Ajuda: Erro</title>
        <link rel="stylesheet" href="assets/css/base.css" type="text/css">
        <link rel="stylesheet" href="assets/css/erros.css" type="text/css">
    </head>
    <body>
        <header>
            <div id="logo">
                <img src="assets/img/logo.png" alt="Cidade Ajuda" width="200">
            </div>
            <nav id="menu">
                <ul>
                    <li><a href="cadusuario.html">Cadastrar-se</a></li>
                    <li><a href="relatorio.jsp">Relatório</a></li>
                    <li><a href="#" onclick="javascript:history.back();">Voltar</a></li>
                </ul>
            </nav>
        </header>
        <section>
            <header id="titulo"><h1><% out.print(erros[id]);%></h1>
            </header>
            <article id="conteudo">
                <h1 align="center"><br><br>
                    <input type="button" class="preto" value="Voltar ao início" onclick="window.location = 'index.jsp';"></h1>
            </article>
        </section>
        <footer>
            <p>Desenvolvido pela equipe GLYBIF</p>
        </footer>
    </body>
</html>