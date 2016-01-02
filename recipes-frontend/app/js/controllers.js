var recipesControllers = angular.module('recipesControllers', ['restangular', 'ui.bootstrap', 'ngAside', 'pdf'])
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

recipesControllers.controller('MainCtrl', function($scope, $state, $aside, Restangular) {
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
        templateUrl: 'partials/menu.html',
        placement: 'left',
        size: 'sm',
        animation: true,
        controller: function($scope, $modalInstance, $filter) {
          $scope.menuElementList = [
            {'route': 'search', 'html': 'Rechercher'},
            {'route': 'insert', 'html': 'Inserer'}
          ];
          
          $scope.go = function(e, element) {
            $modalInstance.dismiss();
            e.stopPropagation();
            $state.go(element.route);
          }
        }
      }).result.then(postClose, postClose);
    }
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

recipesControllers.controller('SearchResultCtrl', function($scope, $stateParams, Restangular) {
  $scope.recipe = {};

  Restangular.one('recipes', $stateParams.id).get().then(function(recipe) {
    $scope.recipe = recipe;
    $scope.pdfUrl = '../services/recipes/pdf/' + recipe.id;
  });
});

recipesControllers.controller('InsertCtrl', function($scope, Restangular, fileUpload) {
  $scope.name = "";
  $scope.nameOverriden = false;
  $scope.tages = "";
  $scope.file = null;

  $scope.$watch('file', function(newValue, oldValue) {
    if (!$scope.nameOverriden && newValue != null) {
      var fullFilename = newValue.name;
      var filename = fullFilename.substr(0, fullFilename.lastIndexOf('.')) || fullFilename;
      $scope.name = filename.replace(/_/g, ' ');
    }
  });
  $scope.textChanged = function() {
    $scope.nameOverriden = true;
  };
  $scope.insert = function() {
    var file = $scope.file;

    var content = { name: $scope.name, filename: file.name };
    if ($scope.tags != undefined && $scope.tags != null) {
        content.tags = $scope.tags.split(' ');
    }
    Restangular.all('recipes').customPOST(content).then(
      function (postedRecipe) {
        fileUpload.uploadFileToUrl(file, '../services/recipes/file/' + postedRecipe.id);
      }
    );
  };
})