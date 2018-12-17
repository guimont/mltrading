'use strict';

angular.module('mltradingApp')
    .factory('AssetService', function ($http) {
        return {
            getAsset: function () {
                return $http.get('api/asset/all').then(function (response) {
                    return response.data;
                });
            }
        };
    });
