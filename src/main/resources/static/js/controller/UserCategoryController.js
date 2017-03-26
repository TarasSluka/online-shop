'use strict';
angular.module('shopApp').controller('UserCategoryController', [
    '$scope', '$log', 'CategoryService', '$state', '$stateParams', 'CategorySession', '$q', '$timeout',
    function ($scope, $log, CategoryService, $state, $stateParams, CategorySession, $q, $timeout) {
        function getParentCategory() {
            $timeout(function () {
                CategorySession.getParentCategory()
                    .then(function (category) {
                        $scope.categories = category;
                    });
            }, 1500, CategorySession.getInitial());
        }

        $scope.serverPath = serverPath;
        $scope.openCategory = getCategoryById;
        function getCategoryById(categoryId) {
            $scope.categories = CategorySession.getCategoryById(categoryId);
        }

        $scope.$on("$stateChangeSuccess", function updatePage() {
                if (!$state.params.categoryId)
                    getParentCategory();
                else
                    getCategoryById($state.params.categoryId);
            }
        );
        $scope.isRootCategory = CategorySession.isRootCategory;
    }
])
;