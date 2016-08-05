(function() {
    'use strict';

    var recipesControllers = angular.module('recipesControllers');
    recipesControllers.directive('appAction', function() {
        return {
            restrict: 'E',
            templateUrl: 'shared/action/action.html',
            transclude: true,
            controllerAs: 'vm',
            controller: ['$scope', '$stateParams', function($scope, $stateParams) {
                $scope.hasAdditionalActions = $scope.displayEdit || $scope.displayDownload;
                $scope.displayAdditionalActions = false;

                var that = $scope;
                var updateHasAdditionalActions = function() {
                    that.hasAdditionalActions = that.displayEdit || that.displayDownload;
                };
                $scope.$watch(function() { return that.displayEdit; }, updateHasAdditionalActions);
                $scope.$watch(function() { return that.displayDownload; }, updateHasAdditionalActions);
                $scope.clickAdditional = function() {
                    $scope.displayAdditionalActions = !$scope.displayAdditionalActions;
                };
            }],
            scope: {
                displayBack: '=',
                displayEdit: '=',
                displayDownload: '=',
                downloadUrl: '=',
                downloadUrlPdf: '=',
                clickBack: '&onClickBack',
                clickEdit: '&onClickEdit'
            }
        };
    });
})();
