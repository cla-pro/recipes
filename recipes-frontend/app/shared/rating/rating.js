(function() {
    'use strict';

    var recipesControllers = angular.module('recipesControllers');
    recipesControllers.directive('appStarRating', function() {
        return {
            restrict: 'E',
            templateUrl: 'shared/rating/rating.html',
            scope: {
                value: '=myRating',
                readOnly: '@',
                updateRating: '&'
            },
            controllerAs: 'vm',
            controller: ['$scope', function($scope) {
                var vm = this;
                vm.max = $scope.readOnly == 'true' ? $scope.value : 3;
                vm.stars = [];

                $scope.$watch('value', function(newValue, oldValue) {
                    vm.updateStars(newValue);
                });

                vm.imageUrl = function(filled) {
                    return filled ? 'star_gold_20.png' : 'star_black_20.png';
                };

                vm.updateStars = function(value) {
                    for (var i = 0; i < vm.max; i++) {
                        vm.stars[i].filled = i < value;
                        vm.stars[i].image = vm.imageUrl(i < value);
                    }
                };
                vm.resetRating = function() {
                    vm.updateStars($scope.value);
                };
                vm.starMouseHover = function(index) {
                    if ($scope.readOnly == 'false') {
                        vm.updateStars(index);
                    }
                };
                vm.starMouseLeave = function() {
                    if ($scope.readOnly == 'false') {
                        vm.resetRating();
                    }
                };
                vm.starClick = function(index) {
                    if ($scope.readOnly == 'false') {
                        $scope.value = index;
                        //$scope.updateRating({value: index});
                        vm.resetRating();
                    }
                };

                for (var i = 0; i < vm.max; i++) {
                    vm.stars.push({
                        index: i + 1
                    });
                }
                vm.resetRating();
            }]
        };
    });
})();
