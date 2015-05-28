(function () {
    var app = angular.module("app", ["ngRoute", "inicio", "configuracao", "usuario",
        "pagamento", "ngAnimate", "pagamentoService",
        "login", "authService", "fabElement", "notifyElement",
        "fornecedor", "fileUploadModule", "cargo", 
        "funcionario", "cliente", "ui.utils", "pesquisaElement","Banco","Conta_Bancaria","Caminhao"]);

    app.config(["$httpProvider", function ($httpProvider) {
            $httpProvider.interceptors.push('authInterceptorService');
        }]);

    app.controller('appCtrl', function ($scope, $location) {
        $scope.$on('$routeChangeStart', function (next, current) {
            $scope.mudarTitulo($location.path());
        });

        $scope.mudarTitulo = function (path) {
            $scope.defaultColor = "rgb(69, 90, 100)";
            $scope.defaultSearchColor = "rgb(114, 138, 150)";

            switch (path) {
                case "/inicio":
                {
                    $scope.title = "Início";
                    $scope.barColor = $scope.defaultColor;
                    $scope.searchColor = $scope.defaultSearchColor;
                    break;
                }
                case "/configuracao":
                {
                    $scope.title = "Configurações";
                    $scope.barColor = $scope.defaultColor;
                    $scope.searchColor = $scope.defaultSearchColor;
                    break;
                }
                case "/usuario":
                {
                    $scope.title = "Usuários";
                    $scope.barColor = $scope.defaultColor;
                    $scope.searchColor = $scope.defaultSearchColor;
                    break;
                }
                case "/Pagamentos":
                {
                    $scope.title = "Pagamentos";
                    $scope.barColor = "#F44242";
                    $scope.searchColor = 'rgb(250, 116, 116)';
                    break;
                }
                case "/Login":
                {
                    $scope.title = "Login";
                    $scope.barColor = $scope.defaultColor;
                    $scope.searchColor = $scope.defaultSearchColor;
                    break;
                }
                case "/Fornecedor":
                {
                    $scope.title = "Fornecedores";
                    $scope.barColor = $scope.defaultColor;
                    $scope.searchColor = $scope.defaultSearchColor;
                    break;
                }
                case "/Cargo":
                {
                    $scope.title = "Cargos";
                    $scope.barColor = $scope.defaultColor;
                    $scope.searchColor = $scope.defaultSearchColor;
                    break;
                }
                case "/Funcionario":
                {
                    $scope.title = "Funcionários";
                    $scope.barColor = $scope.defaultColor;
                    $scope.searchColor = $scope.defaultSearchColor;
                    break;
                }
                case "/Clientes":
                {
                    $scope.title = "Clientes";
                    $scope.barColor = $scope.defaultColor;
                    $scope.searchColor = $scope.defaultSearchColor;
                    break;
                }
                case "/Conta":
                {
                    $scope.title = "Conta Bancaria";
                    $scope.barColor = "#898984";
                    $scope.searchColor = "rgb(171, 171, 171)";
                    break;
                }
                case "/Caminhao":
                {
                    $scope.title = "Caminhão";
                    $scope.barColor = "#898984";
                    $scope.searchColor = "rgb(171, 171, 171)";
                    break;
                }
                case "/Banco":
                {
                    $scope.title = "Bancos";
                    $scope.barColor = "#898984";
                    $scope.searchColor = "rgb(171, 171, 171)";
                    break;
                }
            }
        };

        $scope.click = function (url) {
            $scope.selectedItem = $location.path();
            $scope.mudarTitulo(url);
        };
    });

    app.config(function ($routeProvider) {
        $routeProvider.when('/inicio', {
            templateUrl: 'componentes/inicio/inicio.html',
            controller: 'inicioCtrl'
        }).when("/configuracao", {
            templateUrl: "componentes/configuracao/configuracao.html",
            controller: "configuracaoCtrl"
        }).when("/usuario", {
            templateUrl: "componentes/usuario/usuario.html",
            controller: "usuarioController"
        }).when("/Pagamentos", {
            templateUrl: "componentes/pagamento/pagamento.html",
            controller: "PagamentoController"
        }).when("/Login", {
            templateUrl: "componentes/login/login.html",
            controller: "loginController"
        }).when("/Fornecedor", {
            templateUrl: "componentes/fornecedor/fornecedor.html",
            controller: "fornecedorController"
        }).when("/Cargo", {
            templateUrl: "componentes/cargo/cargo.html",
            controller: "cargoController"
        }).when("/Funcionario", {
            templateUrl: "componentes/funcionario/funcionario.html",
            controller: "funcionarioController"
        }).when("/Clientes", {
            templateUrl: "componentes/clientes/cliente.html",
            controller: "clienteController"
        }).when("/Banco", {
            templateUrl: "componentes/banco/banco.html",
            controller: "bancoController"
        }).when("/Caminhao", {
            templateUrl: "componentes/caminhao/caminhao.html",
            controller: "caminhaoController"
        }).when("/Conta", {
            templateUrl: "componentes/conta_bancaria/conta_bancaria.html",
            controller: "conta_bancariaController"
        });
        $routeProvider.otherwise({
            redirectTo: '/inicio'
        });

    });


})();
