'use strict';

angular.module('jhipsterApp')
    .factory('Article', function ($resource) {
        return $resource('api/articles/:id', {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    data = angular.fromJson(data);
                    var postedFrom = data.posted.split("-");
                    data.posted = new Date(new Date(postedFrom[0], postedFrom[1] - 1, postedFrom[2]));
                    return data;
                }
            }
        });
    });
