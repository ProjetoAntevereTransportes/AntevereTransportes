(function () {
    var app = angular.module("modulo", []);

    app.service("moduloService", ["$http", "notifyService", function ($http, notifyService) {
            this.formatar = function (operacao, dado) {
                return "data=" + JSON.stringify({operacao: operacao, json: JSON.stringify(dado)});
            };

            this.lerVarios = function (sucesso, erro, sempre) {
                var id = notifyService.add({
                    fixed: true,
                    message: "Carregando módulos..."
                });

                var server = "/AntevereTransportes";
                $http.post(server + "/Modulo", this.formatar("LERVARIOS", null), {
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
                    erro("Não foi possível carregar os módulos.");
                });
                if (sempre)
                    sempre();
            };
        }]);
})();