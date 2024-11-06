window.onload = function() {
    showTable('general');
};

function showTable(category) {
    const allTables = document.querySelectorAll('.company-tables');
    allTables.forEach(table => {
        table.style.display = 'none';
    });
    document.getElementById('report-' + category).style.display = 'block';
}

// Función para abrir el modal de registro de entrada dependiendo del tipo de producto //
function verificarTipoProducto() {
    const tipoProducto = document.getElementById('tipoProductoEntrada').value;
    const formInsumo = document.getElementById('formInsumooo');
    const formRopa = document.getElementById('formRopaaa');
    const formMaquinaria = document.getElementById('formMa');

    // Ocultar todos los formularios al inicio
    formInsumo.style.display = 'none';
    formRopa.style.display = 'none';
    formMaquinaria.style.display = 'none';

    // Mostrar el formulario correspondiente según el tipo de producto seleccionado
    if (tipoProducto === 'insumo') {
        formInsumo.style.display = 'block';
    } else if (tipoProducto === 'ropa') {
        formRopa.style.display = 'block';
    } else if (tipoProducto === 'maquinaria') {
        formMaquinaria.style.display = 'block';
    }
}

function buscarMaquinaria(input) {
    var nombre = input.value.trim();
    var suggestionsContainer = input.parentElement.querySelector('.suggestions');
    var sugerenciasDiv = suggestionsContainer;

    // Limpiar sugerenciasDiv al inicio de la búsqueda
    sugerenciasDiv.innerHTML = '';

    if (nombre === "") { // Si el campo está vacío
        return;
    }

    fetch('/maquinaria/sugerenciasE?nombre=' + nombre) // Buscar sugerencias
        .then(response => response.json())
        .then(data => {
            if (data.length > 0) {
                data.forEach(maquinaria => {
                    // Crear un elemento de sugerencia
                    var suggestion = document.createElement('div');
                    suggestion.textContent = `${maquinaria.nombre} - Reingreso: ${maquinaria.fechaReingreso} - Empresa: ${maquinaria.empresa}`;
                    suggestion.classList.add('suggestion');

                    // Manejar clic en la sugerencia
                    suggestion.onclick = function() {
                        // Actualizar campo de entrada con la sugerencia seleccionada
                        input.value = maquinaria.nombre;

                        // Asignar el ID de la maquinaria al campo oculto
                        document.getElementById('id-Maquinaria').value = maquinaria.id;

                        // Limpiar sugerenciasDiv después de seleccionar
                        sugerenciasDiv.innerHTML = '';
                    };

                    // Agregar la sugerencia al contenedor de sugerencias
                    sugerenciasDiv.appendChild(suggestion);
                });
            } else {
                // Mostrar mensaje si no hay resultados
                sugerenciasDiv.innerHTML = '<div class="no-results">No se encontraron resultados</div>';
            }
        })
        .catch(error => {
            console.error('Error al obtener sugerencias:', error);
            sugerenciasDiv.innerHTML = '<div class="error-message">Error al obtener sugerencias</div>';
        });
}


