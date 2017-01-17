'use strict';

angular.module('mltradingApp')
    .factory('RealtimeService', function ($http) {
        return {
            findAll: function () {
                return $http.get('api/rt/all').then(function (response) {
                    return response.data;
                });

            },

            findSector: function () {
                return $http.get('api/rt/sector').then(function (response) {
                    return response.data;
                });
            },

            findIndice: function () {
                return $http.get('api/rt/px1').then(function (response) {
                    return response.data;
                });
            }
        };
    });
