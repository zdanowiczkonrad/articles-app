'use strict';

angular.module('jhipsterApp')
    .controller('CommentDetailController', function ($scope, $stateParams, Comment, Article) {
        $scope.comment = {};
        $scope.load = function (id) {
            Comment.get({id: id}, function(result) {
              $scope.comment = result;
            });
        };
        $scope.load($stateParams.id);
    });
