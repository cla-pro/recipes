(function() {
    'use strict';

    var recipesControllers = angular.module('recipesControllers');
    recipesControllers.component('appResultList', {
        templateUrl: 'partials/result_list.html',
        controllerAs: 'vm',
        bindings: {
            results: '=',
            message: '='
        }
    });
})();

