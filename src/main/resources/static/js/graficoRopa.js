document.addEventListener('DOMContentLoaded', function() {
    fetch('/salida/stockDataRopa')
        .then(response => response.json())
        .then(data => {
            console.log('Ropa:', data); // Depuración de datos

            if (data.length === 0) {
                data = [
                    { nombreEmpresa: 'Total', totalStock: 0, totalSinSalida: 0, stockSalido: 0 },
                    { nombreEmpresa: 'Ejemplo', totalStock: 0, totalSinSalida: 0, stockSalido: 0 }
                ];
            }

            const totalStockDataRopa = data.filter(item => item.nombreEmpresa === 'Total')[0];
            const stockSalidoDataRopa = data.filter(item => item.nombreEmpresa !== 'Total');

            const totalStockRopa = totalStockDataRopa ? totalStockDataRopa.totalStock : 0;
            const labelsRopa = stockSalidoDataRopa.map(item => item.nombreEmpresa);
            const stockSalidoRopa = stockSalidoDataRopa.map(item => item.stockSalido);

            labelsRopa.unshift('Total');
            stockSalidoRopa.unshift(0);

            const ctxRopa = document.getElementById('stockChartRopa').getContext('2d');
            new Chart(ctxRopa, {
                type: 'bar',
                data: {
                    labels: labelsRopa,
                    datasets: [{
                        label: 'Stock Total de ropa',
                        data: [totalStockRopa, ...Array(stockSalidoRopa.length - 1).fill(null)],
                        backgroundColor: 'rgba(54, 162, 235, 0.2)',
                        borderColor: 'rgba(54, 162, 235, 1)',
                        borderWidth: 1
                    }, {
                        label: 'Stock que ha salido',
                        data: stockSalidoRopa,
                        backgroundColor: 'rgba(255, 99, 132, 0.2)',
                        borderColor: 'rgba(255, 99, 132, 1)',
                        borderWidth: 1
                    }]
                },
                options: {
                    scales: {
                        y: {
                            beginAtZero: true
                        }
                    },
                    plugins: {
                        tooltip: {
                            callbacks: {
                                afterLabel: function(context) {
                                    const empresa = labelsRopa[context.dataIndex];
                                    return context.dataIndex === 0 ? '' : 'Cliente: ' + empresa;
                                }
                            }
                        }
                    },
                    barPercentage: 0.5,
                    categoryPercentage: 0.8
                }
            });
        })
        .catch(error => console.error('Error al obtener los datos de ropa:', error));
});
