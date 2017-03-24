angular.module('shopApp')
    .directive('cartItemPrice',
        function ($rootScope, $log, CartSessionService, CART_EVENTS) {
            return {
                template: "<div ng-show='!isEmpty()'>" +
                "<span>&nbsp;cart: {{cartItem |number}}</span>" +
                "<span>&nbsp;(price: {{cartPrice | number}})</span>" +
                "</div>",
                scope: false,
                link: function (scope, element, attrs) {
                    // $log.log("directive", scope, element, attrs);
                    scope.cartPrice = CartSessionService.getTotalPrice();
                    scope.cartItem = CartSessionService.getTotalItem();

                    $rootScope.$on(CART_EVENTS.change, function () {
                        scope.cartPrice = CartSessionService.getTotalPrice();
                        scope.cartItem = CartSessionService.getTotalItem();
                        $log.log("directive Event,getTotalPrice, getTotalItem");
                    })
                }
            }
        }
    )
    .directive('cartTotalPrice',
        function ($rootScope, $log, CartSessionService, CART_EVENTS) {
            return {
                template: "<span >&nbsp;cart: {{cartPrice |number}}</span>",
                scope: false,
                link: function (scope, element, attrs) {
                    scope.cartPrice = CartSessionService.getTotalPrice();
                    $rootScope.$on(CART_EVENTS.change, function () {
                        scope.cartPrice = CartSessionService.getTotalPrice();
                        $log.log("directive Event getTotalPrice");
                    })
                }
            }
        }
    )
    .directive('cartTotalItem',
        function ($rootScope, $log, CartSessionService, CART_EVENTS) {
            return {
                template: "<span>&nbsp;cart: {{cartItem |number}</span>",
                scope: false,
                link: function (scope, element, attrs) {
                    scope.cartItem = CartSessionService.getTotalItem();
                    $rootScope.$on(CART_EVENTS.change, function () {
                        scope.cartItem = CartSessionService.getTotalItem();
                        $log.log("directive Event getTotalItem");
                    })
                }
            }
        }
    )
;
