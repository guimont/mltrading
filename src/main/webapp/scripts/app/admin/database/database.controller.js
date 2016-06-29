'use strict';

angular.module('mltradingApp')
    .controller('DatabaseController', function ($scope, $http) {

        $scope.onLoad = function () {

            DatabaseService.findAll().then(function (data) {
                $scope.rts = data;
            });
        };

        $scope.onLoad();


    });
