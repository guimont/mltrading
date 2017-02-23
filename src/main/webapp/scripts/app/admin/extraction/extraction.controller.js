'use strict';

angular.module('mltradingApp')
    .controller('ExtractionController', function ($scope, $http) {

        $scope.extraction = function () {
            return $http.get('/api/extractionAction').then(function (response) {
                return response.data;
            });
        }

        $scope.extractSeriesFull = function () {
            return $http.get('/api/extractionSeries').then(function (response) {
                return response.data;
            });
        }

        $scope.extractionSpecific = function () {
            return $http.get('/api/extractionSpecific').then(function (response) {
                return response.data;
            });
        }


        $scope.extractSeriesDailly = function () {
            return $http.get('/api/extractionSeriesDailly').then(function (response) {
                return response.data;
            });
        }

        $scope.extractSeriesWeekly = function () {
            return $http.get('/api/extractionSeriesWeekly').then(function (response) {
                return response.data;
            });
        }


        $scope.extractProcessAT = function () {
            return $http.get('/api/processAT').then(function (response) {
                return response.data;
            });
        }


        $scope.extractProcessVCac = function () {
            return $http.get('/api/extractionVCac').then(function (response) {
                return response.data;
            });
        }



        $scope.extractSeriesRaw = function () {
            return $http.get('/api/extractionRaw').then(function (response) {
                return response.data;
            });
        }

        $scope.extractSeriesSector = function () {
            return $http.get('/api/extractionSector').then(function (response) {
                return response.data;
            });
        }

        $scope.extractSeriesIndice = function () {
            return $http.get('/api/extractionIndice').then(function (response) {
                return response.data;
            });
        }


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



        $scope.processML = function () {
            return $http.get('/api/processML').then(function (response) {
                return response.data;
            });
        }

        $scope.checkML = function () {
            return $http.get('/api/checkML').then(function (response) {
                return response.data;
            });
        }

        $scope.optimizeML = function () {
            return $http.get('/api/optimizeML').then(function (response) {
                return response.data;
            });
        }

        $scope.optimizeMLLR = function () {
            return $http.get('/api/optimizeMLLR').then(function (response) {
                return response.data;
            });
        }



        $scope.saveML = function () {
            return $http.get('/api/saveML').then(function (response) {
                return response.data;
            });
        }

        $scope.loadML = function () {
            return $http.get('/api/loadML').then(function (response) {
                return response.data;
            });
        }

        $scope.evaluate = function () {
            return $http.get('/api/evaluate').then(function (response) {
                return response.data;
            });
        }

    });
