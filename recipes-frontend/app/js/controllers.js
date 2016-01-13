var recipesControllers = angular.module('recipesControllers', ['restangular', 'ui.bootstrap', 'ngAside', 'pdf', 'ngTagsInput'])
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
  this.uploadFileToUrl = function(file, uploadUrl, success, failure, context){
    var fd = new FormData();
    fd.append('file', file);
    $http.post(uploadUrl, fd, {
        transformRequest: angular.identity,
        headers: {'Content-Type': undefined}
    })
    .success(function(args) {
      if (success != undefined) {
        success.apply(context, []);
      }
    })
    .error(function() {
      if (failure != undefined) {
        failure.apply(context, []);
      }
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
  $scope.tags = [];
  $scope.file = null;
  $scope.message = "";
  $scope.errorMessage = "";
  $scope.allTags = [];

  $scope.loadAllTags = function() {
      Restangular.all('tags').getList().then(function(allTags) {
        $scope.allTags = allTags;
      });
  };
  $scope.loadAllTags();

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
  $scope.loadTags = function(query) {
    var matchingTags = $scope.allTags.filter(function(element) {
        return element.name.search(query.toLowerCase()) != -1;
    });
    return matchingTags.map(function(e) {return e.name;});
  };
  $scope.insert = function() {
    var file = $scope.file;

    if ($scope.isEmpty($scope.name) || $scope.isEmpty($scope.file)) {
        $scope.setMessage('', 'Le nom de la recette et le fichier sont obligatoires');
        return;
    } else {
        $scope.setMessage('', '');
    }

    var content = { name: $scope.name, filename: file.name };
    if ($scope.tags != undefined && $scope.tags != null) {
        content.tags = $scope.tags.map(function(e) {return e.text;});
    }
    Restangular.all('recipes').customPOST(content).then(
      function (postedRecipe) {
        fileUpload.uploadFileToUrl(file, '../services/recipes/file/' + postedRecipe.id,
          function() {
            $scope.name = '';
            $scope.nameOverriden = false;
            $scope.file = null;
            $scope.tags = '';
            document.getElementById('iptRecipeFile').value = '';
            $scope.setMessage('Recette enregistrée', '');
            $scope.loadAllTags();
          },
          function() {
            $scope.setMessage('', 'Une erreur est survenue pendant l\'enregistrement du fichier');
          });
      },
      function (args) {
        $scope.setMessage('', 'Une erreur est survenue pendant l\'enregistrement de la recette');
      }
    );
  };

  $scope.setMessage = function(msg, errorMsg) {
    $scope.message = msg;
    $scope.errorMessage = errorMsg;
  };
  $scope.isEmpty = function(obj) {
    return obj == undefined || obj == null || obj == '';
  }
})