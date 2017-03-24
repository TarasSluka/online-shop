'use strict';
angular.module('shopApp').controller('ProductDetailController', [
    '$rootScope', '$scope', '$http', '$log', 'ProductService', '$stateParams', 'PhotoFactory', '$q', 'CategorySession', 'UserSession', 'CartSessionService', 'CartLocalStorageService', 'CART_EVENTS',
    function ($rootScope, $scope, $http, $log, ProductService, $stateParams, PhotoFactory, $q, CategorySession, UserSession, CartSessionService, CartLocalStorageService, CART_EVENTS) {

        var self = this;
        $scope.id = $stateParams.productId;
        getProduct();
        var listImg = [];
        $scope.checkProductInCart = CartSessionService.isExist;

        $scope.addToCard = function addToCard(product, quantity) {
            $log.log("addToCard", product, quantity);
            if (UserSession.isAuthorized())
                CartService.save(product.id, quantity)
                    .then(function (response) {
                    }, function (response) {
                    });
            else {
                CartLocalStorageService.save(product.id, quantity);
            }
            CartSessionService.save(product, quantity);
            $rootScope.$emit(CART_EVENTS.change);
        };
        $scope.deleteFromCart = function deleteFromCart(productId) {
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
        function getProduct() {
            ProductService.getProduct($stateParams.productId)
                .then(function (response) {
                    $log.log(response);
                    console.log(response.data);
                    $scope.product = response.data;
                    var category = CategorySession.getCategoryById(response.data.id_category);
                    $scope.hierarchy = CategorySession.getHierarchyCategory(category);
                }, function (error) {
                    $scope.status = 'Unable to load products data: ' + error.message;
                });
        }

        $scope.inline = true;
        openGalleryImage($scope.id);
        function openGalleryImage(id) {
            $log.log('id = ' + id);
            PhotoFactory.getPhotos(id)
                .then(function (response) {
                    response.data.forEach(function (tmp) {
                        listImg.push({url: tmp});
                    });
                    $scope.images = listImg;
                    $scope.conf = {
                        imgAnim: 'slide'
                    };
                });
        }
    }]);
