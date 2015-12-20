var recipesApp = angular.module('recipesApp', ['ui.router', 'recipesControllers'])
.config(function($stateProvider, $urlRouterProvider) {
  //For any unmatched urls, redirect to the infos page
  $urlRouterProvider.otherwise('/search');
  $urlRouterProvider.when('', '/search');

  $stateProvider
    .state('search', {
      url: '/search',
      templateUrl: 'partials/search.html',
      controller: 'SearchCtrl'
    })
    .state('search_result', {
      url: '/search/:id',
      templateUrl: 'partials/search.result.html',
      controller: 'SearchResultCtrl'
    })
    .state('insert', {
      url: '/insert',
      templateUrl: 'partials/insert.html',
      controller: 'InsertCtrl'
    })
});

