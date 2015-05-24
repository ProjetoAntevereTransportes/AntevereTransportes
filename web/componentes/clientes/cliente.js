(function () {
    var app = angular.module("cliente", []);

    app.controller("clienteController", ["clienteService", "$scope", "notifyService",
        function (clienteService, $scope, notifyService) {
            $scope.titulo = "Gerenciamento de Clientes";
            $scope.itens = [];
            $scope.lstatus = []; // list status
            $scope.novo = {
                nome: "",
                email: "",
                telefone: "",
                cnpj: "",
                observacao: "",
                statusID: "",
                statusNome: ""
            };

            $scope.formularioValido = function () {
                var inputs = $("[name='clienteform']").find("input");
                return $.grep(inputs, function (i) {
                    return $(i).val() == "";
                }).length != 0;
            };


            $scope.inserir = function (novo) {
                clienteService.inserir(function () {
                    $scope.itens.push(novo);
                    $("#add").modal("hide");
                    $scope.carregarClientes();
                }, function () {

                }, null, novo);
            };

            $scope.carregarClientes = function () {
                clienteService.listar(function (resultado) {
                    $scope.itens = resultado;
                }, function () {

                }, null);
            };

            $scope.carregarClientes();

            $scope.fab = {
                principalClick: function () {
                    $scope.modal = {
                        salvarNome: "Salvar",
                        titulo: "Novo Cliente",
                        salvarFuncao: $scope.inserir,
                        item: $scope.novo
                    };
                    
                    $("#add").modal().modal("show");

                    $scope.listarStatus();

                },
                principalIcon: "glyphicon glyphicon-plus",
                secondIcon: "glyphicon glyphicon-plus",
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

                $scope.listarTipos();

                $scope.novo = item;
            };

            $scope.editarSalvar = function (item) {
                clienteService.editar(function () {
                    $("#add").modal().modal("hide");

                }, function () {
                }, null, item);
            };

            $scope.listarStatus = function () {
                clienteService.listarStatus(function (resultado) {
                    $scope.lstatus = resultado;
                }, function () {
                }, null);
            };

            $scope.excluir = function (item) {
                if (!item.excluirID)
                    item.excluirID = notifyService.add({
                        fixed: true,
                        message: "Deseja excluir " + item.nome + "?",
                        buttons: [{
                                text: "Sim",
                                f: function (i) {
                                    clienteService.excluir(function () {
                                    }, function () {
                                    }, null, item.ID);
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

    app.service("clienteService", ["$http", "notifyService", function ($http, notifyService) {
            this.inserir = function (sucesso, erro, sempre, data) {
                var id = notifyService.add({
                    fixed: true,
                    message: "Adicionando cliente..."
                });

                var server = "/AntevereTransportes";

                $http.post(server + "/Cliente", this.formatar("INSERIR", data),
                        {
                            headers: {'Content-Type': 'application/x-www-form-urlencoded'}
                        })
                        .success(function (resultado) {
                            notifyService.remove(id);
                            if (resultado.sucesso) {
                                sucesso(resultado.resultado);
                                notifyService.add({
                                    seconds: 5,
                                    message: "Cliente adicionado"
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
                    erro("Não foi possível cadastrar o cliente.");
                });

                if (sempre)
                    sempre();
            };

            this.listar = function (sucesso, erro, sempre) {
                var id = notifyService.add({
                    fixed: true,
                    message: "Carregando clientes..."
                });

                var server = "/AntevereTransportes";
                $http.post(server + "/Cliente", this.formatar("LERVARIOS", null), {
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
                    erro("Não foi possível carregar os clientes.");
                });
                if (sempre)
                    sempre();
            };

            this.excluir = function (sucesso, erro, sempre, usuarioID) {
                var id = notifyService.add({
                    fixed: true,
                    message: "Excluindo Cliente..."
                });

                var server = "/AntevereTransportes";

                $http.post(server + "/Cliente", this.formatar("REMOVER", usuarioID),
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
                        message: "Não foi possível excluir o cliente."
                    });
                    erro("Não foi possível excluir o cliente.");
                });

                if (sempre)
                    sempre();
            };

            this.formatar = function (operacao, dado) {
                return "data=" + JSON.stringify({operacao: operacao, json: JSON.stringify(dado)});
            };


            this.listarStatus = function (sucesso, erro, sempre) {
                var server = "/AntevereTransportes";
                $http.post(server + "/StatusCliente", this.formatar("LERVARIOS", null), {
                    headers: {'Content-Type': 'application/x-www-form-urlencoded'}})
                        .success(function (resultado) {
                            if (resultado.sucesso) {
                                sucesso(resultado.resultado);
                            }
                            else {
                                erro(resultado.mensagem);
                            }
                        }).error(function () {
                    erro("Não foi possível carregar os status de clientes.");
                });

                if (sempre)
                    sempre();

            };


            this.editar = function (sucesso, erro, sempre, item) {
                var id = notifyService.add({
                    fixed: true,
                    message: "Editar cliente..."
                });

                var server = "/AntevereTransportes";

                $http.post(server + "/Cliente", this.formatar("EDITAR", item),
                        {
                            headers: {'Content-Type': 'application/x-www-form-urlencoded'}
                        }).success(function (resultado) {
                    notifyService.remove(id);
                    if (resultado.sucesso) {
                        sucesso(resultado.resultado);
                        notifyService.add({
                            seconds: 5,
                            message: "Cliente editado."
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
                        message: "Não foi possível editar o cliente."
                    });
                    erro("Não foi possível editar o cliente.");
                });

                if (sempre)
                    sempre();
            };
        }]);

})();
