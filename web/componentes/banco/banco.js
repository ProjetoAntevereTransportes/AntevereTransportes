(function () {
    var app = angular.module("Banco", []);

    app.controller("bancoController", ["bancoService", "$scope", "notifyService", "pesquisaService",
        function (bancoService, $scope, notifyService, pesquisaService) {
            $scope.titulo = "Título";
            $scope.itens = [];

            $scope.novo = {
                nome: "",
                numero: ""
            };
            pesquisaService.setFunction(function (search) {
                $scope.search = search;
            });

            $scope.formularioValido = function () {
                var inputs = $("[name='bancoform']").find("input");
                return $.grep(inputs, function (i) {
                    return $(i).val() == "";
                }).length != 0;
            };
            $scope.reset = function () {
                $scope.novo = {
                    nome: "",
                    numero: ""
                };

                if ($scope.bancoform) {
                    $scope.bancoform.$setPristine();
                    $scope.bancoform.$setUntouched();
                }
            };



            $scope.inserir = function (novo) {
                bancoService.inserir(function () {
                    $scope.itens.push(novo);
                    $("#add").modal("hide");
                    $scope.carregarBanco();
                }, function () {

                }, null, novo);
            };

            $scope.carregarBanco = function () {
                bancoService.listar(function (resultado) {
                    $scope.itens = resultado;
                }, function () {

                }, null);
            };

            $scope.carregarBanco();

            $scope.fab = {
                principalClick: function () {
                    $scope.modal = {
                        salvarNome: "Salvar",
                        titulo: "Novo Banco",
                        salvarFuncao: $scope.inserir,
                        item: $scope.novo
                    };
                    $scope.reset();
                    $("#add").modal().modal("show");
                    $('#bancoform')[0].reset();
                },
                principalIcon: "md md-add",
                secondIcon: "md md-add",
                principalAlt: "Único"
            };
            $scope.consultar = function (item) {
                $scope.modal = {
                    salvarNome: "Consultar",
                    titulo: "Consultar " + item.nome,
                    item: item,
                    salvarFuncao: $scope.fechar
                };
                $("#add").modal().modal("show");
                $(".form-group > *").attr("disabled", true);
                $("#salvar").hide();
              
            };
            
            $scope.fechar = function (item) {
                $("#add").modal().modal("hide");
                $(".form-group > *").attr("disabled", false);
                $("#salvar").show();
            }



            $scope.editar = function (item) {
                $scope.modal = {
                    salvarNome: "Editar",
                    titulo: "Editar " + item.nome,
                    item: item,
                    salvarFuncao: $scope.editarSalvar
                };
                $("#add").modal().modal("show");

            };

            $scope.editarSalvar = function (item) {
                bancoService.editar(function () {
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
                                    bancoService.excluir(function () {
                                        $scope.carregarBanco();
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

    app.service("bancoService", ["$http", "notifyService", function ($http, notifyService) {
            this.inserir = function (sucesso, erro, sempre, data) {
                var id = notifyService.add({
                    fixed: true,
                    message: "Adicionando Banco..."
                });

                var server = "/AntevereTransportes";

                $http.post(server + "/Banco", this.formatar("INSERIR", data))
                        .success(function (resultado) {
                            notifyService.remove(id);
                            if (resultado.sucesso) {
                                sucesso(resultado.resultado);
                                notifyService.add({
                                    seconds: 5,
                                    message: "Banco adicionado"
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
                    erro("Não foi possível cadastrar o banco.");
                });

                if (sempre)
                    sempre();
            };

            this.listar = function (sucesso, erro, sempre) {
                var id = notifyService.add({
                    fixed: true,
                    message: "Carregando banco..."
                });

                var server = "/AntevereTransportes";

                $http.post(server + "/Banco", this.formatar("LERVARIOS", null))
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
                        message: "Não foi possível carregar os banco."
                    });
                    erro("Não foi possível carregar os banco.");
                });

                if (sempre)
                    sempre();
            };

            this.excluir = function (sucesso, erro, sempre, bancoID) {
                var id = notifyService.add({
                    fixed: true,
                    message: "Excluindo banco..."
                });

                var server = "/AntevereTransportes";

                $http.post(server + "/Banco", this.formatar("REMOVER", bancoID))
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
                        message: "Não foi possível excluir o banco."
                    });
                    erro("Não foi possível excluir o banco.");
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
                    message: "Editar banco..."
                });

                var server = "/AntevereTransportes";

                $http.post(server + "/Banco", this.formatar("EDITAR", item)).success(function (resultado) {
                    notifyService.remove(id);
                    if (resultado.sucesso) {
                        sucesso(resultado.resultado);
                        notifyService.add({
                            seconds: 5,
                            message: "Banco editado."
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
                        message: "Não foi possível editar o banco."
                    });
                    erro("Não foi possível editar o banco.");
                });

                if (sempre)
                    sempre();
            };
        }]);
})();