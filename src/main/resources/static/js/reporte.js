document.addEventListener("DOMContentLoaded", function() {
    function toggleDetails(id, button) {
        var element = document.getElementById(id);
        if (element) {
            if (element.style.display === 'none') {
                element.style.display = 'block';
                button.textContent = 'Ocultar Detalles';
            } else {
                element.style.display = 'none';
                button.textContent = 'Ver Detalles';
            }
        } else {
            console.log('No se encontró el elemento con ID:', id);
        }
    }

    function toggleSalidaDetails(id, button) {
        var element = document.getElementById(id);
        if (element) {
            if (element.style.display === 'none') {
                element.style.display = 'block';
                button.textContent = 'Ocultar Detalle';
            } else {
                element.style.display = 'none';
                button.textContent = 'Ver Detalle';
            }
        } else {
            console.log('No se encontró el elemento con ID:', id);
        }
    }

    function applyDateFilter() {
        var startDate = document.getElementById('start-date').value;
        var endDate = document.getElementById('end-date').value;
        var today = new Date().toISOString().split('T')[0];

        // Establecer fechas si están vacías
        if (!startDate && endDate) {
            startDate = '1900-01-01'; // Fecha mínima
        } else if (startDate && !endDate) {
            endDate = today; // Fecha actual
        }

        var empresas = document.querySelectorAll('.empresa-header');

        if (!startDate && !endDate) {
            empresas.forEach(empresa => {
                var salidas = empresa.nextElementSibling.querySelectorAll('.salida-detail');
                salidas.forEach(salida => {
                    salida.style.display = ''; // Muestra todas las salidas
                });
                empresa.parentElement.style.display = ''; // Muestra todas las empresas
            });
            document.getElementById('downloadReportBtn').style.display = 'none'; // Oculta el botón de descarga
            return;
        }

        empresas.forEach(empresa => {
            var salidas = empresa.nextElementSibling.querySelectorAll('.salida-detail');
            var isVisible = false;
            salidas.forEach(salida => {
                var fechaTexto = salida.querySelector('span').textContent;
                var fecha = fechaTexto.match(/\d{4}-\d{2}-\d{2}/)[0]; // Extrae la fecha en formato YYYY-MM-DD
                if (fecha >= startDate && fecha <= endDate) {
                    salida.style.display = ''; // Muestra la salida si está en el rango
                    isVisible = true;
                } else {
                    salida.style.display = 'none'; // Oculta la salida si no está en el rango
                }
            });
            empresa.parentElement.style.display = isVisible ? '' : 'none'; // Muestra u oculta toda la sección de la empresa
        });

        document.getElementById('downloadReportBtn').style.display = 'block'; // Muestra el botón de descarga
    }

    function resetFilter() {
        document.getElementById('start-date').value = '';
        document.getElementById('end-date').value = '';
        var empresas = document.querySelectorAll('.empresa-header');

        empresas.forEach(empresa => {
            var salidas = empresa.nextElementSibling.querySelectorAll('.salida-detail');
            salidas.forEach(salida => {
                salida.style.display = ''; // Muestra todas las salidas
            });
            empresa.parentElement.style.display = ''; // Muestra todas las empresas
        });

        document.getElementById('downloadReportBtn').style.display = 'none'; // Oculta el botón de descarga
    }

    function downloadReport() {
        var startDate = document.getElementById('start-date').value;
        var endDate = document.getElementById('end-date').value;
        var today = new Date().toISOString().split('T')[0];

        // Establecer fechas si están vacías
        if (!startDate && endDate) {
            startDate = '1900-01-01'; // Fecha mínima
        } else if (startDate && !endDate) {
            endDate = today; // Fecha actual
        }

        window.location.href = '/reportes/descargar-reporte?startDate=' + startDate + '&endDate=' + endDate;

        // Mostrar alerta de descarga completada
        Swal.fire({
            icon: 'success',
            title: 'Descarga completada',
            text: 'El reporte se ha descargado exitosamente.',
            customClass: {
                confirmButton: 'swal2-confirm'
            }

        });
    }

    // Exponer funciones globalmente
    window.toggleDetails = toggleDetails;
    window.toggleSalidaDetails = toggleSalidaDetails;
    window.applyDateFilter = applyDateFilter;
    window.resetFilter = resetFilter;
    window.downloadReport = downloadReport;
});
