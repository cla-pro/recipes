angular.module('recipeApp', ['ui.router', 'recipeControllers', 'hc.marked'])
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
    /*.state('infos_detail', {
      url: '/infos/:id',
      templateUrl: 'partials/infos.detail.html',
      controller: 'InfosDetailCtrl'
    })*/
    .state('update', {
      url: '/update',
      templateUrl: 'partials/update.html',
      controller: 'UpdateCtrl'
    })
});
