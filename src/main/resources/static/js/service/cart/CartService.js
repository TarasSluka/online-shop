'use strict';
angular.module('shopApp').factory('CartService', [
    '$http',
    function ($http) {
        var urlBase = serverPath + 'api/cart';
        var CartService = {};

        CartService.save = function (productId, quantity) {
            var item = {
                productID: productId,
                quantity: quantity || 1
            };
            $http.post(urlBase, item)
        };
        CartService.delete = function () {
            return $http.delete(urlBase);
        };

        CartService.delete = function (productId) {
            return $http.delete(urlBase + '/' + productId);
        };
        CartService.update = function (productId, quantity) {
            var item = {
                productId: productId,
                quantity: quantity || 1
            };
            return $http.put(urlBase, item);
        };
        CartService.getCart = function () {
            return $http.get(urlBase);
        };
        CartService.getCart = function () {
            return $http.get(urlBase);
        };
        CartService.synchronizeCartWithServer = function (cart) {
            return $http.put(urlBase + "/synchronizeCartWithServer", cart);
        };


        return CartService;
    }
])
;