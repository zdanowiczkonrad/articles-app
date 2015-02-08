'use strict';

angular.module('jhipsterApp')
    .controller('ArticleController', function ($scope, Article, Comment) {
        $scope.articles = [];
        $scope.comments = Comment.query();
        $scope.loadAll = function() {
            Article.query(function(result) {
               $scope.articles = result;
            });
        };
        $scope.loadAll();

        $scope.create = function () {
            Article.save($scope.article,
                function () {
                    $scope.loadAll();
                    $('#saveArticleModal').modal('hide');
                    $scope.clear();
                });
        };

        $scope.update = function (id) {
            $scope.article = Article.get({id: id});
            $('#saveArticleModal').modal('show');
        };

        $scope.delete = function (id) {
            $scope.article = Article.get({id: id});
            $('#deleteArticleConfirmation').modal('show');
        };

        $scope.confirmDelete = function (id) {
            Article.delete({id: id},
                function () {
                    $scope.loadAll();
                    $('#deleteArticleConfirmation').modal('hide');
                    $scope.clear();
                });
        };

        $scope.clear = function () {
            $scope.article = {title: null, content: null, posted: null, voteCount: null, averageVote: null, author: null, id: null};
        };
    });
