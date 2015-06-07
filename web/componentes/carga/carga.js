(function () {
    var app = angular.module("carga", []);

    app.controller("cargaController", ["cargaService", "$scope", "notifyService", "pesquisaService",
        function (cargaService, $scope, notifyService, pesquisaService) {
            $scope.titulo = "Título";
            $scope.itens = [];

            $scope.novo = {
               
                nome: "",
                descricao: ""
            };

            $scope.formularioValido = function () {
                var inputs = $("[name='cargaform']").find("input");
                return $.grep(inputs, function (i) {
                    return $(i).val() == "";
                }).length != 0;
            };
            pesquisaService.setFunction(function (search) {
                $scope.search = search;
            });

            $scope.inserir = function (novo) {
                cargaService.inserir(function () {
                    $scope.itens.push(novo);
                    $("#add").modal("hide");
                    $scope.carregarCargas();
                }, function () {

                }, null, novo);
            };

            $scope.carregarCargas = function () {
                cargaService.listar(function (resultado) {
                    $scope.itens = resultado;
                }, function () {

                }, null);
            };

            $scope.carregarCargas();

            $scope.fab = {
                principalClick: function () {
                    $scope.modal = {
                        salvarNome: "Salvar",
                        titulo: "Nova Carga",
                        salvarFuncao: $scope.inserir,
                        item: $scope.novo
                    };
                    $scope.reset();
                    $('#cargaform')[0].reset();
                    $("#add").modal("show");
                },
                principalIcon: "md md-add",
                secondIcon: "md md-add",
                principalAlt: "Único"
            };

            $scope.reset = function () {
                $scope.novo = {
                  
                    nome: "",
                    descricao: ""
                };
                if ($scope.cargaform) {
                    $scope.cargaform.$setPristine();
                    $scope.cargaform.$setUntouched();
                }
            };

            $scope.editar = function (item) {
                $scope.modal = {
                    salvarNome: "Editar",
                    titulo: "Editar " + item.nome,
                    item: item,
                    salvarFuncao: $scope.editarSalvar
                };
                $("#add").modal().modal("show");
                
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


            $scope.editarSalvar = function (item) {
                cargaService.editar(function () {
                    $("#add").modal().modal("hide");
                    $scope.carregarCargas();
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
                                    cargaService.excluir(function () {
                                        $scope.carregarCargas();
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

    app.service("cargaService", ["$http", "notifyService", function ($http, notifyService) {
            this.inserir = function (sucesso, erro, sempre, data) {
                var id = notifyService.add({
                    fixed: true,
                    message: "Adicionando carga..."
                });

                var server = "/AntevereTransportes";

                $http.post(server + "/Carga", this.formatar("INSERIR", data))
                        .success(function (resultado) {
                            notifyService.remove(id);
                            if (resultado.sucesso) {
                                sucesso(resultado.resultado);
                                notifyService.add({
                                    seconds: 5,
                                    message: "Carga adicionada"
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
                    erro("Não foi possível cadastrar a carga.");
                });

                if (sempre)
                    sempre();
            };

            this.listar = function (sucesso, erro, sempre) {
                var id = notifyService.add({
                    fixed: true,
                    message: "Carregando cargas..."
                });

                var server = "/AntevereTransportes";

                $http.post(server + "/Carga", this.formatar("LERVARIOS", null))
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
                        message: "Não foi possível carregar as cargas."
                    });
                    erro("Não foi possível carregar as cargas.");
                });

                if (sempre)
                    sempre();
            };

            this.excluir = function (sucesso, erro, sempre, fornecedorID) {
                var id = notifyService.add({
                    fixed: true,
                    message: "Excluindo carga..."
                });

                var server = "/AntevereTransportes";

                $http.post(server + "/Carga", this.formatar("REMOVER", fornecedorID),
                        {
                            headers: {'Content-Type': 'application/x-www-form-urlencoded'}

                        })
                        .success(function (resultado) {
                            notifyService.remove(id);
                            if (resultado.sucesso) {
                                sucesso(resultado.resultado);
                                //$scope.carregarCargos();
                            }
                            else {
                                erro(resultado.mensagem);
                            }
                        }).error(function () {
                    notifyService.remove(id);
                    notifyService.add({
                        seconds: 5,
                        message: "Não foi possível excluir a carga."
                    });
                    erro("Não foi possível excluir a carga.");
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
                    message: "Editar carga..."
                });

                var server = "/AntevereTransportes";

                $http.post(server + "/Carga", this.formatar("EDITAR", item))
                        .success(function (resultado) {
                    notifyService.remove(id);
                    if (resultado.sucesso) {
                        sucesso(resultado.resultado);
                        notifyService.add({
                            seconds: 5,
                            message: "Carga editada."
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
                        message: "Não foi possível editar a carga."
                    });
                    erro("Não foi possível editar a carga.");
                });

                if (sempre)
                    sempre();
            };
        }]);
})();
