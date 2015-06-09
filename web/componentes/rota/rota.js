(function () {
    var app = angular.module("rota", []);
    app.controller("rotaController", ["$scope", "notifyService", "funcionarioService",
        "clienteService", "caminhaoService", "cargaService", "viagemService", "$location",
        "$routeParams",
        function ($scope, notifyService, funcionarioService, clienteService,
                caminhaoService, cargaService, viagemService, $location, $routeParams) {
            $scope.passo = 1;
            $scope.rotas = [];
            $scope.rc = {
            };
            $scope.consumoKmCaminhao = 0;
            $scope.consumoEstimado = 0;
            $scope.valorEstimado = 0;
            $scope.valorDiesel = 2;
            $scope.caminhaoSelecionado = null;
            $scope.nRota = {};

            $scope.changeCaminhao = function (caminhaoID) {
                $scope.caminhaoSelecionado = $.grep($scope.caminhoes, function (e) {
                    return e.id == caminhaoID;
                })[0];
                $scope.calcularValores();
            };

            $scope.calcularValores = function () {
                if (!$scope.caminhaoSelecionado)
                    return;

                $scope.consumoKmCaminhao = $scope.caminhaoSelecionado.gasto;
                var totalKilometros = $scope.getTotalKilometros();
                var totalLitros = totalKilometros / $scope.consumoKmCaminhao;
                var valorDiesel = $scope.valorDiesel;
                var totalCusto = totalLitros * valorDiesel;

                var updateValue = !$scope.nRota.valor || $scope.nRota.valor == $scope.valorEstimado;

                $scope.consumoEstimado = totalLitros;
                $scope.valorEstimado = totalCusto;

                if (updateValue)
                    $scope.nRota.valor = $scope.valorEstimado;
            };

            $scope.proximoPasso = function () {
                $scope.passo++;
                if ($scope.passo == 2)
                    $scope.carregarSelects();
            };

            $scope.anteriorPasso = function () {
                $scope.passo--;
            };

            $scope.getTotalKilometros = function () {
                var totalMetros = 0;
                $($scope.rotas).each(function (i, r) {
                    totalMetros += r.metros;
                });

                return totalMetros / 1000;
            };

            $scope.rotaToString = function (rota) {
                var r = "";
                if (rota.numero)
                    r += rota.numero + ", ";

                if (rota.rua)
                    r += rota.rua + ", ";

                if (rota.bairro)
                    r += rota.bairro + ", ";

                if (rota.cidade)
                    r += rota.cidade + ", ";

                if (rota.estado)
                    r += rota.estado + ", ";

                if (rota.pais)
                    r += rota.pais;

                return r;
            };

            $scope.salvar = function (rota) {
                rota.rotas = [];
                $($scope.rotas).each(function (i, r) {
                    rota.rotas.push({
                        partida: r.partida.string,
                        destino: r.destino.string,
                        metros: r.metros,
                        segundos: r.segundos
                    });
                });

                viagemService.inserir(function () {
                    notifyService.add({
                        seconds: 8,
                        message: "Viagem inserida com sucesso!"
                    });

                    $scope.voltar();
                }, function () {
                    notifyService.add({
                        seconds: 5,
                        message: "Não foi possível inserir a viagem. Tente novamente."
                    });
                }, null, rota);
            };

            $scope.novaRota = function (prua, pnumero, pbairro, pcidade, pestado, ppais,
                    drua, dnumero, dbairro, dcidade, destado, dpais) {
                var partida = {
                    id: $scope.newGuid(),
                    rua: prua,
                    numero: pnumero,
                    bairro: pbairro,
                    cidade: pcidade,
                    estado: pestado,
                    pais: ppais
                };

                partida.string = $scope.rotaToString(partida);

                var destino = {
                    id: $scope.newGuid(),
                    rua: drua,
                    numero: dnumero,
                    bairro: dbairro,
                    cidade: dcidade,
                    estado: destado,
                    pais: dpais
                };

                destino.string = $scope.rotaToString(destino);

                var color = '#' + (0x1000000 + (Math.random()) * 0xffffff).toString(16).substr(1, 6);

                var novaRota = {
                    request: {
                        origin: partida.string,
                        destination: destino.string,
                        travelMode: google.maps.TravelMode["DRIVING"]
                    },
                    directionsDisplay: new google.maps.DirectionsRenderer(
                            {
                                draggable: true,
                                polylineOptions: {
                                    strokeColor: color,
                                    visible: true
                                }
                            }
                    ),
                    id: $scope.newGuid(),
                    partida: partida,
                    destino: destino,
                    metros: "",
                    segundos: "",
                    duracao: "",
                    distancia: "",
                    sucesso: 2,
                    cor: color,
                    show: true
                };

                google.maps.event.addListener(novaRota.directionsDisplay,
                        'directions_changed', function () {
                            var response = novaRota.directionsDisplay.getDirections();
                            if (response.status == google.maps.DirectionsStatus.OK) {

                                novaRota.metros = response.routes[0].legs[0].distance.value;
                                novaRota.segundos = response.routes[0].legs[0].duration.value;
                                novaRota.distancia = response.routes[0].legs[0].distance.text;
                                novaRota.duracao = response.routes[0].legs[0].duration.text;
                                novaRota.partida.string = response.routes[0].legs[0].start_address;
                                novaRota.destino.string = response.routes[0].legs[0].end_address;

                                var e = JSON.stringify(response);

                                $scope.calcularValores();
                                $scope.$apply();
                                novaRota.sucesso = 1;
                            } else {
                                novaRota.sucesso = 0;
                            }
                        });

                $scope.rotas.push(novaRota);
                $scope.carregarDirecao(novaRota);

                return novaRota;
            };

            $scope.carregar = function (rota) {
                rota.directionsDisplay.setMap($scope.map);
            };

            $scope.hoverRoute = function (rota) {
                rota.directionsDisplay.setOptions({
                    polylineOptions: {
                        strokeColor: rota.cor,
                        visible: rota.show,
                        strokeWeight: 3
                    }
                });
            };

            $scope.remover = function (rota) {
                rota.directionsDisplay.setMap(null);

                $($scope.rotas).each(function (i, r) {
                    if (r.id == rota.id) {
                        $scope.rotas.splice(i, 1);
                        return;
                    }
                });
            };

            $scope.inserir = function () {
                $scope.novaRota($scope.rc.prua, $scope.rc.pnumero, $scope.rc.pbairro,
                        $scope.rc.pcidade, $scope.rc.pestado, $scope.rc.ppais, $scope.rc.drua,
                        $scope.rc.rnumero, $scope.rc.dbairro,
                        $scope.rc.dcidade, $scope.rc.destado, $scope.rc.dpais);
            };

            $scope.loadMap = function () {
                $scope.directionsDisplay = new google.maps.DirectionsRenderer({draggable: true});
                $scope.directionsService = new google.maps.DirectionsService();
                var myOptions = {
                    zoom: 10,
                    mapTypeId: google.maps.MapTypeId.ROADMAP,
                    center: new google.maps.LatLng(35.270, -80.837)
                };
                $scope.map = new google.maps.Map(document.getElementById("maps"), myOptions);
                $scope.directionsDisplay.setMap($scope.map);
                $scope.directionsDisplay.setPanel(document.getElementById("directions"));
                if (navigator.geolocation) {
                    navigator.geolocation.getCurrentPosition(function (position) {

                        var pos = new google.maps.LatLng(position.coords.latitude,
                                position.coords.longitude);
                        $scope.map.setCenter(pos);
                    }, function () {
                        handleNoGeolocation(true);
                    });
                } else {
                    handleNoGeolocation(false);
                }
            };

            $scope.carregarDirecao = function (novaRota) {
                $scope.directionsService.route(novaRota.request, function (response, status) {
                    if (status == google.maps.DirectionsStatus.OK) {
                        novaRota.directionsDisplay.setMap($scope.map);
                        novaRota.directionsDisplay.setDirections(response);

                        novaRota.sucesso = 1;
                    } else {
                        novaRota.sucesso = 0;
                    }

                    $scope.$apply();
                });
            };

            $scope.newGuid = function () {
                function s4() {
                    return Math.floor((1 + Math.random()) * 0x10000)
                            .toString(16)
                            .substring(1);
                }
                return s4() + s4() + '-' + s4() + '-' + s4() + '-' +
                        s4() + '-' + s4() + s4() + s4();
            };

            $scope.carregarFuncionarios = function () {
                funcionarioService.listar(function (resultado) {
                    $scope.funcionarios = resultado;
                }, function () {
                    notifyService.add({
                        seconds: 5,
                        message: "Não foi possível carregar os funcionários.\n\
                          Verifique sua conexão com a internet."
                    });
                }, null, null);
            };

            $scope.carregarClientes = function () {
                clienteService.listar(function (resultado) {
                    $scope.clientes = resultado;
                }, function () {
                    notifyService.add({
                        seconds: 5,
                        message: "Não foi possível carregar os clientes.\n\
                          Verifique sua conexão com a internet."
                    });
                }, null, null);
            };

            $scope.carregarCaminhoes = function () {
                caminhaoService.listar(function (resultado) {
                    $scope.caminhoes = resultado;
                }, function () {
                    notifyService.add({
                        seconds: 5,
                        message: "Não foi possível carregar os caminhões.\n\
                          Verifique sua conexão com a internet."
                    });
                }, null, null);
            };

            $scope.carregarCargas = function () {
                cargaService.listar(function (resultado) {
                    $scope.cargas = resultado;
                }, function () {
                    notifyService.add({
                        seconds: 5,
                        message: "Não foi possível carregar as cargas.\n\
                          Verifique sua conexão com a internet."
                    });
                }, null);
            };

            $scope.voltar = function () {
                $location.url("/Viagem");
            };

            $scope.init = function () {
                $scope.loadMap();
                if ($routeParams.viagemID) {
                    var id = notifyService.add({
                        fixed: true,
                        message: "Carregando informações da viagem..."
                    });

                    viagemService.carregar(function (resultado) {
                        notifyService.remove(id);

                        $(resultado.rotas).each(function (i, r) {
                            $scope.novaRota(r.partida, "", "", "", "", "",
                                    r.destino, "", "", "", "", "");
                        });

                        $scope.nRota = resultado;


                        $scope.passo1Titulo = "1. Rotas";
                        $scope.passo2Titulo = "2. Informações da viagem";
                    }, function () {
                        notifyService.add({
                            seconds: 10,
                            message: "Não foi possível carregar as informações da viagem."
                        });
                    }, null, $routeParams.viagemID);
                } else {
                    $scope.passo1Titulo = "1. Informe as rotas";
                    $scope.passo2Titulo = "2. Informe os dados da viagem";
                }
            };

            $scope.carregarSelects = function () {
                if (!$scope.funcionarios)
                    $scope.carregarFuncionarios();
                if (!$scope.clientes)
                    $scope.carregarClientes();
                if (!$scope.caminhoes)
                    $scope.carregarCaminhoes();
                if (!$scope.cargas)
                    $scope.carregarCargas();
            };

            $scope.init();

//            $scope.novaRota("Av. Maestro Flaminio Mazzone", "213", "Portal do Sol", "Dobrada",
//                    "São Paulo", "Brasil",
//                    "", "", "", "Matão", "São Paulo", "Brasil");

        }]);
})();