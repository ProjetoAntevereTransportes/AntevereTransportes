(function () {
    var app = angular.module("login", []);

    app.factory('authService', ["$http", "$q",
        function ($http, $q) {

            var serviceBase = '/AntevereTransportes/';
            var authServiceFactory = {};

            var _authentication = {
                isAuth: false,
                userName: ""
            };

            var _saveRegistration = function (registration) {

                _logOut();

                return $http.post(serviceBase + 'api/account/register', registration).then(function (response) {
                    return response;
                });

            };

            var _login = function (loginData) {
                var deferred = $q.defer();

                $http.post(serviceBase + 'Login', "data=" + JSON.stringify(loginData),
                        {
                            headers: {'Content-Type': 'application/x-www-form-urlencoded'}
                        }).success(function (response) {
                    if (response.sucesso) {

                        localStorage["authorizationData"] = JSON.stringify({userName: loginData.userName,
                            token: response.resultado.Token});

                        _authentication.isAuth = true;
                        _authentication.userName = loginData.userName;
                    }

                    deferred.resolve(response);
                }).error(function (err, status) {
                    _logOut();
                    deferred.reject(err);
                });

                return deferred.promise;
            };

            var _logOut = function () {

                localStorage["authorizationData"] = "";

                _authentication.isAuth = false;
                _authentication.userName = "";

            };

            var _fillAuthData = function () {

                var authData = localStorage["authorizationData"];
                if (authData)
                {
                    _authentication.isAuth = true;
                    _authentication.userName = JSON.parse(authData).userName;
                }

            };

            authServiceFactory.saveRegistration = _saveRegistration;
            authServiceFactory.login = _login;
            authServiceFactory.logOut = _logOut;
            authServiceFactory.fillAuthData = _fillAuthData;
            authServiceFactory.authentication = _authentication;

            return authServiceFactory;
        }]);

    app.controller("loginController", ["$scope", "$location", "authService", "$window",
        function ($scope, $location, authService, $window) {
            $scope.loginData = {
                userName: "",
                password: ""
            };

            $scope.mensagemErro = "";

            $scope.login = function () {
                authService.login($scope.loginData).then(function (response) {
                    if (response.Sucesso)
                        $window.history.back();
                    else
                        $scope.mensagemErro = response.Mensagem;
                },
                        function (err) {
                            $scope.mensagemErro = err.error_description;
                        });
            };
        }]);

    app.controller('signupController', ["$scope", "$location", "$timeout", "authService",
        function ($scope, $location, $timeout, authService) {
            $scope.savedSuccessfully = false;
            $scope.message = "";

            $scope.registration = {
                userName: "",
                password: "",
                confirmPassword: ""
            };

            $scope.signUp = function () {

                authService.saveRegistration($scope.registration).then(function (response) {

                    $scope.savedSuccessfully = true;
                    $scope.message = "User has been registered successfully, you will be redicted to login page in 2 seconds.";
                    startTimer();

                },
                        function (response) {
                            var errors = [];
                            for (var key in response.data.modelState) {
                                for (var i = 0; i < response.data.modelState[key].length; i++) {
                                    errors.push(response.data.modelState[key][i]);
                                }
                            }
                            $scope.message = "Failed to register user due to:" + errors.join(' ');
                        });
            };

            var startTimer = function () {
                var timer = $timeout(function () {
                    $timeout.cancel(timer);
                    $location.path('/login');
                }, 2000);
            };

        }]);
})();