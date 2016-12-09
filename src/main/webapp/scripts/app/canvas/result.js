var result = [];


var SIZERESX=1000;
var RSIZERESX=1020;
var SIZERESY=240;

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


    var stageIndice = new Kinetic.Stage({
        container: "indice",
        width: SIZERESX/2,
        height: SIZERESY
    });

    var stageSector = new Kinetic.Stage({
        container: "sector",
        width: SIZERESX/2,
        height: SIZERESY
    });


    var layerChart = new Kinetic.Layer();

    result = data;

    chartResult({x:30,y:20}, layerChart, true);
    stageResult.add(layerChart);


    var layerChartIndice = new Kinetic.Layer();
    draw({x:30,y:20},layerChartIndice,'indice')
    stageIndice.add(layerChartIndice);

    var layerChartSector = new Kinetic.Layer();
    draw({x:30,y:20},layerChartSector,'sector')
    stageSector.add(layerChartSector);

};


//data.history[i].value (rond gris)


function chartResult(pos, layer) {

    var group = new Kinetic.Group();
    var max = getmaxRunResult( result.data, 'value', 0);
    max = getmaxRunResult( result.data, 'opening', max);
    max = getmaxRunResult( result.data, 'predD20', max);
    max = getmaxRunResult( result.data, 'predD40', max);
    var min = getminRunResult( result.data, 'value',100000);
    min = getminRunResult( result.data, 'opening',min);
    min = getminRunResult( result.data, 'predD20',min);
    min = getminRunResult( result.data, 'predD40',min);
    var heightM = (SIZERESY)/((max-min)*1.6);
    var marge =  SIZERESY/4;

    for (var i=0; i<result.data.length;i++) {
        //if (result.data[i].value != 0) drawValue(group, pos, result.data ,min ,  heightM,marge, i,  layer, 'grey','square' );
        //drawValue(group, pos, result.data[i].predD1- min,  heightM,marge, i,  layer, 'blue' );
        //drawValue(group, pos, result.data[i].predD5- min,  heightM,marge, i,  layer, 'green' );
        drawValue(group, pos, result.data, min,  heightM,marge, i,  layer);

    }

    var groupEffect = new Kinetic.Group();
    effect(groupEffect, pos, result.data, min,  heightM,marge,  layer)

    drawGridRes(group,pos, max, max-min,  heightM, marge);

    layer.add(group);
}

function getmaxRunResult( list,c,maxP) {
    var max = maxP;
    for (var i=0;i<list.length;i++) {
        if (list[i][c]) {
            var v = Math.abs(list[i][c]);
            if (v > max) max = v;
        }
    }
    return max;
}

function getminRunResult( list,c, minP) {
    var min = minP;
    for (var i=0;i<list.length;i++) {
        if (list[i][c]) {
            var v = Math.abs(list[i][c]);
            if (v != 0 && v < min) min = v;
        }
    }
    return min;
}



