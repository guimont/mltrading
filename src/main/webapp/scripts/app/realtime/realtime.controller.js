'use strict';

angular.module('mltradingApp')
    .controller('RealtimeController', function ($scope,$state, $http, RealtimeService) {

        $scope.updatingRT = true;

        $scope.showSector = function() {
            RealtimeService.findSector().then(function (data) {
                $scope.sectors = data;
                loadSector($scope.sectors);

            });
        };

        setInterval(function() {
            $scope.showSector();
            $scope.showIndice();
            $scope.onChangeDate();
        }, 20000);



        $scope.showSector()


        $scope.showIndice = function() {
            RealtimeService.findIndice().then(function (data) {
                $scope.indice = data;
                loadIndice($scope.indice,"indicePanel","PX1 stock", false);
            });
        };

        $scope.showIndice()



        $scope.onChangeDate = function () {

            RealtimeService.findAll().then(function (data) {
                $scope.rts = data;
                //$state.reload();
            });


        };

        $scope.onSelected = function () {
            RealtimeService.findSelected().then(function (data) {
                $scope.selected = data;
            });
        }

        $scope.onSelected();

        $scope.getIcon = function(val) {

            if(val ==="FRIN") return 'glyphicon glyphicon-briefcase'
            if(val ==="FRBM") return 'glyphicon glyphicon-home'
            if(val ==="FROG") return 'glyphicon glyphicon-oil'
            if(val ==="FRCG") return 'glyphicon glyphicon-shopping-cart'
            if(val ==="FRHC") return 'glyphicon glyphicon-heart'
            if(val ==="FRCS") return 'glyphicon glyphicon-credit-card'
            if(val ==="FRTEL") return 'glyphicon glyphicon-earphone'
            if(val ==="FRUT") return 'glyphicon glyphicon-refresh' //Services aux collectivités
            if(val ==="FRFIN") return 'glyphicon glyphicon-usd'
            if(val ==="FRTEC") return 'glyphicon glyphicon-phone'

        }



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
            if (val < 0) return '#ED5565!important';
            return '#48CFAD!important';
        }




    });
