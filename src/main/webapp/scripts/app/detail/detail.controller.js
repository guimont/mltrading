/**
 * Created by gmo on 05/03/2016.
 */

'use strict';

angular.module('mltradingApp')
    .controller('DetailController', function ($scope, $state, $stateParams, $http, DetailService, color) {

        var code = $stateParams.code; //getting fooVal
        $scope.state = $state.current
        $scope.params = $stateParams;

        window.scrollTo(0, 0);

        document.getElementsByTagName("BODY")[0].onresize = function() {loadResult($scope.detail)()};

        $scope.showDetail = function(code) {
            DetailService.find(code).then(function (data) {
                $scope.detail = data;
                loadResult(data);
                loadMap(data)
            });
        };

        $scope.showDetail(code);

        $scope.calculateVolumeDay = function() {
            if ($scope.detail === undefined) return;
            var global = $scope.detail.stock.titleNb.replace(/\s/g, "");
            return ($scope.detail.volume * 100 / global).toFixed(3);
        }

        $scope.getcolorSign = function(val) {
            return color.getcolorSign(val);
        }

        $scope.getcolor = function(val) {
            if (val < 40) return 'red';
            if (val < 70) return 'orange';
            return 'green';
        }


    });
