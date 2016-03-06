/**
 * Created by gmo on 05/03/2016.
 */
'use strict';

angular.module('mltradingApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('detail', {
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
                        templateUrl: 'scripts/app/detail/detail.html',
                        controller: 'DetailController'
                    }
                },
                resolve: {
                    mainTranslatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate,$translatePartialLoader) {
                        $translatePartialLoader.addPart('detail');
                        return $translate.refresh();
                    }]
                }
            });
    });
