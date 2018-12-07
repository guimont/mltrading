'use strict';

angular.module('mltradingApp')
    .controller('MLActivitiesController', function ($scope, $http, MLActivitiesService) {

        $scope.onLoad = function () {

            MLActivitiesService.findAll().then(function (data) {
                $scope.data = data;
            });

        };

        $scope.onLoad();


        $scope.resume = function () {
            MLActivitiesService.resume().then(function (data) {
                $scope.resume = data;
            });
        };

        $scope.resume();

        var formData = {
            globalLoop: "1",
            inputLoop: "1",
            validator: "generateSimpleModel",
            modelType: "RANOMFOREST",
            target: "PX1",
            specific: "ALL"
        };


        $scope.forecast = function() {
            formData = $scope.form;
            return $http.post('/api/ML/optimize',formData).then(function (response) {
                return response.data;
            });
        }

        $scope.export = function() {
            return $http.get('/api/ML/export').then(function (response) {
                return response.data;
            });
        }

        $scope.import = function() {
            return $http.get('/api/ML/import').then(function (response) {
                return response.data;
            });
        }



    });
