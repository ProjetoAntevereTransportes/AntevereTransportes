(function () {
    var app = angular.module("cliente", []);

    app.controller("clienteController", ["clienteService", "$scope", "notifyService", "pesquisaService",
        function (clienteService, $scope, notifyService, pesquisaService) {
            $scope.titulo = "Gerenciamento de Clientes";
            $scope.itens = [];
            $scope.lstatus = [];
            $scope.novo = {
                nome: "",
                email: "",
                telefone: "",
                cnpj: "",
                observacao: "",
                statusID: "",
                statusNome: "",
                endereco: {
                    rua: "",
                    bairro: "",
                    numero: "",
                    estado: "",
                    pais: "",
                    cidade: "",
                    contato: "",
                }
            };

            $scope.formularioValido = function (cnpj) {
                var inputs = $("[name='clienteform']").find("input");
                if ($scope.validaCnpj($("#cnpj").val()))
                    return false;
                return $.grep(inputs, function (i) {
                    return $(i).val() == "";
                }).length != 0;
            };
            pesquisaService.setFunction(function (search) {
                $scope.search = search;
            });

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
                $scope.listarStatus();
                $scope.listarPergunta();
                $scope.listarTipos();
            };

            $scope.fechar = function (item) {
                $("#add").modal().modal("hide");
                $(".form-group > *").attr("disabled", false);
                $("#salvar").show();
            }


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

            $scope.validaCnpj = function (cnpj) {
                if (cnpj == null || cnpj == "")
                    return false
                cnpj = cnpj.replace(/[^\d]+/g, '');

                if (cnpj == '')
                    return false;

                if (cnpj.length != 14)
                    return false;

                // Elimina CNPJs invalidos conhecidos
                if (cnpj == "00000000000000" ||
                        cnpj == "11111111111111" ||
                        cnpj == "22222222222222" ||
                        cnpj == "33333333333333" ||
                        cnpj == "44444444444444" ||
                        cnpj == "55555555555555" ||
                        cnpj == "66666666666666" ||
                        cnpj == "77777777777777" ||
                        cnpj == "88888888888888" ||
                        cnpj == "99999999999999")
                    return false;

                // Valida DVs
                tamanho = cnpj.length - 2
                numeros = cnpj.substring(0, tamanho);
                digitos = cnpj.substring(tamanho);
                soma = 0;
                pos = tamanho - 7;
                for (i = tamanho; i >= 1; i--) {
                    soma += numeros.charAt(tamanho - i) * pos--;
                    if (pos < 2)
                        pos = 9;
                }
                resultado = soma % 11 < 2 ? 0 : 11 - soma % 11;
                if (resultado != digitos.charAt(0))
                    return false;

                tamanho = tamanho + 1;
                numeros = cnpj.substring(0, tamanho);
                soma = 0;
                pos = tamanho - 7;
                for (i = tamanho; i >= 1; i--) {
                    soma += numeros.charAt(tamanho - i) * pos--;
                    if (pos < 2)
                        pos = 9;
                }
                resultado = soma % 11 < 2 ? 0 : 11 - soma % 11;
                if (resultado != digitos.charAt(1))
                    return false;

                return true;

            }

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
                    $scope.reset();
                    $('#clienteForm')[0].reset();
                    $scope.listarStatus();

                },
                principalIcon: "md md-add",
                secondIcon: "md md-add",
                principalAlt: "Único"
            };

            $scope.reset = function () {
                $scope.novo = {
                    nome: "",
                    email: "",
                    telefone: "",
                    cnpj: "",
                    observacao: "",
                    statusID: "",
                    statusNome: "",
                    endereco: {
                        rua: "",
                        bairro: "",
                        numero: "",
                        estado: "",
                        pais: "",
                        cidade: "",
                        contato: "",
                    }
                };
                if ($scope.clienteform) {
                    $scope.clienteform.$setPristine();
                    $scope.clienteform.$setUntouched();
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
                $scope.listarStatus();


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
                                        $scope.carregarClientes();
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

    app.service("clienteService", ["$http", "notifyService", function ($http, notifyService) {
            this.inserir = function (sucesso, erro, sempre, data) {
                var id = notifyService.add({
                    fixed: true,
                    message: "Adicionando cliente..."
                });

                var server = "/AntevereTransportes";

                $http.post(server + "/Cliente", this.formatar("INSERIR", data))
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
                $http.post(server + "/Cliente", this.formatar("LERVARIOS", null))
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

            this.excluir = function (sucesso, erro, sempre, clienteID) {
                var id = notifyService.add({
                    fixed: true,
                    message: "Excluindo Cliente..."
                });

                var server = "/AntevereTransportes";

                $http.post(server + "/Cliente", this.formatar("REMOVER", clienteID))
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
                $http.post(server + "/StatusCliente", this.formatar("LERVARIOS", null))
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

                $http.post(server + "/Cliente", this.formatar("EDITAR", item)).success(function (resultado) {
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
