function loadMap (data) {


    var stageMapRF = new Kinetic.Stage({
        container: "kineticMapRF",
        width: 220,
        height: 580
    });

    var stageMapGBT = new Kinetic.Stage({
        container: "kineticMapGBT",
        width: 220,
        height: 600
    });

    var layerChartRF = new Kinetic.Layer();
    var layerChartGBT = new Kinetic.Layer();


    drawMap({x:5,y:10}, data.rfd20 ,layerChartRF);
    drawMap({x:5,y:10}, data.gbtd20 ,layerChartGBT);


    stageMapRF.add(layerChartRF);
    stageMapGBT.add(layerChartGBT);

}



function drawMap(pos, data, layer) {


    var group = new Kinetic.Group();
    for (var i = 0; i < 110; i++) {
        for (var j = 0; j < 46; j++) {

            if( data[i * j + j ] > 0)

            group.add(new Kinetic.Rect({
                x: pos.x + j * 4,
                y: pos.y + i * 5,
                width: 3,
                height: 3,
                fill: 'white',
                opacity: 0.5
            }));

        }
    }

    layer.add(group)

}

