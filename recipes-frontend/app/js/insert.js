(function() {
    'use strict';

    var recipesControllers = angular.module('recipesControllers');
    recipesControllers.component('appInsert', {
        templateUrl: 'partials/insert.html',
        controllerAs: 'vm',
        controller: ['$scope', '$http', 'Restangular', '$accents', function($scope, $http, Restangular, $accents) {
            var vm = this;

            vm.name = '';
            vm.nameOverriden = false;
            vm.tags = [];
            $scope.file = undefined;
            vm.message = '';
            vm.isError = false;
            vm.allTags = [];

            vm.loadAllTags = function() {
                Restangular.all('tags').getList().then(function(allTags) {
                    vm.allTags = allTags;
                });
            };
            vm.loadAllTags();

            $scope.$watch('file', function(newValue, oldValue) {
                if (!$scope.nameOverriden && newValue !== undefined) {
                    var fullFilename = newValue.name;
                    var filename = fullFilename.substr(0, fullFilename.lastIndexOf('.')) || fullFilename;
                    vm.name = filename.replace(/_/g, ' ');
                }
            });

            vm.textChanged = function() {
                vm.nameOverriden = true;
            };

            vm.loadTags = function(query) {
                var lowerQuery = query.toLowerCase();
                var matchingTags = vm.allTags.filter(function(element) {
                    var lowerElement = element.name.toLowerCase();
                    return lowerElement.search(lowerQuery) !== -1 ||
                            $accents.removeAccents(lowerElement).search($accents.removeAccents(lowerQuery)) !== -1;
                });
                return matchingTags.map(function(e) {return e.name;});
            };

            vm.insert = function() {
                var file = $scope.file;

                if (vm.isEmpty(vm.name) || vm.isEmpty(file)) {
                    vm.setMessage('Le nom de la recette et le fichier sont obligatoires', true);
                    return;
                } else {
                    vm.setMessage(undefined, false);
                }

                var content = { name: vm.name, filename: file.name };
                if (vm.tags !== undefined && vm.tags !== null) {
                    content.tags = $scope.tags.map(function(e) { return e.text; });
                }

                var fd = new FormData();
                fd.append('recipe', angular.toJson(content));
                fd.append('file', file);

                $http.post('../services/recipes/file', fd, {
                    transformRequest: angular.identity,
                    headers: {'Content-Type': undefined}
                }).then(function(args) {
                    vm.name = '';
                    vm.nameOverriden = false;
                    $scope.file = undefined;
                    vm.tags = '';
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
