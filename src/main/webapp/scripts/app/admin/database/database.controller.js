'use strict';

angular.module('mltradingApp')
    .controller('DatabaseController', function ($scope, $http, DatabaseService) {

        $scope.onLoad = function () {

            DatabaseService.findAll().then(function (data) {
                $scope.data = data;
            });
        };

        $scope.onLoad();


    });
