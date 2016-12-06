(function() {
    'use strict';

    var recipesControllers = angular.module('recipesControllers');
    recipesControllers.component('appInsert', {
        templateUrl: 'components/insert/insert.html',
        controllerAs: 'vm',
        controller: ['$scope', '$http', 'Restangular', '$accents', '$timeout', '$tags', InsertController]
    });

    function InsertController($scope, $http, Restangular, $accents, $timeout, $tags) {
        var vm = this;

        vm.helpText = 'PDF, Word (docx), ODT, images';
        vm.loading = false;
        vm.name = '';
        vm.nameOverriden = false;
        vm.tags = [];
        vm.rating = 0;
        $scope.file = undefined;
        vm.message = '';
        vm.isError = false;
        vm.allTags = [];

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

        vm.findTags = function(query) { return $tags.findTags(query); };

        vm.save = function() {
            vm.loading = true;
            var file = $scope.file;

            if (vm.isEmpty(vm.name) || vm.isEmpty(file)) {
                vm.setMessage('Le nom de la recette et le fichier sont obligatoires', true);
                vm.loading = false;
                return;
            } else {
                vm.setMessage(undefined, false);
            }

            Restangular
                .all('recipes')
                .getList({'filter': vm.name})
                .then(function(found) {
                    if (found.filter(function(elem) { return elem.name === vm.name }).length > 0) {
                        vm.setMessage('Le nom est déjà utilisé par une autre recette', true);
                        vm.loading = false;
                    } else {
                        vm.sendInsertRequest(file);
                    }
                })
                .catch(function(err) {
                    var data = err.data;
                    vm.loading = false;
                    vm.setMessage('Erreur lors de l\'enregistrement: ' + data.message);
                });
        };

        vm.sendInsertRequest = function(file) {
            var content = { name: vm.name, filename: file.name, rating: vm.rating };
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
                vm.name = '';
                vm.nameOverriden = false;
                vm.tags = '';
                vm.rating = 0;
                $scope.file = undefined;
                document.getElementById('iptRecipeFile').value = '';
                vm.setMessage('Recette enregistrée', false);
                $tags.reloadTags();
                vm.loading = false;

                return $timeout(function() {
                    vm.setMessage('', false);
                }, 5000);
            }).catch(function(err) {
                var data = err.data;
                vm.loading = false;
                vm.setMessage('Une erreur est survenue pendant l\'enregistrement de la recette: ' + data.message, true);
                console.log("Error during insert: " + data.code + "\n" + data.stacktrace);
            });
        }

        vm.setMessage = function(msg, isError) {
            vm.message = msg;
            vm.isError = isError;
        };

        vm.isEmpty = function(obj) {
            return obj === undefined || obj === null || obj === '';
        };
    }
})();
