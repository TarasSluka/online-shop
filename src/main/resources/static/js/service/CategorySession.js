angular.module('shopApp')
    .factory("CategorySession",
        function ($q, $log, CategoryService, $timeout) {
            var CategorySession = {};
            CategorySession.isInitial = false;
            CategorySession.categories = {};
            CategorySession.currentCategory = {};

            CategorySession.getInitial = function () {
                return CategorySession.isInitial;
            };
            CategorySession.initialCategories = function () {
                CategoryService.getCategories()
                    .then(function (response) {
                        CategorySession.getHierarchyCategory = function (category) {
                            var res = [];
                            var f = true;
                            while (f) {
                                res.push(category);
                                if (CategorySession.isRootCategory(category)) {
                                    f = false;
                                }
                                category = CategorySession.getCategoryById(category.parent);
                            }
                            res.reverse();
                            return res;
                        };
                        CategorySession.getCategoryById = function (id) {
                            var res;
                            var brake = true;
                            response.data.forEach(function (value, key) {
                                if (brake) {
                                    if (parseInt(value.id) === parseInt(id)) {
                                        res = value;
                                        brake = false;
                                    }
                                }
                            });
                            return res;
                        };
                        CategorySession.getParentCategory = function () {
                            var defer = $q.defer();
                            var brake = false;
                            response.data.forEach(function (value, key) {
                                if (!brake) {
                                    if (CategorySession.isRootCategory(value)) {
                                        defer.resolve(value);
                                        brake = true;
                                    }
                                }
                            });
                            return defer.promise;
                        };

                    }, function (response) {
                        $log.log("error get Categories from server");
                    });
            };


            CategorySession.getCurrentCategory = function () {
                return CategorySession.currentCategory;
            };

            CategorySession.setCurrentCategory = function (category) {
                CategorySession.currentCategory = category;
            };
            CategorySession.setCategories = function (categories) {
                CategorySession.categories = categories;
                CategorySession.isInitial = true;
            };

            CategorySession.isRootCategory = function (category) {
                return category == undefined || category.parent == null;
            };

            CategorySession.getCategories = function () {
                return CategorySession.categories;
            };

            CategorySession.isRootCategory = function (categories) {
                return categories == undefined || categories.parent == null;
            };

            return CategorySession;
        })
;