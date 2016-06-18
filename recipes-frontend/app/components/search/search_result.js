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
                vm.comments = [];
                vm.recipeId = $stateParams.id;

                var parentScope = $scope.$parent;
                Restangular.one('recipes', vm.recipeId).get().then(function(recipe) {
                    vm.recipe = recipe;
                    vm.tags = recipe.tags.join(', ');
                    $scope.pdfUrl = '../services/recipes/pdf/' + recipe.id;

                    parentScope.enableDownload('../services/recipes/pdf/' + vm.recipe.id);

                    return Restangular.all('comments').getList({'recipe_id': vm.recipeId});
                }).then(function(comments) {
                    vm.comments = comments;
                });

                vm.commentAdded = function(comment) {
                    return Restangular.all('comments').getList({'recipe_id': vm.recipeId}).then(function(comments) { vm.comments = comments; });
                };

                $scope.onLoad = function() {
                    vm.loading = false;
                };

                $scope.$parent.enableBack(function() {
                    $state.go('search', { 'query': vm.query });
                });
                $scope.$parent.enableEdit(function() {
                    $state.go('edit', { 'id': vm.recipe.id, 'query': vm.query });
                });
            }]
        };
    });
})();
