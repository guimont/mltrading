'use strict';

angular.module('mltradingApp')
    .factory('MLActivitiesService', function ($http) {
        return {
            findAll: function () {
                return $http.get('api//MLActivities/all').then(function (response) {
                    return response.data;
                });
            },

            resume: function () {
                return $http.get('api/resume').then(function (response) {
                    return response.data;
                });
            }
        };
    });
