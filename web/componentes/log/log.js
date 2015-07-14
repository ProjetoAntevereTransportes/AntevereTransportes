(function () {
    var app = angular.module("log", []);

    app.controller("logController", ["$scope", "logService", "notifyService", "pesquisaService", "moduloService",
        function ($scope, logService, notifyService, pesquisaService, moduloService) {
            $scope.itens = [];
            $scope.filter = {
                month: new Date().getMonth() + 1,
                year: new Date().getFullYear(),
                moduloId: 0
            };
            $scope.modulos = [];
                        
            pesquisaService.setFunction(function (search) {
                $scope.search = search;
            });
            
            $scope.carregar = function(filter){
                logService.lerVarios(function(resultado){
                    $scope.itens = resultado;
                }, function(error){
                    notifyService.add({
                        message: error,
                        seconds: 10
                    });
                }, null,
                filter);
            };
            
            $scope.carregarModulos = function(){
                moduloService.lerVarios(function(resultado){
                    $scope.modulos = resultado;
                }, function(erro){
                    notifyService.add({
                        message: erro,
                        seconds: 10
                    });
                }, null);
            };
            
            $scope.carregarModulos();
            $scope.carregar($scope.filter);
            
            $scope.nextMonth = function(){
                if($scope.filter.month == 12){
                    $scope.filter.month = 1;
                    $scope.filter.year++;
                }else{
                    $scope.filter.month++;
                }
                $scope.carregar($scope.filter);
            };
            
            $scope.previousMonth = function(){
                if($scope.filter.month == 1){
                    $scope.filter.month = 12;
                    $scope.filter.year--;
                }else{
                    $scope.filter.month--;
                }
                $scope.carregar($scope.filter);
            };
        }]);

    app.service("logService", ["$http", "notifyService", function ($http, notifyService) {
            this.formatar = function (operacao, dado) {
                return "data=" + JSON.stringify({operacao: operacao, json: JSON.stringify(dado)});
            };

            this.lerVarios = function (sucesso, erro, sempre, dado) {
                var id = notifyService.add({
                    fixed: true,
                    message: "Carregando logs..."
                });

                var server = "/AntevereTransportes";
                $http.post(server + "/Log", this.formatar("LERVARIOS", dado), {
                    headers: {'Content-Type': 'application/x-www-form-urlencoded'}})
                        .success(function (resultado) {
                            notifyService.remove(id);
                            if (resultado.sucesso) {
                                sucesso(resultado.resultado);
                            }
                            else {
                                erro(resultado.mensagem);
                            }
                        }).error(function () {
                    erro("Não foi possível carregar os logs.");
                });
                if (sempre)
                    sempre();
            };
        }]);
})();