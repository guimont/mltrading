'use strict';

angular.module('mltradingApp')
    .controller('ExtractionController', function ($scope, $http) {

        $scope.extraction = function () {
            return $http.get('/api/extractionAction').then(function (response) {
                return response.data;
            });
        }

        $scope.extractionSeries = function () {
            return $http.get('/api/extractionSeries').then(function (response) {
                return response.data;
            });
        }


    });
