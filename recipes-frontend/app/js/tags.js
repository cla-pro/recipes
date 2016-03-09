(function() {
    'use strict';

    angular.module('tags', ['accents']).factory('$tags', ['Restangular', '$accents', function(Restangular, $accents) {
        var service = {
            tags: []
        };

        service.findTags = function(query) {
            var lowerQuery = query.toLowerCase();
            var matchingTags = service.tags.filter(function(element) {
                var lowerElement = element.name.toLowerCase();
                return lowerElement.search(lowerQuery) !== -1 ||
                        $accents.removeAccents(lowerElement).search($accents.removeAccents(lowerQuery)) !== -1;
            });
            return matchingTags.map(function(e) {return e.name;});
        };

        Restangular.all('tags').getList().then(function(allTags) {
            service.tags = allTags;
        });

        return service;
    }]);
})();