// Función para agregar un producto a la tabla de productos // 
function agregarProducto(tipo) {
    const table = document.getElementById('productosTable').getElementsByTagName('tbody')[0];
    let producto, idTalla, cantidad;

    if (tipo === 'insumo') {
        const idProductoInsumo = document.getElementById('idProductoInsumo').value;
        const cantidadInsumo = document.getElementById('cantidadInsumo').value;

        if (idProductoInsumo.trim() === '' || cantidadInsumo.trim() === '') {
            Swal.fire({
                icon: 'warning',
                title: 'Campos incompletos',
                text: 'Por favor, complete todos los campos del insumo.',
                customClass: {
                    confirmButton: 'swal2-confirm'
                }
            });
            if (idProductoInsumo.trim() === '') document.getElementById('idProductoInsumo').classList.add('error');
            else document.getElementById('idProductoInsumo').classList.remove('error');
            if (cantidadInsumo.trim() === '') document.getElementById('cantidadInsumo').classList.add('error');
            else document.getElementById('cantidadInsumo').classList.remove('error');
            return;
        } else {
            document.getElementById('idProductoInsumo').classList.remove('error');
            document.getElementById('cantidadInsumo').classList.remove('error');
        }

        producto = "Insumo";
        idTalla = idProductoInsumo;
        cantidad = cantidadInsumo;
    } else if (tipo === 'ropa') {
        const productoRopa = document.getElementById('productoRopa').value;
        const tallaProducto = document.getElementById('tallaProducto').value;
        const cantidadRopa = document.getElementById('cantidadRopa').value;

        if (productoRopa.trim() === '' || tallaProducto.trim() === '' || cantidadRopa.trim() === '') {
            Swal.fire({
                icon: 'warning',
                title: 'Campos incompletos',
                text: 'Por favor, complete todos los campos de la ropa.',
                customClass: {
                    confirmButton: 'swal2-confirm'
                }
            });
            if (productoRopa.trim() === '') document.getElementById('productoRopa').classList.add('error');
            else document.getElementById('productoRopa').classList.remove('error');
            if (tallaProducto.trim() === '') document.getElementById('tallaProducto').classList.add('error');
            else document.getElementById('tallaProducto').classList.remove('error');
            if (cantidadRopa.trim() === '') document.getElementById('cantidadRopa').classList.add('error');
            else document.getElementById('cantidadRopa').classList.remove('error');
            return;
        } else {
            document.getElementById('productoRopa').classList.remove('error');
            document.getElementById('tallaProducto').classList.remove('error');
            document.getElementById('cantidadRopa').classList.remove('error');
        }

        producto = productoRopa;
        idTalla = tallaProducto;
        cantidad = cantidadRopa;
    } else if (tipo === 'maquinaria') {
        const nombreMaquinaria = document.getElementById('nombre-Maquinaria').value;
        const idMaquinaria = document.getElementById('id-Maquinaria').value;

        if (nombreMaquinaria.trim() === '' || idMaquinaria.trim() === '') {
            Swal.fire({
                icon: 'warning',
                title: 'Campos incompletos',
                text: 'Por favor, complete todos los campos de la maquinaria.',
                customClass: {
                    confirmButton: 'swal2-confirm'
                }
            });
            if (nombreMaquinaria.trim() === '') document.getElementById('nombre-Maquinaria').classList.add('error');
            else document.getElementById('nombre-Maquinaria').classList.remove('error');
            if (idMaquinaria.trim() === '') document.getElementById('id-Maquinaria').classList.add('error');
            else document.getElementById('id-Maquinaria').classList.remove('error');
            return;
        } else {
            document.getElementById('nombre-Maquinaria').classList.remove('error');
            document.getElementById('id-Maquinaria').classList.remove('error');
        }

        producto = "Maquinaria:" + " " + nombreMaquinaria;
        idTalla = idMaquinaria;
        cantidad = '1';
    }

    const newRow = table.insertRow();
    const cell1 = newRow.insertCell(0);
    const cell2 = newRow.insertCell(1);
    const cell3 = newRow.insertCell(2);
    const cell4 = newRow.insertCell(3);

    cell1.textContent = producto;
    cell2.textContent = idTalla;
    cell3.textContent = cantidad;
    cell4.innerHTML = `
        <button class="action-button delete-button" style="background-color: #f44336;" onclick="eliminarProducto(this,event)">
            <img src="/css/eliminar.png" alt="Eliminar">
        </button>
    `;

    // Limpiar los campos después de agregar el producto
    if (tipo === 'insumo') {
        document.getElementById('idProductoInsumo').value = '';
        document.getElementById('cantidadInsumo').value = '';
    } else if (tipo === 'ropa') {
        document.getElementById('productoRopa').value = '';
        document.getElementById('tallaProducto').value = '';
        document.getElementById('cantidadRopa').value = '';
    } else if (tipo === 'maquinaria') {
        document.getElementById('nombre-Maquinaria').value = '';
        document.getElementById('id-Maquinaria').value = '';
    }

    // Restablecer el formulario para ocultar todos los formularios y mostrar solo el selector
    document.getElementById('tipoProductoEntrada').value = '';
    verificarTipoProducto();
}

// Función para eliminar un producto de la tabla de productos //
function eliminarProducto(button,event) {
    // Confirmar si el usuario está seguro de eliminar el producto
    event.preventDefault();
    Swal.fire({
        title: '¿Estás seguro?',
        text: '¿Estás seguro deseas eliminar este item?',
        icon: 'warning',
        showCancelButton: true,
        confirmButtonText: 'Sí, cambiar',
        cancelButtonText: 'No, cancelar',
        customClass: {
            confirmButton: 'swal2-confirm'
        }
    }).then((result) => {
    if (result.isConfirmed) {
        const row = button.parentNode.parentNode;
        row.parentNode.removeChild(row);
        Swal.fire({
            icon: 'success',
            title: 'Producto eliminado',
            customClass: {
                confirmButton: 'swal2-confirm'
            }
        })
    }});

}

