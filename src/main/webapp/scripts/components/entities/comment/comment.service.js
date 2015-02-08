'use strict';

angular.module('jhipsterApp')
    .factory('Comment', function ($resource) {
        return $resource('api/comments/:id', {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    data = angular.fromJson(data);
                    var dateFrom = data.date.split("-");
                    data.date = new Date(new Date(dateFrom[0], dateFrom[1] - 1, dateFrom[2]));
                    return data;
                }
            }
        });
    });
