angular.module('shopApp').constant('AUTH_EVENTS', {
    loginSuccess: 'event: auth-login-success',
    loginFailed: 'event: auth-login-failed',
    logoutSuccess: 'event: auth-logout-success',
    sessionTimeout: 'event: auth-session-timeout',
    notAuthenticated: 'event: auth-not-authenticated',
    notAuthorized: 'event: auth-not-authorized'
});
angular.module('shopApp').constant('USER_ROLES', {
    all: 'ROLE_ANONYMOUS',
    admin: 'ROLE_ADMIN',
    user: 'ROLE_USER'
});
angular.module('shopApp').constant('CART_EVENTS', {
    change: 'event: cart-change',
    delete: 'event: cart-delete',
    add: 'event: cart-add',
    update: 'event: cart-update',
    synchronize: 'event: cart-synchronize-localStorage-server'
});