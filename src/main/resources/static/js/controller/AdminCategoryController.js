'use strict';
angular.module('shopApp').controller('AdminCategoryController', [
    '$stateParams', '$scope', '$log', 'CategoryService', '$timeout', 'modalService', '$state', 'CategorySession',
    function ($stateParams, $scope, $log, CategoryService, $timeout, modalService, $state, CategorySession) {
        $scope.isRootCategory = CategorySession.isRootCategory;
        var currentCategory = {};
        $scope.showAdd = false;
        $scope.showEdit = false;
        $scope.showMassage = false;
        initController();
        $scope.serverPath = serverPath;
        $scope.addProduct = function () {
            $state.go('shop.root.admin.product.new', {categoryId: $scope.categories.id});
        };

        function initController() {
            var idCategory = $stateParams.categoryId;
            $log.log("$stateParams", $stateParams.categoryId);
            if ($stateParams.categoryId === "") {
                CategoryService.getParentCategory()
                    .then(function (resonse) {
                        $scope.categories = resonse.data;
                        getHierarchyCategory($scope.categories.id);
                    }, function (errResponse) {
                        $log.log(errResponse);
                    })
            }
            else {
                CategoryService.getCategory($stateParams.categoryId)
                    .then(function (resonse) {
                        $scope.categories = resonse.data;
                        getHierarchyCategory($scope.categories.id);
                    }, function (errResponse) {
                        $log.log(errResponse);
                    })
            }

        }

        $scope.addCategory = function (category, parentId) {
            var data = category;
            data.parent = parseInt(parentId);
            $log.log(data);
            $scope.newCategory = {};
            CategoryService.saveCategory(data)
                .then(function (response) {
                    $scope.categories.children.push(response.data);
                    $scope.showMassage = true;
                    $scope.massage = "success";
                    $timeout(function () {
                        $scope.showMassage = false;
                    }, 3000);
                }, function () {
                    $scope.showMassage = true;
                    $scope.massage = "error";
                    $timeout(function () {
                        $scope.showMassage = false;
                    }, 3000);

                });
            toggleShowAdd();
        };
        $scope.deleteCategory = function (category) {
            var categoryName = category.name;
            var modalOptions = {
                closeButtonText: 'Cancel',
                actionButtonText: 'Delete Category',
                headerText: 'Delete ' + categoryName + '?',
                bodyText: 'Are you sure you want to delete this Category?'
            };
            modalService.showModal({}, modalOptions)
                .then(function (response) {
                    CategoryService.deleteCategory(category.id)
                        .then(function (response) {
                            $log.log(response);
                        }, function (response) {
                            $log.log(response);
                        })
                });
        };
        $scope.toggleShowEdit = function () {
            toggleShowEdit();
        };
        function toggleShowEdit() {
            $scope.editCategoryName = $scope.categories.name;
            $scope.editCategoryDescription = $scope.categories.description;
            $scope.showEdit = !$scope.showEdit;
        }

        $scope.toggleShowAdd = function () {
            toggleShowAdd();
        };
        function toggleShowAdd() {
            $scope.showAdd = !$scope.showAdd;
        }


        function getHierarchyCategory(categoryId) {
            $log.log("getHierarchyCategory");
            CategoryService.getHierarchyCategory(parseInt(categoryId))
                .then(function (response) {
                    $scope.hierarchy = response.data;
                    $log.info(response);
                })
        }

    }
]);
