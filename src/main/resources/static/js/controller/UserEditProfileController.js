'use strict';
angular.module('shopApp').controller('UserEditProfileController', [
    '$scope', '$log', '$state', '$stateParams', 'UserSession', 'UserService', 'Upload', '$timeout',
    function ($scope, $log, $state, $stateParams, UserSession, UserService, Upload, $timeout) {
        var ava = {};
        $scope.avaNew = true;
        $scope.avaUrl = UserSession.ava;
        $scope.user = UserSession.getUser();
        $scope.newUser = {};
        $scope.disableSave = true;
        $scope.$watch('picFile', function (file) {
            if (file) {
                $scope.avaNew = false;
                ava = file;
                Upload.dataUrl(file, true)
                    .then(function (url) {
                        $scope.avaUrl = url;
                    });
            }
        });

        $scope.saveNewUser = function () {
            $scope.newUser.id = $scope.user.id;
            var newUser = {};
            newUser.id = $scope.user.id;
            if ($scope.newUser.firstName != $scope.user.firstName)
                newUser.firstName = $scope.newUser.firstName;
            if ($scope.newUser.lastName != $scope.user.lastName)
                newUser.lastName = $scope.newUser.lastName;
            if ($scope.newUser.email != $scope.user.email)
                newUser.email = $scope.newUser.email;
            if ($scope.newUser.phoneNumber != $scope.user.phoneNumber)
                newUser.phoneNumber = $scope.newUser.phoneNumber;
            if ($scope.newUser.sex != $scope.user.sex)
                newUser.sex = $scope.newUser.sex;
            $log.log("save new User", newUser);
            UserService.update(newUser).then(function (response) {
                $log.log("success update", response);
                UserSession.createUser(response.data);
            }, function (errResponse) {
                $log.log("err update", errResponse)
            });
            if (ava) {
                var formData = new FormData();
                formData.append('image', ava);
                UserService.saveAva(formData)
                    .then(function () {
                        var tmp = UserSession.getAva();
                        UserSession.setAva("");
                        $timeout(function () {
                            UserSession.setAva(tmp);
                        }, 10);
                    });
            }

        };
        $scope.passwordLoading = false;
        $scope.password = {};
        $scope.password = {};
        $scope.showchangePasswordMassage = false;
        $scope.changePassword = function () {
            $log.log($scope.password);
            $scope.passwordLoading = !$scope.passwordLoading;
            UserService.resetPassword($scope.password)
                .then(function (response) {
                    $log.log("success", response);
                    $scope.passwordLoading = !$scope.passwordLoading;
                    $scope.changePasswordMassage = "success"
                }, function (errresponse) {
                    $log.log("error", errresponse);
                    $scope.passwordLoading = !$scope.passwordLoading;
                    $scope.changePasswordMassage = errresponse.data.massage;
                });
            $scope.showchangePasswordMassage = !$scope.showchangePasswordMassage;
            $scope.password.old = "";
            $scope.password.new = "";
            $timeout(function () {
                $scope.showchangePasswordMassage = !$scope.showchangePasswordMassage;
            }, 6000)
        }
    }]);
