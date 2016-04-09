(function() {
    'use strict';

    var recipesControllers = angular.module('recipesControllers');
    recipesControllers.directive('appResultList', function() {
        return {
            restrict: 'E',
            templateUrl: 'components/search/result_list.html',
            scope: {
                results: '=',
                message: '=',
                query: '='
            }
        };
    });
})();

