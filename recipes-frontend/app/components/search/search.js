(function() {
    'use strict';

    var recipesControllers = angular.module('recipesControllers');
    recipesControllers.directive('appSearch', function() {
        return {
            restrict: 'E',
            templateUrl: 'components/search/search.html',
            controllerAs: 'vm',
            controller: ['$scope', '$stateParams', 'Restangular', '$timeout', function($scope, $stateParams, Restangular, $timeout) {
                var vm = this;
                vm.helpText = 'AND (par défaut quand rien n\' indiqué) et OR pour coupler les critères, ' +
                    'NOT pour inverser et (...) pour grouper';
                vm.query = (isObjectEmpty($stateParams.query) ? '' : decodeURIComponent($stateParams.query));
                vm.encodedQuery = encodeURIComponent(vm.query);
                vm.recipes = [];
                vm.message = '';
                vm.loading = false;

                $scope.$watch('vm.query', function(newValue, oldValue) {
                    vm.encodedQuery = encodeURIComponent(newValue);
                });

                vm.search = function() {
                    vm.loading = true;
                    Restangular.all('recipes').getList({'filter': vm.query})
                        .then(function(recipes) {
                            vm.recipes = recipes;
                            vm.loading = false;
                            if (vm.recipes.length === 0) {
                                vm.message = 'Pas de recette trouvée';
                            } else {
                                vm.message = '';
                            }
                        })
                        .catch(function() {
                            vm.loading = false;
                            vm.message = 'Erreur lors de la requete';
                        });
                };

                if (isObjectNotEmpty(vm.query)) {
                    $timeout(function() { vm.search(); }, 100);
                }
            }]
        };
    });
})();
