'use strict';

angular.module('jhipsterApp')
    .controller('CommentController', function ($scope, Comment, Article) {
        $scope.comments = [];
        $scope.articles = Article.query();
        $scope.loadAll = function() {
            Comment.query(function(result) {
               $scope.comments = result;
            });
        };
        $scope.loadAll();

        $scope.create = function () {
            Comment.save($scope.comment,
                function () {
                    $scope.loadAll();
                    $('#saveCommentModal').modal('hide');
                    $scope.clear();
                });
        };

        $scope.update = function (id) {
            $scope.comment = Comment.get({id: id});
            $('#saveCommentModal').modal('show');
        };

        $scope.delete = function (id) {
            $scope.comment = Comment.get({id: id});
            $('#deleteCommentConfirmation').modal('show');
        };

        $scope.confirmDelete = function (id) {
            Comment.delete({id: id},
                function () {
                    $scope.loadAll();
                    $('#deleteCommentConfirmation').modal('hide');
                    $scope.clear();
                });
        };

        $scope.clear = function () {
            $scope.comment = {author: null, mail: null, title: null, content: null, date: null, upvotes: null, id: null};
        };
    });
