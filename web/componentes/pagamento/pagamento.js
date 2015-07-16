(function () {
    var app = angular.module("pagamento", []);

    app.directive("pagamentoItem", function () {
        return {
            restrict: 'E',
            templateUrl: '/componentes/pagamento/pagamento-item.html',
            scope: {
                filter: "=filter"
            }
        };
    });

    app.controller("PagamentoController", ["$scope", "$http", "$pgService", "notifyService",
        "fornecedorService", "fileUpload", "$window", "$document", "pesquisaService",
        "hotkeys", "conta_bancariaService",
        function ($scope, $http, $pgService, notifyService, fornecedorService,
                fileUpload, $window, $document, pesquisaService, hotkeys, conta_bancariaService) {
            $scope.dataLoad = new Date();
            $scope.itens = [];
            $scope.mensagem = "";
            $scope.mostrarCalendario = false;
            $scope.semanas = [];
            $scope.fornecedores = [];
            $scope.mes = new Date().getMonth();
            $scope.ano = new Date().getFullYear();
            $scope.itemNovoMassa = {
                parcelas: [],
                fornecedorID: "",
                inicial: null,
                final: null,
                nome: "",
                valor: 0
            };

            $scope.vazio = {
                titulo: "Não há contas",
                icone: "md md-attach-money",
                descricao: "Clique em + para inserir contas únicas, carnês e débitos automáticos"
            };

            $scope.adicionarMes = function () {
                if ($scope.mes == 11) {
                    $scope.mes = 0;
                    $scope.ano++;
                } else
                    $scope.mes++;

                $scope.carregarPagamentos();
            };

            $scope.removerMes = function () {
                if ($scope.mes == 0) {
                    $scope.mes = 11;
                    $scope.ano--;
                } else
                    $scope.mes--;

                $scope.carregarPagamentos();
            };

            hotkeys.bindTo($scope)
                    .add({
                        combo: 'right',
                        description: 'Adiciona um mês.',
                        callback: function () {
                            $scope.adicionarMes();
                        }
                    });

            hotkeys.bindTo($scope)
                    .add({
                        combo: 'left',
                        description: 'Remove um mês.',
                        callback: function () {
                            $scope.removerMes();
                        }
                    });

            pesquisaService.setFunction(function (search) {
                $scope.search = search;
            });

            $scope.resetUnico = function () {
                $scope.unico = {
                    fornecedorID: "",
                    nome: "",
                    parcelas: []
                };

                $scope.unicoParcela = {
                    vencimento: "",
                    descricao: "",
                    valor: "",
                    boletoID: null
                };

                if ($scope.unicoForm) {
                    $scope.unicoForm.$setPristine();
                    $scope.unicoForm.$setUntouched();
                }
            };

            $scope.resetUnico();

            $scope.expandirInfo = function (item) {
                if (item.c.expandirInfo)
                    item.c.expandirInfo = false;
                else {
                    item.c.expandirInfo = true;

                    $pgService.pagamentoFornecedorMensal(function (dados) {

                        new Morris.Bar({
                            element: item.ID + "-fornecedor-chart",
                            data: dados,
                            xkey: 'mes',
                            ykeys: ['valor'],
                            labels: ['Valor'],
                            yLabelFormat: function (y) {
                                return "R$ " + y.toString();
                            },
                            xLabelFormat: function (x) {
                                x = new Date(x.label);
                                return (x.getMonth() + 1) + "/" + x.getFullYear();
                            }
                        });

                    }, function () {
                        notifyService.add({
                            seconds: 5,
                            message: "Não foi possível obter as informações dos fornecedores."
                        });
                    }, null, item.fornecedorID);
                }
            };

            $scope.gerarParcelas = function () {
                if ($scope.itemNovoMassa.inicial && $scope.itemNovoMassa.final) {
                    $scope.itemNovoMassa.parcelas = [];


                    var inicial = angular.copy($scope.itemNovoMassa.inicial);
                    var final = angular.copy($scope.itemNovoMassa.final);

                    while (inicial <= final) {

                        $scope.itemNovoMassa.parcelas.push({
                            descricao: null,
                            vencimento: angular.copy(inicial),
                            valor: $scope.itemNovoMassa.valor,
                            boletoID: null
                        });

                        inicial = new Date(inicial.setMonth(inicial.getMonth() + 1));

                        console.log(inicial);
                    }
                }
            };


            $scope.debitoAutomaticoValido = function () {
                if (!$scope.debito)
                    return true;

                var d = $scope.debito;

                return !(d.contaBancariaID && d.fornecedorID && d.nome && d.valor &&
                        ((d.dataFim && d.dataInicio) || (d.dia && d.dia >= 1 && d.dia <= 31)));
            };

            $scope.corrigirDatasDebitoAutomatico = function (data) {
                var dia = data.getDate();

                $scope.debito.dataFim = new Date($scope.debito.dataFim.setDate(dia));
                $scope.debito.dataInicio = new Date($scope.debito.dataInicio.setDate(dia));
            };

            $scope.salvarMassa = function (contas) {
                $pgService.salvarMassa(function () {
                    $scope.carregarPagamentos($scope.dataLoad);
                    $("#editar").modal("hide");
                    $("#unico").modal("hide");
                }, function () {
                }, null, contas);
            };

            $scope.salvarUnico = function () {
                var id = notifyService.add({
                    fixed: true,
                    message: "Salvando conta..."
                });

                if ($scope.unicoParcela.boletoID != null)
                    fileUpload.upload(function (fileId) {
                        $scope.unicoParcela.boletoID = fileId;

                        $scope.salvarUnicoSemComprovante(id);

                    }, function () {
                        notifyService.remove(id);
                        notifyService.add({
                            seconds: 5,
                            message: "Houve um erro ao salvar o boleto. Tente novamente."
                        });
                    }, null, $scope.unicoParcela.boletoID);
                else
                    $scope.salvarUnicoSemComprovante(id);
            };

            $scope.salvarUnicoSemComprovante = function (notificationID) {
                $scope.unico.parcelas = [];
                $scope.unico.parcelas.push($scope.unicoParcela);

                $pgService.salvarMassa(function () {
                    notifyService.remove(notificationID);

                    notifyService.add({
                        seconds: 5,
                        message: "Conta salva."
                    });

                    $scope.carregarPagamentos($scope.dataLoad);
                    $("#editar").modal("hide");
                    $("#unico").modal("hide");

                    $scope.resetUnico();
                }, function () {
                    notifyService.remove(notificationID);
                    notifyService.add({
                        seconds: 10,
                        message: "Houve um erro ao salvar a conta. Tente novamente."
                    });
                }, null, $scope.unico);
            };



            $scope.mouseOverItem = function (item) {

            };

            $scope.mouseLeaveItem = function (item) {

            };

            $scope.mostrarAcaoBotao = function (item) {
                if (item.c.mostrarBotoes)
                    item.c.mostrarBotoes = false;
                else
                    item.c.mostrarBotoes = true;
            };

            $scope.alterarStatusCalendario = function () {
                if ($scope.mostrarCalendario) {
                    $scope.mostrarCalendario = false;
                }
                else {
                    $scope.mostrarCalendario = true;
                }
            };

            $scope.pagarUnico = function (item) {
                if (!item.comprovanteID && (item.unico || item.carne)) {
                    notifyService.add({
                        message: "Insira um comprovante",
                        seconds: 5
                    });
                    return;
                }

                var message = "";
                if (item.vencido && !item.juros)
                    message = "O valor do juros não foi preenchido.";

                if (item.c.notificationID == null)
                    item.c.notificationID = notifyService.add({
                        fixed: true,
                        message: "Deseja realizar o pagamento de " + item.descricao + "? " + message,
                        buttons: [{
                                text: "Sim",
                                parameter: item,
                                f: $scope.realizarPagamentoUnico
                            },
                            {
                                text: "Não",
                                parameter: item,
                                f: $scope.cancelarPagamentoUnico,
                                defaultExit: true
                            }
                        ]
                    });
            };

            $scope.pagarDebito = function (item) {
                $pgService.pagarDebito(function (item) {

                    notifyService.add({
                        seconds: 5,
                        message: "Conta paga."
                    });

                    $scope.carregarPagamentos($scope.dataLoad);

                }, function () {
                    notifyService.add({
                        seconds: 5,
                        message: "Não foi possível pagar a conta."
                    });
                }, null, item);

            };

            $scope.realizarPagamentoUnico = function (item) {
                item.c.expandir = false;
                item.c.notificationID = null;

                notifyService.add({
                    seconds: 3,
                    message: "Realizando pagamento de " + item.nome
                });

                if (item.debitoLimitado || item.debitoIlimitado) {
                    $scope.pagarDebito(item);
                    return;
                }

                fileUpload.upload(function (comprovanteID) {

                    item.comprovanteID = comprovanteID;

                    $pgService.pagarUnico(function () {

                        notifyService.add({
                            seconds: 5,
                            message: "Conta paga."
                        });

                        $scope.carregarPagamentos($scope.dataLoad);

                    }, function () {
                        notifyService.add({
                            seconds: 5,
                            message: "Não foi possível pagar a conta."
                        });
                    }, null, item);

                }, function () {
                    notifyService.add({
                        seconds: 5,
                        message: "Não foi possível salvar o comprovante."
                    });
                }, null, item.comprovanteID);



            };

            $scope.cancelarPagamentoUnico = function (item) {
                if (item.c.notificationID != null) {
                    notifyService.remove(item.c.notificationID);
                    item.c.notificationID = null;
                }

                $scope.mais(item);
            };

            $scope.carregarPagamentos = function () {
//                if (data == null) {
//                    notifyService.add({
//                        seconds: 5,
//                        message: "O mês e ano não podem ser nulos."
//                    });
//                    return;
//                }

                var date = new Date($scope.ano, $scope.mes, 01);

                $pgService.listar(function (result) {
                    $scope.semanas = result;
                    $scope.itens = [];
                    $scope.totalPago = 0;
                    $scope.quantidadePago = 0;
                    $scope.totalNaoPago = 0;
                    $scope.quantidadeNaoPago = 0;
                    
                    $(result).each(function (i, w) {
                        $(w.dias).each(function (j, d) {
                            $(d.pagamentos).each(function (k, p) {
                                p.c = {};

                                p.c.diasVencidos = 
                                        (((new Date(p.vencimento.substring(0, 10)) -
                                        new Date()) / (1000 * 60 * 60 * 24)) * -1)
                                        .toFixed(0) - 1;

                                if (p.debitoIlimitado)
                                    p.c.numeroVezes = p.numero;
                                else
                                    p.c.numeroVezes = p.numero + "/" + p.quantidade;

                                if (p.boletoID)
                                    p.c.boletoUrl = fileUpload.getUrlDownload(p.boletoID);

                                if (p.comprovanteID)
                                    p.c.comprovanteUrl = fileUpload.getUrlDownload(p.comprovanteID);

                                    if(p.pago){
                                        $scope.quantidadePago++;
                                        $scope.totalPago += p.valor;
                                    }else{
                                        $scope.quantidadeNaoPago++;
                                        $scope.totalNaoPago += p.valor;
                                    }
                                    
                                $scope.itens.push(p);
                            });
                        });
                    });
                    if ($scope.itens.length)
                        $scope.showVazio = false;
                    else
                        $scope.showVazio = true;
                },
                        function (mensagem) {
                            if (mensagem)
                                $scope.mensagem = mensagem;
                            else
                                $scope.mensagem = "Nao foi possivel comunicar com o servidor.";
                        },
                        null, date);
            };

            $scope.carregarPagamentos();

            $scope.itemEditar = {};

            $scope.editar = function (item) {
                $scope.itemEditar = item;
                $("#editar").modal("show");
            };

            $scope.uploadBoleto = function () {
                var id = notifyService.add({
                    fixed: true,
                    message: "Salvando boleto..."
                });

                fileUpload.upload(function () {
                    notifyService.remove(id);
                    notifyService.add({
                        seconds: 5,
                        message: "Boleto salvo."
                    });
                }, function () {
                    notifyService.remove(id);
                    notifyService.add({
                        seconds: 5,
                        message: "Boleto não salvo."
                    });
                }, null, $scope.unicoParcela.boleto);
            };

            $scope.downloadFile = function (id) {
                fileUpload.download(function () {
                }, function () {
                }, null, id);
            };

            $scope.salvarEdicao = function (item) {
                $("#editar").modal("hide");
                $http.post("/Pagamento", item).success(function () {

                }).error(function () {
                    $scope.mensagem = "Houve um erro ao editar o pagamento " + item.descricao + ".";
                });
            };

            $scope.excluir = function (item) {
                if (item.pago)
                    notifyService.add({
                        seconds: 5,
                        message: "Não é possível remover contas pagas"
                    });

                var id = notifyService.add({
                    fixed: true,
                    message: "Tem certeza que deseja excluir " + item.nome + "?",
                    buttons: [
                        {
                            text: "Excluir",
                            parameter: item,
                            f: $scope.confirmarExclusao
                        },
                        {
                            text: "Não",
                            parameter: item,
                            f: $scope.cancelarExclusao
                        }
                    ]
                });
            };

            $scope.confirmarExclusao = function (item) {
                var id = notifyService.add({
                    fixed: true,
                    message: "Excluindo " + item.nome
                });

                $pgService.excluir(function () {

                    $($scope.itens).each(function (i, it) {
                        if (it.ID == item.ID) {
                            $scope.itens.splice(i, 1);
                            return false;
                        }
                    });

                    notifyService.remove(id);

                }, function () {
                    notifyService.add({
                        seconds: 5,
                        message: "Não foi possível excluir " + item.nome
                    });
                }, null, item.ID);
            };

            $scope.cancelarExclusao = function () {

            };

            $scope.pegarTotal = function () {
                var total = 0;
                $($scope.itens).each(function (i, item) {
                    total += item.valor;
                });
                return total;
            };

            $scope.pegarPorcentagem = function (item) {
                return (item.valor / $scope.pegarTotal()) * 100;
            };

            $scope.mais = function (item) {
                if (item.c.expandir) {
                    item.c.expandir = false;
                }
                else
                    item.c.expandir = true;

                $scope.carregarContaBancaria();
            };

            $scope.carregarContaBancaria = function () {
                conta_bancariaService.listar(function (resultado) {
                    $scope.contasBancarias = resultado;
                }, function () {
                    notifyService.add({
                        message: "Não foi possível carregar as contas bancárias para realizar o pagamento",
                        seconds: 5
                    });
                }, null);
            };

            $scope.clicarPagamentoCalendario = function (item) {
                if (!item.c.mostrarBotoes)
                    $scope.mostrarAcaoBotao(item);
                item.c.expandir = true;
                window.scrollBy(0, $("#" + item.ID).offset().top - 100);
            };

            $scope.carregarFornecedores = function () {
                fornecedorService.listar(function (resultado) {
                    $scope.fornecedores = resultado;
                }, function () {
                }, null);
            };

            $scope.salvarDebito = function (debito) {
                $pgService.salvarDebito(function () {
                    $scope.carregarPagamentos($scope.dataLoad);
                }, function () {
                    notifyService.add({
                        seconds: 5,
                        message: "Não foi possível salvar o débito automático."
                    });
                }, null, debito);
            };

            $scope.fab = {
                principalClick: function () {
                    $("#unico").modal("show");
                    $scope.carregarFornecedores();
                },
                principalIcon: "md md-add",
                secondIcon: "md md-crop-portrait",
                principalAlt: "Único",
                miniButtons: [
                    {
                        icon: "md md-autorenew",
                        color: "green",
                        alt: "Automáticos",
                        click: function () {
                            $scope.debito = {
                                nome: "",
                                descricao: "",
                                fornecedorID: "",
                                contaBancariaID: "",
                                dataInicio: "",
                                dataFim: "",
                                dia: "",
                                valor: 0
                            };

                            $("#debito").modal("show");
                            $scope.carregarFornecedores();
                            $scope.carregarContaBancaria();
                        },
                        id: 2
                    }, {
                        icon: "md md-content-copy",
                        color: "blue",
                        alt: "Diversos",
                        click: function () {
                            $("#editar").modal().modal("show");
                            $scope.carregarFornecedores();
                        },
                        id: 1
                    }]
            };

            $scope.mostrarEstatisticasMensais = function () {
                if ($scope.mostrarEstatisticas)
                    $scope.mostrarEstatisticas = false;
                else
                    $scope.mostrarEstatisticas = true;
            };

            $scope.getTotalDay = function(dia){
                var total = 0;
                $(dia.pagamentos).each(function(i, p){
                    total += p.valor;
                });
                
                return total;
            };
            
            $scope.hoje = function(date){
                var d = new Date();
                date = new Date(date);
                return date.getDate() == d.getDate() && date.getMonth() == d.getMonth() &&
                        date.getFullYear() == d.getFullYear();
            };

            $scope.carregarFornecedores();
        }]);
})();