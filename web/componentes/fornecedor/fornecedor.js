(function () {
    var app = angular.module("fornecedor", []);

    app.controller("fornecedorController", ["fornecedorService", "$scope", "notifyService", "pesquisaService",
        function (fornecedorService, $scope, notifyService, pesquisaService) {
            $scope.titulo = "Título";
            $scope.itens = [];

            $scope.novo = {
                nome: "",
                email: "",
                telefone: "",
                endereco: {
                    rua: "",
                    bairro: "",
                    numero: "",
                    estado: "",
                    pais: "",
                    cidade: "",
                    contato:"",
                }
            };

            pesquisaService.setFunction(function(search){
                $scope.search = search;
            });

            $scope.formularioValido = function () {
                var inputs = $("[name='fornecedorform']").find("input");
                return $.grep(inputs, function (i) {
                    return $(i).val() == "";
                }).length != 0;
            };

            $scope.inserir = function (novo) {
                fornecedorService.inserir(function () {
                    $scope.itens.push(novo);
                    $("#add").modal("hide");
                    $scope.carregarFornecedores();
                }, function () {

                }, null, novo);
            };

            $scope.carregarFornecedores = function () {
                fornecedorService.listar(function (resultado) {
                    $scope.itens = resultado;
                }, function () {

                }, null);
            };

            $scope.carregarFornecedores();

            $scope.fab = {
                principalClick: function () {
                    $scope.modal = {
                        salvarNome: "Salvar",
                        titulo: "Novo fornecedor",
                        salvarFuncao: $scope.inserir,
                        item: $scope.novo
                    };
                    $("#add").modal().modal("show");
                },
                principalIcon: "md md-add",
                secondIcon: "md md-add",
                
            };

            $scope.editar = function (item) {
                $scope.modal = {
                    salvarNome: "Editar",
                    titulo: "Editar " + item.nome,
                    item: item,
                    salvarFuncao: $scope.editarSalvar
                };
                $("#add").modal().modal("show");
                $scope.novo = item;
            };

            $scope.editarSalvar = function (item) {
                fornecedorService.editar(function () {
                    $("#add").modal().modal("hide");

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
                                    fornecedorService.excluir(function () {
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

    app.service("fornecedorService", ["$http", "notifyService", function ($http, notifyService) {
            this.inserir = function (sucesso, erro, sempre, data) {
                var id = notifyService.add({
                    fixed: true,
                    message: "Adicionando fornecedor..."
                });

                var server = "/AntevereTransportes";

                $http.post(server + "/Fornecedor", this.formatar("INSERIR", data),
                        {
                            headers: {'Content-Type': 'application/x-www-form-urlencoded'}
                        })
                        .success(function (resultado) {
                            notifyService.remove(id);
                            if (resultado.sucesso) {
                                sucesso(resultado.resultado);
                                notifyService.add({
                                    seconds: 5,
                                    message: "Fornecedor adicionado"
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
                    erro("Não foi possível cadastrar o fornecedor.");
                });

                if (sempre)
                    sempre();
            };

            this.listar = function (sucesso, erro, sempre) {
                var id = notifyService.add({
                    fixed: true,
                    message: "Carregando fornecedores..."
                });

                var server = "/AntevereTransportes";

                $http.post(server + "/Fornecedor", this.formatar("LERVARIOS", null), {
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
                        message: "Não foi possível carregar os fornecedores."
                    });
                    erro("Não foi possível carregar os fornecedores.");
                });

                if (sempre)
                    sempre();
            };

            this.excluir = function (sucesso, erro, sempre, fornecedorID) {
                var id = notifyService.add({
                    fixed: true,
                    message: "Excluindo fornecedor..."
                });

                var server = "/AntevereTransportes";

                $http.post(server + "/Fornecedor", this.formatar("REMOVER", fornecedorID),
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
                        message: "Não foi possível excluir o fornecedor."
                    });
                    erro("Não foi possível excluir o fornecedor.");
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
                    message: "Editar fornecedor..."
                });

                var server = "/AntevereTransportes";

                $http.post(server + "/Fornecedor", this.formatar("EDITAR", item),
                        {
                            headers: {'Content-Type': 'application/x-www-form-urlencoded'}
                        }).success(function (resultado) {
                    notifyService.remove(id);
                    if (resultado.sucesso) {
                        sucesso(resultado.resultado);
                        notifyService.add({
                            seconds: 5,
                            message: "Fornecedor editado."
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
                        message: "Não foi possível editar o fornecedor."
                    });
                    erro("Não foi possível editar o fornecedor.");
                });

                if (sempre)
                    sempre();
            };
        }]);
})();
