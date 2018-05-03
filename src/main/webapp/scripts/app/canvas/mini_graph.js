
var SIZERESX_MG=300;
var RSIZERESX_MG=300;
var SIZERESY_MG=80;


function graph(data) {

    var stageGraph1 = new Kinetic.Stage({
        container: "Selected_0",
        width: 300,
        height: 80
    });

    var stageGraph2 = new Kinetic.Stage({
        container: "Selected_1",
        width: 300,
        height: 80
    });

    var stageGraph3 = new Kinetic.Stage({
        container: "Selected_2",
        width: 300,
        height: 80
    });

    var stageGraph4 = new Kinetic.Stage({
        container: "Selected_3",
        width: 300,
        height: 80
    });

    perf = data;
    var layerGraph1 = new Kinetic.Layer();
    chartGraph({x:10,y:0}, layerGraph1, 0);
    stageGraph1.add(layerGraph1);

    var layerGraph2 = new Kinetic.Layer();
    chartGraph({x:10,y:0}, layerGraph2, 1);
    stageGraph2.add(layerGraph2);

    var layerGraph3 = new Kinetic.Layer();
    chartGraph({x:10,y:0}, layerGraph3, 2);
    stageGraph3.add(layerGraph3);

    var layerGraph4 = new Kinetic.Layer();
    chartGraph({x:10,y:0}, layerGraph4, 3);
    stageGraph4.add(layerGraph4);



}


function chartGraph(pos, layer, indice) {

    var group = new Kinetic.Group();
    var max = getmaxRunResult( perf[indice].data, 'value', 0);
    max = getmaxRunResult( perf[indice].data, 'opening', max);
    //max = getMax( perf[indice].prediction.yieldD20, max);

    var min = getminRunResult( perf[indice].data, 'value',100000);
    min = getminRunResult( perf[indice].data, 'opening',min);
    //min = getMin( perf[indice].prediction.yieldD20, min);

    var heightM = (SIZERESY_MG)/((max-min)*3);
    var marge =  40;

    for (var i=0; i < 20;i++) {
        drawValueGraph(group, pos, perf[indice].data, min,  heightM, marge, i, layer);
    }

    drawPredictionGraph(group, pos, perf[indice].data, min,  heightM, marge, i, layer, perf[indice].prediction.yieldD20);


    drawGridResGraph(group,pos, max, max-min,  heightM, marge);

    layer.add(group);
}


function getMax(value, max) {
    var v = Math.abs(value);
    if (v > max) max = v;
    return max;
}

function getMin(value, min) {
    var v = Math.abs(value);
    if (v < min) min = v;
    return min;
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

var margeX_MG = 10
function drawPredictionGraph(group, pos, data,min, heightM, marge,  i ,  layer , yieldD20) {

    var line = new Kinetic.Rect({
        x: 279,
        y: 00,
        width: 5,
        height:SIZERESY_MG-10,
        fill: 'lightgrey',
        opacity: 0.2
    });


    var yieldD20pred = data[39].value  + (data[39].value / 100. * yieldD20);
    var height = 10;
    var color = 'DarkOrchid'


    var predValue = new Kinetic.Rect({
        x: 280,
        y: SIZERESY_MG - (yieldD20pred - min) * heightM  - marge + height/2 ,
        width: 3,
        opacity: 1,
        height: height ,
        fill: color
    });
    group.add(predValue);




    group.add(line);
    layer.add(group);

}



function drawValueGraph(group, pos, data,min, heightM, marge,  i ,  layer) {


    var line = new Kinetic.Rect({
        x: pos.x * 2 + i *(margeX_MG),
        y: 00,
        width: 5,
        height:SIZERESY_MG-10,
        fill: 'lightgrey',
        opacity: 0.2
    });


    var ref = new Kinetic.Rect({
        x: pos.x * 2 + i *(margeX_MG)-5,
        y: 00,
        width: 10,
        height:SIZERESY_MG-10,
        opacity: 0
    });

    group.add(line);
    //group.add(ref);


    if  (i%4==0) {

        group.add(new Kinetic.Rect({
            x: pos.x * 2 + i *(margeX_MG)+2,
            y: 0,
            width: 1,
            height:SIZERESY_MG-10,
            opacity: 0.4,
            fill: '#AC7969'
        }));

        group.add(new Kinetic.Text({
            x: pos.x * 2 + i *(margeX_MG),
            y: SIZERESY_MG-10-10*(i%2),
            text: data[i+20].date,
            fontSize: 8,
            fontFamily: 'Calibri',
            fill: '#AC7969',
            width: 150,
            align: 'left'
        }));
    }

    var subopacity = 1
    var color = 'grey'

    if (data[i+20].value < data[i+20].opening) {
        color = '#997A8D'
        subopacity = 0.5
    }
    var height = (data[i+20].opening - data[i+20].value)  * heightM * 2;
    //height = Math.max(Math.abs(height),20)
    height = Math.abs(height)
    if (data[i+20].value > 0) {
        var pred = new Kinetic.Rect({
            x: pos.x * 2 + i * (margeX_MG) + 1,
            y: SIZERESY_MG - (data[i+20].value - min) * heightM  - marge,
            width: 3,
            opacity: subopacity,
            height: height ,
            fill: color
        });
        group.add(pred);
    }



    layer.add(group);

}


function showPred() {

}


function  drawGridResGraph(group, pos, maxV, max,  heightM, marge) {

   /* group.add(new Kinetic.Rect({
        x: pos.x,
        y: SIZERESY_MG-20,
        width: RSIZERESX_MG,
        height: 1,
        fill: 'lightgrey'

    }));

    group.add(new Kinetic.Rect({
        x: pos.x,
        y: 20,
        width: 1,
        height:SIZERESY_MG-40,
        fill: 'lightgrey'

    }));

*/
    group.add(new Kinetic.Rect({
        x: pos.x,
        y: SIZERESY_MG - max * heightM  - marge,
        width: RSIZERESX_MG,
        height: 1,
        fill: 'AC7969',
        opacity: 0.1

    }));

    group.add(new Kinetic.Text({
        x:0,
        y: SIZERESY_MG - max * heightM  - marge,
        text: maxV.toFixed(2),
        fontFamily: 'Calibri',
        fontSize: 9,
        fill: 'black'

    }));

    group.add(new Kinetic.Text({
        x:0,
        y: SIZERESY_MG + max * heightM  - marge,
        text: (maxV - 2*max).toFixed(2),
        fontFamily: 'Calibri',
        fontSize: 9,
        fill: 'black'

    }));


}
