angular.module('shopApp').factory('CartSessionService',
    function ($log, ProductService, CartLocalStorageService, $q, CART_EVENTS, $rootScope) {
        var CartSessionService = {};
        CartSessionService.cart = [];

        CartSessionService.dropCart = function () {
            CartSessionService.cart = [];
        };
        CartSessionService.getCart = function () {
            return CartSessionService.cart;
        };

        CartSessionService.isExist = function (productId) {
            var tmp = false;
            angular.forEach(CartSessionService.cart, function (value, key) {
                if (!tmp)
                    if (value.productDto.id === productId)
                        tmp = true;
            });
            return tmp;
        };
        CartSessionService.isEmpty = function () {
            return CartSessionService.cart == {};
        };
        CartSessionService.update = function (productId, quantity) {
            var f = false;
            angular.forEach(CartSessionService.cart, function (value, key) {
                if (!f)
                    if (value.productDto.id === productId) {
                        CartSessionService.cart[key].quantity = quantity || 1;
                        f = true;
                    }
            });
            return f;
        };
        CartSessionService.save = function (productDto, quantity) {
            var item = {
                productDto: productDto,
                quantity: quantity
            };
            CartSessionService.cart.push(item);
        };
        CartSessionService.delete = function (productId) {
            var f = false;
            angular.forEach(CartSessionService.cart, function (value, key) {
                if (!f)
                    if (value.productDto.id === productId) {
                        CartSessionService.cart.splice(key, 1);
                        f = true;
                    }
            });
        };
        CartSessionService.synchronizeCartWithServer = function () {
            var cart = [];
            angular.forEach(CartSessionService.cart, function (value, key) {
                var item = {
                    productId: value.productDto.id,
                    quantity: value.quantity
                };
                cart.push(item);
            });
            return cart;
        };

        CartSessionService.getTotalItem = function () {
            var totalItem = 0;
            angular.forEach(CartSessionService.cart, function (value, key) {
                totalItem += value.quantity;
            });
            return totalItem || 0;
        };

        CartSessionService.getTotalPrice = function () {
            var totalPrice = 0;
            angular.forEach(CartSessionService.cart, function (value, key) {
                totalPrice += value.productDto.price * value.quantity;
            });
            return totalPrice || 0;
        };

        CartSessionService.initSessionCartFromLocalStorage = function () {
            // $log.log("....initSessionCartFromLocalStorage");
            var deferred = $q.defer();
            var mas = CartLocalStorageService.get();
            var promise = [];
            angular.forEach(mas, function (value, key) {
                var deferred = $q.defer();
                ProductService.getProduct(value.productID)
                    .then(function (response) {
                        $log.log("ProductService.getProduct", response);
                        deferred.resolve(response.data);
                    }, function (response) {
                    });
                promise.push(deferred.promise);
            });

            $q.all(promise)
                .then(function (result) {
                    // $log.log("$q.all(promise)", result);
                    angular.forEach(result, function (value, key) {
                        var item = {
                            productDto: value,
                            quantity: mas[key].quantity
                        };
                        CartSessionService.cart.push(item);
                    });
                    // $log.log("CartSessionService.cart", CartSessionService.cart);
                    $rootScope.$emit(CART_EVENTS.change);
                });
        };
        return CartSessionService;
    });