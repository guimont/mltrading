/**
 * Created by gmo on 17/01/2017.
 */
/**
 * Created by gmo on 16/01/2017.
 */


function loadIndice(dataV, id, label,bdate, color) {

    var data = {
        labels: [],
        datasets: [
            {
                label: label,
                fill: origin,
                backgroundColor: color,
                //borderColor: "rgba(75,192,192,1)",
                borderColor: color,

                borderDash: [],
                borderDashOffset: 0.0,

                //pointBorderColor: "rgba(75,192,192,1)",
                pointBorderColor:color,
                pointBackgroundColor: "#fff",
                pointBorderWidth: 1,
                pointHoverRadius: 5,
                pointHoverBackgroundColor: "rgba(75,192,192,1)",
                pointHoverBorderColor: "rgba(220,220,220,1)",
                pointHoverBorderWidth: 2,
                pointRadius: 2,
                pointHitRadius: 10,
                data: [],
                spanGaps: true,

            }
        ]
    };

    for (var i=0; i<dataV.length;i++) {
        data.labels[i] = dataV[i].day.substring(5, 10)
        data.datasets[0].data[i] = dataV[i].value
    }
    var timeFormat = 'MM/DD/YYYY'

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
                        display : bdate,
                        distribution: 'series',
                        type: 'time',
                        bounds: 'data',
                        time: {
                            parser: timeFormat
                        },
                        scaleLabel: {
                            display: true
                        },

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

                    intersect: true
                },
                responsive: true
            }
        }
    );
}
