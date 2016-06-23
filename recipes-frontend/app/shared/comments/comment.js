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

                vm._comment = $scope.comment;
                vm.recipeId = $scope.recipeId;
                vm.editingContent = '';
                vm.state = (vm._comment === undefined) ? 'EMPTY' : 'VIEWING';

                vm.isViewingState = function() { return vm.state === 'VIEWING'; };
                vm.isEditingState = function() { return vm.state === 'EDITING'; };
                vm.isEmptyState = function() { return vm.state === 'EMPTY'; };

                vm.addNewComment = function() { vm.startEditing(''); }
                vm.editComment = function() { vm.startEditing(vm._comment.content); }
                vm.startEditing = function(content) {
                  vm.state = 'EDITING';
                  vm.editingContent = content;
                };
                vm.cancelEditing = function() {
                    vm.state = (vm._comment === undefined) ? 'EMPTY' : 'VIEWING';
                    vm.editingContent = '';
                };

                var that = $scope;
                vm.saveComment = function() {
                    if (vm._comment === undefined) {
                        var commentToSave = {
                            content: vm.editingContent,
                            recipeId: vm.recipeId
                        };
                        $http.post('../services/comments', commentToSave)
                            .then(function(args) {
                                vm.state = 'EMPTY';
                                vm.editingContent = '';
                                that.commentUpdated(args);
                            });
                    } else {
                        vm._comment.content = vm.editingContent;
                        $http.put('../services/comments/' + vm._comment.id, {id: vm._comment.id, content: vm._comment.content, recipeId: vm._comment.recipeId})
                            .then(function(args) {
                                vm.state = 'VIEWING';
                                vm.editingContent = '';
                                that.commentUpdated(vm._comment);
                            });
                    }
                };
                vm.deleteComment = function() {
                    $http.delete('../services/comments/' + vm._comment.id)
                        .then(function() {
                            that.commentUpdated(vm._comment);
                        });
                };
            }],
            scope: {
                comment: '=',
                recipeId: '=',
                commentUpdated: '&commentUpdated'
            }
        };
    });
})();
