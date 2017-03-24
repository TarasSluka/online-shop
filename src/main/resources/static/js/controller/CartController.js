'use strict';
angular.module('shopApp').controller('CartController', [
    '$rootScope', '$scope', '$log', 'CartSessionService', 'ProductService', '$q', 'CART_EVENTS', 'UserSession', 'CartLocalStorageService', 'CartService',
    function ($rootScope, $scope, $log, CartSessionService, ProductService, $q, CART_EVENTS, UserSession, CartLocalStorageService, CartService) {

        callServer();
        $scope.changeQuantity = function (productId, quantity) {
            $log.log(productId, quantity);

            if (quantity != undefined) {
                if (UserSession.isAuthorized())
                    CartService.update(productId, quantity)
                        .then(function (response) {

                        }, function (response) {
                        });
                else {
                    CartLocalStorageService.update(productId, quantity);
                }
                CartSessionService.update(productId, quantity);
                $rootScope.$emit(CART_EVENTS.change);
            }
        };

        function callServer() {
            $scope.productInCart = CartSessionService.getCart();
        }

        $rootScope.$on(CART_EVENTS.change, function () {
            callServer();
        });

        $scope.delete = function deleteFromCart(productId) {
            if (UserSession.isAuthorized())
                CartService.delete(productId)
                    .then(function (response) {
                    }, function (response) {
                    });
            else {
                CartLocalStorageService.delete(productId);
            }
            CartSessionService.delete(productId);
            $rootScope.$emit(CART_EVENTS.change);
        };
    }]);