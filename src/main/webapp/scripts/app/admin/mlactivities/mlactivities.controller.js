'use strict';

angular.module('mltradingApp')
    .controller('MLActivitiesController', function ($scope, $http, MLActivitiesService) {

        $scope.onLoad = function () {

            MLActivitiesService.findAll().then(function (data) {
                $scope.data = data;
            });
        };

        $scope.onLoad();


        var formData = {
            globalLoop: "1",
            inputLoop: "1",
            validator: "generateSimpleModel",
            target: "PX1"
        };



        $scope.forecast = function() {
            formData = $scope.form;
            return $http.post('/api/optimizeML',formData).then(function (response) {
                return response.data;
            });
        }


    });
