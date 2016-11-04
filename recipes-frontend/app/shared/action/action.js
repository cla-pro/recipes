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

                var that = $scope;
                that.config.hideAdditionalActions = function() {
                    $scope.displayAdditionalActions = false;
                };

                function updateHasAdditionalActions() {
                    that.hasAdditionalActions = that.config.displayEdit || that.config.displayDownload;
                };
                $scope.$watch(function() { return that.config.displayEdit; }, updateHasAdditionalActions);
                $scope.$watch(function() { return that.config.displayDownload; }, updateHasAdditionalActions);
                $scope.clickAdditional = function() {
                    $scope.displayAdditionalActions = !$scope.displayAdditionalActions;
                };

                updateHasAdditionalActions();
                that.displayAdditionalActions = false;
            }],
            scope: {
                config: '='
            }
        };
    });
})();
