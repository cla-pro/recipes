var recipeControllers = angular.module('recipeControllers', ['restangular'])
  .config(function() {

  });

recipeControllers.controller('MainCtrl', function($scope, Restangular) {
  Restangular.setBaseUrl('http://localhost:9998/services');
});


recipeControllers.controller('SearchCtrl', function($scope, Restangular) {
  $scope.filter = "";
  $scope.recipes = [];
  
  $scope.search = function() {
    Restangular.all('recipes').getList({"filter": $scope.filter}).then(function(recipes) {
      $scope.recipes = recipes;
    });
  };
});
