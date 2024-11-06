window.onload = function() {
    showTable('general');
};


function showTable(category) {
    const allTables = document.querySelectorAll('.company-tables');
    allTables.forEach(table => {
        table.style.display = 'none';
    });
    document.getElementById('report-' + category).style.display = 'block';

    if (category === 'insumos') {
        fetchInsumos();
    }
}

// Obtiene los insumo desde el backend y los muestra en una tabla //
function fetchInsumos() {
    fetch('/productos/insumo') // Ruta para obtener los insumos
        .then(response => response.json())
        .then(data => {
            const tbody = document.querySelector('#report-insumos tbody');
            tbody.innerHTML = ''; // Limpiar cualquier contenido anterior
            data.forEach(insumo => {
                const row = document.createElement('tr');
                row.innerHTML = `
                    <td>${insumo.nombre}</td>
                    <td>${insumo.tamaño}</td>
                    <td>${insumo.precio}</td>
                    <td style="text-align: center;">
                        <button onclick="editarInsumo(${insumo.id})">Editar</button>
                        <button onclick="eliminarInsumo(${insumo.id})">Eliminar</button>
                    </td>
                `;
                tbody.appendChild(row);
            });
        })
        .catch(error => console.error('Error fetching insumos:', error));
}

// Funcion para editar un insumo //
function editarInsumo(id) {
    // Implementa la lógica para editar un insumo
}

// Funcion para eliminar un insumo //
function eliminarInsumo(id) {
    // Implementa la lógica para eliminar un insumo
}

// Funcion para cerrar los modal //
function closeModal(type) {
    const modal = document.getElementById('modal-' + type);
    if (modal) {
        modal.style.display = 'none';
    }

    if (type === 'ingreso') {
        const formIngresa = document.getElementById('formIngresa');
        if (formIngresa) formIngresa.reset();
        const formInsumoo = document.getElementById('formInsumoo');
        if (formInsumoo) formInsumoo.classList.add('hidden');
        const formRopa = document.getElementById('formRopa');
        if (formRopa) formRopa.classList.add('hidden');

        // Limpiar errores
        const errorIdsIngreso = ["codigoProductoInsumo", "cantidadProductoInsumo", "nombreProductoRopa", "cantidadProductoRopa", "tallaProductoRopa"];
        errorIdsIngreso.forEach(id => {
            const element = document.getElementById(id);
            if (element) element.classList.remove("error");
        });

    } else if (type === 'empresa') {
        const formEmpresa = document.getElementById('formEmpresa');
        if (formEmpresa) formEmpresa.reset();
        const nombreEmpresa = document.getElementById("nombreEmpresa");
        if (nombreEmpresa) nombreEmpresa.classList.remove("error");

    } else if (type === 'producto') {
        const formInsumo = document.getElementById('formInsumo');
        if (formInsumo) formInsumo.reset();

        const elementsToHide = ["formProducto", "formInsumoFields", "formMaquinariaFields", "nuevaCategoria", "nuevoFormato", "formatoContainer"];
        elementsToHide.forEach(elementId => {
            const element = document.getElementById(elementId);
            if (element) element.style.display = "none";
        });

        // Limpiar errores
        const errorIdsProducto = ["nombreMaquinaria", "costoUsoMaquinaria", "codigoProducto", "cantidadProducto", "nombreCategoria", "unidadMedida", "tamañoCategoria", "unidadMedidaFormato", "tamañoFormato", "codigoBarraInsumo", "costoInsumo", "tipoProducto", "categoriaInsumo", "formatoCategoria", "nombreRopa", "tallaRopa", "precioRopa"];
        errorIdsProducto.forEach(id => {
            const element = document.getElementById(id);
            if (element) element.classList.remove("error");
        });

    } else if (type === 'entrada') {
        const formEntrada = document.getElementById('formEntrada');
        if (formEntrada) formEntrada.reset();
        const productosTable = document.querySelector('#productosTable tbody');
        if (productosTable) productosTable.innerHTML = '';
        verificarTipoProducto(); // Asegúrate de que esta función maneja correctamente la ausencia de elementos esperados.

        // Limpiar errores
        const errorIdsEntrada = ['idProductoInsumo', 'cantidadInsumo', 'productoRopa', 'tallaProducto', 'cantidadRopa'];
        errorIdsEntrada.forEach(id => {
            const element = document.getElementById(id);
            if (element) element.classList.remove('error');
        });
    } else if (type === 'salida') {
        const formSalida = document.getElementById('formSalida');
        if (formSalida) formSalida.reset();
        const productosTable = document.querySelector('#productosTableSal tbody');
        if (productosTable) productosTable.innerHTML = '';
        verificarTipoProductoSal(); // Asegúrate de que esta función maneja correctamente la ausencia de elementos esperados.

        const errorIdsSalida = ['empresaSalida', 'idProductoInsumoSal', 'cantidadInsumoSal', 'productoRopaSal', 'tallaProductoSal', 'cantidadRopaSal', 'idProductoMaquinariaSal', 'fechaSalida', 'fechaVuelta'];
        errorIdsSalida.forEach(id => {
            const element = document.getElementById(id);
            if (element) element.classList.remove('error');
        });

        // Limpiar campo de entrada de cliente y sugerencias
        const empresaSalida = document.getElementById('empresaSalida');
        if (empresaSalida) empresaSalida.value = '';

        const suggestionsContainer = document.querySelector('#empresaSalida ~ .suggestions-container .suggestions');
        if (suggestionsContainer) suggestionsContainer.innerHTML = '';

        // Limpiar las sugerencias de ropa
        const ropaSuggestionsContainer = document.querySelector('#productoRopaSal ~ .suggestions-container .suggestions');
        if (ropaSuggestionsContainer) ropaSuggestionsContainer.innerHTML = '';

        // Limpiar las sugerencias de maquinaria
        const maquinariaSuggestionsContainer = document.querySelector('#idProductoMaquinariaSal ~ .suggestions-container .suggestions');
        if (maquinariaSuggestionsContainer) maquinariaSuggestionsContainer.innerHTML = '';

        // Limpiar la cantidad disponible
        const cantidadDisponibleRopa = document.getElementById('cantidadDisponibleRopa');
        if (cantidadDisponibleRopa) cantidadDisponibleRopa.textContent = '';

        // Limpiar la cantidad disponible de insumos
        const cantidadDisponibleInsumo = document.getElementById('cantidadDisponibleInsumo');
        if (cantidadDisponibleInsumo) cantidadDisponibleInsumo.textContent = '';
    }

}

