(function () {
    var app = angular.module("pagamento", []);

    app.controller("PagamentoController", ["$scope", "$http", "$pgService", "notifyService",
        "fornecedorService", "fileUpload",
        function ($scope, $http, $pgService, notifyService, fornecedorService, fileUpload) {
            $scope.itens = [];
            $scope.mensagem = "";
            $scope.mostrarCalendario = false;
            $scope.semanas = [];
            $scope.fornecedores = [];
            $scope.itemNovoMassa = {
                parcelas: [],
                fornecedorID: "",
                inicial: null,
                final: null,
                nome: "",
                valor: 0
            };
            
            $scope.unico = {
                parcelas: [{
                    vencimento: "",
                    descricao: "",
                    valor: 0,
                    boleto: ""
                }],
                fornecedorID: "",                
                nome: ""
            };

            $scope.gerarParcelas = function () {
                if ($scope.itemNovoMassa.inicial && $scope.itemNovoMassa.final) {
                    $scope.itemNovoMassa.parcelas = [];
                    var anoInicial = $scope.itemNovoMassa.inicial.getFullYear();
                    var mesInicial = $scope.itemNovoMassa.inicial.getMonth();
                    var diaInicial = $scope.itemNovoMassa.inicial.getDate();

                    var anoFinal = $scope.itemNovoMassa.final.getFullYear();
                    var mesFinal = $scope.itemNovoMassa.final.getMonth();

                    for (var ano = anoInicial; ano <= anoFinal; ano++) {                        
                        for (var mes = mesInicial; mes <= mesFinal; mes++) {
                            $scope.itemNovoMassa.parcelas.push({
                                descricao: "descriocao!",
                                vencimento: new Date(ano, mes, diaInicial),
                                valor: $scope.itemNovoMassa.valor,
                                comprovante: "OLA MUNDO"
                            });
                        }
                    }
                }
            };

            $scope.salvarMassa = function (contas) {
                $pgService.salvarMassa(function () {
                    $scope.carregarPagamentos();
                    $("#editar").modal("hide");
                    $("#unico").modal("hide");
                }, function () {
                }, null, contas);
            };

            $scope.mouseOverItem = function (item) {

            };

            $scope.mouseLeaveItem = function (item) {

            };

            $scope.mostrarAcaoBotao = function (item) {
                if (item.mostrarBotoes)
                    item.mostrarBotoes = false;
                else
                    item.mostrarBotoes = true;
            };

            $scope.alterarStatusCalendario = function () {
                if ($scope.mostrarCalendario) {
                    $scope.mostrarCalendario = false;
                }
                else {
                    $scope.mostrarCalendario = true;
                }
            };

            $scope.carregarPagamentos = function () {
                $pgService.listar(function (result) {
                    $scope.semanas = result;
                    $scope.itens = [];
                    $(result).each(function (i, w) {
                        $(w.dias).each(function (j, d) {
                            $(d.pagamentos).each(function (k, p) {
                                $scope.itens.push(p);
                            });
                        });
                    });
                },
                        function (mensagem) {
                            if (mensagem)
                                $scope.mensagem = mensagem;
                            else
                                $scope.mensagem = "Nao foi possivel comunicar com o servidor.";
                        },
                        null);
            };

            $scope.carregarPagamentos();

            $scope.itemEditar = {};

            $scope.editar = function (item) {
                $scope.itemEditar = item;
                $("#editar").modal("show");
            };

            $scope.salvarEdicao = function (item) {
                $("#editar").modal("hide");
                $http.post("/Pagamento", item).success(function () {

                }).error(function () {
                    $scope.mensagem = "Houve um erro ao editar o pagamento " + item.descricao + ".";
                });
            };

            $scope.excluir = function (item) {
                var index;
                $($scope.itens).each(function (i, it) {
                    if (it.ID == item.ID) {
                        $scope.itens.splice(i, 1);
                        return false;
                    }
                });
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
                if (item.expandir) {
                    item.expandir = false;
                }
                else
                    item.expandir = true;
            };

            $scope.pagar = function (item) {
                if (item.notificationID == null)
                    item.notificationID = notifyService.add({
                        fixed: true,
                        message: "Deseja realizar o pagamento de " + item.descricao + "?",
                        buttons: [{
                                text: "Sim",
                                parameter: item,
                                f: $scope.realizarPagamento
                            },
                            {
                                text: "Não",
                                parameter: item,
                                f: $scope.cancelarPagamento
                            }
                        ]
                    });
            };

            $scope.realizarPagamento = function (item) {
                item.expandir = false;
                item.notificationID = null;

                notifyService.add({
                    seconds: 3,
                    message: item.descricao + " pago!"
                });
            };

            $scope.cancelarPagamento = function (item) {
                if (item.notificationID != null) {
                    notifyService.remove(item.notificationID);
                    item.notificationID = null;
                }

                $scope.mais(item);
            };

            $scope.clicarPagamentoCalendario = function (item) {
                item.expandir = true;
                window.scrollBy(0, $("#" + item.ID).offset().top - 100);
            };

            $scope.carregarFornecedores = function () {
                fornecedorService.listar(function (resultado) {
                    $scope.fornecedores = resultado;
                }, function () {
                }, null);
            };

            $scope.salvarDebito = function(debito){
                $pgService.salvarDebito(function(){
                    
                },function(){}, null, debito);
            };

            $scope.fab = {
                principalClick: function () {
                    $("#unico").modal("show");
                    $scope.carregarFornecedores();
                },
                principalIcon: "glyphicon glyphicon-plus",
                secondIcon: "glyphicon glyphicon-file",
                principalAlt: "Único",
                miniButtons: [
                    {
                        icon: "glyphicon glyphicon-refresh",
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
                        },
                        id: 2
                    }, {
                        icon: "glyphicon glyphicon-duplicate",
                        color: "blue",
                        alt: "Diversos",
                        click: function () {
                            $("#editar").modal().modal("show");
                            $scope.carregarFornecedores();
                        },
                        id: 1
                    }]
            };
        }]);
})();