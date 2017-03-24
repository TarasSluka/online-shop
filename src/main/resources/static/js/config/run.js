angular.module('shopApp').run(
    function ($rootScope, $state, $stateParams, $log, $location, $http, USER_ROLES, $q, $timeout,
              UserSession, AUTH_EVENTS, CartService, UserService, AuthenticationService, CartSessionService, CategorySession/*, $trace*/) {
        $state.go("shop.root.home");
        $rootScope.$state = $state;
        $rootScope.$stateParams = $stateParams;
        var promise = AuthenticationService.getCurrentUser();
        $rootScope.role = UserSession.role;
        $rootScope.authorized = UserSession.isAuthorized;
        CartSessionService.initSessionCartFromLocalStorage();
        $rootScope.$on('$stateChangeStart',
            function (event, toState, toParams, fromState, fromParams, options) {
                promise.then(function () {
                    if (toState.data.role === $rootScope.role || toState.data.role === USER_ROLES.all) {
                    }
                    else {
                        event.preventDefault();
                        $state.go('shop.login');
                    }

                });

            });

        $rootScope.$on('$stateChangeError', function (event, toState, toParams, fromState, fromParams, error) {
            $log.log("$stateChangeError", {
                event: event,
                toState: toState,
                toParams: toParams,
                fromState: fromState,
                fromParams: fromParams,
                error: error
            });
            $state.go('shop.404');
        });

        $rootScope.$on('event: auth-login-success', function (event, data) {
            $log.log(AUTH_EVENTS.loginSuccess);
            AuthenticationService.getCurrentUser();
            $state.go("shop.root.home");
        });
        $rootScope.$on('event: auth-login-failed', function () {
            $log.log(AUTH_EVENTS.loginSuccess);
        });
        $rootScope.$on('event: auth-logout-success', function (event) {
            $log.log('event: auth-logout-success');
            $rootScope.role = USER_ROLES.all;
            UserSession.deleteUser();
            UserSession.setAuthorized(false);
            $rootScope.authorized = UserSession.isAuthorized;
            event.preventDefault();
            $state.go("shop.root.home");
        });
    })
;
