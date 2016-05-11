'use strict';

angular.module('mltradingApp')
    .controller('ArticleController', function ($scope, $http) {



        $scope.extractionArticles = function () {
            return $http.get('/api/extractionArticles').then(function (response) {
                return response.data;
            });
        }

        $scope.extractionArticle = function () {
            return $http.get('/api/extractionArticle').then(function (response) {
                return response.data;
            });
        }


    });
