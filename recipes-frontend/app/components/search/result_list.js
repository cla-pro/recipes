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
            },
            controller: ['$scope', '$state', function($scope, $state) {
                var vm = $scope;
                $scope.goToRecipe = function(id) {
                    $state.go('search_result', { 'id': id, 'query': vm.query });
                };
            }]
        };
    });
})();

