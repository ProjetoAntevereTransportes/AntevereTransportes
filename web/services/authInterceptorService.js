(function () {
    var app = angular.module("authService", []);
    
    app.factory('authInterceptorService', ['$q', '$location', 
        function ($q, $location) {

            var authInterceptorServiceFactory = {};

            var _request = function (config) {

                config.headers = config.headers || {};

                var authData = localStorage["authorizationData"];
                if (authData) {
                    config.headers = {
                        "Authorization": authData,
                        "Content-Type": "application/x-www-form-urlencoded",
                        "Accept": "application/json;charset=utf-32"
                    };
                    /*config.headers.Authorization = authData;
                    config.headers.ContentType = 'application/x-www-form-urlencoded';
                    config.headers.Accept = 'application/json;charset=utf-32';
                    config.headers.AcceptCharset = 'charset=utf-32';*/
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