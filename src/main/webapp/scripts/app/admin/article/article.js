'use strict';

angular.module('mltradingApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('article', {
                parent: 'admin',
                url: '/article',
                data: {
                    roles: ['ROLE_ADMIN'],
                    pageTitle: 'article.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/admin/article/article.html',
                        controller: 'ArticleController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('article');
                        return $translate.refresh();
                    }]
                }
            });
    });
