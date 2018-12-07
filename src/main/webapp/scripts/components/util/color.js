
'use strict';

angular.module('mltradingApp')
    .service('color', function () {
        return {
            getcolorSign: function (val) {

            if (val < 0) return '#ED5565!important';
            return '#48CFAD!important';
        }}

return {
         getcolor : function(val) {
                    if (val < 40) return 'red';
                    if (val < 70) return 'orange';
                    return 'green';
                }
                }
    });