// Función para buscar un producto de ropa //
function buscarProductoo(input) {
    var nombre = input.value.trim();
    var suggestionsContainer = input.parentElement.querySelector('.suggestions');
    var sugerenciasDiv = suggestionsContainer;

    // Limpiar sugerenciasDiv al inicio de la búsqueda
    sugerenciasDiv.innerHTML = '';

    if (nombre === "") { // Si el campo está vacío
        return;
    }

    fetch('/ropa/sugerencias?nombre=' + nombre) // Buscar sugerencias
        .then(response => response.json())
        .then(data => {
            if (data.length > 0) {
                data.forEach(producto => {
                    // Crear un elemento de sugerencia
                    var suggestion = document.createElement('div');
                    suggestion.textContent = `${producto.nombre} - ${producto.talla}`;
                    suggestion.classList.add('suggestion');

                    // Manejar clic en la sugerencia
                    suggestion.onclick = function() {
                        // Actualizar campo de entrada con la sugerencia seleccionada
                        input.value = producto.nombre;

                        // Opcional: Asignar la talla a un campo oculto si lo necesitas
                        document.getElementById('tallaProducto').value = producto.talla;

                        // Limpiar sugerenciasDiv después de seleccionar
                        sugerenciasDiv.innerHTML = '';
                    };

                    // Agregar la sugerencia al contenedor de sugerencias
                    sugerenciasDiv.appendChild(suggestion);
                });
            } else {
                // Mostrar mensaje si no hay resultados
                sugerenciasDiv.innerHTML = '<div class="no-results">No se encontraron resultados</div>';
            }
        })
        .catch(error => {
            console.error('Error al obtener sugerencias:', error);
            sugerenciasDiv.innerHTML = '<div class="error-message">Error al obtener sugerencias</div>';
        });
}

// Función para registrar una entrada de productos en la base de datos //
function registrarEntrada() {
    const table = document.getElementById('productosTable').getElementsByTagName('tbody')[0];
    const rows = table.rows;

    if (rows.length === 0) { // Validar que haya productos para registrar
        Swal.fire({
            icon: 'warning',
            title: 'No hay productos',
            text: 'Agregue al menos un producto a la lista antes de enviar.',
            customClass: {
                confirmButton: 'swal2-confirm'
            }
        });
        return;
    }

    const productos = []; // Array para almacenar los productos
    for (let i = 0; i < rows.length; i++) {
        const cells = rows[i].cells;
        const producto = {
            nombre: cells[0].textContent.trim(),
            idTalla: cells[1].textContent.trim(),
            cantidad: cells[2].textContent.trim()
        };

        if (!producto.nombre || !producto.idTalla || !producto.cantidad) {
            Swal.fire({
                icon: 'warning',
                title: 'Campos incompletos',
                text: 'Por favor, complete todos los campos de la tabla de productos.',
                customClass: {
                    confirmButton: 'swal2-confirm'
                }
            });
        }

        productos.push(producto);
        console.log('producto:', producto);
    }

    console.log('productos:', productos)
    fetch('entradas/registrar', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify({ productos: productos })
    }).then(response => {
        if (response.ok) {
            Swal.fire({
                icon: 'success',
                title: 'Entrada registrada',
                text: 'La entrada de productos ha sido registrada exitosamente.',
                customClass: {
                    confirmButton: 'swal2-confirm'
                }
            }).then(() => {
                location.reload(); // Recargar la página después de la actualización
            });
            table.innerHTML = '';
            closeModal('entrada');
        } else {
            console.error('Error al registrar la entrada:', response);
            throw new Error('Error al registrar la entrada.');
        }
    }).catch(error => {
        console.error('Error al registrar la entrada:', error);
        Swal.fire({
            icon: 'error',
            title: 'Error',
            text: 'Ocurrió un error al registrar la entrada de productos.',
            customClass: {
                confirmButton: 'swal2-confirm'
            }
        });
    });


}


