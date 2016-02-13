(function() {
    'use strict';

    var recipesControllers = angular.module('recipesControllers');
    recipesControllers.component('appEdit', {
        templateUrl: 'partials/edit.html',
        controllerAs: 'vm',
        controller: ['$scope', '$stateParams', '$http', 'Restangular', '$accents',
                function($scope, $stateParams, $http, Restangular, $accents) {
            var vm = this;

            vm.id = undefined;
            vm.name = undefined
            vm.tags = [];
            $scope.file = undefined;
            vm.message = '';
            vm.isError = false;
            vm.allTags = [];

            Restangular.one('recipes', $stateParams.id).get().then(function(recipe) {
                vm.id = recipe.id;
                vm.name = recipe.name;
                vm.tags = recipe.tags.map(function(t) { return { text: t };});
            });

            vm.loadAllTags = function() {
                Restangular.all('tags').getList().then(function(allTags) {
                    vm.allTags = allTags;
                });
            };
            vm.loadAllTags();

            vm.loadTags = function(query) {
                var lowerQuery = query.toLowerCase();
                var matchingTags = vm.allTags.filter(function(element) {
                    var lowerElement = element.name.toLowerCase();
                    return lowerElement.search(lowerQuery) !== -1 ||
                            $accents.removeAccents(lowerElement).search($accents.removeAccents(lowerQuery)) !== -1;
                });
                return matchingTags.map(function(e) {return e.name;});
            };

            vm.save = function() {
                var file = $scope.file;
                vm.setMessage(undefined, false);

                var fd = new FormData();
                var content = { id: vm.id, name: vm.name };
                if (vm.isEmpty(vm.name)) {
                    vm.setMessage('Le nom de la recette est obligatoires', true);
                    return;
                }

                if (file !== undefined) {
                    content.filename = file.name;
                    fd.append('file', file);
                }

                if (vm.tags !== undefined && vm.tags !== null) {
                    content.tags = vm.tags.map(function(e) { return e.text; });
                }
                fd.append('recipe', angular.toJson(content));

                $http.put('../services/recipes', fd, {
                    transformRequest: angular.identity,
                    headers: {'Content-Type': undefined}
                }).then(function(args) {
                    $scope.file = undefined;
                    document.getElementById('iptRecipeFile').value = '';
                    vm.setMessage('Recette enregistrée', false);
                    vm.loadAllTags();
                }).catch(function() {
                    vm.setMessage('Une erreur est survenue pendant l\'enregistrement de la recette', true);
                });
            };

            vm.setMessage = function(msg, isError) {
                vm.message = msg;
                vm.isError = isError;
            };

            vm.isEmpty = function(obj) {
                return obj === undefined || obj === null || obj === '';
            };
        }]
    });
})();
