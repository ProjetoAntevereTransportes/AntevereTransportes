(function () {
    var app = angular.module("usuario", []);

    app.controller("usuarioController", ["usuarioService", "$scope", "notifyService","pesquisaService",
        function (usuarioService, $scope, notifyService,pesquisaService) {
            $scope.titulo = "Gerenciamento de Usuario";
            $scope.itens = [];

            $scope.perguntas = [];
            $scope.tipos = [];
            $scope.lstatus = []; // list status
            $scope.novo = {
                nome: "",
                email: "",
                senha: "",
                perguntaID: "",
                perguntaNome: "",
                resposta: "",
                tipoUsuarioID: "",
                tipoUsuarioNome: "",
                statusNome: "",
                statusID: "",
                chave: ""

            };


            $scope.formularioValido = function () {
                var inputs = $("[name='usuarioform']").find("input");
                return $.grep(inputs, function (i) {
                    return $(i).val() == "";
                }).length != 0;
            };


            $scope.carregarPerguntas = function () {
                usuarioService.listar(function (resultado) {
                    $scope.perguntas = resultado;
                }, function () {
                }, null);
            };


            $scope.inserir = function (novo) {
                usuarioService.inserir(function () {
                    $scope.itens.push(novo);
                    $("#add").modal("hide");
                    $scope.carregarUsuarios();
                }, function () {

                }, null, novo);
            };

            $scope.carregarUsuarios = function () {
                usuarioService.listar(function (resultado) {
                    $scope.itens = resultado;
                }, function () {

                }, null);
            };

            $scope.carregarUsuarios();

            $scope.fab = {
                principalClick: function () {
                    $scope.modal = {
                        salvarNome: "Salvar",
                        titulo: "Novo usuario",
                        salvarFuncao: $scope.inserir,
                        item: $scope.novo
                    };
                    $('#usuarioForm')[0].reset();
                    $("#add").modal().modal("show");

                    $scope.listarPergunta();
                    $scope.listarTipos();
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
                $scope.listarStatus();
                $scope.listarPergunta();
                $scope.listarTipos();


            };

            $scope.editarSalvar = function (item) {
                usuarioService.editar(function () {
                    $("#add").modal().modal("hide");

                }, function () {
                }, null, item);
            };
            $scope.listarPergunta = function () {
                usuarioService.listarPergunta(function (resultado) {
                    $scope.perguntas = resultado;
                }, function () {
                }, null);
            };
            $scope.listarTipos = function () {
                usuarioService.listarTipos(function (resultado) {
                    $scope.tipos = resultado;
                }, function () {
                }, null);
            };
            $scope.listarStatus = function () {
                usuarioService.listarStatus(function (resultado) {
                    $scope.lstatus = resultado;
                }, function () {
                }, null);
            };
            pesquisaService.setFunction(function (search) {
                $scope.search = search;
            });

            $scope.excluir = function (item) {
                if (!item.excluirID)
                    item.excluirID = notifyService.add({
                        fixed: true,
                        message: "Deseja excluir " + item.nome + "?",
                        buttons: [{
                                text: "Sim",
                                f: function (i) {
                                    usuarioService.excluir(function () {
                                        $scope.carregarUsuarios();
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



    app.service("usuarioService", ["$http", "notifyService", function ($http, notifyService) {
            this.inserir = function (sucesso, erro, sempre, data) {
                var id = notifyService.add({
                    fixed: true,
                    message: "Adicionando usuario..."
                });

                var server = "/AntevereTransportes";

                $http.post(server + "/Usuario", this.formatar("INSERIR", data),
                        {
                            headers: {'Content-Type': 'application/x-www-form-urlencoded'}
                        })
                        .success(function (resultado) {
                            notifyService.remove(id);
                            if (resultado.sucesso) {
                                sucesso(resultado.resultado);
                                notifyService.add({
                                    seconds: 5,
                                    message: "Usuario adicionado"
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
                    erro("Não foi possível cadastrar o usuario.");
                });

                if (sempre)
                    sempre();
            };

            this.listar = function (sucesso, erro, sempre) {
                var id = notifyService.add({
                    fixed: true,
                    message: "Carregando usuarios..."
                });

                var server = "/AntevereTransportes";
                $http.post(server + "/Usuario", this.formatar("LERVARIOS", null), {
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
                    erro("Não foi possível carregar os usuarios.");
                });
                if (sempre)
                    sempre();
            };

            this.excluir = function (sucesso, erro, sempre, usuarioID) {
                var id = notifyService.add({
                    fixed: true,
                    message: "Excluindo usuario..."
                });

                var server = "/AntevereTransportes";

                $http.post(server + "/Usuario", this.formatar("REMOVER", usuarioID),
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
                        message: "Não foi possível excluir o usuario."
                    });
                    erro("Não foi possível excluir o usuario.");
                });

                if (sempre)
                    sempre();
            };

            this.formatar = function (operacao, dado) {
                return "data=" + JSON.stringify({operacao: operacao, json: JSON.stringify(dado)});
            }
            this.listarPergunta = function (sucesso, erro, sempre) {
                var server = "/AntevereTransportes";
                $http.post(server + "/Pergunta", this.formatar("LERVARIOS", null), {
                    headers: {'Content-Type': 'application/x-www-form-urlencoded'}})
                        .success(function (resultado) {
                            if (resultado.sucesso) {
                                sucesso(resultado.resultado);
                            }
                            else {
                                erro(resultado.mensagem);
                            }
                        }).error(function () {
                    erro("Não foi possível carregar as perguntas.");
                });

                if (sempre)
                    sempre();
            };

            this.listarTipos = function (sucesso, erro, sempre) {
                var server = "/AntevereTransportes";
                $http.post(server + "/TipoUsuario", this.formatar("LERVARIOS", null), {
                    headers: {'Content-Type': 'application/x-www-form-urlencoded'}})
                        .success(function (resultado) {
                            if (resultado.sucesso) {
                                sucesso(resultado.resultado);
                            }
                            else {
                                erro(resultado.mensagem);
                            }
                        }).error(function () {
                    erro("Não foi possível carregar os tipos de usuarios.");
                });

                if (sempre)
                    sempre();

            };

            this.listarStatus = function (sucesso, erro, sempre) {
                var server = "/AntevereTransportes";
                $http.post(server + "/StatusUsuario", this.formatar("LERVARIOS", null), {
                    headers: {'Content-Type': 'application/x-www-form-urlencoded'}})
                        .success(function (resultado) {
                            if (resultado.sucesso) {
                                sucesso(resultado.resultado);
                            }
                            else {
                                erro(resultado.mensagem);
                            }
                        }).error(function () {
                    erro("Não foi possível carregar os status de usuarios.");
                });

                if (sempre)
                    sempre();

            };


            this.editar = function (sucesso, erro, sempre, item) {
                var id = notifyService.add({
                    fixed: true,
                    message: "Editar usuario..."
                });

                var server = "/AntevereTransportes";

                $http.post(server + "/Usuario", this.formatar("EDITAR", item),
                        {
                            headers: {'Content-Type': 'application/x-www-form-urlencoded'}
                        }).success(function (resultado) {
                    notifyService.remove(id);
                    if (resultado.sucesso) {
                        sucesso(resultado.resultado);
                        notifyService.add({
                            seconds: 5,
                            message: "Usuario editado."
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
                        message: "Não foi possível editar o usuario."
                    });
                    erro("Não foi possível editar o usuario.");
                });

                if (sempre)
                    sempre();
            };
        }]);

})();
