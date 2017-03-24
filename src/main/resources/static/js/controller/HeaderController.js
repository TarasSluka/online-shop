'use strict';
angular.module('shopApp').controller('HeaderController', [
    '$state',
    '$scope', '$http', '$log', 'CategoryService', 'ProductService', 'AuthenticationService', 'UserSession', '$rootScope', 'AUTH_EVENTS', 'UserService', 'CategorySession', '$timeout',
    function ($state, $scope, $http, $log, CategoryService, ProductService, AuthenticationService, UserSession, $rootScope, AUTH_EVENTS, UserService, CategorySession, $timeout) {
        $scope.firstName = UserSession.getFirstName;
        $scope.lastName = UserSession.getLastName;
        initController();
        var selectCat = {};
        $scope.avaUrl = UserSession.getAva;
        $scope.obj = $scope.selectCat;
        function initController() {
            $scope.selectCat = null;
            CategoryService.getParentCategory()
                .then(function (response) {
                    $scope.categories = response.data;
                });
        }

        $scope.selectCategory = function (category) {
            if (category === undefined)
                $scope.selectCat = {};
            else
                $scope.selectCat = category;
        };

        $scope.search = function () {
            // $log.log('search');
            var request = {};
            if ($scope.searchTxt != undefined) {
                if ($scope.selectCat !== undefined && !angular.equals($scope.selectCat, {}) && $scope.selectCat !== null)
                    request.categoryId = $scope.selectCat.id;
                request.search = $scope.searchTxt;
                $log.log(request);

                $state.go('shop.root.home.search', {
                    search: request.search,
                    categoryId: request.categoryId
                });
            }
        };
        $scope.logout = function () {
            AuthenticationService.logout()
                .then(function (response) {
                    $log.log("success logout", response);
                    $rootScope.$emit(AUTH_EVENTS.logoutSuccess);
                }, function (response) {
                    $log.log("error logout", response);
                });
        };
    }
]);
