angular.module('shopApp').controller('FilterController', [
    '$log', '$scope', 'FilterService', '$state', '$rootScope',
    function ($log, $scope, FilterService, $state, $rootScope) {

        $scope.$on('$stateChangeSuccess',
            function (event, toState, toParams, fromState, fromParams) {
                if (toState.name === "shop.root.home.category" || toState.name === "shop.root.home.search")
                    $scope.showFilter = true;
                else //if (fromState.abstract === true)
                    $scope.showFilter = false;
            });

        $rootScope.$on('showFilter', function (event, data) {
            $scope.showFilter = data;
        });
        $scope.sendFilter = function (min, max) {
            FilterService.setShowPrice(true);
            $rootScope.$emit('addFilterPrice', {
                min: min,
                max: max
            });
        };

        $scope.$watch(function () {
                return FilterService.getMinPrice();
            }, function (newValue, oldValue) {
                $scope.minRangeSlider.options.floor = newValue;
            }
        );
        $scope.$watch(function () {
                return FilterService.getMaxPrice();
            }, function (newValue, oldValue) {
                $scope.minRangeSlider.options.ceil = newValue;
            }
        );
        $scope.$watch(function () {
                return FilterService.getCurrentMinPrice();
            }, function (newValue, oldValue) {
                $scope.minRangeSlider.minValue = newValue;
            }
        );
        $scope.$watch(function () {
                return FilterService.getCurrentMaxPrice();
            }, function (newValue, oldValue) {
                $scope.minRangeSlider.maxValue = newValue;
            }
        );
        $scope.minRangeSlider = {
            minValue: {},
            maxValue: {},
            options: {
                floor: {},
                ceil: {},
                step: 1
            }
        };

    }]);