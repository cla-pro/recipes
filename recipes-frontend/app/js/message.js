(function() {
    'use strict';

    var recipesControllers = angular.module('recipesControllers');
    recipesControllers.directive('appMessage', function() {
        return {
            restrict: 'E',
            templateUrl: 'partials/message.html',
            scope: {
                message: '=',
                isError: '='
            }
        };
    });
})();

