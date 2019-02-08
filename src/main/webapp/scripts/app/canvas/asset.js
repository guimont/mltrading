


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
                    responsive: true,

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
                    categoryPercentage: 0.5,
                    tooltips: {
                        mode: 'index',
                        callbacks: {
                            // Use the footer callback to display the sum of the items showing in the tooltip

                            afterBody: function(tooltipItems) {
                                return 'buy date: ' + dataV[tooltipItems[0].index].dateBuyIn ;
                            },

                            beforeFooter: function(tooltipItems) {
                                return 'sell date: ' + dataV[tooltipItems[0].index].dateBuyOut ;
                            },


                            footer: function(tooltipItems) {

                                return 'price in: ' + dataV[tooltipItems[0].index].priceBuyIn + '\n price out '+ dataV[tooltipItems[0].index].priceBuyOut ;
                            },
                            afterFooter: function(tooltipItems) {

                                return 'priceStopLose: ' + dataV[tooltipItems[0].index].priceStopLose.toFixed(2) + '\n priceStopWin '+ dataV[tooltipItems[0].index].priceStopWin.toFixed(2) ;
                            },
                        },
                        footerFontStyle: 'normal'
                    }

                }
            }
        );
    }


}
