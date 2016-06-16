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

                vm.helpText = 'PDF, Word (docx), ODT, images';
                vm.loading = false;
                vm.id = undefined;
                vm.name = undefined
                vm.tags = [];
                vm.rating = 0;
                $scope.file = undefined;
                vm.message = '';
                vm.isError = false;
                //vm.allTags = [];
                vm.query = (isObjectEmpty($stateParams.query) ? '' : $stateParams.query);

                Restangular.one('recipes', $stateParams.id).get().then(function(recipe) {
                    vm.id = recipe.id;
                    vm.name = recipe.name;
                    vm.tags = recipe.tags.map(function(t) { return { text: t };});
                    vm.rating = recipe.rating;
                });

                vm.findTags = function(query) { return $tags.findTags(query); };

                vm.save = function() {
                    vm.loading = true;
                    var file = $scope.file;
                    vm.setMessage(undefined, false);

                    var fd = new FormData();
                    var content = { id: vm.id, name: vm.name, rating: vm.rating };
                    if (isObjectEmpty(vm.name)) {
                        vm.setMessage('Le nom de la recette est obligatoires', true);
                        return;
                    }

                    if (file !== undefined) {
                        content.filename = file.name;
                        fd.append('file', file);
                    }

                    if (isObjectNotEmpty(vm.tags)) {
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

                        $tags.reloadTags();

                        vm.back();
                    }).catch(function() {
                        vm.loading = false;
                        vm.setMessage('Une erreur est survenue pendant l\'enregistrement de la recette', true);
                    });
                };

                vm.setMessage = function(msg, isError) {
                    vm.message = msg;
                    vm.isError = isError;
                };

                vm.back = function() {
                    $state.go('search_result', { 'id': $stateParams.id, 'query': vm.query });
                };
                $scope.$parent.enableBack(vm.back);
            }]
        };
    });
})();
