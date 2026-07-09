(function() {
    'use strict';
    
    angular.module('accents', []).factory('$accents', function() {
        var service = {
        };

        service.removeAccents = function(s) {
            s = s.replace(/[áàäâ]/g, 'a');
            s = s.replace(/[ç]/g, 'c');
            s = s.replace(/[éèëê]/g, 'e');
            s = s.replace(/[íìïî]/g, 'i');
            s = s.replace(/[óòöô]/g, 'o');
            s = s.replace(/[úùüû]/g, 'u');
            return s;
        };

        return service;
    });
})();