/**
 * Created by gmo on 08/03/2016.
 */
var validator = [];



var SIZERESX=700;
var SIZERESY=400;
var marginRESX= 340;

/**
 * init stage kinetic canvas
 * init route for get parameter data
 * init websocket for new notification
 */
function loadValidator (data) {

    var stageResult = new Kinetic.Stage({
        container: "validator",
        width: SIZERESX,
        height: SIZERESY
    });


    var layerChart = new Kinetic.Layer();

    validator = data;

    //chartResult({x:30,y:20}, layerChart, true);


    //stageResult.add(layerChart);

};
