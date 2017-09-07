<%@ page contentType="text/html; charset=UTF-8" %>
<%@page import="br.edu.ifsp.spo.pds2016glybif.modelo.Usuario"%>
<%
    Usuario user = null;
    long pk = 0;
    if (request.getSession().getAttribute("usuario") == null) {
        response.sendRedirect(getServletContext().getInitParameter("host") + "erros.jsp?id=1");
    } else {
        user = (Usuario) request.getSession().getAttribute("usuario");
        pk = user.getPkUsuario();
    }
%>
<!DOCTYPE html>
<html lang="pt-br">
    <head>
        <meta charset="utf-8">
        <link rel="icon" href="assets/img/icon.png">
        <title>Cidade Ajuda: Menu</title>
        <script src="https://ajax.googleapis.com/ajax/libs/jquery/2.2.2/jquery.min.js" type="text/javascript"></script>
        <script src="assets/js/xml2jsobj.js" type="text/javascript"></script>
        <script src="https://maps.googleapis.com/maps/api/js?key=AIzaSyDgAVVKrvn3hQ2vkTDI8xsn4ap1dZvbhSg&libraries=places" type="text/javascript"></script>
        <link rel="stylesheet" href="assets/css/base.css" type="text/css">
        <link rel="stylesheet" href="assets/css/menu.css" type="text/css">
        <script type="text/javascript">
            var popup, overlay, closePopup, map, bounds, Nlat, Nlon, Slat, Slon, xml, markers, mapOptions, input, searchBox, geolocate = null;

            function get_info(id) {
                var xhttp;
                if (window.XMLHttpRequest) {
                    xhttp = new XMLHttpRequest();
                } else {
                    // Para IE6, IE5
                    xhttp = new ActiveXObject("Microsoft.XMLHTTP");
                }
                var str = '/funcoes/ocorrencia/getinfo/' + id;
                xhttp.open("GET", str, true);
                xhttp.setRequestHeader("accept", "application/xml");
                xhttp.send();
                xhttp.onreadystatechange = function () {
                    if (xhttp.readyState === 4 && xhttp.status === 200) {
                        var info = XML2jsobj(xhttp.responseXML.documentElement);
                        overlay.style.display = "block";
                        popup.style.display = 'block';
                        var autor = document.getElementById("autor");
                        var img = document.getElementById("foto");
                        var tipo = document.getElementById("tipo");
                        var data = document.getElementById("data_hora");
                        var veiculo = document.getElementById("veiculo");
                        var descricao = document.getElementById("descricao");
                        var comentar = document.getElementById("comentar");
                        var comentario = document.getElementById("comentario");
                        var pe = document.getElementById("pe");
                        var tmp = new Date(info.dataHora);
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
                        if (parseInt(hora / 10) === 0) {
                            hora = "0" + hora;
                        }
                        comentario.setAttribute("href", '/comentario.jsp?id=' + id);
                        //comentar.setAttribute("href", '/comentar.jsp?id=' + info.pkOcorrencia);
                        autor.innerHTML = "Relatado por " + info.usuario.apelido;
                        data.innerHTML = "Informado: " + day + "/" + mes + "/" + ano + " às " + hora + ":" + min;
                        if (info.transitavelVeiculo === "true") {
                            veiculo.className = "sim";
                        } else {
                            veiculo.className = "nao";
                        }
                        if (info.transitavelAPe === "true") {
                            pe.className = "sim";
                        } else {
                            pe.className = "nao";
                        }
                        if (typeof info.descricao === 'undefined') {
                            descricao.value = 'Sem descrição';
                        } else {
                            descricao.value = info.descricao;
                        }
                        if (typeof info.listaImagens !== 'undefined') {
                            if (Array.isArray(info.listaImagens)) {
                                img.src = "data:image/png;base64," + info.listaImagens[0].imagem;
                                //TODO: Preparado para abrir mais de uma imagem
                            } else {
                                img.src = "data:image/png;base64," + info.listaImagens.imagem;
                            }
                        } else {
                            img.src = "assets/img/default_oco.png";
                        }
                        document.getElementById("oco").value = info.pkOcorrencia;
                        tipo.innerHTML = info.tipo.nome;
                        var existente = document.getElementById("existente");
                        existente.setAttribute("onclick", "interagir('E'," + '<%=pk%>' + "," + info.pkOcorrencia + ");");
                        var inexistente = document.getElementById("inexistente");
                        inexistente.setAttribute("onclick", "interagir('I'," + '<%=pk%>' + "," + info.pkOcorrencia + ");");
                        var caso = document.getElementById("caso");
                        caso.setAttribute("onclick", "interagir('C'," + '<%=pk%> ' + "," + info.pkOcorrencia + ");");
                    }
                };
            }

            function interagir(resposta, usuario, ocorrencia) {
                var xhttp;
                if (window.XMLHttpRequest) {
                    xhttp = new XMLHttpRequest();
                } else {
                    // Para IE6, IE5
                    xhttp = new ActiveXObject("Microsoft.XMLHTTP");
                }
                var str = '/funcoes/interacao/registrar/' + resposta.toUpperCase() + "/" + usuario + "/" + ocorrencia;
                xhttp.open("PUT", str, true);
                xhttp.setRequestHeader("accept", "text/plain");
                xhttp.send();
                xhttp.onreadystatechange = function () {
                    if (xhttp.readyState === 4 && xhttp.status === 200) {
                        if (xhttp.responseText !== null) {
                            alert("Resposta enviada!");
                        }
                    }
                };
            }

            function esconde() {
                document.getElementById("info").innerHTML = "";
                document.getElementById("info").style.visibility = 'hidden';
            }

            function set_point() {
                var xhttp;
                if (window.XMLHttpRequest) {
                    xhttp = new XMLHttpRequest();
                } else {
                    // Para IE6, IE5
                    xhttp = new ActiveXObject("Microsoft.XMLHTTP");
                }
                var str = '/funcoes/ocorrencia/mapa/';
                str = str.concat(Nlat.toString(), "/", Nlon.toString(), "/", Slat.toString(), "/", Slon.toString());
                xhttp.open("GET", str, true);
                xhttp.setRequestHeader("Accept", "application/xml");
                xhttp.send();
                xhttp.onreadystatechange = function () {
                    if (xhttp.readyState === 4 && xhttp.status === 200) {
                        markers = XML2jsobj(xhttp.responseXML.documentElement);
                        if (Array.isArray(markers.ponto)) {
                            for (var i = 0; i < markers.ponto.length; i++) {
                                var position = new google.maps.LatLng(markers.ponto[i].lat, markers.ponto[i].lon);
                                bounds.extend(position);
                                marker = new google.maps.Marker({
                                    position: position,
                                    icon: 'assets/img/mark/' + markers.ponto[i].tipo + '.png',
                                    map: map
                                });
                                marker.setMap(map);
                                var info = markers.ponto[i].pkPonto;
                                marker.addListener('click', function () {
                                    get_info(info);
                                });
                            }
                        } else {
                            var position = new google.maps.LatLng(markers.ponto.lat, markers.ponto.lon);
                            bounds.extend(position);
                            marker = new google.maps.Marker({
                                position: position,
                                icon: 'assets/img/mark/' + markers.ponto.tipo + '.png',
                                map: map
                            });
                            marker.setMap(map);
                            var info = markers.ponto.pkPonto;
                            marker.addListener('click', function () {
                                get_info(info);
                            });
                        }
                    }
                };
            }

            function set_mapa() {
                google.maps.event.addListenerOnce(map, 'idle', function () {
                    Nlat = map.getBounds().getNorthEast().lat();
                    Nlon = map.getBounds().getNorthEast().lng();
                    Slat = map.getBounds().getSouthWest().lat();
                    Slon = map.getBounds().getSouthWest().lng();
                    set_point();
                });
                map.controls[google.maps.ControlPosition.TOP_LEFT].push(input);

                searchBox.addListener('places_changed', function () {
                    var places = searchBox.getPlaces();

                    if (places.length === 0) {
                        return;
                    }

                    bounds = new google.maps.LatLngBounds();
                    places.forEach(function (place) {
                        map.setCenter(place.geometry.location);

                        if (place.geometry.viewport) {
                            bounds.union(place.geometry.viewport);
                        } else {
                            bounds.extend(place.geometry.location);
                        }
                        map.fitBounds(bounds);
                    });

                });

            }

            function exibe_comentar() {
                overlay.style.zIndex=998;
                document.getElementById("coment").style.display = "block";
            }

            $(document).ready(function () {
                closePopup = document.getElementById("popupclose");
                overlay = document.getElementById("overlay");
                popup = document.getElementById("popup");
                $('#Fcomentario').submit(function (e) {
                    e.preventDefault();
                    $.ajax({
                        url: $(this).attr("action"),
                        type: 'post',
                        data: $('#Fcomentario').serialize(),
                        success: function () {
                            overlay.style.zIndex=1;
                            document.getElementById("coment").style.display = "none";
                            alert("Enviado com sucesso!");
                        }
                    });
                });
                closePopup.onclick = function () {
                    overlay.style.display = 'none';
                    popup.style.display = 'none';
                };
                markers = [];
                bounds = new google.maps.LatLngBounds();
                input = document.getElementById('pac-input');
                searchBox = new google.maps.places.SearchBox(input);
                geolocate = new google.maps.LatLng(-23.5240963, -46.623221400000034);
                navigator.geolocation.getCurrentPosition(function (position) {
                    geolocate = new google.maps.LatLng(position.coords.latitude, position.coords.longitude);
                    map.setCenter(geolocale);
                    set_mapa();
                });
                var mapOptions = {
                    center: geolocate,
                    zoom: 13,
                    mapTypeId: google.maps.MapTypeId.ROADMAP
                };
                map = new google.maps.Map(document.getElementById("googleMap"), mapOptions);
                set_mapa();

                google.maps.event.addListener(map, 'bounds_changed', function () {
                    searchBox.setBounds(map.getBounds());
                    Nlat = map.getBounds().getNorthEast().lat();
                    Nlon = map.getBounds().getNorthEast().lng();
                    Slat = map.getBounds().getSouthWest().lat();
                    Slon = map.getBounds().getSouthWest().lng();
                    set_point();

                });
            });
        </script>
    </head>
    <body>
        <div id="overlay"></div>
        <div id="coment">
            <form id="Fcomentario" action="ChamadasComentario">
                <input type="hidden" name="acao" value="comentar">
                <input type="hidden" name="usuario" value="<%=pk%>">
                <input id="oco" type="hidden" name="ocorrencia">
                <textarea placeholder="Escreva aqui o seu comentário." id="txt" name="comentario"></textarea>
                <input type="submit" value="Enviar">
            </form>
        </div>
        <div id="popup">
            <div class="popupcontrols">
                <span id="popupclose">X</span>
            </div>
            <div class="popupcontent">
                <h1 id="tipo"></h1>
                <table style="width: 100%;">
                    <tr>
                        <td><img src="assets/img/default_oco.png" id="foto" alt="Foto da ocorrência" width="300"><br>
                            <p id="autor"></p>
                            <button onclick="javascript:exibe_comentar();">Comentar</button><br>
                            <a id="comentario">Ler os comentários</a>
                        </td>
                        <td>
                            <p id="data_hora"></p>
                            <button type="button" id="veiculo" class="sim">Transitável veículo</button>
                            <button type="button" id="pe" class="sim">Transitável a pé</button><br><br>
                            <textarea id="descricao" rows="5" cols="30">Fala sobre a ocorrência</textarea>
                        </td>
                    </tr>
                    <tr>
                        <td colspan="2" id="interacao">
                            <br><br>
                            <div id="existente"></div>
                            <div id="inexistente"></div>
                            <div id="caso"></div>
                        </td>
                    </tr>
                </table>
            </div>
        </div>
        <header>
            <div id="logo">
                <img src="assets/img/logo.png" alt="Cidade Ajuda" width="200">
            </div>
            <nav id="menu">
                <div id="user">
                    <p><%out.print(user.getApelido());%></p>
                </div>
                <ul>
                    <li><a href="informaroco.jsp">Informar ocorrência</a></li>
                    <li><a href="relatorio.jsp">Relatório</a></li>
                    <li><a href="ChamadasUsuario?acao=Logout">Sair</a></li>
                </ul>
            </nav>
        </header>
        <section>
            <article id="conteudo">
                <input id="pac-input" class="controls" type="text" placeholder="Digite aqui a sua busca">
                <div id="googleMap">
                </div>
            </article>
        </section>
        <footer>
            <p>Desenvolvido pela equipe GLYBIF</p>
        </footer>
    </body>
</html>
