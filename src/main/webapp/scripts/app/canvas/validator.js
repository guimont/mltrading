function loadValidator(validator) {
    var data = {
        labels: [],
        datasets: [

            {
                label: "Validator D1",
                backgroundColor: "rgba(255,215,0,0.2)",
                borderColor: "rgba(255,215,0,1)",
                borderWidth: 1,
                data: []
            },
            {
                label: "Validator D2",
                backgroundColor: "rgba(255,140,0,0.2)",
                borderColor: "rgba(255,140,0,1)",
                borderWidth: 1,
                data: []
            }


        ]
    };



    for (var i=0; i < validator[0].label.length;i++) {
        data.labels[i] = validator[0].label[i]
        data.datasets[0].data[i] = validator[0].matrix[i]
        data.datasets[0].label = "Validator "+ validator[0].period
        data.datasets[1].data[i] = validator[1].matrix[i]
        data.datasets[1].label = "Validator "+ validator[1].period
    }


    var ctx = document.getElementById("Validator");


     new Chart(ctx, {
        type: "radar",
        data: data,
        options: {
            scale: {
                reverse: false,
                ticks: {
                    beginAtZero: true
                }
            }
        }
    });

}
