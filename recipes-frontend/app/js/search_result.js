(function() {
    'use strict';

    var recipesControllers = angular.module('recipesControllers');
    recipesControllers.component('appSearchResult', {
        templateUrl: 'partials/search.result.html',
        controllerAs: 'vm',
        controller: ['$scope', '$stateParams', 'Restangular', function($scope, $stateParams, Restangular) {
            var vm = this;
            vm.recipe = {};
            vm.tags = '';

            Restangular.one('recipes', $stateParams.id).get().then(function(recipe) {
                vm.recipe = recipe;
                vm.tags = recipe.tags.join(', ');
                $scope.pdfUrl = '../services/recipes/pdf/' + recipe.id;
            });
        }]
    });
})();
