angular.module('shopApp').directive('avaLoadByIdProduct', function (PhotoFactory) {
    return {
        restrict: 'A',
        replace: true,
        scope: true,
        link: function (scope, element, attrs) {
            PhotoFactory.getAvaProduct(attrs.avaLoadByIdProduct)
                .then(function (response) {
                    scope.avaUrl = response.data.avaUrl;
                });
        }
    }
});