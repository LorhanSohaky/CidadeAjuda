<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%
    String id = request.getParameter("id");
%>
<!DOCTYPE html>
<html>
    <head>
        <title>Comentar</title>
        <meta charset="UTF-8">
        <script type="text/xml">
        </script>
    </head>
    <body>
        <form action="ChamadasComentario" method="post">
            <input type="hidden" name="acao" value="comentar">
            <input type="hidden" name="ocorrencia" value="<%=id%>">
            <textarea name="comentario" placeholder="Escreva aqui o seu comentário."></textarea>
            <br>
            <button type="submit">Enviar</button>
        </form>
    </body>
</html>
