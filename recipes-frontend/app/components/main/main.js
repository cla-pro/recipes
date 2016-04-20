(function() {
    'use strict';

    var recipesControllers = angular.module('recipesControllers');
    recipesControllers.controller('MainCtrl', ['$scope', '$rootScope', '$state', '$aside', 'Restangular',
        function($scope, $rootScope, $state, $aside, Restangular) {
            Restangular.setBaseUrl('../services');

            $scope.displayBack = false;
            $scope.displayEdit = false;

            $scope.enableBack = function(onClickBack) {
                $scope.displayBack = true;
                $scope.onClickBack = onClickBack;
            };
            $scope.enableEdit = function(onClickEdit) {
                $scope.displayEdit = true;
                $scope.onClickEdit = onClickEdit;
            };
            var scope = $scope;
            $rootScope.$on('$stateChangeStart',
                function(){
                    scope.displayBack = false;
                    scope.onClickBack = undefined;
                    scope.displayEdit = false;
                    scope.onClickEdit = undefined;
                });

            $scope.asideState = {
                open: false
            };

            $scope.openAside = function() {
                $scope.asideState = {
                    open: true,
                };

                function postClose() {
                    $scope.asideState.open = false;
                }

                $aside.open({
                    templateUrl: 'components/main/menu.html',
                    placement: 'left',
                    size: 'sm',
                    animation: true,
                    controller: ['$scope', '$modalInstance', '$filter', function($scope, $modalInstance, $filter) {
                        $scope.menuElementList = [
                            {'route': 'search', 'html': 'Rechercher'},
                            {'route': 'insert', 'html': 'Insérer'}
                        ];

                        $scope.go = function(e, element) {
                            $modalInstance.dismiss();
                            e.stopPropagation();
                            $state.go(element.route);
                        };
                    }]
                }).result.then(postClose, postClose);
            };
        }]);
})();
