'use strict';
angular.module('shopApp')
    .factory('UserService', ['$http', '$log', function ($http, $log) {
        var urlBase = serverPath + 'api/user';
        var UserService = {};

        UserService.getUsers = function (request) {
            return $http.get(urlBase + '?' + $.param(request));
        };
        UserService.getUser = function (id) {
            return $http.get(urlBase + '/' + id);
        };
        UserService.getCurrentUser = function () {
            return $http.get(urlBase + '/currentUser');
        };
        UserService.saveUser = function (user) {
            return $http.post(urlBase, user);
        };
        UserService.updateUser = function (user) {
            return $http.put(urlBase + '/' + user.id, user);
        };
        UserService.update = function (user) {
            return $http.put(urlBase, user);
        };
        UserService.enabledUser = function (id) {
            return $http.put(urlBase + '/' + id + '/enabled');
        };
        UserService.resetPassword = function (data) {
            return $http.post(urlBase + '/resetPassword', data);
        };

        UserService.deleteUser = function (id) {
            return $http.delete(urlBase + '/' + id);
        };
        UserService.getAva = function () {
            return $http.get(urlBase + "/ava", {
                transformRequest: angular.identity,
                headers: {
                    'Content-Type': undefined
                }
            });
        };
        UserService.saveAva = function (ava) {
            return $http.post(urlBase + "/ava", ava, {
                transformRequest: angular.identity,
                headers: {
                    'Content-Type': undefined
                }
            })
        };
        return UserService;
    }]);
