/**
 * Created by gmo on 20/01/2016.
 */
'use strict';

angular.module('mltradingApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('asset', {
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
                        templateUrl: 'scripts/app/asset/asset.html',
                        controller: 'AssetController'
                    }
                },
                resolve: {
                    mainTranslatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate,$translatePartialLoader) {
                        $translatePartialLoader.addPart('asset');
                        return $translate.refresh();
                    }]
                }
            });
    });
