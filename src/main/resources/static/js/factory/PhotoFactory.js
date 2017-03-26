'use strict';
angular.module('shopApp')
    .factory('PhotoFactory', ['$http', '$log', function ($http, $log) {
        var urlBase = serverPath + '/api/product/photo/';
        var PhotoFactory = {};

        PhotoFactory.getPhotos = function (id) {
            return $http.get(urlBase + id);
        };
        PhotoFactory.savePhoto = function (id, photo) {
            return $http.post(urlBase + id, photo, {
                transformRequest: angular.identity,
                headers: {
                    'Content-Type': undefined
                }
            });
        };
        PhotoFactory.getAvaProduct = function (id) {
            return $http.get(urlBase + id + '/ava');
        };
        PhotoFactory.deleteDirectory = function (id) {
            return $http.delete(urlBase + id);
        };
        PhotoFactory.deletePhoto = function (id, name) {
            $log.log(name);
            return $http.delete(urlBase + id + '/' + name);
        };
        PhotoFactory.getFileNameFromURL = function (urlFile) {
            var s = urlFile.replace(/\\/g, '/');
            s = s.substring(s.lastIndexOf('/') + 1);
            return urlFile ? s.replace(/[?#].+$/, '') : s.split('.')[0];
        };
        return PhotoFactory;
    }]);
