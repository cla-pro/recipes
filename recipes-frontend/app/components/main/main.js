(function() {
    'use strict';

    var recipesControllers = angular.module('recipesControllers');
    recipesControllers.controller('MainCtrl', ['$scope', '$transitions', '$state', '$aside', 'Restangular', MainController]);

    function MainController($scope, $transitions, $state, $aside, Restangular) {
        Restangular.setBaseUrl('../services');

        $scope.actionConfig = {
            displayBack: false,
            displayEdit: false,
            displayDownload: false
        };

        $scope.enableBack = function(onClickBack) {
            $scope.actionConfig.displayBack = true;
            $scope.actionConfig.onClickBack = onClickBack;
        };
        $scope.enableEdit = function(onClickEdit) {
            $scope.actionConfig.displayEdit = true;
            $scope.actionConfig.onClickEdit = onClickEdit;
        };
        $scope.enableDownload = function(url, urlPdf) {
            $scope.actionConfig.displayDownload = true;
            $scope.actionConfig.downloadUrl = url;
            $scope.actionConfig.downloadUrlPdf = urlPdf;
        };
        var scope = $scope;
        $transitions.onStart({},
            function(){
                scope.actionConfig.displayBack = false;
                scope.actionConfig.onClickBack = undefined;
                scope.actionConfig.displayEdit = false;
                scope.actionConfig.onClickEdit = undefined;
                scope.actionConfig.displayDownload = false;
                scope.actionConfig.downloadUrl = undefined;
                if (scope.actionConfig.hideAdditionalActions) {
                    scope.actionConfig.hideAdditionalActions();
                }
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
                controller: ['$scope', '$uibModalInstance', '$filter', function($scope, $uibModalInstance, $filter) {
                    $scope.menuElementList = [
                        {'route': 'search', 'html': 'Rechercher'},
                        {'route': 'insert', 'html': 'Insérer'}
                    ];

                    $scope.go = function(e, element) {
                        $uibModalInstance.dismiss();
                        e.stopPropagation();
                        $state.go(element.route);
                    };
                }]
            }).result.then(postClose, postClose);
        };
    }
})();
