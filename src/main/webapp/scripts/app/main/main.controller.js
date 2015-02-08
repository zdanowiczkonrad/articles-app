'use strict';

angular.module('jhipsterApp')
    .controller('MainController', function ($scope, Principal, Article, Comment) {
        Principal.identity().then(function(account) {
            $scope.account = account;
            $scope.isAuthenticated = Principal.isAuthenticated;
        });

        $scope.articles = [];
        $scope.comments = [];
        $scope.newComment = {};
        $scope.loadComments = function() {
        	  Comment.query(function(result){
	        	console.log(result)
	        	$scope.comments = result;
	        })
        	};
        $scope.loadData = function() {
	        Article.query(function(result){
	        	$scope.articles = result;
	        	console.log(result);
	        });
		}
		
		$scope.loadData();
		$scope.loadComments();
		$scope.addComment = function(articleId) {
			var newComment = new Comment({title:$scope.newComment.title,
				content:$scope.newComment.content,date:new Date(),article:{id:articleId}});
			newComment.$save(function(){$scope.loadComments()});
		}

    });
