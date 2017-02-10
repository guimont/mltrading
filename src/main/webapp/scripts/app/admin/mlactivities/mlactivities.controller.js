'use strict';

angular.module('mltradingApp')
    .controller('MLActivitiesController', function ($scope, $http, MLActivitiesService) {

        $scope.onLoad = function () {

            MLActivitiesService.findAll().then(function (data) {
                $scope.data = data;
            });
        };

        $scope.onLoad();


    });
