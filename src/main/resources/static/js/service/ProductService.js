'use strict';
angular.module('shopApp')
    .factory('ProductService', ['$state', '$http', '$q',
        function ($state, $http, $q) {
            var urlBase = 'api/product';
            var request = {};
            var ProductService = {};
            ProductService.request = 1;

            ProductService.getRequest = function () {
                return ProductService.request;
            };
            ProductService.setRequest = function (r) {
                ProductService.request++;
                return ProductService.request;
            };

            ProductService.getMaxAndMinPrice = function (request) {
                return $http.get(urlBase + '/min&MaxPrice' + '?' + $.param(request));
            };
            ProductService.getProducts = function (request) {
                return $http.get(urlBase + '?' + $.param(request));
            };

            ProductService.getProduct = function (id) {
                return $http.get(urlBase + '/' + id);
            };
            ProductService.saveProduct = function (product) {
                return $http.post(urlBase, product);
            };
            ProductService.updateProduct = function (product) {
                return $http.put(urlBase + '/' + product.id, product);
            };
            ProductService.deleteProduct = function (id) {
                return $http.delete(urlBase + '/' + id);
            };
            return ProductService;
        }]);
