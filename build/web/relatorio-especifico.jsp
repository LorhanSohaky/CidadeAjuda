<%@page import="java.net.URLEncoder"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%
    request.setCharacterEncoding("UTF-8");
    String url = "/funcoes/relatorio/especifico/" + request.getParameter("pkTipo") + "/" + request.getParameter("localTipo") + "/" + request.getParameter("ativo") + "/" + URLEncoder.encode(request.getParameter("txt"), "UTF-8");
%>
<!DOCTYPE html>
<html>
    <head>
        <meta charset="utf-8">
        <title>Relatório específico</title>
        <script src="assets/js/xml2jsobj.js" type="text/javascript"></script>
        <link rel="stylesheet" href="assets/css/relatorio_especifico.css" type="text/css">
        <script type="text/javascript">
            function gerar() {
                var xhttp;
                if (window.XMLHttpRequest) {
                    xhttp = new XMLHttpRequest();
                } else {
                    // Para IE6, IE5
                    xhttp = new ActiveXObject("Microsoft.XMLHTTP");
                }
                var str = '<%=url%>';
                xhttp.open("GET", str, true);
                xhttp.setRequestHeader("accept", "text/xml");
                xhttp.send();
                xhttp.onreadystatechange = function () {
                    if (xhttp.readyState === 4 && xhttp.status === 200) {
                        var info = XML2jsobj(xhttp.responseXML.documentElement);

                        if (Array.isArray(info.ocorrencia)) {
                            for (i = 0; i < info.ocorrencia.length; i++) {
                                var vei = info.ocorrencia[i].transitavelVeiculo;
                                var ape = info.ocorrencia[i].transitavelAPe;
                                var tmp = new Date(info.ocorrencia[i].dataHora);
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
                                if (parseInt(hora / 10) === 0) {
                                    hora = "0" + hora;
                                }
                                if (parseInt(min / 10) === 0) {
                                    min = "0" + min;
                                }
                                if (vei === "true") {
                                    vei = "Sim";
                                } else {
                                    vei = "Não";
                                }
                                if (ape === "true") {
                                    ape = "Sim";
                                } else {
                                    ape = "Não";
                                }
                                var tabela = document.getElementById("conteudo").getElementsByTagName('tbody')[0];
                                var nova_linha = tabela.insertRow(tabela.rows.length);
                                var data = nova_linha.insertCell(0);
                                var user = nova_linha.insertCell(1);
                                var veiculo = nova_linha.insertCell(2);
                                var pe = nova_linha.insertCell(3);
                                var descricao = nova_linha.insertCell(4);
                                data.appendChild(document.createTextNode(day + "/" + mes + "/" + ano + "\n" + hora + ":" + min));
                                user.appendChild(document.createTextNode(info.ocorrencia[i].usuario.apelido));
                                veiculo.appendChild(document.createTextNode(vei));
                                descricao.appendChild(document.createTextNode(info.ocorrencia[i].descricao));
                                pe.appendChild(document.createTextNode(ape));
                            }
                        } else {
                            var vei = info.ocorrencia.transitavelVeiculo;
                            var ape = info.ocorrencia.transitavelAPe;
                            var tmp = new Date(info.ocorrencia.dataHora);
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
                            if (parseInt(hora / 10) === 0) {
                                hora = "0" + hora;
                            }
                            if (parseInt(min / 10) === 0) {
                                min = "0" + min;
                            }
                            if (vei === "true") {
                                vei = "Sim";
                            } else {
                                vei = "Não";
                            }
                            if (ape === "true") {
                                ape = "Sim";
                            } else {
                                ape = "Não";
                            }
                            var tabela = document.getElementById("conteudo").getElementsByTagName('tbody')[0];
                            var nova_linha = tabela.insertRow(tabela.rows.length);
                            var data = nova_linha.insertCell(0);
                            var user = nova_linha.insertCell(1);
                            var veiculo = nova_linha.insertCell(2);
                            var pe = nova_linha.insertCell(3);
                            var descricao = nova_linha.insertCell(4);
                            data.appendChild(document.createTextNode(day + "/" + mes + "/" + ano + "\n" + hora + ":" + min));
                            user.appendChild(document.createTextNode(info.ocorrencia.usuario.apelido));
                            veiculo.appendChild(document.createTextNode(vei));
                            descricao.appendChild(document.createTextNode(info.ocorrencia.descricao));
                            pe.appendChild(document.createTextNode(ape));
                        }
                    }
                }
                ;
            }
            gerar();
        </script>
    </head>
    <body>
        <table id="conteudo" border="1" width="100%">
            <col style="width:10%">
            <col style="width:20%">
            <col style="width:15%">
            <col style="width:15%">
            <col style="width:40%">
            <thead>
                <tr id="menu">
                    <td style="width:10%">Data</td>
                    <td style="width:20%">Usuário</td>
                    <td style="width:15%">Transitável<br>veículo</td>
                    <td style="width:15%">Transitável<br>a pé</td>
                    <td style="width:40%">Descrição</td>
                </tr>
            </thead>
            <tbody>
            </tbody>
        </table>
    </body>
</html>
