(function () {
    var app = angular.module("Conta_Bancaria", []);

    app.controller("conta_bancariaController", ["conta_bancariaService", "$scope", "notifyService", "pesquisaService",
        function (conta_bancariaService, $scope, notifyService, pesquisaService) {
            $scope.titulo = "Título";
            $scope.itens = [];

            $scope.novo = {
                id: "0",
                nome: "",
                numero: "",
                pessoa_id: "",
                pessoa_nome: "",
                banco_id: "",
                banco: {
                    id: "",
                    nome: "",
                    numero: ""
                }
            };

            $scope.formularioValido = function () {
                var inputs = $("[name='conta_bancariaform']").find("input");
                return $.grep(inputs, function (i) {
                    return $(i).val() == "";
                }).length != 0;
            };


            pesquisaService.setFunction(function (search) {
                $scope.search = search;
            });

            $scope.inserir = function (novo) {
                conta_bancariaService.inserir(function () {
                    $scope.itens.push(novo);
                    $("#add").modal("hide");
                    $scope.carregarConta_Bancaria();
                }, function () {

                }, null, novo);
            };

            $scope.carregarConta_Bancaria = function () {
                conta_bancariaService.listar(function (resultado) {
                    $scope.itens = resultado;
                }, function () {

                }, null);
            };

            $scope.carregarConta_Bancaria();

            $scope.fab = {
                principalClick: function () {
                    $scope.modal = {
                        salvarNome: "Salvar",
                        titulo: "Nova Conta Bancaria",
                        salvarFuncao: $scope.inserir,
                        item: $scope.novo
                    };
                    $("#add").modal().modal("show");
                    //TODO: carragar os bancos
                },
                principalIcon: "md md-add",
                secondIcon: "md md-add",
                principalAlt: "Único"
            };

            $scope.editar = function (item) {
                $scope.modal = {
                    salvarNome: "Editar",
                    titulo: "Editar " + item.nome,
                    item: item,
                    salvarFuncao: $scope.editarSalvar
                };
                $("#add").modal().modal("show");
                //TODO: carragar os bancos
                $scope.novo = item;
            };

            $scope.editarSalvar = function (item) {
                conta_bancariaService.editar(function () {
                    $("#add").modal().modal("hide");
                    $scope.carregarConta_Bancaria();
                }, function () {
                }, null, item);
            };

            $scope.excluir = function (item) {
                if (!item.excluirID)
                    item.excluirID = notifyService.add({
                        fixed: true,
                        message: "Deseja excluir " + item.nome + "?",
                        buttons: [{
                                text: "Sim",
                                f: function (i) {
                                    conta_bancariaService.excluir(function () {
                                        $scope.carregarConta_Bancaria();//AQUI
                                    }, function () {
                                    }, null, item.id);
                                    i.excluirID = null;
                                },
                                parameter: item
                            },
                            {
                                text: "Não",
                                f: function (i) {
                                    i.excluirID = null;
                                },
                                parameter: item
                            }
                        ]
                    });
            };

        }]);

    app.service("conta_bancariaService", ["$http", "notifyService", function ($http, notifyService) {
            this.inserir = function (sucesso, erro, sempre, data) {
                var id = notifyService.add({
                    fixed: true,
                    message: "Adicionando Conta Bancaria..."
                });

                var server = "/AntevereTransportes";

                $http.post(server + "/ContaBancaria", this.formatar("INSERIR", data),
                        {
                            headers: {'Content-Type': 'application/x-www-form-urlencoded'}
                        })
                        .success(function (resultado) {
                            notifyService.remove(id);
                            if (resultado.sucesso) {
                                sucesso(resultado.resultado);
                                notifyService.add({
                                    seconds: 5,
                                    message: "Conta Bancaria adicionado"
                                });
                            }
                            else {
                                notifyService.add({
                                    seconds: 5,
                                    message: resultado.mensagem
                                });
                            }
                        }).error(function () {
                    notifyService.remove(id);
                    erro("Não foi possível cadastrar a Conta Bancaria.");
                });

                if (sempre)
                    sempre();
            };

            this.listar = function (sucesso, erro, sempre) {
                var id = notifyService.add({
                    fixed: true,
                    message: "Carregando conta bancaria..."
                });

                var server = "/AntevereTransportes";

                $http.post(server + "/ContaBancaria", this.formatar("LERVARIOS", null), {
                    headers: {'Content-Type': 'application/x-www-form-urlencoded'}

                })
                        .success(function (resultado) {
                            notifyService.remove(id);
                            if (resultado.sucesso) {
                                sucesso(resultado.resultado);
                            }
                            else {
                                erro(resultado.mensagem);
                            }
                        }).error(function () {
                    notifyService.remove(id);
                    notifyService.add({
                        seconds: 5,
                        message: "Não foi possível carregar as Contas Bancarias."
                    });
                    erro("Não foi possível carregar as Contas Bancarias.");
                });

                if (sempre)
                    sempre();
            };

            this.excluir = function (sucesso, erro, sempre, conta_bancariaID) {
                var id = notifyService.add({
                    fixed: true,
                    message: "Excluindo Conata Bancaria..."
                });

                var server = "/AntevereTransportes";

                $http.post(server + "/ContaBancaria", this.formatar("REMOVER", conta_bancariaID),
                        {
                            headers: {'Content-Type': 'application/x-www-form-urlencoded'}

                        })
                        .success(function (resultado) {
                            notifyService.remove(id);
                            if (resultado.sucesso) {
                                sucesso(resultado.resultado);
                            }
                            else {
                                erro(resultado.mensagem);
                            }
                        }).error(function () {
                    notifyService.remove(id);
                    notifyService.add({
                        seconds: 5,
                        message: "Não foi possível excluir a conta bancaria."
                    });
                    erro("Não foi possível excluir a conta bancaria.");
                });

                if (sempre)
                    sempre();
            };

            this.formatar = function (operacao, dado) {
                return "data=" + JSON.stringify({operacao: operacao, json: JSON.stringify(dado)});
            };

            this.editar = function (sucesso, erro, sempre, item) {
                var id = notifyService.add({
                    fixed: true,
                    message: "Editar conta bancaria..."
                });

                var server = "/AntevereTransportes";

                $http.post(server + "/ContaBancaria", this.formatar("EDITAR", item),
                        {
                            headers: {'Content-Type': 'application/x-www-form-urlencoded'}
                        }).success(function (resultado) {
                    notifyService.remove(id);
                    if (resultado.sucesso) {
                        sucesso(resultado.resultado);
                        notifyService.add({
                            seconds: 5,
                            message: "Conta Bancaria editada."
                        });
                    }
                    else {
                        notifyService.add({
                            seconds: 5,
                            message: resultado.mensagem
                        });
                        erro(resultado.mensagem);
                    }
                }).error(function () {
                    notifyService.remove(id);
                    notifyService.add({
                        seconds: 5,
                        message: "Não foi possível editar a conta bancaria."
                    });
                    erro("Não foi possível editar a conta bancaria.");
                });

                if (sempre)
                    sempre();
            };
        }]);
})();