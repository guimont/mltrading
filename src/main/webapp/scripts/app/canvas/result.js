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
        //if (result.data[i].value != 0) drawValue(group, pos, result.data ,min ,  heightM,marge, i,  layer, 'grey','square' );
        //drawValue(group, pos, result.data[i].predD1- min,  heightM,marge, i,  layer, 'blue' );
        //drawValue(group, pos, result.data[i].predD5- min,  heightM,marge, i,  layer, 'green' );
        drawValue(group, pos, result.data, min,  heightM,marge, i,  layer);

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



function drawValue(group, pos, data,min, heightM, marge,  i ,  layer) {

    var line = new Kinetic.Rect({
        x: pos.x * 2 + i *(10),
        y: 100,
        width: 3,
        height:SIZERESY-120,
        fill: 'lightgrey',
        opacity: 0.2
    });

    group.add(line);

    var pred = new Kinetic.Rect({
        x: pos.x * 2 + i * (10) - 2,
        y: SIZERESY - (data[i].value-min) * heightM - marge,
        width: 5,
        height: 5,
        fill: 'grey'
    });
    group.add(pred);



    var pred = new Kinetic.Circle({
        x: pos.x * 2 + i * (10),
        y: SIZERESY - (data[i].predD20-min) * heightM - marge,
        width: 5,
        height: 5,
        fill: 'orange'
    });
    group.add(pred);
    layer.draw();


    var chartGroup = new Kinetic.Group();
    line.on('mouseover', function() {
        dist = 0;
        detail.start();
        group.add(chartGroup);
        layer.draw();
    });

    line.on('mouseout', function() {
        detail.stop();
        chartGroup.remove();
        layer.draw();
    });

    var detail = new Kinetic.Animation(function() {
        chartGroup.destroyChildren();

        var spos = {x:pos.x/2,y:pos.y};
        if (i<42) spos.x+=90;


        detail.stop();

        chartGroup.add(new Kinetic.Rect({
            x: pos.x*2+i*(10),
            y: 100,
            width: 2,
            height:SIZERESY-120,
            fill: 'red',
            opacity: 0.1
        }));

        var origin = i >= 20 ? data[i-20].value: data[0].value;
        origin = origin - min;
        var dest = data[i].predD20-min;


        var colorSign = data[i].signD20 == true ? 'green':'red';

        var pts = [pos.x * 2 + i * (10) - 2, SIZERESY - dest * heightM - marge, pos.x * 2 + (i >= 20 ? i-20:0) * (10) - 2, SIZERESY - origin * heightM - marge +2];
        chartGroup.add(new Kinetic.Line({
            points: pts,
            stroke:colorSign,
            strokeWidth: 2,
            lineCap: 'round',
            lineJoin: 'round',
            dashArray: [1, 1]
        }));


        var dest = data[i].value-min;
        var pts = [pos.x * 2 + i * (10) - 2, SIZERESY - dest * heightM - marge, pos.x * 2 + (i >= 20 ? i-20:0) * (10) - 2, SIZERESY - origin * heightM - marge+2];
        chartGroup.add(new Kinetic.Line({
            points: pts,
            stroke:colorSign,
            strokeWidth: 2,
            lineCap: 'round',
            lineJoin: 'round',
            dashArray: [1, 1]
        }));





        tooltip = new Kinetic.Label({
            x: spos.x,
            y: 50,
            height: 85
        });

        var rect = new Kinetic.Rect({
            x:spos.x,
            y: 80,
            width: 170,
            height: -110,
            fill: 'white',
            shadowOffset: {x:1,y:1}
        });

        tooltip.add(rect);


        chartGroup.add(tooltip);

        layer.draw();
    });


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
