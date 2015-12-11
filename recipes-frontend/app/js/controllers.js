var recipesControllers = angular.module('recipesControllers', ['restangular'])
  .config(function() {

  });

recipesControllers.directive('fileModel', ['$parse', function ($parse) {
  return {
    restrict: 'A',
    link: function(scope, element, attrs) {
      var model = $parse(attrs.fileModel);
      var modelSetter = model.assign;

      element.bind('change', function(){
        scope.$apply(function(){
          modelSetter(scope, element[0].files[0]);
        });
      });
    }
  };
}]);
  
recipesControllers.service('fileUpload', ['$http', function ($http) {
  this.uploadFileToUrl = function(file, uploadUrl){
    var fd = new FormData();
    fd.append('file', file);
    $http.post(uploadUrl, fd, {
        transformRequest: angular.identity,
        headers: {'Content-Type': undefined}
    })
    .success(function(){
    })
    .error(function(){
    });
  }
}]);


recipesControllers.controller('MainCtrl', function($scope, Restangular) {
  Restangular.setBaseUrl('http://localhost:9998/services');
});

recipesControllers.controller('SearchCtrl', function($scope, Restangular) {
  $scope.filter = "";
  $scope.recipes = [];
  
  $scope.search = function() {
    Restangular.all('recipes').getList({"filter": $scope.filter}).then(function(recipes) {
      $scope.recipes = recipes;
    });
  };
});

recipesControllers.controller('InsertCtrl', function($scope, Restangular, fileUpload) {
  $scope.name = "";
  $scope.file = null;

  $scope.insert = function() {
    var file = $scope.file;
    console.log('file is ' );
    console.dir(file);
    var uploadUrl = "/fileUpload";
    fileUpload.uploadFileToUrl(file, 'http://localhost:9998/services/recipes/file');

    /*Restangular.all('recipes').post('recipes', {name: $scope.name}).then(
      function (postedRecipe) {
        console.log('posted with id: ' + postedRecipe.id);
      }
    );*/
  };
});
