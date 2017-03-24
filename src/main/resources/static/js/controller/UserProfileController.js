'use strict';
angular.module('shopApp').controller('UserProfileController', [
    '$scope', 'UserSession',
    function ($scope, UserSession) {
        $scope.user = UserSession.user;
    }])
;
