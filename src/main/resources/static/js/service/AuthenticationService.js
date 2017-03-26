'use strict';
angular.module('shopApp').factory('AuthenticationService', [
    '$q',
    'CartService', '$state', '$rootScope', '$log', '$http', '$localStorage', 'UserService', 'UserSession', 'USER_ROLES', 'CART_EVENTS', 'CartSessionService', 'CartLocalStorageService', '$timeout',
    function ($q, CartService, $state, $rootScope, $log, $http, $localStorage, UserService, UserSession, USER_ROLES, CART_EVENTS, CartSessionService, CartLocalStorageService, $timeout) {
        var AuthenticationService = {};
        AuthenticationService.login = function (auth) {
            return $http.post(serverPath + 'login', $.param(auth), {
                headers: {'Content-Type': 'application/x-www-form-urlencoded'}
            });
        };
        AuthenticationService.logout = function () {
            return $http.post('/logout')
                .then(function (response) {
                    $log.log("logout success", response);
                    CartSessionService.dropCart();
                    $rootScope.$emit(CART_EVENTS.change);
                }, function (response) {
                    $log.log("logout error", response);
                });
        };
        AuthenticationService.getCurrentUser = function () {
            var def = $q.defer();
            UserService.getCurrentUser()
                .then(function (response) {
                    $rootScope.role = response.data.role;
                    UserSession.setAuthorized(true);
                    UserSession.createUser(response.data);
                    def.resolve();
                    if (response.data.role === USER_ROLES.user) {
                        var defer = $q.defer();
                        CartService.synchronizeCartWithServer(CartSessionService.synchronizeCartWithServer())
                            .then(function (response) {
                                defer.resolve(response.data);
                                CartLocalStorageService.clear();
                                CartSessionService.dropCart();
                            }, function (response) {
                                $log.log("error synchronizeCartWithServer", response);
                            });
                        defer.promise
                            .then(function (response) {
                                angular.forEach(response, function (value, key) {
                                    CartSessionService.save(value.productDto, value.quantity);
                                });
                                $rootScope.$emit(CART_EVENTS.change);
                            })
                    }

                }, function (response) {
                    def.resolve();
                });
            return def.promise;
        };
        return AuthenticationService;
    }
]);
angular.module('shopApp').directive('visibleToRoles', [
    '$rootScope', '$log',
    function ($rootScope, $log, AUTH_EVENTS, UserSession) {
        return {
            link: function (scope, element, attrs) {

                $rootScope.$watch(function () {
                        return $rootScope.role;
                    }, function (newValue, oldValue) {
                        roles = attrs.visibleToRoles.split(',');
                        if (roles.length > 0)
                            determineVisibility(true);
                    }
                );
                var makeVisible = function () {
                    element.removeClass('hidden');
                };
                var makeHidden = function () {
                    element.addClass('hidden');
                };
                var hasSecurityRoles = function (roles) {
                    var hasRole = false;
                    angular.forEach(roles, function (value) {
                        if ($rootScope.role === value)
                            hasRole = true;
                    });
                    return hasRole;
                };
                var determineVisibility = function (resetFirst) {
                        if (resetFirst) {
                            makeVisible();
                        }
                        if (hasSecurityRoles(roles)) {
                            makeVisible();
                        } else {
                            makeHidden();
                        }
                    },
                    roles = attrs.visibleToRoles.split(',');

                if (roles.length > 0) {
                    determineVisibility(true);
                }
            }
        };
    }
]);
