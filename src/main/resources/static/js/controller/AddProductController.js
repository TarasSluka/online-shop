'use strict';
angular.module('shopApp').controller('AddProductController',
    function ($stateParams, $http, $scope, $log, CategoryService, ProductService, PhotoFactory, $timeout) {
        $scope.category = {};
        $scope.photos = [];
        $scope.files = [];
        $scope.listDescription = [];
        $scope.massageDescription = false;
        $scope.messageSave = false;
        var productDto = {};
        $scope.obg = productDto;
        $scope.$watch('picFile', function (file) {
            if (file) {
                angular.forEach(file, function (value, key) {
                    if (!checkFile($scope.photos, value)) {
                        $log.log(value);
                        $scope.photos.push(value);
                    }
                });
            }
        });
        function checkFile(masFile, file) {
            var f = false;
            angular.forEach(masFile, function (value, key) {
                if (!f) {
                    if (value.name === file.name && value.size === file.size)
                        f = true;
                }
            });
            return f;
        }

        $scope.updateSelection = function (position, photos) {
            productDto.avaName = photos[position].name;
            angular.forEach(photos, function (photo, index) {
                if (position != index) {
                    photo.ava = false;
                }
            })
        };
        $scope.removePhoto = function ($index) {
            $scope.photos.splice($index, 1);
        };
        $scope.removeAllPhoto = function () {
            $scope.photos = [];
        };
        $scope.addDescription = function () {
            if ($scope.type != undefined && $scope.type != '' && $scope.value != '' && $scope.value != undefined) {
                $scope.listDescription.push({type: $scope.type, value: $scope.value});
                $scope.value = '';
                $scope.type = '';
            }
            else {
                $scope.massageDescription = true;
                $timeout(function () {
                    $scope.massageDescription = false;
                }, 4000);
            }
        };

        $scope.saveProduct = function () {
            $scope.dataLoading = true;
            productDto.name = $scope.name;
            productDto.price = $scope.price;
            productDto.descriptionList = $scope.listDescription;
            if ($scope.category.selected)
                productDto.id_category = $scope.category.selected.id;

            ProductService.saveProduct(productDto)
                .then(function (response) {
                    var formData = new FormData();
                    angular.forEach($scope.photos, function (value, key) {
                        formData.append('image', value);
                    });
                    if ($scope.photos.length)
                        PhotoFactory.savePhoto(response.data, formData)
                            .then(function (response) {
                                $log.log("success", response);
                            }, function (response) {
                                $log.log("error", response);
                            });
                    $scope.dataLoading = false;
                    $scope.saveMessages = "success";
                    $scope.messageSave = true;
                    $scope.price = '';
                    $scope.name = '';
                    $scope.description = '';
                    $scope.type = '';
                    $scope.listDescription = [];
                    $scope.photos = [];
                    $timeout(function () {
                        $scope.messageSave = false;
                    }, 4000);
                }, function (response) {
                    $scope.dataLoading = false;
                    $scope.messageSave = true;
                    $scope.saveMessages = "error";
                    $timeout(function () {
                        $scope.messageSave = false;
                    }, 4000);

                });
        };
        $scope.removeDescription = function (item) {
            var index = $scope.listDescription.indexOf(item);
            $scope.listDescription.splice(index, 1);
        };
        getAllLoverChildCategory();
        function getAllLoverChildCategory() {
            // $scope.category.selected = undefined;
            CategoryService.getAllLoverChildCategory()
                .then(function (response) {
                    $log.log(response);
                    $scope.categories = response.data;
                    if ($stateParams.categoryId) {
                        angular.forEach(response.data, function (value, key) {
                            if (value.id === $stateParams.categoryId)
                                $scope.category.selected = value;
                        })
                    }
                }, function (error) {
                    $scope.status = 'Unable to load products data: ' + error.message;
                });
        }


    });