var j = 40;
var anim;
function effect(group,pos, data,min, heightM, marge, layer) {

    var pred5, pred20, pred40;
    var time = 0;

    anim = new Kinetic.Animation(function(frame) {

        if (j < 60) {
            if (frame.time - time < 400) return;
        } else
        if (frame.time - time < 150) return;
        time = frame.time
        group.destroyChildren();
        if (j >= data.length) j = 40;

        if (data[j].predD5 > 0 && j <44) {
            pred5 = new Kinetic.Circle({
                x: pos.x * 2 + j * (margeX),
                y: SIZERESY - (data[j].predD5 - min) * heightM - marge,
                width: 7,
                height: 7,
                opacity: 0.3,
                fill: 'blue'
            });
            group.add(pred5);

            /*var size =  (data[j].predD5 - min) * heightM  - (data[j].predD20 - min) * heightM

            var line = new Kinetic.Rect({
                x: pos.x * 2 + j * (10) -1,
                y: SIZERESY - (data[j].predD5 - min) * heightM - marge,
                width: 2,
                height: size,
                opacity: 0.5,
                fill: 'blue'
            });

            group.add(line);*/


            var diff = data[j].predD5;
            /*line = new Kinetic.Rect({
             x: 900 ,
             y: SIZERESY - 100,
             width: 8,
             height: diff *2,
             opacity: 0.5,
             fill: 'blue'
             });
             group.add(line);*/


            var per = (diff/data[j-40].value*100-100).toFixed(2)

            group.add(new Kinetic.Text({
                x: 460,
                y: SIZERESY-235,
                text: 'expected: '+per+'%',
                fontSize: 12,
                fontFamily: 'Calibri',
                fill: 'blue',
                width: 150,
                align: 'left'
            }));

        }


        if (data[j].predD20 > 0 && j >43 && j <59) {
            pred20 = new Kinetic.Circle({
                x: pos.x * 2 + j * (margeX),
                y: SIZERESY - (data[j].predD20 - min) * heightM - marge,
                width: 7,
                height: 7,
                opacity: 0.3,
                fill: 'orange'
            });
            group.add(pred20);

            /*var size =  (data[j].predD20 - min) * heightM  - (data[j].predD40 - min) * heightM

            var line = new Kinetic.Rect({
                x: pos.x * 2 + j * (10) -1,
                y: SIZERESY - (data[j].predD20 - min) * heightM - marge,
                width: 2,
                height: size,
                opacity: 0.5,
                fill: 'red'
            });

            group.add(line);*/

            var diff = data[j].predD20;
            /*line = new Kinetic.Rect({
             x: 930 ,
             y: SIZERESY - 100,
             width: 8,
             height: diff *2,
             opacity: 0.5,
             fill: 'orange'
             });
             group.add(line);*/


            var per = (diff/data[j-20].value*100-100).toFixed(2)

            group.add(new Kinetic.Text({
                x: 600,
                y: SIZERESY-235,
                text: 'expected: '+per+'%',
                fontSize: 12,
                fontFamily: 'Calibri',
                fill: 'orange',
                width: 150,
                align: 'left'
            }));

        }

        if (data[j].predD40 > 0 && j >59 ) {
            pred40 = new Kinetic.Circle({
                x: pos.x * 2 + j * (margeX),
                y: SIZERESY - (data[j].predD40 - min) * heightM - marge,
                width: 7,
                height: 7,
                opacity: 0.3,
                fill: 'green'
            });
            group.add(pred40);

            var diff = data[j].predD40;
            /*line = new Kinetic.Rect({
             x: 960 ,
             y: SIZERESY - 100,
             width: 8,
             height: diff *2,
             opacity: 0.5,
             fill: 'green'
             });
             group.add(line);*/


            var per = (diff/data[j-40].value*100-100).toFixed(2)

            group.add(new Kinetic.Text({
                x: 720,
                y: SIZERESY-235,
                text: 'expected: '+per+'%',
                fontSize: 12,
                fontFamily: 'Calibri',
                fill: 'green',
                width: 150,
                align: 'left'
            }));



        }
        j++
        layer.add(group);
        layer.draw();

    });

    anim.start()

}



function drawZoom(group,pos, data,min, heightM, marge, layer) {

}

