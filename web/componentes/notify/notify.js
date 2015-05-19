(function () {
    var app = angular.module("notifyElement", []);

    app.directive("notify", ["notifyService", "$timeout",
        function (notifyService, $timeout) {
            return {
                restrict: 'E',
                templateUrl: "componentes/notify/notify.html",
                link: function (scope, element, attrs) {
                    scope.messages = [];
                    scope.showNotifies = scope.messages.length > 0;

                    scope.$watch(function () {
                        return notifyService.get();
                    }, function (newVal, oldVal) {
                        scope.messages = notifyService.get();
                        scope.showNotifies = scope.messages.length > 0;
                    });

                    scope.func = function (msg, btn) {
                        if (btn.f != null)
                            btn.f(btn.parameter);
                        notifyService.remove(msg.ID);
                    };

                    scope.mouseOver = function (msg) {
                        if (msg.timeout)
                            $timeout.cancel(msg.timeout);
                    };

                    scope.mouseLeave = function (msg) {
                        if (msg.timeout) {
                            notifyService.remove(msg.ID);
                            notifyService.add(msg);
                        }
                    };
                }
            };
        }]);

    app.factory("notifyService", function ($timeout) {
        var msgs = [];

        function add(msg) {
            msg.ID = guid();

            if (!msg.fixed) {
                var s = 5000;

                if (msg.seconds)
                    s = msg.seconds * 1000;

                msg.timeout = $timeout(function () {
                    remove(msg.ID);
                }, s);
            }

            msgs.unshift(msg);

            return msg.ID;
        }

        function get() {
            return msgs;
        }

        function remove(messageId) {
            $(msgs).each(function (i, item) {
                if (item.ID == messageId) {
                    msgs.splice(i, 1);
                    return;
                }
            });
        }

        function guid() {
            function s4() {
                return Math.floor((1 + Math.random()) * 0x10000)
                        .toString(16)
                        .substring(1);
            }
            return s4() + s4() + '-' + s4() + '-' + s4() + '-' +
                    s4() + '-' + s4() + s4() + s4();
        }

        return {
            msgs: msgs,
            add: add,
            get: get,
            remove: remove
        };
    });
})();