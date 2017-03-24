'use strict';
angular.module('shopApp')
    .factory('FilterService', [
        '$http', '$log',
        function ($http, $log) {
            var FilterService = {};

            FilterService.minPrice;
            FilterService.maxPrice;

            FilterService.currentMinPrice;
            FilterService.currentMaxPrice;

            FilterService.showPrice = false;
            FilterService.getCurrentMinPrice = function () {
                return FilterService.currentMinPrice;
            };
            FilterService.getCurrentMaxPrice = function () {
                return FilterService.currentMaxPrice;
            };
            FilterService.setCurrentMinPrice = function (currentMinPrice) {
                FilterService.currentMinPrice = currentMinPrice;
            };
            FilterService.setCurrentMaxPrice = function (currentMaxPrice) {
                FilterService.currentMaxPrice = currentMaxPrice;
            };
            FilterService.setCurrentPrice = function (min, max) {
                FilterService.currentMinPrice = min;
                FilterService.currentMaxPrice = max;
            };
            FilterService.getShowPrice = function () {
                return FilterService.showPrice;
            };
            FilterService.setShowPrice = function (showPrice) {
                return FilterService.showPrice = showPrice;
            };
            FilterService.toogleShowPrice = function () {
                return FilterService.showPrice = !FilterService.showPrice;
            };

            /*------------------------minPrice------------------------*/
            FilterService.getPrice = function () {
                var price = {};
                price.min = FilterService.currentMinPrice;
                price.max = FilterService.currentMaxPrice;
                return price;
            };
            FilterService.setNull = function () {
                FilterService.minPrice = 0;
                FilterService.maxPrice = 0;

                FilterService.currentMinPrice = 0;
                FilterService.currentMaxPrice = 0;
            };
            FilterService.setPrice = function (min, max) {
                FilterService.price.min = min;
                FilterService.price.max = max;
                return FilterService.price;
            };
            /*------------------------minPrice------------------------*/
            FilterService.getMinPrice = function () {
                return FilterService.minPrice;
            };
            FilterService.setMinPrice = function (price) {
                FilterService.minPrice = price;
                return FilterService.minPrice;
            };
            /*------------------------maxPrice------------------------*/
            FilterService.getMaxPrice = function () {
                return FilterService.maxPrice;
            };
            FilterService.setMaxPrice = function (price) {
                FilterService.maxPrice = price;
                return FilterService.maxPrice;
            };
            return FilterService;
        }]);