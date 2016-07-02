(function() {
    'use strict';

    var recipesControllers = angular.module('recipesControllers');
    recipesControllers.directive('appAction', function() {
        return {
            restrict: 'E',
            templateUrl: 'shared/action/action.html',
            transclude: true,
            controllerAs: 'vm',
            controller: ['$scope', '$stateParams', function($scope, $stateParams) {}],
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
