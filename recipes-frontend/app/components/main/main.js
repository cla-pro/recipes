(function() {
    'use strict';

    var recipesControllers = angular.module('recipesControllers');
    recipesControllers.controller('MainCtrl', ['$scope', '$state', '$aside', 'Restangular', function($scope, $state, $aside, Restangular) {
        Restangular.setBaseUrl('../services');

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
