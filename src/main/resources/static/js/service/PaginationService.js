angular.module('shopApp').factory(
    'PaginationService', ['$log',
        function ($log) {
            var PaginationService = {};

            PaginationService.getPages = function (currentPage, totalPages) {
                var startPage, endPage;
                var currentPage = parseInt(currentPage) || 1;
                if (totalPages <= 10) {
                    // less than 10 total pages so show all
                    startPage = 1;
                    endPage = totalPages;
                } else {
                    if (currentPage <= 6) {
                        startPage = 1;
                        endPage = 10;
                    } else if (currentPage + 4 >= totalPages) {
                        startPage = totalPages - 9;
                        endPage = totalPages;
                    } else {
                        startPage = currentPage - 5;
                        endPage = currentPage + 4;
                    }
                }
                return range(startPage, endPage + 1);
            }
            function range(start, end) {
                var start = parseInt(start);
                var end = parseInt(end);
                var res = [];
                for (; start < end; start++) {
                    res.push(start);
                }
                return res;
            }
            return PaginationService;


        }]);