// Funcion para abrir los modal //
function openModal(type) {
    document.getElementById('modal-' + type).style.display = 'block';
}

// Cerrar modal al presionar la tecla Escape //
document.onkeydown = function(event) {
    if (event.key === "Escape") {
        const modals = document.querySelectorAll('.modal');
        modals.forEach(modal => {
            const type = modal.id.replace('modal-', '');
            closeModal(type);
        });
    }
};
// Funcion para verificar el tipo de producto //
function verificarTipo() {
    var tipo = document.getElementById("tipoProducto").value;
    if (tipo === "insumo") { // El tipo de producto es insumo
        document.getElementById("formProducto").style.display = "block";
        document.getElementById("formInsumoFields").style.display = "block";
        document.getElementById("formMaquinariaFields").style.display = "none";
        document.getElementById("formRopaFields").style.display = "none";
    } else if (tipo === "maquina") { // El tipo de producto es maquinaria
        document.getElementById("formProducto").style.display = "block";
        document.getElementById("formInsumoFields").style.display = "none";
        document.getElementById("formMaquinariaFields").style.display = "block";
        document.getElementById("formRopaFields").style.display = "none";
    } else if (tipo === "ropa") { // El tipo de producto es ropa
        document.getElementById("formProducto").style.display = "block";
        document.getElementById("formInsumoFields").style.display = "none";
        document.getElementById("formMaquinariaFields").style.display = "none";
        document.getElementById("formRopaFields").style.display = "block";
    } else {
        document.getElementById("formProducto").style.display = "none";
    }
}

async function descargarRespaldo() {
    try {
        const response = await fetch('/file/respaldo', {
            method: 'GET'
        });

        if (!response.ok) {
            throw new Error('Error al descargar el respaldo');
        }

        const blob = await response.blob();
        const url = window.URL.createObjectURL(blob);
        const a = document.createElement('a');
        a.style.display = 'none';
        a.href = url;
        a.download = 'respaldo.xlsx';
        document.body.appendChild(a);
        a.click();
        window.URL.revokeObjectURL(url);
        Swal.fire({
            icon: 'success',
            title: 'Respaldo descargado',
            customClass: {
                confirmButton: 'swal2-confirm'
            }
        });
    } catch (error) {
        console.error('Error:', error);
        Swal.fire({
            icon: 'error',
            title: 'Error',
            text: 'Error al descargar el respaldo.',
            customClass: {
                confirmButton: 'swal2-confirm'
            }
        });
    }
}

async function cargarRespaldo() {
    const fileInput = document.getElementById('fileRespaldo');
    if (!fileInput || !fileInput.files || fileInput.files.length === 0) {
        Swal.fire({
            icon: 'warning',
            title: 'Seleccione un archivo para cargar',
            customClass: {
                confirmButton: 'swal2-confirm'
            }
        });
        return;
    }

    const file = fileInput.files[0];
    const formData = new FormData();
    formData.append('file', file);

    try {
        const response = await fetch('/file/cargar', {
            method: 'POST',
            body: formData,
            headers: {
                'Accept': 'application/json',
            }
        });

        if (!response.ok) {
            throw new Error('Error al cargar el respaldo');
        }

        Swal.fire({
            icon: 'success',
            title: 'Respaldo cargado.',
            customClass: {
                confirmButton: 'swal2-confirm'
            }
        });
        closeModal('respaldo'); // Cerrar el modal después de subir el archivo
    } catch (error) {
        console.error('Error:', error);
        Swal.fire({
            icon: 'error',
            title: 'Error',
            text: 'Error al cargar el respaldo.',
            customClass: {
                confirmButton: 'swal2-confirm'
            }
        });
    }
}
