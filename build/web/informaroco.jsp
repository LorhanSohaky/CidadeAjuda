<%@page import="br.edu.ifsp.spo.pds2016glybif.modelo.Tipo"%>
<%@page import="java.util.List"%>
<%@page import="br.edu.ifsp.spo.pds2016glybif.banco.Conexao"%>
<%@page import="br.edu.ifsp.spo.pds2016glybif.banco.TipoDAO"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%
    if (request.getSession().getAttribute("usuario") == null) {
        response.sendRedirect(getServletContext().getInitParameter("host") + "erros.jsp?id=1");
    }
    Conexao con = new Conexao();
    TipoDAO dao = new TipoDAO(con.getConexao());
    List<Tipo> list = dao.listaTipos();
    String tmp;
    con.close();
    String array = "[\'Selecione um tipo de ocorrência e veja a sugestão de descrição\',";
    for (int i = 0; i < list.size() - 1; i++) {
        tmp = list.get(i).getSugestaoDeDescricao().replaceAll("\n", "<br>");
        array += "\'" + tmp + "\',";
    }
    tmp = list.get(list.size() - 1).getSugestaoDeDescricao().replaceAll("\n", "<br>");
    array += "\'" + tmp + "\']";
%>
<!DOCTYPE html>
<html lang="pt-br">
    <head>
        <meta charset="utf-8">
        <link rel="icon" href="assets/img/icon.png">
        <title>Cidade Ajuda: Informar ocorrência</title>
        <link rel="stylesheet" href="assets/css/base.css" type="text/css">
        <link rel="stylesheet" href="assets/css/informaroco.css" type="text/css">
        <script src="https://ajax.googleapis.com/ajax/libs/jquery/2.2.2/jquery.min.js" type="text/javascript"></script>
        <script src="https://maps.googleapis.com/maps/api/js?key=AIzaSyDgAVVKrvn3hQ2vkTDI8xsn4ap1dZvbhSg&libraries=places" type="text/javascript"></script>
        <script type="text/javascript">
            var map, bounds, markers, mapOptions, input, searchBox, geolocate = null, x, y;
            var vet = <% out.print(array); %>;
            $(document).ready(function () {
                document.getElementById("desc").placeholder = vet[0];
                markers = [];
                x = document.getElementById("x");
                y = document.getElementById("y");
                bounds = new google.maps.LatLngBounds();
                input = document.getElementById('pac-input');
                searchBox = new google.maps.places.SearchBox(input);
                geolocate = new google.maps.LatLng(-23.5240963, -46.623221400000034);
                navigator.geolocation.getCurrentPosition(function (position) {
                    geolocate = new google.maps.LatLng(position.coords.latitude, position.coords.longitude);
                    map.setCenter(geolocale);
                });
                var mapOptions = {
                    center: geolocate,
                    zoom: 13,
                    mapTypeId: google.maps.MapTypeId.ROADMAP
                };
                map = new google.maps.Map(document.getElementById("googleMap"), mapOptions);
                map.controls[google.maps.ControlPosition.TOP_LEFT].push(input);

                searchBox.addListener('places_changed', function () {
                    var places = searchBox.getPlaces();
                    if (places.length === 0) {
                        return;
                    }
                    bounds = new google.maps.LatLngBounds();
                    places.forEach(function (place) {
                        marker = new google.maps.Marker({
                            position: place.geometry.location,
                            map: map
                        });
                        x.value = marker.getPosition().lng();
                        y.value = marker.getPosition().lat();
                        marker.setMap(map);
                        map.setCenter(marker);
                        if (place.geometry.viewport) {
                            bounds.union(place.geometry.viewport);
                        } else {
                            bounds.extend(place.geometry.location);
                        }
                        map.fitBounds(bounds);
                    });

                });
            });

            function mudaSug() {
                document.getElementById("desc").placeholder = vet[document.getElementById("tipo").value];
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
                    <li><a href="relatorio.jsp">Relatório</a></li>
                    <li><a href="ChamadasUsuario?acao=Logout">Sair</a></li>
                </ul>
            </nav>
        </header>
        <section>
            <header id="titulo"><h1>INFORMAR OCORRÊNCIA</h1>
            </header>
            <article id="conteudo">
                <input id="pac-input" class="controls" type="text" placeholder="Digite aqui a sua busca">
                <div id="googleMap">
                </div>
                <form name="Formulario" id="form_cadastro" method="POST" action="ChamadasOcorrencia">
                    <input style="display: none;" type="text" name="acao" value="CadastrarOcorrencia">
                    <input style="display: none;" type="text" name="x" id="x" value="0">
                    <input style="display: none;" type="text" name="y" id="y" value="0">
                    Transitável a pé: <select name="pe">
                        <option value="TRUE" selected>Sim</option>
                        <option value="FALSE">Não</option>
                    </select>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                    Transitável de veiculo: <select name="veiculo">
                        <option value="TRUE" selected>Sim</option>
                        <option value="FALSE">Não</option>
                    </select>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                    Tipo de Ocorrência: <select name="tipo" onchange="mudaSug();" id="tipo">
                        <option value="0" selected>Selecione um tipo</option>
                        <%
                            for (int i = 0; i < list.size(); i++) {
                        %>
                        <option value="<%=list.get(i).getPkTipo()%>"><%=list.get(i).getNome()%></option>
                        <%}%>
                    </select><br><br>
                    Descrição:<br>
                    <textarea name="descricao" id="desc" rows="5" cols="50"></textarea>
                    <div class="row-button-submit">
                        <button class="header-content-button enviar" type="submit">Cadastrar</button> 
                    </div>
                </form>
            </article>
        </section>
        <footer>
            <p>Desenvolvido pela equipe GLYBIF</p>
        </footer>
    </body>
</html>
