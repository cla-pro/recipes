(function() {
    'use strict';

    var recipesControllers = angular.module('recipesControllers');
    recipesControllers.directive('appSearch', function() {
        return {
            restrict: 'E',
            templateUrl: 'partials/search.html',
            controllerAs: 'vm',
            controller: ['$stateParams', 'Restangular', '$timeout', function($stateParams, Restangular, $timeout) {
                var vm = this;
                vm.query = (isObjectEmpty($stateParams.query) ? '' : $stateParams.query);
                vm.recipes = [];
                vm.message = '';

                vm.search = function() {
                    Restangular.all('recipes').getList({'filter': vm.query}).then(function(recipes) {
                        vm.recipes = recipes;
                        if (vm.recipes.length === 0) {
                            vm.message = 'Pas de recette trouvée';
                        } else {
                            vm.message = '';
                        }
                    });
                };

                $timeout(function() { vm.search(); }, 100);
            }]
        };
    });
})();
