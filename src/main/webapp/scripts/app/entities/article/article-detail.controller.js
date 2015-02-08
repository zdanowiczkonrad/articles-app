'use strict';

angular.module('jhipsterApp')
    .controller('ArticleDetailController', function ($scope, $stateParams, Article, Comment) {
        $scope.article = {};
        $scope.load = function (id) {
            Article.get({id: id}, function(result) {
              $scope.article = result;
            });
        };
        $scope.load($stateParams.id);
    });
