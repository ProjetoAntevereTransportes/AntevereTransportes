(function () {
    var app = angular.module("funcionario", []);

    app.controller("funcionarioController", ["funcionarioService", "$scope", "notifyService", "cargoService",
        "pesquisaService",
        function (funcionarioService, $scope, notifyService, cargoService, pesquisaService) {
            $scope.titulo = "Título";
            $scope.itens = [];
            $scope.cargos = [];

            $scope.novo = {
                id: "0",
                nome: "",
                sobrenome: "",
                telefone: "",
                email: "",
                rg: "",
                cpf: "",
                endereco: {
                    id: "0",
                    rua: "",
                    bairro: "",
                    numero: "",
                    estado: "",
                    pais: "",
                    cidade: ""
                },
                cargo: {
                    id: "0",
                    nome: "",
                    descricao: ""
                }
            };

            $scope.formularioValido = function () {
                var inputs = $("[name='funcionarioform']").find("input");
                return $.grep(inputs, function (i) {
                    return $(i).val() == "";
                }).length != 0;
            };

            pesquisaService.setFunction(function (search) {
                $scope.search = search;
            });

            $scope.inserir = function (novo) {
                funcionarioService.inserir(function () {
                    $scope.itens.push(novo);
                    $("#add").modal("hide");
                    $scope.carregarFuncionarios();
                }, function () {

                }, null, novo);
            };

            $scope.carregarFuncionarios = function () {
                funcionarioService.listar(function (resultado) {
                    $scope.itens = resultado;
                }, function () {

                }, null);
            };

            $scope.validaCPF = function (cpf) {
                if (cpf == '' || cpf == null)
                    return false;

                cpf = cpf.replace(/[^\d]+/g, '');

                if (cpf.length != 11)
                    return false;

                // Elimina CPFs invalidos conhecidos    
                if (cpf == "00000000000" ||
                        cpf == "11111111111" ||
                        cpf == "22222222222" ||
                        cpf == "33333333333" ||
                        cpf == "44444444444" ||
                        cpf == "55555555555" ||
                        cpf == "66666666666" ||
                        cpf == "77777777777" ||
                        cpf == "88888888888" ||
                        cpf == "99999999999")
                    return false;
                // Valida 1o digito 
                add = 0;
                for (i = 0; i < 9; i ++)
                    add += parseInt(cpf.charAt(i)) * (10 - i);
                rev = 11 - (add % 11);
                if (rev == 10 || rev == 11)
                    rev = 0;
                if (rev != parseInt(cpf.charAt(9)))
                    return false;
                // Valida 2o digito 
                add = 0;
                for (i = 0; i < 10; i ++)
                    add += parseInt(cpf.charAt(i)) * (11 - i);
                rev = 11 - (add % 11);
                if (rev == 10 || rev == 11)
                    rev = 0;
                if (rev != parseInt(cpf.charAt(10)))
                    return false;
                return true;
            }

            $scope.carregarFuncionarios();

            $scope.fab = {
                principalClick: function () {
                    $scope.modal = {
                        salvarNome: "Salvar",
                        titulo: "Novo funcionário",
                        salvarFuncao: $scope.inserir,
                        item: $scope.novo
                    };
                    $("#add").modal().modal("show");
                    $scope.carregarCargos();
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
                $scope.carregarCargos();
                $scope.novo = item;
            };

            $scope.editarSalvar = function (item) {
                funcionarioService.editar(function () {
                    $("#add").modal().modal("hide");
                    $scope.carregarFuncionarios();
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
                                    funcionarioService.excluir(function () {
                                        $scope.carregarFuncionarios();
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

            $scope.carregarCargos = function () {
                cargoService.listar(function (resultado) {
                    $scope.cargos = resultado;
                }, function () {
                }, null);
            };

        }]);

    app.service("funcionarioService", ["$http", "notifyService", function ($http, notifyService) {
            this.inserir = function (sucesso, erro, sempre, data) {
                var id = notifyService.add({
                    fixed: true,
                    message: "Adicionando funcionário..."
                });

                var server = "/AntevereTransportes";

                $http.post(server + "/Funcionario", this.formatar("INSERIR", data),
                        {
                            headers: {'Content-Type': 'application/x-www-form-urlencoded'}
                        })
                        .success(function (resultado) {
                            notifyService.remove(id);
                            if (resultado.sucesso) {
                                sucesso(resultado.resultado);
                                notifyService.add({
                                    seconds: 5,
                                    message: "Funcionário adicionado"
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
                    erro("Não foi possível cadastrar o funcionário.");
                });

                if (sempre)
                    sempre();
            };

            this.listar = function (sucesso, erro, sempre) {
                var id = notifyService.add({
                    fixed: true,
                    message: "Carregando funcionários..."
                });

                var server = "/AntevereTransportes";

                $http.post(server + "/Funcionario", this.formatar("LERVARIOS", null), {
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
                        message: "Não foi possível carregar os funcionários."
                    });
                    erro("Não foi possível carregar os funcionários.");
                });

                if (sempre)
                    sempre();
            };

            this.excluir = function (sucesso, erro, sempre, fornecedorID) {
                var id = notifyService.add({
                    fixed: true,
                    message: "Excluindo funcionário..."
                });

                var server = "/AntevereTransportes";

                $http.post(server + "/Funcionario", this.formatar("REMOVER", fornecedorID),
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
                        message: "Não foi possível excluir o funcionário."
                    });
                    erro("Não foi possível excluir o funcionário.");
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
                    message: "Editar funcionário..."
                });

                var server = "/AntevereTransportes";

                $http.post(server + "/Funcionario", this.formatar("EDITAR", item),
                        {
                            headers: {'Content-Type': 'application/x-www-form-urlencoded'}
                        }).success(function (resultado) {
                    notifyService.remove(id);
                    if (resultado.sucesso) {
                        sucesso(resultado.resultado);
                        notifyService.add({
                            seconds: 5,
                            message: "Funcionário editado."
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
                        message: "Não foi possível editar o funcionário."
                    });
                    erro("Não foi possível editar o funcionário.");
                });

                if (sempre)
                    sempre();
            };
        }]);
})();