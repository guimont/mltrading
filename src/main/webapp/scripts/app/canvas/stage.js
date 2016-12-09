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


    var stagePredictionD1 = new Kinetic.Stage({
        container: "kinetic1D",
        width: 1220,
        height: 200
    });

    var stagePredictionD1Error = new Kinetic.Stage({
        container: "kinetic1DError",
        width: 1220,
        height: 200
    });

    var stagePredictionD5 = new Kinetic.Stage({
        container: "kinetic5D",
        width: 1220,
        height: 200
    });

    var stagePredictionD5Error = new Kinetic.Stage({
        container: "kinetic5DError",
        width: 1220,
        height: 200
    });

    var stagePredictionD20 = new Kinetic.Stage({
        container: "kinetic20D",
        width: 1220,
        height: 200
    });

    var stagePredictionD20Error = new Kinetic.Stage({
        container: "kinetic20DError",
        width: 1220,
        height: 200
    });

    var stagePredictionD40 = new Kinetic.Stage({
        container: "kinetic40D",
        width: 1220,
        height: 200
    });

    var stagePredictionD40Error = new Kinetic.Stage({
        container: "kinetic40DError",
        width: 1220,
        height: 200
    });


    var layerChart = new Kinetic.Layer();
    var layerChartError = new Kinetic.Layer();

    var layerChartD5 = new Kinetic.Layer();
    var layerChartErrorD5 = new Kinetic.Layer();

    var layerChartD20 = new Kinetic.Layer();
    var layerChartErrorD20 = new Kinetic.Layer();

    var layerChartD40 = new Kinetic.Layer();
    var layerChartErrorD40 = new Kinetic.Layer();

    perf = data;

    chartRun({x:20,y:75}, layerChart, 'mlD1', 'yield', 'realyield', 'grey' , true, 1);
    chartRun({x:20,y:75}, layerChart, 'mlD1', 'realyield', 'yield', '#96B399', false,0.5);
    chartRun({x:20,y:75}, layerChartError, 'mlD1', 'error', 'error', 'orange', false, 0.6);

    chartRun({x:20,y:75}, layerChartD5, 'mlD5', 'yield', 'realyield', 'grey' , true, 1);
    chartRun({x:20,y:75}, layerChartD5, 'mlD5', 'realyield', 'yield', '#96B399', false,0.5);
    chartRun({x:20,y:75}, layerChartErrorD5, 'mlD5', 'error', 'error', 'orange', false, 0.6);

    chartRun({x:20,y:75}, layerChartD20, 'mlD20', 'yield', 'realyield', 'grey' , true, 1);
    chartRun({x:20,y:75}, layerChartD20, 'mlD20', 'realyield', 'yield', '#96B399', false,0.5);
    chartRun({x:20,y:75}, layerChartErrorD20, 'mlD20', 'error', 'error', 'orange', false, 0.6);

    chartRun({x:20,y:75}, layerChartD40, 'mlD40', 'yield', 'realyield', 'grey' , true, 1);
    chartRun({x:20,y:75}, layerChartD40, 'mlD40', 'realyield', 'yield', '#96B399', false,0.5);
    chartRun({x:20,y:75}, layerChartErrorD40, 'mlD40', 'error', 'error', 'orange', false, 0.6);

    stagePredictionD1.add(layerChart);
    stagePredictionD1Error.add(layerChartError);
    stagePredictionD5.add(layerChartD5);
    stagePredictionD5Error.add(layerChartErrorD5);
    stagePredictionD20.add(layerChartD20);
    stagePredictionD20Error.add(layerChartErrorD20);
    stagePredictionD40.add(layerChartD40);
    stagePredictionD40Error.add(layerChartErrorD40);

};



var SIZEX=380;
var SIZEY=140;
var marginX= 1220;


var marginInverseFrameY = 80;
var fontSize = 18;



/**
 *
 * @param list
 * @param f
 * @param h
 * @returns {number}
 */
