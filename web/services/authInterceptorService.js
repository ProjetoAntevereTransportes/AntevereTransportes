(function () {
    var app = angular.module("authService", []);
    
    app.factory('authInterceptorService', ['$q', '$location', 
        function ($q, $location) {

            var authInterceptorServiceFactory = {};

            var _request = function (config) {

                config.headers = config.headers || {};

                var authData = localStorage["authorizationData"];
                if (authData) {
                    config.headers.Authorization = authData;
                }

                return config;
            };

            var _responseError = function (rejection) {
                if (rejection.status === 401) {
                    localStorage["authorizationData"] = null;
                    $location.path('/Login');
                }
                return $q.reject(rejection);
            };

            authInterceptorServiceFactory.request = _request;
            authInterceptorServiceFactory.responseError = _responseError;

            return authInterceptorServiceFactory;
        }]);
})();