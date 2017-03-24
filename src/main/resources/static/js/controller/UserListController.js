'use strict';
angular.module('shopApp')
    .constant("userConstant", {
        "size": "20",
        "page": "1"
    })
    .controller('UserListController',
        ['$http', '$scope', '$log', 'UserService', '$state', '$stateParams', '$filter', 'userConstant', '$timeout', 'PaginationService',
            function ($http, $scope, $log, UserService, $state, $stateParams, $filter, userConstant, $timeout, PaginationService) {
                var request = {};
                $scope.myObj = request;
                $scope.setPage = function (page) {
                    if (page === 0)
                        page = userConstant.page;
                    else if (page - 1 === $scope.totalPages)
                        page = $scope.totalPages;
                    request.page = page;
                    $scope.currentPage = page;
                    $scope.pages = PaginationService.getPages(page, $scope.totalPages);
                    goState();

                };
                function goState() {
                    $state.transitionTo($state.current, {
                            page: request.page || userConstant.page,
                            size: request.size || userConstant.size,
                            sortType: request.sortType,
                            sortParam: request.sortParam,
                            global: request.global,
                            id: request.id,
                            firstName: request.firstName,
                            lastName: request.lastName,
                            email: request.email,
                            ban: request.ban,
                            sex: request.sex
                        }, {notify: false}
                    );
                    callServer();
                }

                function callServer() {
                    $scope.isLoading = true;
                    UserService.getUsers(request)
                        .then(function (response) {
                            $scope.users = response.data.content;
                            $scope.totalElements = response.data.totalElements;
                            $scope.totalPages = response.data.totalPages;
                            $log.log('callServer');
                            $log.log(PaginationService.getPages(request.page, $scope.totalPages));
                            $scope.pages = PaginationService.getPages(request.page, $scope.totalPages);
                            $log.log("$scope.currentPage  = " + $scope.currentPage);
                            $timeout(function () {
                                $scope.currentPage = parseInt(request.page) + 1;
                                $scope.currentPage = parseInt(request.page);
                            }, 50);

                            $scope.isLoading = false;
                        }, function () {
                            $log.log('error setPage');
                            $scope.isLoading = false;
                        })
                }

                initController();
                function initController() {
                    request.page = $stateParams.page || userConstant.page;
                    request.size = $stateParams.size || userConstant.size;
                    request.sortType = $stateParams.sortType || '';
                    request.sortParam = $stateParams.sortParam || '';
                    request.global = $stateParams.global || '';
                    request.id = $stateParams.id || '';
                    request.firstName = $stateParams.firstName || '';
                    request.lastName = $stateParams.lastName || '';
                    request.email = $stateParams.email || '';
                    request.ban = $stateParams.ban || '';
                    request.sex = $stateParams.sex || '';
                    goState();
                }

                $scope.$watch('id', function handleIdByPage(newValue, oldValue) {
                    if (newValue === oldValue)
                        return;
                    request.id = newValue;
                    goState();
                });
                $scope.$watch('global', function handleGlobalByPage(newValue, oldValue) {
                    if (newValue === oldValue)
                        return;
                    request.global = newValue;
                    goState();
                });
                $scope.$watch('firstName', function handleFirstNameByPage(newValue, oldValue) {
                    if (newValue === oldValue)
                        return;
                    request.firstName = newValue;
                    goState();
                });
                $scope.$watch('lastName', function handleLastNameByPage(newValue, oldValue) {
                    if (newValue === oldValue)
                        return;
                    request.lastName = newValue;
                    goState();
                });
                $scope.$watch('createdDate', function handleCreatedDateByPage(newValue, oldValue) {
                    if (newValue === oldValue)
                        return;
                    request.createdDate = newValue;
                    goState();
                });
                $scope.$watch('email', function handleEmailByPage(newValue, oldValue) {
                    if (newValue === oldValue)
                        return;
                    request.email = newValue;
                    goState();
                });
                $scope.$watch('userName', function handleEmailByPage(newValue, oldValue) {
                    if (newValue === oldValue)
                        return;
                    request.userName = newValue;
                    goState();
                });
                $scope.$watch('ban', function handleBanByPage(newValue, oldValue) {
                    if (newValue === oldValue)
                        return;
                    request.ban = newValue;
                    goState();
                });
                $scope.$watch('sex', function handleSexByPage(newValue, oldValue) {
                    if (newValue === oldValue)
                        return;
                    if (newValue === 'All')
                        delete request["sex"];
                    else
                        request.sex = newValue;
                    goState();
                });
                $scope.$watch('sortParam', function handleSortParam(newValue, oldValue) {
                    if (newValue === oldValue)
                        return;
                    if (newValue === '')
                        delete request["sortParam"];
                    else
                        request.sortParam = newValue;
                    goState();
                });
                $scope.$watch('sortReverse', function handleSortReverse(newValue, oldValue) {
                    if (newValue === oldValue)
                        return;

                    if (newValue === '')
                        delete request["sortType"];
                    else
                        request.sortType = newValue;
                    goState();
                });
                $scope.$watch('itemsByPage', function handleItemsByPage(newValue, oldValue) {
                    if (newValue === oldValue)
                        return;
                    request.size = newValue;
                    goState();
                });
                $scope.deleteUser = function (user) {
                    UserService.deleteUser(user.id)
                        .then(function () {
                            $log.log("success delete user" + user.id);
                            var index = $scope.users.indexOf(user);
                            $scope.users.splice(index, 1);
                        }, function () {
                            $log.log("error delete user" + user.id);
                        })
                };
                $scope.baneUser = function (user) {
                    UserService.enabledUser(user.id)
                        .then(function () {
                            $log.log("success user enabled");
                        }, function () {
                            user.enabled = !user.enabled;
                            $log.log("error user enabled");

                        })
                };
            }

        ]);
