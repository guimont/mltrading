


function loadAssetHistory(list) {

    for (var j = 0; j < list.length; j++) {

        var dataV =  list[j].assetStockList;

        var data = {
            labels: [],
            datasets: [
                {
                    label: "History asset",
                    backgroundColor: [],
                    borderColor: [],
                    borderWidth: 1,
                    data: []
                }
            ]
        };

        for (var i = 0; i < dataV.length; i++) {
            data.labels[i] = dataV[i].code
            data.datasets[0].data[i] = dataV[i].margin
            if (dataV[i].margin < 0)
                data.datasets[0].backgroundColor[i] = "rgba(237, 85, 101, 0.6)"
            else
                data.datasets[0].backgroundColor[i] = "rgba(72, 207, 173, 0.6)"
        }


        var ctx = document.getElementById("CANVAS_"+list[j].id);
        //var ctx = document.getElementById("CANVAS_1");

        if (ctx == null) return;
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
                                beginAtZero: true
                            }
                        }]
                    },
                    categoryPercentage: 0.5


                }
            }
        );
    }


}
