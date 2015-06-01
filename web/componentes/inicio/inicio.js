(function () {
    var app = angular.module('inicio', []);

    app.controller('inicioCtrl', ["$scope", function ($scope) {

            $scope.chart1 = function () {

                new Morris.Line({
                    element: 'chart1',
                    data: [
                        {y: '2014-07', a: 1000, b: 9056.4, c: 3654},
                        {y: '2014-08', a: 75.45, b: 6534.2, c: 3454},
                        {y: '2014-09', a: 5034.4, b: 4021.3, c: 3564},
                        {y: '2014-10', a: 753.2, b: 6521.3, c: 8734},
                        {y: '2014-11', a: 50.45, b: 4022, c: 3454},
                        {y: '2014-12', a: 7554.3, b: 6523, c: 5634},
                        {y: '2015-01', a: 10000, b: 9012, c: 3564},
                        {y: '2015-02', a: 10023.89, b: 9320, c: 3412},
                        {y: '2015-03', a: 10012.2, b: 2390, c: 344},
                        {y: '2015-04', a: 1003.2, b: 9230, c: 3423},
                        {y: '2015-05', a: 1003.2, b: 9210, c: 6734},
                        {y: '2015-06', a: 1005.6, b: 930, c: 3674}
                    ],
                    xkey: 'y',
                    ykeys: ['a', 'b', 'c'],
                    labels: ['Únicos', 'Carnês', 'Débitos Auto.'],
                    yLabelsFormat: function (y) {
                        return y.toString();
                    },
                    xLabelsFormat: function (x) {
                        return x.toString();
                    }
                });
            };


            $scope.chart1();
        }]);
})();
