var result = [];


var SIZERESX=1300;
var RSIZERESX=1080;
var SIZERESY=240;

var margeX = 10;
var GLOBAL_X =1400;

/**
 * init stage kinetic canvas
 * init route for get parameter data
 * init websocket for new notification
 */
function loadResult (data) {

    if (data == null)
        return;

    var stageResult = new Kinetic.Stage({
        container: "result",
        width: SIZERESX,
        height: SIZERESY
    });

    var layerChart = new Kinetic.Layer();

    result = data;

    GLOBAL_X = window.innerWidth;
    margeX = window.innerWidth/150;

    chartResult({x:5,y:20}, layerChart, true);
    stageResult.add(layerChart);

    loadIndice(result.indice,"indicePanelDetail","", true, "rgba(75,192,192,0.2)")
    loadIndice(result.sector,"sectorPanelDetail","", true, "rgba(119,136,153,0.2)")

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
        drawValue(group, pos, result.data, min,  heightM,marge, i,  layer);

    }

    var groupEffect = new Kinetic.Group();
    //effect(groupEffect, pos, result.data, min,  heightM,marge,  layer)

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


function drawValue(group, pos, data,min, heightM, marge,  i ,  layer) {

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
        height:SIZERESY-0,
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

    if (i> 79) {
        if (i > 89 && data[i].predD20 > 0) {
            var pred = new Kinetic.Ellipse({
                x: pos.x * 2 + i * (margeX) +1,
                y: SIZERESY - (data[i].predD20 - min) * heightM - marge,
                radius: {
                    x: 6,
                    y: 12
                },
                opacity: 0.2,
                fill: 'orange'
            });
            group.add(pred);
        }

        if (i > 108 && data[i].predD40 > 0) {

            var pred = new Kinetic.Ellipse({
                x: pos.x * 2 + i * (margeX) +1,
                y: SIZERESY - (data[i].predD40 - min) * heightM - marge,
                radius: {
                    x: 8,
                    y: 12
                },
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
        detail.start();
        group.add(chartGroup);
        layer.draw();
    });

    ref.on('mouseout', function() {
        detail.stop();
        chartGroup.remove();
        layer.draw();
    });







    var detail = new Kinetic.Animation(function() {
        chartGroup.destroyChildren();

        var elt = data [i];
        if (i> 79) return;

        detail.stop();

        var decalage  = i <70 ? 15: -120;
        var spos = {x:i*5+decalage,y:pos.y};

        var colorR  = elt.opening > elt.value ? 'red' : 'green'

        chartGroup.add(new Kinetic.Rect({
            x: pos.x*2+i*(margeX),
            y: 10,
            width: 2,
            height:SIZERESY,
            fill: 'red',
            opacity: 0.4
        }));

        var tooltip = new Kinetic.Label({
            x: spos.x,
            y: 60,
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


        tooltip.add(new Kinetic.Text({
            x:spos.x+10,
            y: -23,
            text: 'date:         ',
            fontFamily: 'Calibri',
            fontSize: 12,
            fill: 'black'
        }));

        tooltip.add(new Kinetic.Text({
            x:spos.x+40,
            y: -23,
            text: elt.date,
            fontFamily: 'Calibri',
            fontSize: 11,
            fill: 'grey'
        }));



        tooltip.add(new Kinetic.Text({
            x:spos.x+10,
            y: -8,
            text: 'day value:    ',
            fontFamily: 'Calibri',
            fontSize: 12,
            fill: 'black'
        }));

        tooltip.add(new Kinetic.Text({
            x:spos.x+100,
            y: -8,
            text: elt.value.toFixed(2),
            fontFamily: 'Calibri',
            fontSize: 12,
            fill: colorR
        }));



        tooltip.add(new Kinetic.Text({
            x:spos.x+10,
            y: 7,
            text: 'opening value:',
            fontFamily: 'Calibri',
            fontSize: 12,
            fill: 'black'
        }));

        tooltip.add(new Kinetic.Text({
            x:spos.x+100,
            y: 7,
            text: elt.opening.toFixed(2),
            fontFamily: 'Calibri',
            fontSize: 12,
            fill: 'grey'
        }));

        tooltip.add(new Kinetic.Text({
            x:spos.x+10,
            y: 22,
            text: 'highest value:',
            fontFamily: 'Calibri',
            fontSize: 12,
            fill: 'black'
        }));

        tooltip.add(new Kinetic.Text({
            x:spos.x+100,
            y: 22,
            text: elt.high.toFixed(2),
            fontFamily: 'Calibri',
            fontSize: 12,
            fill: 'grey'
        }));

        tooltip.add(new Kinetic.Text({
            x:spos.x+10,
            y: 37,
            text: 'lowest value:',
            fontFamily: 'Calibri',
            fontSize: 12,
            fill: 'black'
        }));

        tooltip.add(new Kinetic.Text({
            x:spos.x+100,
            y: 37,
            text: elt.low.toFixed(2),
            fontFamily: 'Calibri',
            fontSize: 12,
            fill: 'grey'
        }));




        chartGroup.add(tooltip);

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






        layer.draw();
    });



}


function showPred() {

}


function  drawGridRes(group, pos, maxV, max,  heightM, marge) {


    group.add(new Kinetic.Rect({
        x: pos.x+10,
        y: SIZERESY - max * heightM  - marge,
        width: margeX*120,
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


    group.add(new Kinetic.Rect({
        x: pos.x+10,
        y: SIZERESY - 20 ,
        width: margeX*120,
        height: 1,
        fill: 'AC7969',
        opacity: 0.1

    }));

    group.add(new Kinetic.Text({
        x:0,
        y: SIZERESY - 30 ,
        text: (maxV - 2*max).toFixed(2),
        fontFamily: 'Calibri',
        fontSize: 9,
        fill: 'black'

    }));


}
