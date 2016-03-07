var result = [];



var SIZERESX=700;
var SIZERESY=400;
var marginRESX= 340;

/**
 * init stage kinetic canvas
 * init route for get parameter data
 * init websocket for new notification
 */
function loadResult (data) {


    var stageResult = new Kinetic.Stage({
        container: "result",
        width: SIZERESX,
        height: SIZERESY
    });


    var layerChart = new Kinetic.Layer();

    result = data;

    chartResult({x:20,y:20}, layerChart, true);


    stageResult.add(layerChart);

};


//data.history[i].value (rond gris)


function chartResult(pos, layer) {

    var group = new Kinetic.Group();
    var max = getmaxRunResult( result.history, 'value');
    var heightM = (SIZERESY/2)/max;

    for (var i=0; i<result.history.length;i++) {
        drawValue(group, pos, result.history[i].value,  heightM, i,  layer, 'grey' );
    }

    drawGridRes(group, pos, max,  heightM, marginRESX);

    layer.add(group);
}

function getmaxRunResult( list,c) {
    var max = 0;
    for (var i=0;i<list.length;i++) {
        if (list[i][c]) {
            var v = Math.abs(list[i][c]);
            if (v > max) max = v;
        }
    }
    return max;
}

function drawValue(group, pos, value, heightM,  i ,  layer, color) {
    group.add(new Kinetic.Circle({
        x: pos.x+i*(15),
        y: SIZERESY - value * heightM,
        width: 5,
        height: 5,
        fill: color
    }));

    layer.draw();
}


function  drawGridRes(group, pos, max,  heightM, marginX) {
    /*group.add(new Kinetic.Rect({
        x: pos.x,
        y: pos.y,
        width: SIZEX,
        height: SIZEY,
        fill: 'red'
    }));*/

    group.add(new Kinetic.Rect({
        x: 0,
        y: SIZERESY-20,
        width: SIZERESX,
        height: 1,
        fill: 'lightgrey'

    }));

    group.add(new Kinetic.Rect({
        x: 0,
        y: 50,
        width: 1,
        height:SIZERESY,
        fill: 'lightgrey'

    }));

}
