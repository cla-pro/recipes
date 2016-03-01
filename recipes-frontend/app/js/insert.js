(function() {
    'use strict';

    var recipesControllers = angular.module('recipesControllers');
    recipesControllers.directive('appInsert', function() {
        return {
            restrict: 'E',
            templateUrl: 'partials/insert.html',
            controllerAs: 'vm',
            controller: ['$scope', '$http', 'Restangular', '$accents', '$timeout',
                    function($scope, $http, Restangular, $accents, $timeout) {
                var vm = this;

                vm.loading = false;
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

                vm.save = function() {
                    vm.loading = true;
                    var file = $scope.file;

                    if (vm.isEmpty(vm.name) || vm.isEmpty(file)) {
                        vm.setMessage('Le nom de la recette et le fichier sont obligatoires', true);
                        return;
                    } else {
                        vm.setMessage(undefined, false);
                    }

                    var content = { name: vm.name, filename: file.name };
                    if (vm.tags !== undefined && vm.tags !== null) {
                        content.tags = vm.tags.map(function(e) { return e.text; });
                    }

                    var fd = new FormData();
                    fd.append('recipe', angular.toJson(content));
                    fd.append('file', file);

                    $http.post('../services/recipes/file', fd, {
                        transformRequest: angular.identity,
                        headers: {'Content-Type': undefined}
                    }).then(function(args) {
                        vm.loading = false;
                        vm.name = '';
                        vm.nameOverriden = false;
                        vm.tags = '';
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

                vm.isEmpty = function(obj) {
                    return obj === undefined || obj === null || obj === '';
                };
            }]
        };
    });
})();
