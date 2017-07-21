(function() {
    'use strict';

    var recipesControllers = angular.module('recipesControllers');
    recipesControllers.component('appEdit', {
        templateUrl: 'components/edit/edit.html',
        controllerAs: 'vm',
        controller: ['$scope', '$state', '$stateParams', '$http', 'Restangular', '$accents', '$timeout', '$tags', '$mdDialog', EditController]
    });

    function EditController($scope, $state, $stateParams, $http, Restangular, $accents, $timeout, $tags, $mdDialog) {
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
                $scope.file = undefined;
                document.getElementById('iptRecipeFile').value = '';

                $tags.reloadTags();
                vm.loading = false;

                vm.back();
            }).catch(function(err) {
                var data = err.data;
                vm.loading = false;
                vm.setMessage('Une erreur est survenue pendant l\'enregistrement de la recette: ' + data.message, true);
                console.log("Error during edit: " + data.code + "\n" + data.stacktrace);
            });
        };
        vm.deleteRecipe = function(ev) {
            var confirm = $mdDialog
                .confirm()
                .title('Supprimer?')
                .textContent('Etes vous sûr de vouloir supprimer cette recette?')
                .ariaLabel('Lucky day')
                .targetEvent(ev)
                .ok('Oui')
                .cancel('Non');

            $mdDialog.show(confirm).then(function(result) {
                console.log('deleting the recipe with id = ' + vm.id);
                $http.delete('../services/recipes/' + vm.id);
                $state.go('search', { 'query': vm.query });
            }, function() {});
        };

        vm.setMessage = function(msg, isError) {
            vm.message = msg;
            vm.isError = isError;
        };

        vm.back = function() {
            $state.go('search_result', { 'id': $stateParams.id, 'query': vm.query });
        };
        $scope.$parent.enableBack(vm.back);
    }
})();
