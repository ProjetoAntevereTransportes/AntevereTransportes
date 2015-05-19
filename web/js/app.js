(function () {
    var app = angular.module("app", ["ngRoute", "inicio", "configuracao", "usuario",
        "pagamento", "ngAnimate", "pagamentoService",
        "login", "authService", "fabElement", "notifyElement", "fornecedor", "fileUploadModule"]);

    app.config(["$httpProvider", function ($httpProvider) {
            $httpProvider.interceptors.push('authInterceptorService');
        }]);

    app.controller('appCtrl', function ($scope, $location) {
        $scope.title = "Pagamentos > Pendentes";
        $scope.selectedItem = "/";

        $scope.mudarTitulo = function (path) {
            alert(path);
            switch (path) {
                case "/":
                {
                    $scope.title = "Início";
                    break;
                }
            }
        };

        $scope.click = function () {
            $scope.selectedItem = $location.path();
            $scope.mudarTitulo($scope.selectedItem);
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
        });
        $routeProvider.otherwise({
            redirectTo: '/inicio'
        });

    });


})();