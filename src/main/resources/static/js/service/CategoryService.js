'use strict';
angular.module('shopApp')
    .factory('CategoryService', ['$http', '$log', function ($http, $log) {
        var urlBase = serverPath + 'api/category';
        var CategoryService = {};
        CategoryService.getCategories = function () {
            return $http.get(urlBase);
        };

        CategoryService.getAllLoverChildCategory = function () {
            return $http.get(urlBase + '/allLowerLevelCategory');
        };

        CategoryService.getCategory = function (id) {
            return $http.get(urlBase + '/' + id);
        };
        CategoryService.getParentCategory = function () {
            return $http.get(urlBase + '/parentCategory');
        };

        CategoryService.getHierarchyCategory = function (id) {
            return $http.get(urlBase + '/' + id + '/hierarchy');
        };
        CategoryService.saveCategory = function (category) {
            return $http.post(urlBase, category);
        };
        CategoryService.updateCategory = function (category) {
            return $http.put(urlBase + '/' + category.id, category);
        };
        CategoryService.deleteCategory = function (id) {
            return $http.delete(urlBase + '/' + id);
        };
        return CategoryService;

    }]);