function getMeanRun( list, f, c) {
    var mean = 0; var sizeNotNull = 0;
    for (var i=0;i<list.length;i++) {
        if (list[i][c]) {
            mean += list[i][c][f]
            sizeNotNull++;
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

function getmaxRun( list,f,c) {
    var max = 0;
    for (var i=0;i<list.length;i++) {
        if (list[i][c]) {
            var v = (list[i][c][f]);
            if (v > max) max = v;
        }
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

function getminRun( list,f,c) {
    var min = 100;
    for (var i=0;i<list.length;i++) {
        if (list[i][c]) {
            var v = (list[i][c][f]);
            if (v < min) min = v;
        }
    }
    return min;
}




/**
 * draw run graph
 * @param pos position in page
 * @param layer : add group to layer
 */
function chartRun(pos, layer, col, key, key2, color, dyn,opacity) {

    var group = new Kinetic.Group();
    var eltLength = perf.perfList.length;
    var eltSize = 2;
    var max = getmaxRun(perf.perfList, key, col);
    var max2 = getmaxRun(perf.perfList, key2, col);
    if (max2 > max) max = max2;
    var min = getminRun(perf.perfList, key, col);
    var min2 = getminRun(perf.perfList, key2, col);
    if (min2 < min) min = min2;
    var ref = Math.max(max , Math.abs(min));
    var heightM = ((SIZEY-20)/2)/ref;

    for (var i=0; i<perf.perfList.length;i++) {
        var dyncolor = color;

        if (perf.perfList[i][col]) {
            if (dyn === true && perf.perfList[i][col].sign === false)
                dyncolor = 'red'
            drawChart(group, pos, perf.perfList[i][col],key,  heightM, eltSize, i, perf.perfList[i][col].date, layer, dyncolor,opacity );
        }

    }

    drawChartMaxStage(group, pos, max, getMeanRun(perf.perfList, key , col),min, heightM, marginX);

    layer.add(group);
}




function drawChartMaxStage(group, pos, max ,mean, min, heightM, width) {
    group.add(new Kinetic.Text({
        x:pos.x-20,
        y: pos.y - heightM*max,
        text: max.toFixed(3),
        fontFamily: 'Calibri',
        fontSize: 10,
        fill: '#AC7969'
    }));

    group.add(new Kinetic.Rect({
        x: pos.x-12,
        y:  pos.y - heightM*max,
        width: width,
        height:1,
        fill: '#AC7969',
        opacity: 0.2
    }));


    group.add(new Kinetic.Text({
        x:pos.x-20,
        y:  pos.y - heightM*mean,
        text: mean.toFixed(3),
        fontFamily: 'Calibri',
        fontSize: 10,
        fill: '#AC7969'
    }));

    group.add(new Kinetic.Rect({
        x: pos.x-10,
        y:  pos.y - heightM*mean,
        width: width,
        height:1,
        fill: '#AC7969',
        opacity: 0.2
    }));

    group.add(new Kinetic.Text({
        x:pos.x-20,
        y:  pos.y - heightM*min ,
        text: min.toFixed(3),
        fontFamily: 'Calibri',
        fontSize: 10,
        fill: '#AC7969'
    }));

    group.add(new Kinetic.Rect({
        x: pos.x-10,
        y:  pos.y - heightM*min,
        width: width,
        height:1,
        fill: '#AC7969',
        opacity: 0.2
    }));

}

/**
 *
 * @param group
 * @param pos
 * @param elt
 * @param heightM
 * @param eltSize
 * @param i
 * @param text
 * @param layer
 * @param color
 */
function drawChart(group, pos, elt ,key , heightM, eltSize, i, text , layer, color, opacity) {
    var tooltip;

    var rectBack = new Kinetic.Rect({
        x: pos.x+i*(eltSize+2),
        y: pos.y*2-10,
        width: eltSize,
        height: -SIZEY+10,
        opacity: 0.3,
        fill: '#EDEAE6'
    });

    group.add(rectBack);

    /*if  (i%30==0) {

        group.add(new Kinetic.Rect({
            x: pos.x+i*(eltSize+2)+4,
            y: pos.y*2,
            width: 2,
            height:-marginInverseFrameY+20,
            opacity: 0.6,
            fill: '#AC7969'
        }));

        if (text != null) {
            group.add(new Kinetic.Text({
                x: pos.x+i*(eltSize+2),
                y: pos.y,
                text: text,
                fontSize: 8,
                fontFamily: 'Calibri',
                fill: '#AC7969',
                width: 150,
                align: 'left'
            }))}
    }*/







    var rect = new Kinetic.Rect({
        x: pos.x+i*(eltSize+2),
        y: pos.y,
        width: eltSize,
        height: -elt[key]*heightM,
        opacity: opacity,
        fill: color
    });


    var rectLayer = new Kinetic.Rect({
        x: pos.x+i*(eltSize+2)-2,
        y: pos.y,
        width: eltSize+4,
        height: -marginInverseFrameY+20
    });

    group.add(rect);
    group.add(rectLayer);

    var chartGroup = new Kinetic.Group();
    rectLayer.on('mouseover', function() {
        dist = 0;
        anim.start();
        group.add(chartGroup);
        layer.draw();
    });

    rectLayer.on('mouseout', function() {
        anim.stop();
        chartGroup.remove();
        layer.draw();
    });






    var dist =  0;
    var anim = new Kinetic.Animation(function() {
        chartGroup.destroyChildren();

        var spos = {x:pos.x/2,y:pos.y};
        if (i<42) spos.x+=90;

        /*dist += 35;
        if (dist >= 360) {
            anim.stop();
            dist = 370;
        }*/

        anim.stop();

        chartGroup.add(new Kinetic.Rect({
            x: pos.x+i*(eltSize+2),
            y: pos.y*2-10,
            width: eltSize+1,
            height: -SIZEY+10,
            fill: 'blue',
            opacity: 0.1
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

        drawRunPie(tooltip, spos, elt);

        chartGroup.add(tooltip);


        layer.draw();
    });


    function drawRunPie(tooltip, pos, elt) {



        tooltip.add(new Kinetic.Text({
            x:pos.x+10,
            y: -23,
            text: 'date:',
            fontFamily: 'Calibri',
            fontSize: 12,
            fill: 'black'
        }));

        tooltip.add(new Kinetic.Text({
            x:pos.x+40,
            y: -23,
            text: elt.date,
            fontFamily: 'Calibri',
            fontSize: 11,
            fill: 'black'
        }));



        tooltip.add(new Kinetic.Text({
            x:pos.x+10,
            y: -8,
            text: 'day value:',
            fontFamily: 'Calibri',
            fontSize: 12,
            fill: 'black'
        }));

        tooltip.add(new Kinetic.Text({
            x:pos.x+100,
            y: -8,
            text: elt.realvalue.toFixed(5),
            fontFamily: 'Calibri',
            fontSize: 12,
            fill: 'grey'
        }));


        var colorR  = elt.realyield < 0 ? 'red' : 'green'

        tooltip.add(new Kinetic.Text({
            x:pos.x+10,
            y: 7,
            text: 'real perf:',
            fontFamily: 'Calibri',
            fontSize: 12,
            fill: 'black'
        }));

        tooltip.add(new Kinetic.Text({
            x:pos.x+100,
            y: 7,
            text: elt.realyield.toFixed(5),
            fontFamily: 'Calibri',
            fontSize: 11,
            fill: colorR
        }));



        var colorP  = elt.yield < 0 ? 'red' : 'green'
        tooltip.add(new Kinetic.Text({
            x:pos.x+10,
            y: 22,
            text: 'prev perf:',
            fontFamily: 'Calibri',
            fontSize: 12,
            fill: 'black'
        }));

        tooltip.add(new Kinetic.Text({
            x:pos.x+100,
            y: 22,
            text: elt.yield.toFixed(5),
            fontFamily: 'Calibri',
            fontSize: 11,
            fill: colorP
        }));



        tooltip.add(new Kinetic.Text({
            x:pos.x+10,
            y: 37,
            text: 'real value:',
            fontFamily: 'Calibri',
            fontSize: 12,
            fill: 'black'
        }));

        tooltip.add(new Kinetic.Text({
            x:pos.x+100,
            y: 37,
            text: elt.realvalue.toFixed(5),
            fontFamily: 'Calibri',
            fontSize: 11,
            fill: colorR
        }));

        tooltip.add(new Kinetic.Text({
            x:pos.x+10,
            y: 52,
            text: 'prediction:',
            fontFamily: 'Calibri',
            fontSize: 12,
            fill: 'black'
        }));

        tooltip.add(new Kinetic.Text({
            x:pos.x+100,
            y: 52,
            text: elt.prediction.toFixed(5),
            fontFamily: 'Calibri',
            fontSize: 11,
            fill: colorP
        }));


/*

        private boolean sign;
        private double prediction;
        private double realvalue;
        private double currentValue;
        private double error;*/

    }

}
