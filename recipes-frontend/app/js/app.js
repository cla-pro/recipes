var recipesApp = angular.module('recipesApp', ['ui.router', 'recipesControllers'])
.config(function($stateProvider, $urlRouterProvider) {
  //For any unmatched urls, redirect to the infos page
  $urlRouterProvider.otherwise('/insert');
  $urlRouterProvider.when('', '/insert');

  $stateProvider
    .state('search', {
      url: '/search',
      templateUrl: 'partials/search.html',
      controller: 'SearchCtrl'
    })
    /*.state('infos_detail', {
      url: '/infos/:id',
      templateUrl: 'partials/infos.detail.html',
      controller: 'InfosDetailCtrl'
    })*/
    .state('insert', {
      url: '/insert',
      templateUrl: 'partials/insert.html',
      controller: 'InsertCtrl'
    })
    /*.state('update', {
      url: '/update',
      templateUrl: 'partials/update.html',
      controller: 'UpdateCtrl'
    })*/
});

