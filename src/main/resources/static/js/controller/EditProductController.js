'use strict';
angular.module('shopApp').controller('EditProductController',
    ['$state', '$stateParams', '$http', '$scope', '$log', 'CategoryService', 'ProductService', 'PhotoFactory', 'DescriptionFactory', 'Upload', '$timeout', '$q',
        function ($state, $stateParams, $http, $scope, $log, CategoryService, ProductService, PhotoFactory, DescriptionFactory, Upload, $timeout, $q) {
            $scope.photos = [];
            $scope.category = {};
            var newPhotosName = [];
            var deletePhotoName = [];
            var oldPhotoName = [];
            $scope.massageDescription = false;
            $scope.dataLoading = false;
            $scope.new = newPhotosName;
            $scope.del = deletePhotoName;
            $scope.old = oldPhotoName;
            var productDto = {};
            initController();
            $scope.checkDeletePhoto = function (photoName) {
                var f = false;
                angular.forEach(deletePhotoName, function (value, key) {
                    if (value === photoName)
                        f = true;
                });
                return f;
            };
            $scope.$watch('picFile', function (file) {
                if (file) {
                    angular.forEach(file, function (value, key) {
                        if (!checkFile($scope.photos, value)) {
                            $log.log(value);
                            $scope.photos.push(value);
                            newPhotosName.push(value.name);
                        }
                        $log.log(newPhotosName);
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

            $scope.removePhoto = function ($index) {
                deletePhotoName.push($scope.photos[$index].name);
                angular.forEach(newPhotosName, function (value, key) {
                    if (value === $scope.photos[$index].name)
                        newPhotosName.splice(key, 1)
                });
            };

            $scope.restorePhoto = function (name) {
                angular.forEach(deletePhotoName, function (value, key) {
                    if (value == name)
                        deletePhotoName.splice(key, 1);
                });
                angular.forEach(newPhotosName, function (value, key) {
                        // if (value == name)
                        // newPhotosName.splice(key, 1);
                    }
                )
            };
            $scope.removeAllPhoto = function () {
                $scope.photos = [];
            };
            $scope.addDescription = function () {
                if ($scope.type != undefined && $scope.type != '' && $scope.description != '' && $scope.description != undefined) {
                    $scope.listDescription.push({type: $scope.type, description: $scope.description});
                    $scope.description = '';
                    $scope.type = '';
                }
                else {
                    $scope.massageDescription = true;
                    $timeout(function () {
                        $scope.massageDescription = false;
                    }, 4000);
                }
            };

            $scope.removeDescription = function (item) {
                var index = $scope.listDescription.indexOf(item);
                $scope.listDescription.splice(index, 1);

            };
            function getAllLoverChildCategory() {
                CategoryService.getAllLoverChildCategory()
                    .then(function (response) {
                        $scope.categories = response.data;
                        deferCategoryId.promise
                            .then(function (idCategory) {
                                angular.forEach(response.data, function (value, key) {
                                    if (value.id === idCategory)
                                        $scope.category.selected = value;
                                })
                            })

                    }, function (error) {
                    });
            }

            $scope.updateProduct = function () {
                $scope.dataLoading = true;
                productDto.id = $stateParams.id;
                productDto.name = $scope.name;
                productDto.price = $scope.price;
                productDto.descriptionList = $scope.listDescription;
                productDto.avaName = $scope.avaName;
                if ($scope.category.selected)
                    productDto.id_category = $scope.category.selected.id;
                $log.log("productDto", productDto);
                ProductService.updateProduct(productDto)
                    .then(function (response) {
                        $log.log("success", response);
                        var formData = new FormData();

                        var newPhotosRequest = newPhotosName;
                        angular.forEach(newPhotosRequest, function (value, key) {
                            angular.forEach(deletePhotoName, function (value1, key1) {
                                if (value === value1) {
                                    newPhotosRequest.splice(key, 1);
                                }
                            });
                        });
                        angular.forEach($scope.photos, function (value, key) {
                            angular.forEach(newPhotosRequest, function (value1, key1) {
                                if (value.name === value1) {
                                    formData.append('image', value);
                                }
                            })
                        });
                        PhotoFactory.savePhoto(productDto.id, formData)
                            .then(function (response) {
                                $log.log("success", response);
                                $state.reload();
                            }, function (response) {
                                $log.log("error", response);
                            });
                        angular.forEach(deletePhotoName, function (value) {
                            PhotoFactory.deletePhoto(productDto.id, value)
                                .then(function (response) {
                                    $log.log("success deletePhoto", response);
                                }, function (errresponse) {
                                    $log.log("errresponse deletePhoto", errresponse)
                                })
                        })
                    }, function (response) {
                        alert('Failed: ');
                    });
            };
            var deferCategoryId = $q.defer();

            function initController() {
                getAllLoverChildCategory();
                ProductService.getProduct($stateParams.id)
                    .then(function (response) {
                        $scope.name = response.data.name;
                        $scope.price = response.data.price;
                        $scope.listDescription = response.data.descriptionList;
                        if (response.data.id_category)
                            deferCategoryId.resolve(response.data.id_category);
                        else
                            deferCategoryId.$$reject();
                        $scope.avaName = response.data.avaName;
                    }, function (errresponse) {
                    });
                PhotoFactory.getPhotos($stateParams.id)
                    .then(function (response) {
                        angular.forEach(response.data, function (value, key) {
                            Upload.urlToBlob(value)
                                .then(function (blob) {
                                    $scope.photos.push(blob);
                                });
                        });
                        $log.log(response.data);
                        angular.forEach(response.data, function (value) {
                            oldPhotoName.push(PhotoFactory.getFileNameFromURL(value));
                        });
                    }, function (errresponse) {
                        $log.log("error", errresponse)
                    })
            }

        }
    ])
;