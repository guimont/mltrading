/**
 * Created by gmo on 20/01/2016.
 */
'use strict';

angular.module('mltradingApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('realtime', {
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
                        templateUrl: 'scripts/app/realtime/realtime.html',
                        controller: 'RealtimeController'
                    }
                },
                resolve: {
                    mainTranslatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate,$translatePartialLoader) {
                        $translatePartialLoader.addPart('realtime');
                        return $translate.refresh();
                    }]
                }
            });
    });
