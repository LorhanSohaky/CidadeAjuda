<%@page import="java.net.URLEncoder"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%
    request.setCharacterEncoding("UTF-8");
    String url = "/funcoes/comentario/lista/" + request.getParameter("id") + "/";
%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Comentário</title>
        <script src="assets/js/xml2jsobj.js" type="text/javascript"></script>
        <script type="text/javascript">
            var lidos = 0;
            var url = '<%=url%>';
            function gerar() {
                var xhttp;
                if (window.XMLHttpRequest) {
                    xhttp = new XMLHttpRequest();
                } else {
                    // Para IE6, IE5
                    xhttp = new ActiveXObject("Microsoft.XMLHTTP");
                }
                xhttp.open("GET", url + lidos, true);
                xhttp.setRequestHeader("accept", "text/xml");
                xhttp.send();
                xhttp.onreadystatechange = function () {
                    if (xhttp.readyState === 4 && xhttp.status === 200) {
                        var info = XML2jsobj(xhttp.responseXML.documentElement);
                        if (Array.isArray(info.comentario)) {
                            for (i = 0; i < info.comentario.length; i++) {
                                var tmp = new Date(info.comentario[i].dataHora);
                                var day = tmp.getDate();
                                var mes = tmp.getMonth() + 1;
                                var ano = tmp.getFullYear();
                                var hora = tmp.getHours();
                                var min = tmp.getMinutes();
                                if (parseInt(day / 10) === 0) {
                                    day = "0" + day;
                                }
                                if (parseInt(mes / 10) === 0) {
                                    mes = "0" + mes;
                                }
                                if (parseInt(min / 10) === 0) {
                                    min = "0" + min;
                                }
                                if (parseInt(hora / 10) === 0) {
                                    hora = "0" + hora;
                                }
                                var tabela = document.getElementById("conteudo").getElementsByTagName('tbody')[0];
                                var nova_linha = tabela.insertRow(tabela.rows.length);
                                var data = nova_linha.insertCell(0);
                                var user = nova_linha.insertCell(1);
                                var comentario = nova_linha.insertCell(2);
                                var img = nova_linha.insertCell(3);
                                data.appendChild(document.createTextNode(day + "/" + mes + "/" + ano + "\n" + hora + ":" + min));
                                user.appendChild(document.createTextNode(info.comentario[i].usuario.apelido));
                                comentario.appendChild(document.createTextNode(info.comentario[i].comentario));
                                if (info.comentario[i].listaImagens !== null) {
                                    var txt = "";
                                    if (Array.isArray(info.comentario[i].listaImagens)) {
                                        for (j = 0; j < info.comentario[i].listaImagens.length; j++) {
                                            var imgO = document.createElement("img");
                                            imgO.src = "data:image/png;base64," + info.comentario[i].imagem[j];
                                            imgO.setAttribute('alt', 'Imagem do comentário');
                                            imgO.setAttribute('width', '300px');
                                            img.appendChild(imgO);
                                            //TODO: Testar
                                        }
                                    } else {
                                        var imgO = document.createElement("img");
                                        imgO.src = "data:image/png;base64," + info.comentario[i].imagem;
                                        imgO.setAttribute('alt', 'Imagem do comentário');
                                        imgO.setAttribute('width', '300px');
                                        img.appendChild(imgO);
                                    }
                                }
                            }
                            lidos += info.comentario.length;
                        } else {
                            var tmp = new Date(info.comentario.dataHora);
                            var day = tmp.getDate();
                            var mes = tmp.getMonth() + 1;
                            var ano = tmp.getFullYear();
                            var hora = tmp.getHours();
                            var min = tmp.getMinutes();
                            if (parseInt(day / 10) === 0) {
                                day = "0" + day;
                            }
                            if (parseInt(mes / 10) === 0) {
                                mes = "0" + mes;
                            }
                            if (parseInt(min / 10) === 0) {
                                min = "0" + min;
                            }
                            if (parseInt(hora / 10) === 0) {
                                hora = "0" + hora;
                            }
                            var tabela = document.getElementById("conteudo").getElementsByTagName('tbody')[0];
                            var nova_linha = tabela.insertRow(tabela.rows.length);
                            var data = nova_linha.insertCell(0);
                            var user = nova_linha.insertCell(1);
                            var comentario = nova_linha.insertCell(2);
                            var img = nova_linha.insertCell(3);
                            data.appendChild(document.createTextNode(day + "/" + mes + "/" + ano + "\n" + hora + ":" + min));
                            user.appendChild(document.createTextNode(info.comentario.usuario.apelido));
                            comentario.appendChild(document.createTextNode(info.comentario.comentario));
                            if (info.comentario.listaImagens !== null) {
                                var txt = "";
                                if (Array.isArray(info.comentario.listaImagens)) {
                                    for (j = 0; j < info.comentario.listaImagens.length; j++) {
                                        var imgO = document.createElement("img");
                                        imgO.src = "data:image/png;base64," + info.comentario.imagem[j];
                                        imgO.setAttribute('alt', 'Imagem do comentário');
                                        imgO.setAttribute('width', '300px');
                                        img.appendChild(imgO);
                                        //TODO: Testar
                                    }
                                } else {
                                    var imgO = document.createElement("img");
                                    imgO.src = "data:image/png;base64," + info.comentario.imagem;
                                    imgO.setAttribute('alt', 'Imagem do comentário');
                                    imgO.setAttribute('width', '300px');
                                    img.appendChild(imgO);
                                }
                            }
                            document.getElementById("footer").style.display = "none";
                        }
                        if (info.comentario.length < 10) {
                            document.getElementById("footer").style.display = "none";
                        }
                    }
                };
            }
            gerar();
        </script>
    </head>
    <body>
        <table id="conteudo" border="1" width="100%">
            <col style="width:10%">
            <col style="width:20%">
            <col style="width:60%">
            <col style="width:10%">
            <thead>
                <tr id="menu">
                    <td style="width:10%">Data</td>
                    <td style="width:20%">Usuário</td>
                    <td style="width:60%">Comentário</td>
                    <td style="width:10%">Imagens</td>
                </tr>
            </thead>
            <tbody>
            </tbody>
            <tfoot>
                <tr>
                    <td colspan="4" id="footer"><button onclick="gerar();">Clique aqui para ler mais</button></td>
                </tr>
            </tfoot>
        </table>
    </body>
</html>
