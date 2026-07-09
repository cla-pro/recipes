(function() {
    'use strict';

    var recipesApp = angular.module('recipesApp', ['ui.router', 'recipesControllers'])
    .config(['$stateProvider', '$urlRouterProvider', function($stateProvider, $urlRouterProvider) {
        //For any unmatched urls, redirect to the infos page
        $urlRouterProvider.otherwise('/search');
        $urlRouterProvider.when('', '/search');

        $stateProvider
        .state('search', {
            url: '/search?query',
            template: '<app-search></app-search>',
        })
        .state('search_result', {
            url: '/search/:id?query',
            template: '<app-search-result></app-search-result>',
        })
        .state('insert', {
            url: '/insert',
            template: '<app-insert></app-insert>',
        })
        .state('edit', {
            url: '/search/:id/edit?query',
            template: '<app-edit></app-edit>',
        });
    }]);
})();
