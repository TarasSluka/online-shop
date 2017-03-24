angular.module('shopApp')
    .factory('UserSession', ['$log', 'UserService', 'CartService', 'USER_ROLES', 'PhotoFactory', '$q', '$timeout',
        function ($log, UserService, CartService, USER_ROLES, PhotoFactory, $q, $timeout) {
            var UserSession = {};
            UserSession.user = {};
            UserSession.ava = {};
            UserSession.role = USER_ROLES.all;
            UserSession.authorized = false;

            UserSession.getRole = function () {
                return UserSession.role;
            };
            UserSession.isAuthorized = function () {
                return UserSession.authorized;
            };
            UserSession.setAuthorized = function (trueOrfalse) {
                UserSession.authorized = trueOrfalse;
            };
            UserSession.createUser = function (data) {
                UserSession.user.id = data.id;
                UserSession.user.cardList = data.cardList;
                UserSession.user.createdDate = data.createdDate;
                UserSession.user.firstName = data.firstName;
                UserSession.user.lastName = data.lastName;
                UserSession.user.modifiedDate = data.modifiedDate;
                UserSession.user.email = data.email;
                UserSession.user.phoneNumber = data.phoneNumber;
                UserSession.user.role = data.role;
                UserSession.user.userName = data.userName;
                UserSession.user.sex = data.sex;
                getAva();
            };
            UserSession.getUser = function () {
                return UserSession.user;
            };
            UserSession.getFirstName = function () {
                return UserSession.user.firstName;
            };

            UserSession.getLastName = function () {
                return UserSession.user.lastName;
            };

            UserSession.deleteUser = function () {
                UserSession.user = null;
                UserSession.user = {};
                UserSession.authorized = false;
                getAva();
            };

            UserSession.setAva = function (avaUrl) {
                UserSession.ava = avaUrl;
            };
            UserSession.getAva = function () {
                return UserSession.ava;
            };
            UserSession.initialAva = getAva();
            function getAva() {
                var ava;
                var defer = $q.defer();
                UserService.getAva()
                    .then(function (response) {
                        defer.resolve(response);
                    });
                defer.promise
                    .then(function (response) {
                        UserSession.ava = "";
                        $timeout(function () {
                            UserSession.ava = response.data.avaUrl;
                        }, 100);

                    });
            }

            return UserSession;
        }]);