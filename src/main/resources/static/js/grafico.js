document.addEventListener('DOMContentLoaded', function() {
    fetch('/salida/stockDataInsumos')
        .then(response => response.json())
        .then(data => {
            console.log('Insumos:', data); // Depuración de datos

            if (data.length === 0) {
                data = [
                    { nombreEmpresa: 'Total', totalStock: 0, totalSinSalida: 0, stockSalido: 0 },
                    { nombreEmpresa: 'Ejemplo', totalStock: 0, totalSinSalida: 0, stockSalido: 0 }
                ];
            }

            const totalStockDataInsumos = data.filter(item => item.nombreEmpresa === 'Total')[0];
            const stockSalidoDataInsumos = data.filter(item => item.nombreEmpresa !== 'Total' && item.nombreEmpresa !== 'Sin Empresa');

            const totalStockInsumos = totalStockDataInsumos ? totalStockDataInsumos.totalStock : 0;
            const labelsInsumos = stockSalidoDataInsumos.map(item => item.nombreEmpresa);
            const stockSalidoInsumos = stockSalidoDataInsumos.map(item => item.stockSalido);

            labelsInsumos.unshift('Total');
            stockSalidoInsumos.unshift(0);

            const ctxInsumos = document.getElementById('stockGraficoInsumos').getContext('2d');
            new Chart(ctxInsumos, {
                type: 'bar',
                data: {
                    labels: labelsInsumos,
                    datasets: [{
                        label: 'Stock Total de insumos',
                        data: [totalStockInsumos, ...Array(stockSalidoInsumos.length - 1).fill(null)],
                        backgroundColor: 'rgba(75, 192, 75, 0.2)',
                        borderColor: 'rgba(75, 192, 75, 1)',
                        borderWidth: 1
                    }, {
                        label: 'Stock que ha salido',
                        data: stockSalidoInsumos,
                        backgroundColor: 'rgba(255, 75, 75, 0.2)',
                        borderColor: 'rgba(255, 75, 75, 1)',
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
                                    const empresa = labelsInsumos[context.dataIndex];
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
        .catch(error => console.error('Error al obtener los datos de insumos:', error));
});
