'use strict';

angular.module('mltradingApp')
    .factory('MLActivitiesService', function ($http) {
        return {
            findAll: function () {
                return $http.get('api/ML/Activities/all').then(function (response) {
                    return response.data;
                });
            },

            resume: function () {
                return $http.get('api/ML/resume').then(function (response) {
                    return response.data;
                });
            }
        };
    });
