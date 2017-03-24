angular.module('shopApp')
    .directive('ngImg', function () {
        return {
            restrict: 'E',
            scope: {
                show: '='
            },
            replase: true,
            transclude: true,
            link: function (scope, element, attrs) {
                if (attrs.whidth) {
                    scope.dialogStyle.width = attrs.width;
                }
                if (attrs.height) {
                    scope.dialogStyle.height = attrs.height;
                }
                scope.hideModal = function () {
                    scope.show = false;
                };
                template: "<div class='ng-modal' ng-show='show'><div class='ng-modal-overlay' ng-click='hideModal()'></div><div class='ng-modal-dialog' ng-style='dialogStyle'><div class='ng-modal-close' ng-click='hideModal()'>X</div><div class='ng-modal-dialog-content' ng-transclude></div></div></div>"
            }
        }
    });