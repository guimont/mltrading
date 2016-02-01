/**
 * Created by gmo on 20/01/2016.
 */
'use strict';

angular.module('mltradingApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('performance', {
                parent: 'site',
                url: '/',
                data: {
                    roles: []
                },
                params: {
                    code: null
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/performance/performance.html',
                        controller: 'PerformanceController'
                    }
                },
                resolve: {
                    mainTranslatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate,$translatePartialLoader) {
                        $translatePartialLoader.addPart('performance');
                        return $translate.refresh();
                    }]
                }
            });
    });
