(function() {
    'use strict';

    var recipesControllers = angular.module('recipesControllers');
    recipesControllers.component('appMessage', {
        templateUrl: 'partials/message.html',
        controllerAs: 'vm',
        bindings: {
            message: '=',
            isError: '='
        }
    });
})();

