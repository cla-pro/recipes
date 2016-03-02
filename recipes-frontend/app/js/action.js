(function() {
    'use strict';

    var recipesControllers = angular.module('recipesControllers');
    recipesControllers.directive('appAction', function() {
        return {
            restrict: 'E',
            templateUrl: 'partials/action.html',
            transclude: true,
            controllerAs: 'vm',
            controller: ['$scope', '$stateParams',
                    function($scope, $stateParams) {

            }],
            scope: {
                displayBack: '=',
                displayEdit: '=',
                clickBack: '&onClickBack',
                clickEdit: '&onClickEdit'
            }
        };
    });
})();
