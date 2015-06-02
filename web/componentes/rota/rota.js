(function () {
    var app = angular.module("rota", []);

    app.controller("rotaController", ["$scope", function ($scope) {
            $scope.loadMap = function () {
                $scope.directionsDisplay = new google.maps.DirectionsRenderer({draggable: true});
                $scope.directionsService = new google.maps.DirectionsService();
                var map;
                var myOptions = {
                    zoom: 10,
                    mapTypeId: google.maps.MapTypeId.ROADMAP,
                    center: new google.maps.LatLng(35.270, -80.837)
                };
                map = new google.maps.Map(document.getElementById("maps"), myOptions);
                $scope.directionsDisplay.setMap(map);
                $scope.directionsDisplay.setPanel(document.getElementById("directions"));

                if (navigator.geolocation) {
                    navigator.geolocation.getCurrentPosition(function (position) {
                        
                        var pos = new google.maps.LatLng(position.coords.latitude,
                                position.coords.longitude);

                        
                    }, function () {
                        handleNoGeolocation(true);
                    });
                } else {
                    // Browser doesn't support Geolocation
                    handleNoGeolocation(false);
                }

//function success(position) {
//     var lat = position.coords.latitude;
//     var long = position.coords.longitude;
//}
            };

            $scope.carregarDirecao = function (origem, destino) {

                if (origem && destino) {

                    var request = {
                        origin: origem,
                        destination: destino,
                        travelMode: google.maps.TravelMode["DRIVING"]
                    };
                    $scope.directionsService.route(request, function (response, status) {
                        if (status == google.maps.DirectionsStatus.OK) {
                            $scope.directionsDisplay.setDirections(response);
                        }
                    });
                }
            };

            $scope.loadMap();

        }]);
})();