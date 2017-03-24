'use strict';
angular.module('shopApp')
    .factory('DescriptionFactory', ['$http', function ($http) {
        var urlBase = 'api/value';
        var DescriptionFactory = {};
        DescriptionFactory.getDescription = function () {
            return $http.get(urlBase);
        };
        DescriptionFactory.getDescription = function (id) {
            return $http.get(urlBase + '/' + id);
        };
        DescriptionFactory.saveDescription = function (product) {
            return $http.post(urlBase, product);
        };
        DescriptionFactory.updateDescription = function (product) {
            return $http.put(urlBase + '/' + product.id, product);
        };
        DescriptionFactory.deleteDescription = function (id) {
            return $http.put(urlBase + '/' + id);
        };
        return DescriptionFactory;
    }]);
