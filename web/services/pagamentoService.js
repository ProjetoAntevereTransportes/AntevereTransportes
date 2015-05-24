(function () {
    var app = angular.module("pagamentoService", []);

    app.service("$pgService", ["$http", "notifyService", function ($http, notifyService) {
            this.formatar = function (operacao, dado) {
                return "data=" + JSON.stringify({operacao: operacao, json: JSON.stringify(dado)});
            };

            this.listar = function (sucesso, erro, sempre, data) {
                var resultado;
                var id = notifyService.add({
                    fixed: true,
                    message: "Carregando contas à pagar..."
                });
                $http.post("/AntevereTransportes/Pagamento", this.formatar("LERVARIOS", data), {
                    headers: {'Content-Type': 'application/x-www-form-urlencoded'}
                }).success(function (data) {
                    notifyService.remove(id);
                    resultado = data;
                    if (data.sucesso)
                        sucesso(data.resultado);
                    else {
                        erro(data.mensagem);
                        notifyService.add({
                            fixed: 5,
                            message: data.mensagem
                        });
                    }
                }).error(function () {
                    erro(resultado.mensagem);
                    notifyService.add({
                        fixed: 5,
                        message: "Não foi possível carregar as contas à pagar."
                    });
                });

                if (sempre)
                    sempre(resultado);
            };

            this.salvarMassa = function (sucesso, erro, sempre, contas) {
                var id = notifyService.add({
                    fixed: true,
                    message: "Salvando contas..."
                });

                $(contas.parcelas).each(function (i, c) {
                    delete c["$$hashKey"];
                });

                var resultado;
                $http.post("/AntevereTransportes/Pagamento", this.formatar("SALVARVARIOS", contas), {
                    headers: {'Content-Type': 'application/x-www-form-urlencoded'}
                }).success(function (data) {
                    notifyService.remove(id);
                    resultado = data;
                    if (data.sucesso) {
                        sucesso(data.resultado);
                        notifyService.add({
                            seconds: 5,
                            message: "Conta salva."
                        });
                    }
                    else {
                        notifyService.add({
                            seconds: 5,
                            message: resultado.mensagem
                        });
                        erro(data.mensagem);
                    }
                }).error(function () {
                    erro(resultado.mensagem);
                    notifyService.add({
                        seconds: 5,
                        message: "Houve um erro ao salvar as contas."
                    });
                });

                if (sempre)
                    sempre(resultado);
            };

            this.salvarDebito = function (sucesso, erro, sempre, debito) {
                var id = notifyService.add({
                    fixed: true,
                    message: "Salvando débito automático..."
                });

                var resultado;
                $http.post("/AntevereTransportes/Pagamento", this.formatar("SALVARDEBITO", debito), {
                    headers: {'Content-Type': 'application/x-www-form-urlencoded'}
                }).success(function (data) {
                    notifyService.remove(id);
                    resultado = data;
                    if (data.sucesso) {
                        sucesso(data.resultado);
                        notifyService.add({
                            seconds: 5,
                            message: "Débito automático salva."
                        });
                    }
                    else {
                        notifyService.add({
                            seconds: 5,
                            message: resultado.mensagem
                        });
                        erro(data.mensagem);
                    }
                }).error(function () {
                    erro(resultado.mensagem);
                    notifyService.add({
                        seconds: 5,
                        message: "Houve um erro ao salvar o débito automático."
                    });
                });
            };
        }]);
})();