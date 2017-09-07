<%@ page contentType="text/html; charset=UTF-8" %>
<!DOCTYPE html>
<html lang="pt-br">
    <head>
        <meta charset="utf-8">
        <link rel="icon" href="assets/img/icon.png">
        <title>Cidade Ajuda: Relatório</title>
        <link rel="stylesheet" href="assets/css/base.css" type="text/css">
        <link rel="stylesheet" href="assets/css/relatorio.css" type="text/css">
        <script src="assets/js/xml2jsobj.js" type="text/javascript"></script>
        <script type="text/javascript">
            function gerar() {
                var xhttp;
                if (window.XMLHttpRequest) {
                    xhttp = new XMLHttpRequest();
                } else {
                    // Para IE6, IE5
                    xhttp = new ActiveXObject("Microsoft.XMLHTTP");
                }
                var ativo = document.getElementById("ativo").checked;
                var str = '/funcoes/relatorio/gerar/' + document.getElementById("tipo").value + "/" + ativo + "/" + encodeURI(document.getElementById("txt").value);
                xhttp.open("PUT", str, true);
                xhttp.setRequestHeader("accept", "application/xml");
                xhttp.send();
                xhttp.onreadystatechange = function () {
                    var ge = document.getElementById("result");
                    if (xhttp.readyState === 4 && xhttp.status === 200) {
                        var info = XML2jsobj(xhttp.responseXML.documentElement);
                        ge.innerHTML = "";
                        if (Array.isArray(info.relatorio)) {
                            for (i = 0; i < info.relatorio.length; i++) {
                                var tabela = document.getElementById("conteudo").getElementsByTagName('tbody')[0];
                                var nova_linha = tabela.insertRow(tabela.rows.length);
                                var img = nova_linha.insertCell(0);
                                var tipo = nova_linha.insertCell(1);
                                var total = nova_linha.insertCell(2);
                                var link = nova_linha.insertCell(3);

                                var imgO = document.createElement("img");
                                imgO.src = "assets/img/relatorio/" + info.relatorio[i].pk_tipo + ".png";
                                imgO.width = "80";

                                var a = document.createElement("a");
                                a.setAttribute("href", '/relatorio-especifico.jsp?pkTipo=' + info.relatorio[i].pk_tipo + '&localTipo=' + document.getElementById("tipo").value + '&ativo=' + ativo + '&txt=' + encodeURI(document.getElementById("txt").value));
                                a.setAttribute("target", "_blank");
                                var linkText = document.createTextNode("Clique para saber mais");
                                a.appendChild(linkText);

                                tipo.appendChild(document.createTextNode(info.relatorio[i].tipo));
                                img.appendChild(imgO);
                                total.appendChild(document.createTextNode(info.relatorio[i].total));
                                link.appendChild(a);
                            }
                        } else if (info.relatorio !== null) {
                            var tabela = document.getElementById("conteudo").getElementsByTagName('tbody')[0];
                            var nova_linha = tabela.insertRow(tabela.rows.length);
                            var img = nova_linha.insertCell(0);
                            var tipo = nova_linha.insertCell(1);
                            var total = nova_linha.insertCell(2);
                            var link = nova_linha.insertCell(3);

                            var imgO = document.createElement("img");
                            imgO.src = "assets/img/relatorio/" + info.relatorio.pk_tipo + ".png";
                            imgO.width = "80";
                            img.appendChild(imgO);

                            var a = document.createElement("a");
                            a.setAttribute("href", '/relatorio-especifico.jsp?pkTipo=' + info.relatorio.pk_tipo + '&localTipo=' + document.getElementById("tipo").value + '&ativo=' + ativo + '&txt=' + encodeURI(document.getElementById("txt").value));
                            a.setAttribute("target", "_blank");
                            var linkText = document.createTextNode("Clique para saber mais");
                            a.appendChild(linkText);


                            tipo.appendChild(document.createTextNode(info.relatorio.tipo));
                            total.appendChild(document.createTextNode(info.relatorio.total));
                            link.appendChild(a);
                        }
                    } else {
                        ge.innerHTML = "<tr><td colspan='4'>Nenhum resultado encontrado</td></tr>";
                    }
                };
            }
        </script>
    </head>
    <body>
        <header>
            <div id="logo">
                <img src="assets/img/logo.png" alt="Cidade Ajuda" width="200">
            </div>
            <nav id="menu">
                <ul>
                    <li><a href="#" onclick="javascript:history.back();">Voltar</a></li>
                </ul>
            </nav>
        </header>
        <section>
            <header id="titulo"><h1>RELATÓRIO</h1>
            </header>
            <article id="Sconteudo">
                <div id="pesq">
                    Tipo: <select name="tipo" id="tipo">
                        <option value="city" selected>Cidade</option>
                        <option value="county" >Município</option>
                        <option value="state" >Estado</option>
                    </select>
                    Ocorrência ativa: <input type="checkbox" id="ativo">
                    <input type="text" id="txt" placeholder="Nome">
                    <button type="button" class="verde" onclick="gerar();">Gerar</button>
                </div>
                <table id="conteudo" border="0" width="100%">
                    <col style="width:10%">
                    <col style="width:20%">
                    <col style="width:10%">
                    <col style="width:60%">
                    <tbody id="result">
                    </tbody>
                </table>
            </article>
        </section>
        <footer>
            <p>Desenvolvido pela equipe GLYBIF</p>
        </footer>
    </body>
</html>
