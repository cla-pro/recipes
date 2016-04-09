(function() {
    'use strict';

    var recipesControllers = angular.module('recipesControllers');
    recipesControllers.directive('appEdit', function() {
        return {
            restrict: 'E',
            templateUrl: 'components/edit/edit.html',
            controllerAs: 'vm',
            controller: ['$scope', '$state', '$stateParams', '$http', 'Restangular', '$accents', '$timeout', '$tags',
                    function($scope, $state, $stateParams, $http, Restangular, $accents, $timeout, $tags) {
                var vm = this;

                vm.loading = false;
                vm.id = undefined;
                vm.name = undefined
                vm.tags = [];
                $scope.file = undefined;
                vm.message = '';
                vm.isError = false;
                //vm.allTags = [];
                vm.query = (isObjectEmpty($stateParams.query) ? '' : $stateParams.query);

                Restangular.one('recipes', $stateParams.id).get().then(function(recipe) {
                    vm.id = recipe.id;
                    vm.name = recipe.name;
                    vm.tags = recipe.tags.map(function(t) { return { text: t };});
                });

                vm.findTags = function(query) { return $tags.findTags(query); };

                vm.save = function() {
                    vm.loading = true;
                    var file = $scope.file;
                    vm.setMessage(undefined, false);

                    var fd = new FormData();
                    var content = { id: vm.id, name: vm.name };
                    if (isObjectEmpty(vm.name)) {
                        vm.setMessage('Le nom de la recette est obligatoires', true);
                        return;
                    }

                    if (file !== undefined) {
                        content.filename = file.name;
                        fd.append('file', file);
                    }

                    if (isObjectNotEmpty(vm.tags)) { //vm.tags !== undefined && vm.tags !== null) {
                        content.tags = vm.tags.map(function(e) { return e.text; });
                    }
                    fd.append('recipe', angular.toJson(content));

                    $http.put('../services/recipes', fd, {
                        transformRequest: angular.identity,
                        headers: {'Content-Type': undefined}
                    }).then(function(args) {
                        vm.loading = false;
                        $scope.file = undefined;
                        document.getElementById('iptRecipeFile').value = '';
                        vm.setMessage('Recette enregistrée', false);
                        vm.loadAllTags();

                        return $timeout(function() {
                            vm.setMessage('', false);
                        }, 5000);
                    }).catch(function() {
                        vm.loading = false;
                        vm.setMessage('Une erreur est survenue pendant l\'enregistrement de la recette', true);
                    });
                };

                vm.setMessage = function(msg, isError) {
                    vm.message = msg;
                    vm.isError = isError;
                };

                $scope.back = function() {
                    $state.go('search_result', { 'id': $stateParams.id, 'query': vm.query });
                };
            }]
        };
    });
})();
