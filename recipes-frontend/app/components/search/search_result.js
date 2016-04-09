(function() {
    'use strict';

    var recipesControllers = angular.module('recipesControllers');
    recipesControllers.directive('appSearchResult', function() {
        return {
            restrict: 'E',
            templateUrl: 'components/search/search.result.html',
            controllerAs: 'vm',
            controller: ['$scope', '$state', '$stateParams', 'Restangular', function($scope, $state, $stateParams, Restangular) {
                var vm = this;
                vm.recipe = {};
                vm.tags = '';
                vm.query = (isObjectEmpty($stateParams.query) ? '' : $stateParams.query);
                vm.loading = true;

                Restangular.one('recipes', $stateParams.id).get().then(function(recipe) {
                    vm.recipe = recipe;
                    vm.tags = recipe.tags.join(', ');
                    $scope.pdfUrl = '../services/recipes/pdf/' + recipe.id;
                });

                $scope.onLoad = function() {
                    vm.loading = false;
                };

                $scope.editRecipe = function() {
                    $state.go('edit', { 'id': vm.recipe.id, 'query': vm.query });
                }
                $scope.back = function() {
                    $state.go('search', { 'query': vm.query });
                }
            }]
        };
    });
})();
