(function () {
    var app = angular.module("fileUploadModule", []);

    app.service("fileUpload", ['$http', function ($http) {
            this.upload = function (sucesso, erro, sempre, file) {
                var fd = new FormData();
                fd.append('file', file);
                $http.post("/AntevereTransportes/FileUpload", fd, {
                    transformRequest: angular.identity,
                    headers: {'Content-Type': 'multipart/form-data', 'Ignore': true}
                })
                        .success(function (resultado) {
                            if (resultado.sucesso) {
                                sucesso(resultado.resultado);
                            } else {
                                erro(resultado.mensagem);
                            }
                        })
                        .error(function () {
                            erro("Não foi possível salvar o arquivo.");
                        });
                if (sempre)
                    sempre();
            };

            this.getUrlDownload = function (fileId) {
                return "/AntevereTransportes/FileDownload?id=" + fileId;                
            };
        }]);

    app.directive('fileModel', ['$parse', function ($parse) {
            return {
                restrict: 'A',
                link: function (scope, element, attrs) {
                    var model = $parse(attrs.fileModel);
                    var modelSetter = model.assign;

                    element.bind('change', function () {
                        scope.$apply(function () {
                            modelSetter(scope, element[0].files[0]);
                        });
                    });
                }
            };
        }]);
})();