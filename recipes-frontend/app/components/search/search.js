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
                vm.chunkSize = 50;
                vm.hasMoreRecipes = false;

                $scope.$watch('vm.query', function(newValue, oldValue) {
                    vm.encodedQuery = encodeURIComponent(newValue);
                });

                vm.newSearch = function() {
                    vm.clearSearch();
                    vm.search();
                };
                vm.clearSearch = function() {
                    vm.recipes = [];
                };
                vm.search = function() {
                    vm.loading = true;
                    var params = {'filter': vm.query, 'size': vm.chunkSize};
                    if (vm.recipes.length > 0) {
                        params['chunkStart'] = vm.recipes[vm.recipes.length - 1].name;
                    }

                    Restangular.all('recipes').getList(params)
                        .then(function(found) {
                            found.forEach(function(elem) { vm.recipes.push(elem); });
                            vm.loading = false;
                            if (vm.recipes.length === 0) {
                                vm.hasMoreRecipes = false;
                                vm.message = 'Pas de recette trouvée';
                            } else {
                                vm.hasMoreRecipes = found.length == vm.chunkSize;
                                vm.message = '';
                            }
                        })
                        .catch(function(err) {
                            var data = err.data;
                            vm.loading = false;
                            vm.message = data.message;
                            console.log("Error during search: " + data.code + "\n" + data.stacktrace);
                        });
                };

                if (isObjectNotEmpty(vm.query)) {
                    $timeout(function() { vm.search(); }, 100);
                }
            }]
        };
    });
})();
