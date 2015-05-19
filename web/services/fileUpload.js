(function () {
    var app = angular.module("fileUploadModule", []);

    app.service("fileUpload", ['$http', function ($http) {
            this.upload = function (sucesso, erro, sempre, file) {
                var fd = new FormData();
                fd.append('file', file);
                $http.post("/AntevereTransportes/FileUpload", fd, {
                    transformRequest: angular.identity,
                    headers: {'Content-Type': undefined}
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

                sempre();
            };
        }]);
})();