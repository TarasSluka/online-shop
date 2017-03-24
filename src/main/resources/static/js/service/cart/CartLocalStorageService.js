angular.module('shopApp').factory('CartLocalStorageService',
    function ($log, $localStorage) {
        var CartLocalStorageService = {};
        $localStorage.$default({
            cart: []
        });
        CartLocalStorageService.get = function () {
            return $localStorage.cart;
        };
        CartLocalStorageService.clear = function () {
            $localStorage.$reset({
                cart: []
            });
        };
        CartLocalStorageService.IsEmpty = function () {
            if ($localStorage.cart === {} || $localStorage.cart === [])
                return true
            return false;
        };

        CartLocalStorageService.save = function (productId, quantity) {
            var item = {
                productID: productId,
                quantity: quantity || 1
            };
            if ($localStorage.cart == null) {
                $localStorage.cart = [];
            }
            $localStorage.cart.push(item);
        };
        CartLocalStorageService.isExist = function (productId) {
            var f = false;
            var isEmpty = CartLocalStorageService.IsEmpty();
            if (!isEmpty)
                if (!f)
                    angular.forEach($localStorage.cart, function (value, key) {
                        if (value.productID = productId)
                            f = true;
                    });
            return f;
        };
        CartLocalStorageService.update = function (productId, quantity) {
            var f = false;
            var isEmpty = CartLocalStorageService.IsEmpty();
            if (!isEmpty)
                if (!f)
                    angular.forEach($localStorage.cart, function (value, key) {
                        if (value.productID = productId) {
                            $localStorage.cart[key].quantity = quantity || 1;
                            f = true;
                        }
                    });
        };

        CartLocalStorageService.delete = function (productId) {
            var f = false;
            var isEmpty = CartLocalStorageService.IsEmpty();
            if (!isEmpty)
                if (!f)
                    angular.forEach($localStorage.cart, function (value, key) {
                        if (value.productID = productId) {
                            $localStorage.cart.splice(key, 1);
                            f = true;
                        }
                    });
        };
        return CartLocalStorageService;
    });