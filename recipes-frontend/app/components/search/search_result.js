(function() {
    'use strict';

    var recipesControllers = angular.module('recipesControllers');
    recipesControllers.component('appSearchResult', {
        templateUrl: 'components/search/search.result.html',
        controllerAs: 'vm',
        controller: ['$scope', '$state', '$stateParams', '$location', 'Restangular', SearchResultController]
    });

    function SearchResultController($scope, $state, $stateParams, $location, Restangular) {
        var vm = this;
        vm.recipe = {};
        vm.tags = '';
        vm.query = (isObjectEmpty($stateParams.query) ? '' : $stateParams.query);
        vm.loading = true;
        vm.comments = [];
        vm.recipeId = $stateParams.id;

        var parentScope = $scope.$parent;
        Restangular.one('recipes', vm.recipeId).get().then(function(recipe) {
            if (recipe === undefined) {
                $state.go('search', { 'query': vm.query });
            }
            vm.recipe = recipe;
            vm.tags = recipe.tags.join(', ');
            $scope.pdfUrl = '../services/recipes/pdf/' + recipe.id;

            parentScope.enableDownload(
                '../services/recipes/file/' + vm.recipe.id,
                '../services/recipes/pdf/' + vm.recipe.id);

            return Restangular.all('comments').getList({'recipe_id': vm.recipeId});
        }).then(function(comments) {
            vm.comments = comments;
        }).catch(function(err) {
            console.log("Error during search: " + err.data.code + "\n" + err.data.stacktrace);
        });

        vm.commentUpdated = function(comment) {
            Restangular
                .all('comments')
                .getList({'recipe_id': vm.recipeId})
                .then(function(comments) { vm.comments = comments; })
                .catch(function(err) { console.log("Error during search: " + err.data.code + "\n" + err.data.stacktrace); });
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
        $scope.$parent.enableCopyUrl($location.absUrl());
    }
})();
