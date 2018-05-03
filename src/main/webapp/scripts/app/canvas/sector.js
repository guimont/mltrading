/**
 * Created by gmo on 16/01/2017.
 */


function loadSector(dataV) {

    var data = {
        labels: [],
        datasets: [
            {
                label: "Sector variation daily",
                backgroundColor: [],
                borderColor: [],
                borderWidth: 1,
                data: []
            }
        ]
    };

    for (var i=0; i<dataV.length;i++) {
        data.labels[i] = dataV[i].code
        data.datasets[0].data[i] = dataV[i].variation
        if (dataV[i].variation < 0)
            data.datasets[0].backgroundColor[i] = "rgba(237, 85, 101, 0.6)"
        else
            data.datasets[0].backgroundColor[i] = "rgba(72, 207, 173, 0.6)"
    }

    var ctx = document.getElementById("sectorPanel");
    var myBarChart = new Chart(ctx, {
        type: 'bar',
        data: data,

        options: {


        legend: {
            labels: {
                usePointStyle: false,
                boxWidth: 0
            }
        },
        scales: {
            yAxes: [{
                ticks: {
                    beginAtZero:true
                }
            }]
        },
            categoryPercentage: 0.5


    }
    }
    );


}
