'use strict';
var app = angular.module('shopApp', ['rzModule', 'ui.bootstrap', 'ui.router', 'ngResource', 'ngSanitize', 'ui.select', 'ngAnimate', 'thatisuday.ng-image-gallery', 'ngStorage', 'ngFileUpload', 'ngMessages']);
angular.module('shopApp')
    .config([
        '$stateProvider', '$urlRouterProvider', '$locationProvider', 'USER_ROLES',
        function ($stateProvider, $urlRouterProvider, $locationProvider, USER_ROLES) {
            $urlRouterProvider.otherwise("/404");
            $urlRouterProvider.when('', '/');
            $stateProvider
                .state('shop', {
                    abstract: true,
                    views: {
                        '@': {
                            templateUrl: '/views/ui-view/header_main_footer.html'
                        },
                        'header@shop': {
                            templateUrl: '/views/fragments/header.html',
                            controller: 'HeaderController as HeaderController'
                        },
                        'main@shop': {
                            templateUrl: '/views/fragments/start_page.html'
                        },
                        'footer@shop': {
                            templateUrl: '/views/fragments/footer.html'
                        }
                    }, data: {
                        role: USER_ROLES.all
                    }
                })
                .state('shop.login', {
                    url: "/login",
                    onEnter: function () {
                        console.log("enter shop.login");
                    },
                    views: {
                        'main@shop': {
                            templateUrl: '/views/fragments/login.html',
                            controller: 'LoginController as LoginController'
                        }
                    }, data: {
                        role: USER_ROLES.all
                    }
                })
                .state('shop.registration', {
                    url: "/registration",
                    onEnter: function () {
                        console.log("enter shop.registration");
                    },
                    views: {
                        'main@shop': {
                            templateUrl: '/views/fragments/registration.html',
                            controller: 'RegistrationController as RegistrationController'
                        }
                    }, data: {
                        role: USER_ROLES.all
                    }
                })
                .state('shop.404', {
                    url: "/404",
                    onEnter: function () {
                        console.log("enter shop.404");
                    },
                    views: {
                        'main@shop': {
                            templateUrl: '/views/fragments/404.html'
                            // controller: ''
                        }
                    }, data: {
                        role: USER_ROLES.user
                    }
                })
                .state('shop.user', {
                    url: "/user",
                    onEnter: function () {
                        console.log("enter shop.user");
                    },
                    views: {
                        'main@shop': {
                            templateUrl: '/views/fragments/user_profile.html',
                            controller: 'UserProfileController as UserProfileController'
                        }
                    },
                    data: {
                        role: USER_ROLES.user
                    }
                })
                .state('shop.user.editProfile', {
                    url: "/edit",
                    onEnter: function () {
                        console.log("enter shop.user.editProfile");
                    },
                    views: {
                        'main@shop': {
                            templateUrl: '/views/fragments/user_profile_edit.html',
                            controller: 'UserEditProfileController as UserEditProfileController'
                        }
                    }, data: {
                        role: USER_ROLES.user
                    }
                })
                .state('shop.user.cart', {
                    url: "/cart",
                    onEnter: function () {
                        console.log("enter shop.user.cart");
                    },
                    views: {
                        'main@shop': {
                            templateUrl: '/views/fragments/cart_large.html',
                            controller: 'CartController as CartController'
                        }
                    }, data: {
                        role: USER_ROLES.all
                    }
                })
                .state('shop.root', {
                    url: "",
                    abstract: true,
                    onEnter: function () {
                        console.log("enter shop.root");
                    },
                    views: {
                        'main@shop': {
                            templateUrl: '/views/ui-view/sidebar_content.html'
                        },
                        'sidebar@shop.root': {
                            templateUrl: '/views/ui-view/category_filter.html'
                        },
                        'filter@shop.root': {
                            templateUrl: '/views/fragments/filterProduct.html',
                            controller: 'FilterController as FilterController'

                        },
                        'category@shop.root': {
                            templateUrl: '/views/fragments/userCategory.html',
                            controller: 'UserCategoryController as UserCategoryController',
                            resolve: {
                                initCategories: function ($rootScope, $state, $stateParams,
                                                          CategorySession, $log, CategoryService) {
                                    CategorySession.initialCategories();
                                }
                            }
                        }
                    }
                })
                .state('shop.root.home', {
                    url: "/",
                    onEnter: function () {
                        console.log("enter shop.root.home");
                    },
                    views: {
                        'content@shop.root': {
                            templateUrl: '/views/fragments/start_page.html'
                        }
                    }, data: {
                        role: USER_ROLES.all
                    }
                })
                .state('shop.root.home.category', {
                    url: "category/{categoryId}?&page&size&sortParam&sortType&minPrice&maxPrice",
                    onEnter: function ($log, $stateParams, $state, $http) {
                        console.log("enter shop.root.home.category");
                    },
                    reloadOnSearch: true,
                    views: {
                        'content@shop.root': {
                            templateUrl: '/views/fragments/product_list.html',
                            controller: 'ProductListController as ProductListController'
                        }
                    },
                    data: {
                        role: USER_ROLES.all
                    }
                })
                .state('shop.root.home.product', {
                    url: "product/{productId}",
                    onEnter: function () {
                        console.log("enter shop.root.home.category");
                    },
                    views: {
                        'content@shop.root': {
                            templateUrl: '/views/fragments/product_detail.html',
                            controller: 'ProductDetailController as ProductDetailController'
                        }
                    },
                    data: {
                        role: USER_ROLES.all
                    }
                })
                .state('shop.root.home.search', {
                    url: "search?&search&categoryId&page&size&sortParam&sortType&minPrice&&maxPrice",
                    onEnter: function () {
                        console.log("enter shop.root.home.search");
                    },
                    views: {
                        'content@shop.root': {
                            templateUrl: '/views/fragments/product_list.html',
                            controller: 'ProductListController as ProductListController'
                        }
                    }, data: {
                        role: USER_ROLES.all
                    }
                })
                .state('shop.root.admin', {
                    url: "/admin",
                    onEnter: function () {
                        console.log("enter shop.root.admin");
                    },
                    views: {
                        'sidebar@shop.root': {
                            templateUrl: '/views/fragments/admin/admin_sidebar_left.html'
                        }, 'content@shop.root': {
                            templateUrl: '/views/fragments/start_page.html'
                        }
                    }, data: {
                        role: USER_ROLES.admin
                    }
                })
                .state('shop.root.admin.user', {
                    url: "/user?page&size&sortType&sortParam&global&id&firstName&lastName&email&ban&sex",
                    onEnter: function () {
                        console.log("enter shop.root.admin.user");
                    },
                    views: {
                        'content@shop.root': {
                            templateUrl: '/views/fragments/user_list.html',
                            controller: 'UserListController as UserListController'
                        }
                    }, data: {
                        role: USER_ROLES.admin
                    }
                })
                .state('shop.root.admin.category', {
                    url: "/category/{categoryId}",
                    onEnter: function () {
                        console.log("enter shop.root.admin.category");
                    },
                    views: {
                        'content@shop.root': {
                            templateUrl: '/views/fragments/admin/admin_category.html',
                            controller: 'AdminCategoryController as AdminCategoryController'
                        }
                    }
                    , data: {
                        role: USER_ROLES.admin
                    }
                })
                .state('shop.root.admin.product', {
                    url: "/products?&page&size&sortParam&sortType",
                    onEnter: function () {
                        console.log("enter shop.root.admin.product");
                    }, views: {
                        'content@shop.root': {
                            templateUrl: '/views/fragments/product_list.html',
                            controller: 'ProductListController as ProductListController'
                        }
                    }, data: {
                        role: USER_ROLES.admin
                    }
                })
                .state('shop.root.admin.product.new', {
                    url: "^/admin/products/new",
                    onEnter: function () {
                        console.log("enter shop.root.admin.product.new");
                    }, views: {
                        'content@shop.root': {
                            templateUrl: '/views/fragments/product_add.html',
                            controller: 'AddProductController as AddProductController'
                        }
                    }, data: {
                        role: USER_ROLES.admin
                    },
                    params: {
                        categoryId: null
                    }
                })
                .state('shop.root.admin.product.edit', {
                    url: "^/admin/products/{id}/edit",
                    onEnter: function () {
                        console.log("enter shop.root.admin.product.edit");
                    }, views: {
                        'content@shop.root': {
                            templateUrl: '/views/fragments/product_edit.html',
                            controller: 'EditProductController as EditProductController'
                        }
                    }, data: {
                        role: USER_ROLES.admin
                    }
                });

        }])
;