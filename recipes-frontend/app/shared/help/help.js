(function() {
    'use strict';

    var recipesControllers = angular.module('recipesControllers');
    recipesControllers.directive('appHelp', function() {
        return {
            restrict: 'E',
            templateUrl: 'shared/help/help.html',
            controllerAs: 'vm',
            controller: ['$scope',
                function($scope) {
                    var vm = this;
                    vm.isClosed = true;
                    vm.switchState = function() {
                        vm.isClosed = !vm.isClosed;
                    };
            }],
            scope: {
                helpText: '='
            }
        };
    });
})();
