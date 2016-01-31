var recipesControllers = angular.module('recipesControllers', ['restangular', 'ui.bootstrap', 'ngAside', 'pdf', 'ngTagsInput', 'accents'])
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
            {'route': 'insert', 'html': 'Insérer'}
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
  $scope.filter = '';
  $scope.recipes = [];
  $scope.message = '';
  
  $scope.search = function() {
    Restangular.all('recipes').getList({'filter': $scope.filter}).then(function(recipes) {
      $scope.recipes = recipes;
      if ($scope.recipes.length == 0) {
        $scope.message = 'Pas de recette trouvée';
      } else {
        $scope.message = '';
      }
    });
  };
});

recipesControllers.controller('SearchResultCtrl', function($scope, $stateParams, Restangular) {
  $scope.recipe = {};
  $scope.tags = '';

  Restangular.one('recipes', $stateParams.id).get().then(function(recipe) {
    $scope.recipe = recipe;
    $scope.tags = recipe.tags.join(', ');
    $scope.pdfUrl = '../services/recipes/pdf/' + recipe.id;
  });
});

recipesControllers.controller('InsertCtrl', function($scope, $http, Restangular, $accents) {
  $scope.name = '';
  $scope.nameOverriden = false;
  $scope.tags = [];
  $scope.file = null;
  $scope.message = '';
  $scope.errorMessage = '';
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
    var lowerQuery = query.toLowerCase();
    var matchingTags = $scope.allTags.filter(function(element) {
        var lowerElement = element.name.toLowerCase();
        return lowerElement.search(lowerQuery) != -1 ||
                $accents.removeAccents(lowerElement).search($accents.removeAccents(lowerQuery)) != -1;
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
        content.tags = $scope.tags.map(function(e) { return e.text; });
    }

    var fd = new FormData();
    fd.append('recipe', angular.toJson(content));
    fd.append('file', file);

    $http.post('../services/recipes/file', fd, {
        transformRequest: angular.identity,
        headers: {'Content-Type': undefined}
    })
    .success(function(args) {
        $scope.name = '';
        $scope.nameOverriden = false;
        $scope.file = null;
        $scope.tags = '';
        document.getElementById('iptRecipeFile').value = '';
        $scope.setMessage('Recette enregistrée', '');
        $scope.loadAllTags();
    })
    .error(function() {
        $scope.setMessage('', 'Une erreur est survenue pendant l\'enregistrement de la recette');
    });
  };

  $scope.setMessage = function(msg, errorMsg) {
    $scope.message = msg;
    $scope.errorMessage = errorMsg;
  };
  $scope.isEmpty = function(obj) {
    return obj == undefined || obj == null || obj == '';
  }
});
