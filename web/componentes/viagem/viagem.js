(function () {
    var app = angular.module("viagem", []);

    app.controller("viagemController", ["$scope", "viagemService", "notifyService", "$location", "pesquisaService",
        function ($scope, viagemService, notifyService, $location, pesquisaService) {
            $scope.viagens = [];

            $scope.cancel = function () {
                notifyService.add({
                    fixed: true,
                    message: "Deseja cancelar a viagem?",
                    buttons: [{
                            text: "Sim",
                            parameter: 1,
                            f: function(){}
                        }, {
                            text: "Não",
                            parameter: 2,
                            f: function(){}
                        }]
                });
            };

            $scope.fab = {
                principalClick: function () {
                    $location.url("/Rota");
                },
                principalIcon: "md md-add",
                secondIcon: "md md-add"
            };

            pesquisaService.setFunction(function (search) {
                $scope.search = search;
            });

            $scope.totalKm = function (item) {
                var total = 0;
                $(item.rotas).each(function (i, r) {
                    total += +r.metros;
                });

                return total / 1000;
            };

            $scope.carregarViagens = function () {
                var id = notifyService.add({
                    fixed: true,
                    message: "Carregando viagens..."
                });

                viagemService.listar(function (resultado) {
                    notifyService.remove(id);
                    $scope.viagens = resultado;
                },
                        function () {
                            notifyService.remove(id);
                            notifyService.add({
                                seconds: 5,
                                message: "Houve um erro ao carregar as viagens"
                            });
                        }, null, null);
            };

            $scope.cliqueViagem = function (viagemID) {
                $location.url("/Rota/" + viagemID);
            };

            $scope.carregarViagens();
        }]);

    app.service("viagemService", ["$http", function ($http) {
            this.formatar = function (operacao, dado) {
                return "data=" + JSON.stringify({operacao: operacao, json: JSON.stringify(dado)});
            };

            this.listar = function (sucesso, erro, sempre, dado) {
                var resultado = null;
                $http.post("/AntevereTransportes/Viagem", this.formatar("LERVARIOS", null))
                        .success(function (data) {
                            resultado = data;
                            if (data.sucesso)
                                sucesso(data.resultado);
                            else {
                                erro(data.mensagem);
                            }
                        }).error(function () {
                    erro(resultado.mensagem);
                });
            };

            this.inserir = function (sucesso, erro, sempre, dado) {
                var resultado = null;
                $http.post("/AntevereTransportes/Viagem", this.formatar("INSERIR", dado))
                        .success(function (data) {
                            resultado = data;
                            if (data.sucesso)
                                sucesso(data.resultado);
                            else {
                                erro(data.mensagem);
                            }
                        }).error(function () {
                    erro(resultado.mensagem);
                });
            };

            this.carregar = function (sucesso, erro, sempre, dado) {
                var resultado = null;
                $http.post("/AntevereTransportes/Viagem", this.formatar("LER", dado))
                        .success(function (data) {
                            resultado = data;
                            if (data.sucesso)
                                sucesso(data.resultado);
                            else {
                                erro(data.mensagem);
                            }
                        }).error(function () {
                    erro(resultado.mensagem);
                });
            };
        }]);
})();