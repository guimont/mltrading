'use strict';

angular.module('mltradingApp')
    .controller('RealtimeController', function ($scope, $http, RealtimeService) {

        $scope.onChangeDate = function () {
            var dateFormat = 'yyyy-MM-dd';


            RealtimeService.findAll().then(function (data) {
                $scope.rts = data;
            });
        };

        $scope.onChangeDate();

        $scope.checkValue = function (val) {
            if (val === NaN) return 0;
            else return val;
        }

        $scope.showPrediction = function(codif) {
            $('#showHealthModal').modal('show');
        };

        $scope.prediction = function (val,per) {
            if (val === undefined) return "";
            return val.toFixed(3) +" / " + per +"%"
        }

        $scope.getcolor = function(val) {
            if (val < 40) return 'red';
            if (val < 70) return 'orange';
            return 'green';
        }

        $scope.getcolorSign = function(val) {
            if (val < 0) return 'red';
            return 'green';
        }




    });
