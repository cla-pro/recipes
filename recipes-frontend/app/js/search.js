(function() {
    'use strict';

    var recipesControllers = angular.module('recipesControllers');
    recipesControllers.directive('appSearch', function() {
        return {
            restrict: 'E',
            templateUrl: 'partials/search.html',
            controllerAs: 'vm',
            controller: ['Restangular', function(Restangular) {
                var vm = this;
                vm.filter = '';
                vm.recipes = [];
                vm.message = '';

                vm.search = function() {
                    Restangular.all('recipes').getList({'filter': vm.filter}).then(function(recipes) {
                        vm.recipes = recipes;
                        if (vm.recipes.length === 0) {
                            vm.message = 'Pas de recette trouvée';
                        } else {
                            vm.message = '';
                        }
                    });
                };
            }]
        };
    });
})();
