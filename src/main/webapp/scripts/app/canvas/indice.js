/**
 * Created by gmo on 17/01/2017.
 */
/**
 * Created by gmo on 16/01/2017.
 */


function loadIndice(dataV, id, label,bdate) {

    var data = {
        labels: [],
        datasets: [
            {
                label: label,
                fill: false,
                backgroundColor: "rgba(75,192,192,0.4)",
                borderColor: "rgba(75,192,192,1)",
                borderCapStyle: 'butt',
                borderDash: [],
                borderDashOffset: 0.0,
                borderJoinStyle: 'miter',
                pointBorderColor: "rgba(75,192,192,1)",
                pointBackgroundColor: "#fff",
                pointBorderWidth: 1,
                pointHoverRadius: 5,
                pointHoverBackgroundColor: "rgba(75,192,192,1)",
                pointHoverBorderColor: "rgba(220,220,220,1)",
                pointHoverBorderWidth: 2,
                pointRadius: 1,
                pointHitRadius: 10,
                data: [],
                spanGaps: false
            }
        ]
    };

    for (var i=0; i<dataV.length;i++) {
        data.labels[i] = dataV[i].day.substring(5, 10)
        data.datasets[0].data[i] = dataV[i].value
    }

    var ctx = document.getElementById(id);
    var myBarChart = new Chart(ctx, {
            type: 'line',
            data: data,

            options: {
                legend: {
                    labels: {
                        usePointStyle: false,
                        boxWidth: 0
                    }
                },
                scales: {
                    xAxes: [{
                        display: bdate,
                        scaleLabel: {
                            display: true
                        },
                        fontSize: 8
                    }],
                    yAxes: [{
                        display: true,
                        scaleLabel: {
                            display: true,
                            labelString: 'Value'
                        }
                    }]
                },
                hover: {
                    mode: 'nearest',
                    intersect: true
                },
                responsive: true
            }
        }
    );
}
