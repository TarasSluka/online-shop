'use strict';
angular.module('shopApp')
    .controller('RegistrationController', ['$rootScope', '$scope', '$log', '$location', 'UserService',
        function ($rootScope, $scope, $log, $location, UserService) {
            $log.log("RegistrationController");
            $scope.register = function () {
                $scope.dataLoading = true;
                var user = {
                    userName: $scope.userName,
                    email: $scope.email,
                    password: $scope.password,
                    lastName: $scope.lastName,
                    firstName: $scope.firstName
                };
                $log.info(user);
                UserService.saveUser(user)
                    .then(function () {
                        $log.log("registration success");
                        $location.replace().path('/login');
                    }, function (response) {
                        $scope.dataLoading = false;
                        $log.log("registration error");
                        angular.forEach(response.data, function (value, key) {
                            console.log(key + ': ' + value);
                            if (key === 'userName')
                                $scope.serverErrorUserName = value;
                            else if (key == 'email')
                                $scope.serverErrorEmail = value;
                        });
                    });
            }

        }]);
