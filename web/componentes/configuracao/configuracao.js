(function(){
    var app = angular.module("configuracao", []);
    
    app.controller("configuracaoCtrl", function($scope){
        $scope.usuario = {
            nome: "Lucas",
            senha: 123456
        };
    });
})();