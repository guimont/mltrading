/**
 * Created by gmo on 04/02/2016.
 */
/**
 * Created with IntelliJ IDEA.
 * User: gmo
 * Date: 13/05/14
 * Time: 12:02
 * To change this template use File | Settings | File Templates.
 */

/**
 * global data array field
 * @type {Array}
 */
var run = [];

var perf = [];

/**
 * init stage kinetic canvas
 * init route for get parameter data
 * init websocket for new notification
 */
function load (data) {


    var stageChart = new Kinetic.Stage({
        container: "kinetic",
        width: 440,
        height: 160
    });


    var layerChart = new Kinetic.Layer();


    perf = data;

    chartRun({x:60,y:150},layerChart);


};



var SIZEX=380;
var SIZEY=180;
var marginX= 340;



/**
 *
 * @param list
 * @param f
 * @returns {number}
 */

function getMean( list, f) {
    var mean = 0; var sizeNotNull = 0;
    for (var i=0;i<list.length;i++) {
        if (list[i].dataDay[f] != 0) {
            mean += list[i].dataDay[f];
            sizeNotNull ++;
        }
    }
    return mean / sizeNotNull;
}

/**
 *
 * @param list
 * @param f
 * @param h
 * @returns {number}
 */
function getMeanRun( list, f,h) {
    var mean = 0; var sizeNotNull = 0;
    for (var i=0;i<list.length;i++) {
        if (list[i].dataDay[f][h] != 0) {
            mean += list[i].dataDay[f][h];
            sizeNotNull ++;
        }
    }
    return mean / sizeNotNull;
}

/**
 *
 * @param list
 * @param f
 * @returns {number}
 */
function getmax( list,f) {
    var max = 0;
    for (var i=0;i<list.length;i++) {
        if (list[i].dataDay[f] > max) max = list[i].dataDay[f];
    }
    return max;
}

/**
 *
 * @param list
 * @param f
 * @param h
 * @returns {number}
 */

function getmaxRun( list,f,h) {
    var max = 0;
    for (var i=0;i<list.length;i++) {
        if (list[i].dataDay[f][h] > max) max = list[i].dataDay[f][h];
    }
    return max;
}

/**
 * draw run graph
 * @param pos position in page
 * @param layer : add group to layer
 */
function chartRun(pos,layer) {

    var group = new Kinetic.Group();
    var eltLength = perf.length;
    var eltSize = (SIZEX-40)/eltLength - 2;
    var max = getmaxRun(perf, 'run','global');
    var heightM = (SIZEY-60)/max;

    for (var i=0; i<run.list.length;i++) {
        drawChart(group, pos, run.list[i].dataDay.run, heightM, eltSize, i, run.list[i].day, layer, 'grey');
    }

    drawChartMax(group, pos, max, getMeanRun(run.list, 'run','global'), heightM, marginX);

    layer.add(group);
}
