'use strict';
angular.module('shopApp').controller('LoginController', [
    '$rootScope', '$scope', '$location', '$log', '$http', '$state', 'AuthenticationService', '$localStorage', 'AUTH_EVENTS', '$timeout',
    function ($rootScope, $scope, $location, $log, $http, $state, AuthenticationService, $localStorage, AUTH_EVENTS, $timeout) {
        // $scope.username = 'user2';
        // $scope.password = '11111111';

        $scope.sendLogin = function () {
            var auth = {
                username: $scope.username,
                password: $scope.password
            };
            AuthenticationService.login(auth)
                .then(function (response) {
                    $log.log("login success", response);
                    $rootScope.$broadcast(AUTH_EVENTS.loginSuccess);
                }, function (response) {
                    $rootScope.$broadcast(AUTH_EVENTS.loginFailed);
                    $scope.showError = true;
                    $timeout(function () {
                        $scope.showError = false;
                    }, 2500);
                });
            $scope.username = '';
            $scope.password = '';
        }
    }]);
