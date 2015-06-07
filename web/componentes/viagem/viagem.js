(function () {
    var app = angular.module("viagem", []);

    app.controller("viagemController", ["$scope", "viagemService", "notifyService", "$location",
        function ($scope, viagemService, notifyService, $location) {
            $scope.viagens = [];
            
            $scope.fab = {
                 principalClick: function () {                    
                    $location.url("/Rota");
                },
                principalIcon: "md md-add",
                secondIcon: "md md-add"
            };

            $scope.carregarViagens = function () {
                viagemService.listar(function (resultado) {
                    $scope.viagens = resultado;
                },
                        function () {
                            notifyService.add({
                                seconds: 5,
                                message: "Houve um erro ao carregar as viagens"
                            });
                        }, null, null);
            };





            $scope.carregarViagens();
        }]);

    app.service("viagemService", ["$http", function ($http) {
            this.formatar = function (operacao, dado) {
                return "data=" + JSON.stringify({operacao: operacao, json: JSON.stringify(dado)});
            };

            this.listar = function () {
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
        }]);
})();