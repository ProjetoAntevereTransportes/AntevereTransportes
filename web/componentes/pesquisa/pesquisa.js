(function () {
    var app = angular.module("pesquisaElement", []);

    app.directive("pesquisa", ["pesquisaService", function (pesquisaService) {
            return {
                restrict: 'E',
                templateUrl: "componentes/pesquisa/pesquisa.html",
                link: function (scope, element, attrs) {
                    scope.value = "Pesquisar";

                    scope.$watch(function () {
                        return pesquisaService.getValue();
                    }, function (newVal, oldVal) {
                        scope.value = pesquisaService.getValue();
                    });

                    scope.change = function () {
                        pesquisaService.setValue(scope.value);
                    };

                    scope.click = function () {
                        if (scope.value == 'Pesquisar')
                            scope.value = '';
                    };

                    scope.blur = function () {
                        if (scope.value == '')
                            scope.value = 'Pesquisar';
                    };
                }
            };
        }]);

    app.service("pesquisaService", function () {
        var value = "";
        var f = function () {
        };

        var getValue = function () {
            return value;
        };

        var setValue = function (v) {
            value = v;
            f(value == "Pesquisar" ? "" : value);
        };

        var setFunction = function (func) {
            f = func;
            value = "Pesquisar";
        };

        return {
            value: value,
            getValue: getValue,
            setValue: setValue,
            setFunction: setFunction
        };
    });
})();