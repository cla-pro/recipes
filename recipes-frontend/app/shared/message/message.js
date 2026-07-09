(function() {
    'use strict';

    var recipesControllers = angular.module('recipesControllers');
    recipesControllers.directive('appMessage', function() {
        return {
            restrict: 'E',
            templateUrl: 'shared/message/message.html',
            scope: {
                message: '=',
                isError: '='
            }
        };
    });
})();

