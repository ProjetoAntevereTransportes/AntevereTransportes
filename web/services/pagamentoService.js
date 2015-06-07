(function () {
    var app = angular.module("pagamentoService", []);

    app.service("$pgService", ["$http", "notifyService", function ($http, notifyService) {
            this.formatar = function (operacao, dado) {
                return "data=" + JSON.stringify({operacao: operacao, json: JSON.stringify(dado)});
            };

            this.excluir = function(sucesso, erro, sempre, pagamentoID){
                var resultado = null;
                $http.post("/AntevereTransportes/Pagamento", this.formatar("EXCLUIR", pagamentoID))
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

            this.pagamentoFornecedorMensal = function(sucesso, erro, sempre, fornecedorID){               
                var resultado = null;
                $http.post("/AntevereTransportes/Pagamento", this.formatar("PAGAMENTOMENSALFORNECEDOR", fornecedorID))
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

            this.pagarUnico = function (sucesso, erro, sempre, data) {
                var resultado = null;
                
                delete data["$$hashKey"];
                delete data["c"];
                data.vencimento = null;
                
                $http.post("/AntevereTransportes/Pagamento", this.formatar("PAGARUNICO", data))
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

            this.pagarDebito = function(sucesso, erro, sempre, data){
                var resultado = null;
                
                delete data["$$hashKey"];
                delete data["c"];
                data.vencimento = new Date(data.vencimento);
                
                $http.post("/AntevereTransportes/Pagamento", this.formatar("PAGARDEBITO", data))
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

            this.listar = function (sucesso, erro, sempre, data) {
                var resultado;
                var id = notifyService.add({
                    fixed: true,
                    message: "Carregando contas à pagar..."
                });
                $http.post("/AntevereTransportes/Pagamento", this.formatar("LERVARIOS", data))
                        .success(function (data) {
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
                $http.post("/AntevereTransportes/Pagamento", this.formatar("SALVARVARIOS", contas)
                        ).success(function (data) {
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
                
                if(!debito.dataInicio)
                    delete debito["dataInicio"];
                
                if(!debito.dataFim)
                    delete debito["dataFim"];
                
                if(!debito.dia)
                    delete debito["dia"];

                var resultado;
                $http.post("/AntevereTransportes/Pagamento", this.formatar("SALVARDEBITO", debito)
                        ).success(function (data) {
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