var margeX = 12
function drawValue(group, pos, data,min, heightM, marge,  i ,  layer) {

    var pred5;

    var line = new Kinetic.Rect({
        x: pos.x * 2 + i *(margeX),
        y: 30,
        width: 3,
        height:SIZERESY-40,
        fill: 'lightgrey',
        opacity: 0.2
    });


    var ref = new Kinetic.Rect({
        x: pos.x * 2 + i *(margeX)-5,
        y: 30,
        width: 10,
        height:SIZERESY-40,
        opacity: 0
    });

    group.add(line);
    group.add(ref);


    if  (i%4==0) {

        group.add(new Kinetic.Rect({
            x: pos.x * 2 + i *(margeX),
            y: 20,
            width: 1,
            height:SIZERESY-40,
            opacity: 0.6,
            fill: '#AC7969'
        }));

        group.add(new Kinetic.Text({
            x: pos.x * 2 + i *(margeX),
            y: SIZERESY-10-10*(i%2),
            text: data[i].date,
            fontSize: 8,
            fontFamily: 'Calibri',
            fill: '#AC7969',
            width: 150,
            align: 'left'
        }));
    }

    var subopacity = 1
    var color = 'grey'

    if (data[i].value < data[i].opening) {
        color = '#997A8D'
        subopacity = 0.5
    }
    var height = (data[i].opening - data[i].value)  * heightM;
    height = Math.max(Math.abs(height),4)

    if (data[i].value > 0) {
        var pred = new Kinetic.Rect({
            x: pos.x * 2 + i * (margeX) - 1,
            y: SIZERESY - (data[i].value - min) * heightM - marge,
            width: 4,
            opacity: subopacity,
            height: height ,
            fill: color
        });
        group.add(pred);
    }

    if (i> 39) {
        if (data[i].predD5 > 0) {
            pred5 = new Kinetic.Circle({
                x: pos.x * 2 + i * (margeX) +1,
                y: SIZERESY - (data[i].predD5 - min) * heightM - marge,
                width: 3,
                height: 6,
                opacity: 0.2,
                fill: 'blue'
            });
            group.add(pred5);
        }

        if (i > 43 && data[i].predD20 > 0) {
            var pred = new Kinetic.Circle({
                x: pos.x * 2 + i * (margeX) +1,
                y: SIZERESY - (data[i].predD20 - min) * heightM - marge,
                height: 6,
                opacity: 0.2,
                fill: 'orange'
            });
            group.add(pred);
        }

        if (i > 58 && data[i].predD40 > 0) {

            var pred = new Kinetic.Circle({
                x: pos.x * 2 + i * (margeX) +1,
                y: SIZERESY - (data[i].predD40 - min) * heightM - marge,
                width: 3,
                height: 6,
                opacity: 0.2,
                fill: 'green'
            });
            group.add(pred);
        }
    }


    layer.draw();



    var chartGroup = new Kinetic.Group();
    ref.on('mouseover', function() {
        dist = 0;
        //detail.start();
        anim.stop()
        group.add(chartGroup);
        layer.draw();
    });

    ref.on('mouseout', function() {
        //detail.stop();
        anim.start()
        chartGroup.remove();
        layer.draw();
    });







    var detail = new Kinetic.Animation(function() {
        chartGroup.destroyChildren();


        detail.stop();

        chartGroup.add(new Kinetic.Rect({
            x: pos.x*2+i*(margeX),
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

        /*var pts = [pos.x * 2 + i * (10) - 2, SIZERESY - dest * heightM - marge, pos.x * 2 + (i >= 20 ? i-20:0) * (10) - 2, SIZERESY - origin * heightM - marge +2];
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
        }));*/







        chartGroup.add(new Kinetic.Text({
            x:880,
            y: 100,
            text: "BONJOUR",
            fontFamily: 'Calibri',
            fontSize: 9,
            fill: 'black'

        }));






        layer.draw();
    });



}


function showPred() {

}


function  drawGridRes(group, pos, maxV, max,  heightM, marge) {

    group.add(new Kinetic.Rect({
        x: pos.x,
        y: SIZERESY-20,
        width: RSIZERESX,
        height: 1,
        fill: 'lightgrey'

    }));

    group.add(new Kinetic.Rect({
        x: pos.x,
        y: 20,
        width: 1,
        height:SIZERESY-40,
        fill: 'lightgrey'

    }));

    /*group.add(new Kinetic.Rect({
     x: pos.x,
     y: SIZERESY - max * heightM  - marge,
     width: SIZERESX,
     height: 1,
     fill: 'blue',
     opacity: 0.2

     }));


     */
    group.add(new Kinetic.Rect({
        x: pos.x,
        y: SIZERESY - max * heightM  - marge,
        width: RSIZERESX,
        height: 1,
        fill: 'AC7969',
        opacity: 0.1

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
