(function() {
    'use strict';

    var recipesControllers = angular.module('recipesControllers');
    recipesControllers.directive('appComment', function() {
        return {
            restrict: 'E',
            templateUrl: 'shared/comments/comment.html',
            transclude: true,
            controllerAs: 'vm',
            controller: ['$scope', '$http', function($scope, $http) {
                var vm = this;

                vm.comment = $scope.comment;
                vm.recipeId = $scope.recipeId;
                vm.editingContent = '';
                vm.state = (vm.comment === undefined) ? 'EMPTY' : 'VIEWING';

                vm.isViewingState = function() { return vm.state === 'VIEWING'; };
                vm.isEditingState = function() { return vm.state === 'EDITING'; };
                vm.isEmptyState = function() { return vm.state === 'EMPTY'; };

                vm.addNewComment = function() { vm.startEditing(''); }
                vm.editComment = function() { vm.startEditing(vm.comment.content); }
                vm.startEditing = function(content) {
                  vm.state = 'EDITING';
                  vm.editingContent = content;
                };
                vm.cancelEditing = function() {
                    vm.state = (vm.comment === undefined) ? 'EMPTY' : 'VIEWING';
                    vm.editingContent = '';
                };

                var that = $scope;
                vm.saveComment = function() {
                    var commentToSave = {
                        content: vm.editingContent,
                        recipeId: vm.recipeId
                    };

                    $http.post('../services/comments', commentToSave)
                        .then(function(args) {
                            vm.state = 'EMPTY';
                            vm.editingContent = '';
                            that.commentAdded(args);
                        });
                };
            }],
            scope: {
                comment: '=',
                recipeId: '=',
                commentAdded: '&commentAdded'
            }
        };
    });
})();
