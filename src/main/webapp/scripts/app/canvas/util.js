var marginInverseFrameY = 160;
var fontSize = 18;

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



function draw(pos, layer,c) {

        var group = new Kinetic.Group();
        var max = getmaxRunResult( result[c], 'value',0);
        var min = getminRunResult( result[c], 'value',100000);
        var heightM = (SIZERESY)/((max-min)*1.55);
        var marge =  heightM*4;

        for (var i=0; i<result[c].length;i++) {
            drawChartStock(group, pos, result[c], min, heightM,marge, i,  layer, 'blue');

        }

        drawGridRes(group,pos, max, max-min,  heightM, marge);

        layer.add(group);
}




function drawChartStock(group, pos, data,min, heightM, marge,  i ,  layer) {


    var line = new Kinetic.Rect({
        x: pos.x * 2 + i * (10),
        y: 60,
        width: 3,
        height: SIZERESY - 20,
        fill: 'lightgrey',
        opacity: 0.4
    });


    var ref = new Kinetic.Rect({
        x: pos.x * 2 + i * (10) - 5,
        y: 60,
        width: 10,
        height: SIZERESY - 20,
        opacity: 0
    });

    group.add(line);
    group.add(ref);


    if (i % 4 == 0) {

        group.add(new Kinetic.Rect({
            x: pos.x * 2 + i * (10),
            y: 55,
            width: 1,
            height: SIZERESY -80,
            opacity: 0.6,
            fill: '#AC7969'
        }));

        group.add(new Kinetic.Text({
            x: pos.x * 2 + i * (10)-2,
            y: SIZERESY - 10 - 10 * (i % 2),
            text: data[i].day.substring(6, 10),
            fontSize: 10,
            fontFamily: 'Calibri',
            fill: '#AC7969',
            width: 150,
            align: 'left'
        }));
    }


    if (data[i].value > 0) {
        var pred = new Kinetic.Circle({
            x: pos.x * 2 + i * (10) - 1,
            y: SIZERESY - (data[i].value - min) * heightM -marge,
            width: 3,
            opacity: 0.5,
            fill: 'grey'
        });
        group.add(pred);
    }



    layer.draw();

}

    function drawChart2(group, pos, elt ,heightM, eltSize, i , layer, color) {
    var tooltip;
    var text="text"

    var rectBack = new Kinetic.Rect({
        x: pos.x+i*(eltSize+2),
        y: pos.y,
        width: eltSize,
        height: -marginInverseFrameY+20,
        fill: '#F5F5F5'
    });

    group.add(rectBack);
    if  (i%4==0) {

        group.add(new Kinetic.Rect({
            x: pos.x+i*(eltSize+2)+4,
            y: pos.y,
            width: 1,
            height:-marginInverseFrameY+20,
            fill: '#AC7969'
        }));

        group.add(new Kinetic.Text({
            x: pos.x+i*(eltSize+2),
            y: pos.y,
            text: text,
            fontSize: 8,
            fontFamily: 'Calibri',
            fill: '#AC7969',
            width: 150,
            align: 'left'
        }));
    }


    var rect = new Kinetic.Rect({
        x: pos.x+i*(eltSize+2),
        y: pos.y,
        width: eltSize,
        height: -elt.value*heightM,
        fill: color
    });


    var rectLayer = new Kinetic.Rect({
        x: pos.x+i*(eltSize+2)-1,
        y: pos.y,
        width: eltSize+2,
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

        var spos = {x:pos.x-18,y:pos.y};
        if (i>14) spos.x-=100;



        dist += 35;
        if (dist >= 360) {
            anim.stop();
            dist = 370;
        }


        tooltip = new Kinetic.Label({
            x: spos.x+i*(eltSize+2)+4,
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

        //drawRunPie(tooltip, spos, elt,dist);

        chartGroup.add(tooltip);

        layer.draw();
    });

}


function drawChartMax(group, pos, max ,mean, heightM, width) {
    group.add(new Kinetic.Text({
        x:pos.x-40,
        y: 146 - heightM*max,
        text: max,
        fontFamily: 'Calibri',
        fontSize: 10,
        fill: '#AC7969'
    }));

    group.add(new Kinetic.Rect({
        x: pos.x-12,
        y: 150 - heightM*max,
        width: width,
        height:1,
        fill: '#AC7969',
        opacity: 0.2
    }));


    group.add(new Kinetic.Text({
        x:pos.x-40,
        y: 146 - heightM*mean,
        text: mean.toFixed(),
        fontFamily: 'Calibri',
        fontSize: 10,
        fill: '#AC7969'
    }));

    group.add(new Kinetic.Rect({
        x: pos.x-10,
        y: 150 - heightM*mean,
        width: width,
        height:1,
        fill: '#AC7969',
        opacity: 0.2
    }));

}


/**
 *
 * @param tooltip
 * @param pos
 * @param elt
 * @param max_ang
 */
function drawRunPie(tooltip, pos, elt, max_ang) {
    var angT = elt.terminate*360 / elt.global;
    var angA = elt.aborted*360 / elt.global;
    var angO = elt.timeover*360 / elt.global;
    var angE = elt.waitevent*360 / elt.global;


    /**
     * completed status
     */
    tooltip.add(new Kinetic.Rect({
        x:pos.x+82,
        y: -20,
        width: 8,
        height: 8,
        fill: '#71CE3B',
        shadowOffset: {x:1,y:1}
    }));

    tooltip.add(new Kinetic.Text({
        x:pos.x+95,
        y: -23,
        text: 'completed',
        fontFamily: 'Calibri',
        fontSize: 14,
        fill: 'black'
    }));

    tooltip.add(new Kinetic.Text({
        x:pos.x+95,
        y: -9,
        text: '('+elt.terminate+')',
        fontFamily: 'Calibri',
        fontSize: 10,
        fill: 'black'
    }));

    tooltip.add(new Kinetic.Arc({
        x: pos.x+40,
        y: pos.y-120,
        innerRadius: 30,
        outerRadius: 35,
        fill: '#71CE3B',
        strokeWidth: 0,
        lineJoin: "round",
        angle: Math.min(angT, max_ang),
        rotationDeg: -90
    }));

    if (max_ang < angT) return;


    /**
     * aborted status
     * @type {number}
     */

    tooltip.add(new Kinetic.Rect({
        x:pos.x+82,
        y: 5,
        width: 8,
        height: 8,
        fill: 'red',
        shadowOffset: {x:1,y:1}
    }));

    tooltip.add(new Kinetic.Text({
        x:pos.x+95,
        y: 2,
        text: 'aborted',
        fontFamily: 'Calibri',
        fontSize: 14,
        fill: 'black'
    }));

    tooltip.add(new Kinetic.Text({
        x:pos.x+95,
        y: 16,
        text: '('+elt.aborted+')',
        fontFamily: 'Calibri',
        fontSize: 10,
        fill: 'black'
    }));


    var angMax = angT + angA <= max_ang ? angA : angT+angA-max_ang;
    tooltip.add(new Kinetic.Arc({
        x: pos.x+40,
        y: pos.y-120,
        innerRadius: 30,
        outerRadius: 35,
        fill: 'red',
        strokeWidth: 0,
        lineJoin: "round",
        angle: angMax,
        rotationDeg: -90+angT
    }));

    if (max_ang < angT+angA) return;


    tooltip.add(new Kinetic.Rect({
        x:pos.x+82,
        y: 30,
        width: 8,
        height: 8,
        fill: 'orange',
        shadowOffset: {x:1,y:1}
    }));

    tooltip.add(new Kinetic.Text({
        x:pos.x+95,
        y: 27,
        text: 'time overrun',
        fontFamily: 'Calibri',
        fontSize: 14,
        fill: 'black'
    }));

    tooltip.add(new Kinetic.Text({
        x:pos.x+95,
        y: 39,
        text: '('+elt.timeover+')',
        fontFamily: 'Calibri',
        fontSize: 10,
        fill: 'black'
    }));

    angMax = angT + angA + angO <= max_ang ? angO : angT+angA+angO-max_ang;
    tooltip.add(new Kinetic.Arc({
        x: pos.x+40,
        y: pos.y-120,
        innerRadius: 30,
        outerRadius: 35,
        fill: 'orange',
        strokeWidth: 0,
        lineJoin: "round",
        angle: angMax,
        rotationDeg: -90+angT+angA
    }));


    if (max_ang < angT+angA+angO) return;

    angMax = angT + angA + angO + angE <= max_ang ? angE : angT + angA + angO + angE - max_ang;
    tooltip.add(new Kinetic.Arc({
        x: pos.x+40,
        y: pos.y-120,
        innerRadius: 30,
        outerRadius: 35,
        fill: 'yellow',
        strokeWidth: 0,
        lineJoin: "round",
        angle: angMax,
        rotationDeg: -90+angT+angA+angO

    }));


    tooltip.add(new Kinetic.Rect({
        x:pos.x+82,
        y: 55,
        width: 8,
        height: 8,
        fill: 'yellow',
        shadowOffset: {x:1,y:1}
    }));

    tooltip.add(new Kinetic.Text({
        x:pos.x+95,
        y: 52,
        text: 'wait event',
        fontFamily: 'Calibri',
        fontSize: 14,
        fill: 'black'
    }));

    tooltip.add(new Kinetic.Text({
        x:pos.x+95,
        y: 66,
        text: '('+elt.waitevent+')',
        fontFamily: 'Calibri',
        fontSize: 10,
        fill: 'black'
    }));



}
