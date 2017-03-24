'use strict';
angular.module('shopApp').controller('ProductListController', [
    '$rootScope', '$state', '$stateParams', '$scope', 'PaginationService', '$log',
    '$timeout', 'PhotoFactory', 'ProductService', 'FilterService', 'CART_EVENTS', 'CartSessionService', 'UserSession', 'CartService', 'CartLocalStorageService', '$interval', '$q', 'modalService', 'CategorySession',
    function ($rootScope, $state, $stateParams, $scope, PaginationService, $log,
              $timeout, PhotoFactory, ProductService, FilterService, CART_EVENTS, CartSessionService, UserSession, CartService, CartLocalStorageService, $interval, $q, modalService, CategorySession) {
        $scope.sortBy = null;
        var request = {};
        $scope.myObj = request;
        $scope.pr = FilterService.getPrice;
        $scope.curMin = FilterService.getCurrentMinPrice;
        $scope.curMax = FilterService.getCurrentMaxPrice;
        $scope.min = FilterService.getMinPrice;
        $scope.max = FilterService.getMaxPrice;
        $scope.conf = {
            imgAnim: 'slide'
        };
        $scope.minPrice = {};
        $scope.maxPrice = {};
        $scope.showPrice = FilterService.getShowPrice;

        $scope.toogleShowPrice = function () {
            delete request["minPrice"];
            delete request["maxPrice"];
            goState();
        };
        $scope.checkProductInCart = CartSessionService.isExist;

        initController();
        function initController() {
            $log.info("state", $state);
            if ($state.current.name === "shop.root.home.category" || $state.current.name === "shop.root.home.search") {
                request.categoryId = $stateParams.categoryId;
                request.size = $stateParams.size || $scope.size;
                request.page = $stateParams.page;
                request.sortParam = $stateParams.sortParam;
                request.sortType = $stateParams.sortType;
                request.minPrice = $stateParams.minPrice;
                request.maxPrice = $stateParams.maxPrice;
                request.search = $stateParams.search;
            }
            goState();
        }

        $rootScope.$on('addFilterPrice', function (event, data) {
            delete request["page"];
            request.minPrice = data.min;
            request.maxPrice = data.max;
            $log.log("addFilterPrice", {data: data, request: request});
            goState();
        });
        function goState() {
            // $log.log("goState", {request: request});
            $state.transitionTo($state.current, {
                    categoryId: request.categoryId,
                    page: request.page,
                    size: request.size,
                    sortType: request.sortType,
                    sortParam: request.sortParam,
                    minPrice: request.minPrice,
                    maxPrice: request.maxPrice,
                    search: request.search
                }, {notify: false}
            );
            getProducts();
        }

        function getProducts() {
            $log.log("getProducts", request);
            ProductService.getProducts(request)
                .then(function (response) {
                    $scope.products = response.data.content;
                    $scope.totalElements = response.data.totalElements;
                    $scope.totalPages = response.data.totalPages;
                    $scope.pages = PaginationService.getPages(request.page, $scope.totalPages);
                    $timeout(function () {
                        $scope.currentPage = parseInt(request.page) + 1 || $stateParams.page + 1 || 2;
                        $scope.currentPage = parseInt(request.page) || $stateParams.page || 1;
                    }, 5);
                }, function (error) {
                    $scope.status = 'Unable to load products data: ' + error.message;
                });
            var tmp = angular.copy(request);
            delete tmp["minPrice"];
            delete tmp["maxPrice"];
            ProductService.getMaxAndMinPrice(tmp)
                .then(function (response) {
                    $log.info("max", response.data.maxPrice, "min", response.data.minPrice);
                    if (response.data.maxPrice === undefined || response.data.minPrice === NaN) {
                        $rootScope.$emit('showFilter', false);
                    } else {
                        FilterService.setMaxPrice(response.data.maxPrice);
                        FilterService.setMinPrice(response.data.minPrice);
                        if (request.minPrice || request.maxPrice) {
                            FilterService.setShowPrice(true);
                            $scope.minPrice = request.minPrice;
                            $scope.maxPrice = request.maxPrice;
                            if (request.minPrice) {
                                FilterService.setCurrentMinPrice(request.minPrice);
                            }
                            else {
                                FilterService.setCurrentMinPrice(FilterService.getMinPrice());
                            }
                            if (request.maxPrice) {
                                FilterService.setCurrentMaxPrice(request.maxPrice);
                            }
                            else {
                                FilterService.setCurrentMaxPrice(FilterService.getMaxPrice());
                            }
                        }
                        else {
                            FilterService.setShowPrice(false);
                            FilterService.setCurrentMinPrice(FilterService.getMinPrice());
                            FilterService.setCurrentMaxPrice(FilterService.getMaxPrice());
                        }
                    }
                }, function (response) {
                    $log.log(response);
                });
            var category;
            if ($stateParams.categoryId)
                category = CategorySession.getCategoryById($stateParams.categoryId);
            else
                category = CategorySession.getParentCategory();
            $scope.hierarchy = CategorySession.getHierarchyCategory(category);
        }

        $scope.$watch('sortBy', function handleIdByPage(newValue, oldValue) {
            if (newValue === oldValue)
                return;
            else if (newValue === 'Date: newer') {
                request.sortParam = 'createdDate';
                request.sortType = 'false';
            } else if (newValue === 'Date: older') {
                request.sortParam = 'createdDate';
                request.sortType = 'true';
            } else if (newValue === 'Price: Low to High') {
                request.sortParam = 'price';
                request.sortType = 'true';
            } else if (newValue === 'Price: High to Low') {
                request.sortParam = 'price';
                request.sortType = 'false';
            }
            else if (newValue === 'Category: null') {
                request.sortParam = 'category';
                request.sortType = 'null';
            }
            goState();
        });
        $scope.$watch('size', function handleIdByPage(newValue, oldValue) {
            if (newValue === oldValue)
                return;
            request.size = newValue;
            goState();
        });

        $scope.openGalleryImage = function (id) {
            var listImg = [];
            PhotoFactory.getPhotos(id)
                .then(function (response) {
                    response.data.forEach(function (tmp) {
                        listImg.push({url: tmp});
                    });
                    $scope.images = listImg;

                    $scope.methods.open();
                });

        };
        $scope.setPage = function (page) {
            if (page === 0)
                page = 1;
            else if (page - 1 === $scope.totalPages)
                page = $scope.totalPages;
            request.page = page;
            $scope.currentPage = page;
            $scope.pages = PaginationService.getPages(page, $scope.totalPages);
            goState();

        };
        $scope.addToCard = function addToCard(product, quantity) {
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
        $scope.deleteProduct = function (productId) {
            var modalOptions = {
                closeButtonText: 'Cancel',
                actionButtonText: 'Delete Product',
                headerText: 'Delete product' + '?',
                bodyText: 'Are you sure you want to delete this Product?'
            };
            modalService.showModal({}, modalOptions)
                .then(function (response) {
                    ProductService.deleteProduct(productId)
                        .then(function (response) {
                            $log.log("success", response);
                            $state.reload();
                        }, function (response) {
                            $log.log("error", response);
                        })
                }, function (erResponse) {
                });
        };
    }
])
;