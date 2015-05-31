(function(){
    var app = angular.module("vazioElement", []);
    
    app.directive("vazio", function(){
        return {
            restrict: 'E',
                templateUrl: "componentes/vazio/vazio.html",
                scope: {
                    dados: "=dados"
                },
                controller: function ($scope) {                    
                    
                }
        };
    });
})();