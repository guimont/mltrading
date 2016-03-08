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

    chartResult({x:30,y:20}, layerChart, true);


    stageResult.add(layerChart);

};


//data.history[i].value (rond gris)


function chartResult(pos, layer) {

    var group = new Kinetic.Group();
    var max = getmaxRunResult( result.data, 'value');
    var min = getminRunResult( result.data, 'value');
    var heightM = (SIZERESY/2)/(max-min*0.8);
    var marge =  heightM*4;

    for (var i=0; i<result.data.length;i++) {
        drawLine (group, pos, result.data[i].predD20- min,  heightM,marge, i,  layer, 'blue' );
        if (result.data[i].value != 0) drawValue(group, pos, result.data[i].value - min,  heightM,marge, i,  layer, 'grey','square' );
        drawValue(group, pos, result.data[i].predD1- min,  heightM,marge, i,  layer, 'blue' );
        drawValue(group, pos, result.data[i].predD5- min,  heightM,marge, i,  layer, 'green' );
        drawValue(group, pos, result.data[i].predD20- min,  heightM,marge, i,  layer, 'orange' );

    }

    drawGridRes(group,pos, max, max-min,  heightM, marge);

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

function getminRunResult( list,c) {
    var min = 10000;
    for (var i=0;i<list.length;i++) {
        if (list[i][c]) {
            var v = Math.abs(list[i][c]);
            if (v != 0 && v < min) min = v;
        }
    }
    return min;
}


function drawLine(group, pos, value, heightM, marge,  i ,  layer, color ) {
    group.add(new Kinetic.Rect({
        x: pos.x*2+i*(15),
        y: 100,
        width: 3,
        height:SIZERESY-120,
        fill: 'lightgrey',
        opacity: 0.2

    }));
}

function drawValue(group, pos, value, heightM, marge,  i ,  layer, color, form) {

    if (form === 'square')
        group.add(new Kinetic.Rect({
            x: pos.x*2+i*(15)-2,
            y: SIZERESY - value * heightM  - marge,
            width: 5,
            height: 5,
            fill: color
        }));
    else
        group.add(new Kinetic.Circle({
            x: pos.x*2+i*(15),
            y: SIZERESY - value * heightM  - marge,
            width: 5,
            height: 5,
            fill: color
        }));

    layer.draw();
}


function  drawGridRes(group, pos, maxV, max,  heightM, marge) {

    group.add(new Kinetic.Rect({
        x: pos.x,
        y: SIZERESY-20,
        width: SIZERESX,
        height: 1,
        fill: 'lightgrey'

    }));

    group.add(new Kinetic.Rect({
        x: pos.x,
        y: 100,
        width: 1,
        height:SIZERESY-120,
        fill: 'lightgrey'

    }));

    group.add(new Kinetic.Rect({
        x: pos.x,
        y: SIZERESY - max * heightM  - marge,
        width: SIZERESX,
        height: 1,
        fill: 'blue',
        opacity: 0.2

    }));


    group.add(new Kinetic.Rect({
        x: pos.x,
        y: SIZERESY + max * heightM  - marge,
        width: SIZERESX,
        height: 1,
        fill: 'blue',
        opacity: 0.2

    }));

    group.add(new Kinetic.Text({
        x:0,
        y: SIZERESY - max * heightM  - marge,
        text: maxV.toFixed(2),
        fontFamily: 'Calibri',
        fontSize: 9,
        fill: 'black'

    }));

    group.add(new Kinetic.Text({
        x:0,
        y: SIZERESY + max * heightM  - marge,
        text: (maxV - 2*max).toFixed(2),
        fontFamily: 'Calibri',
        fontSize: 9,
        fill: 'black'

    }));


}
