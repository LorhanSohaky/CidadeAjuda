<%@ page contentType="text/html; charset=UTF-8" %>
<%
    if (request.getSession().getAttribute("usuario") != null) {
        response.sendRedirect(getServletContext().getInitParameter("host") + "menu.jsp");
    }
%>
<!DOCTYPE html>
<html lang="pt-br">
    <head>
        <meta charset="utf-8">
        <link rel="icon" href="assets/img/icon.png">
        <title>Cidade Ajuda: Home</title>
        <script src="https://ajax.googleapis.com/ajax/libs/jquery/2.2.2/jquery.min.js" type="text/javascript"></script>
        <script src="assets/js/xml2jsobj.js" type="text/javascript"></script>
        <script src="https://maps.googleapis.com/maps/api/js?key=AIzaSyDgAVVKrvn3hQ2vkTDI8xsn4ap1dZvbhSg&libraries=places" type="text/javascript"></script>
        <link rel="stylesheet" href="assets/css/base.css" type="text/css">
        <link rel="stylesheet" href="assets/css/index.css" type="text/css">
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
                var str = "/funcoes/ocorrencia/getinfo/" + id;
                xhttp.open("GET", str, true);
                xhttp.setRequestHeader("accept", "application/xml");
                xhttp.send();
                xhttp.onreadystatechange = function () {
                    if (xhttp.readyState === 4 && xhttp.status === 200) {
                        var info = XML2jsobj(xhttp.responseXML.documentElement);
                        overlay.style.display = "block";
                        popup.style.display = 'block';
                        var comentario = document.getElementById("comentario");
                        var autor = document.getElementById("autor");
                        var img = document.getElementById("foto");
                        var tipo = document.getElementById("tipo");
                        var data = document.getElementById("data_hora");
                        var veiculo = document.getElementById("veiculo");
                        var descricao = document.getElementById("descricao");
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
                        if (parseInt(min / 10) === 0) {
                            min = "0" + min;
                        }
                        if (parseInt(hora / 10) === 0) {
                            hora = "0" + hora;
                        }
                        comentario.setAttribute("href", '/comentario.jsp?id=' + id);
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
                        tipo.innerHTML = info.tipo.nome;
                    }
                };
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

            $(document).ready(function () {
                closePopup = document.getElementById("popupclose");
                overlay = document.getElementById("overlay");
                popup = document.getElementById("popup");
                closePopup.onclick = function () {
                    overlay.style.display = 'none';
                    popup.style.display = 'none';
                };

                markers = [];
                bounds = new google.maps.LatLngBounds();
                input = document.getElementById('pac-input');
                searchBox = new google.maps.places.SearchBox(input);
                geolocate = new google.maps.LatLng(-23.5240963, -46.623221400000034);
                if (navigator.geolocation) {
                    navigator.geolocation.getCurrentPosition(function (position) {
                        geolocate = new google.maps.LatLng(position.coords.latitude, position.coords.longitude);
                        map.setCenter(geolocate);
                    });
                }
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

            function esconde() {
                $('#painelLogin').slideUp(300, function () {
                    $('#painelLogin').animate({
                        opacity: 0
                    }, 300);
                });

            }
            function exibe() {
                $('#painelLogin').animate({
                    opacity: 1
                }, 300, function () {
                    $('#painelLogin').slideDown(300);
                });
            }
        </script>
    </head>
    <body>
        <div id="overlay"></div>
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
                        </td>
                        <td>
                            <p id="data_hora"></p>
                            <button type="button" id="veiculo" class="sim">Transitável veículo</button>
                            <button type="button" id="pe" class="sim">Transitável a pé</button><br><br>
                            <textarea id="descricao" rows="5" cols="30">Fala sobre a ocorrência</textarea>
                        </td>
                    </tr>
                    <tr>
                        <td><a id="comentario">Ler os comentários</a></td>
                    </tr>
                </table>
            </div>
        </div>
        <header id="header">
            <div id="logo">
                <img src="assets/img/logo.png" alt="Cidade Ajuda" width="200">
            </div>
            <nav id="menu">
                <ul>
                    <li><a href="javascript:exibe();" onclick="exibe();">Acessar</a></li>
                    <li><a href="cadusuario.html">Cadastrar-se</a></li>
                    <li><a href="relatorio.jsp">Relatório</a></li>
                </ul>
            </nav>
            <div id="painelLogin">
                <form id="form" name="login" method="post" action="ChamadasUsuario">
                    <input type="hidden" name="acao" value="Login">
                    <img id="fechar" src="assets/img/red.png" width="30" alt="fechar" onclick="esconde();">
                    <input id="email" type="email" name="email" placeholder="E-mail">
                    <br>
                    <input id="senha" type="password" name="pass" placeholder="Senha">
                    <br>
                    <button type="submit" class="preto" id="login">Entrar</button>
                    <br>
                    <a href="esquecisenha.html">Esqueci minha senha</a>
                </form>
            </div>
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
