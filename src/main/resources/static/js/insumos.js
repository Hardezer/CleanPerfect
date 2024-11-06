//Obtener los insumos de la base de datos y mostrarlos en la tabla
document.addEventListener('DOMContentLoaded', function() {
    fetch('/productos/insumo')
        .then(response => response.json())
        .then(data => {
            const tbody = document.getElementById('insumos-tbody');
            tbody.innerHTML = ''; // Limpiar cualquier contenido anterior
            data.forEach(insumo => {
                const row = document.createElement('tr');
                const cantidadDisplay = insumo.cantidad === 0 ? '<span style="color: red;">Sin Stock</span>' : insumo.cantidad;
                row.innerHTML = `
                    <td>${insumo.categoria}</td>
                    <td>${insumo.formato}</td>
                    <td>${formatCurrencyy(insumo.costoDeCompra)}</td>
                    <td>${cantidadDisplay}</td>
                    <td style="text-align: center;">
                        <button class="action-button edit-button" onclick="editarInsumo(${insumo.id})">
                        <img src="/css/editar.png" alt="Editar">
                        </button>
                        <button class="action-button change-state-button" onclick="cambiarCantidad(${insumo.id})">
                        <img src="/css/cambiar.png" alt="Estado">
                        </button>
                    </td>
                `;
                tbody.appendChild(row);
            });
        })
        .catch(error => console.error('Error fetching insumos:', error));
});

// Función para editar un insumo en la base de datos
function editarInsumo(id) {
    fetch(`/productos/${id}`)
        .then(response => response.json())
        .then(data => {
            // Llenar los campos visibles del formulario con los datos del producto
            document.getElementById('edit-id').value = data.id;
            document.getElementById('edit-codigoBarra').value = data.codigoBarra;
            document.getElementById('edit-costoDeCompra').value = data.costoDeCompra;
            document.getElementById('edit-cantidad').value = data.cantidad;

            // Llenar los campos ocultos con los datos actuales del producto
            document.getElementById('edit-tipo').value = data.tipo;
            document.getElementById('edit-categoria').value = data.categoria;
            document.getElementById('edit-formato').value = data.formato;

            // Mostrar el modal de edición
            document.getElementById('editModal').style.display = 'block';
        })
        .catch(error => console.error('Error fetching producto:', error));
}

// Función para actualizar un producto en la base de datos
function actualizarProducto() {
    const id = document.getElementById('edit-id').value;
    const producto = {
        codigoBarra: document.getElementById('edit-codigoBarra').value,
        costoDeCompra: document.getElementById('edit-costoDeCompra').value,
        cantidad: document.getElementById('edit-cantidad').value,
        // Tipo, categoría y formato no se envían explícitamente para mantener los originales
    };

    fetch(`/productos/${id}`, {
        method: 'PUT',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(producto)
    })
        .then(response => {
            if (response.ok) {
                Swal.fire({
                    icon: 'success',
                    title: 'Producto actualizado',
                    text: 'El insumo se ha actualizado correctamente.',
                    customClass: {
                        confirmButton: 'swal2-confirm'
                    }
                }).then(() => {
                    location.reload(); // Recargar la página después de la confirmación
                });
            } else {
                response.json().then(data => {
                    Swal.fire({
                        icon: 'error',
                        title: 'Error al actualizar',
                        text: data.message,
                        customClass: {
                            confirmButton: 'swal2-confirm'
                        }
                    });
                }).catch(error => {
                    console.error('Error al procesar respuesta JSON:', error);
                });
            }
        })
        .catch(error => console.error('Error updating producto:', error));
}

// Funcion para buscar productos por criterios
function buscarPorCriterios() {
    const inputCategoria = document.getElementById('searchCategoria').value.toLowerCase();
    const tbody = document.getElementById('insumos-tbody');
    const rows = tbody.getElementsByTagName('tr');

    for (let i = 0; i < rows.length; i++) { // Recorrer cada fila de la tabla
        const categoria = rows[i].getElementsByTagName('td')[0].textContent.toLowerCase();
        const tamano = rows[i].getElementsByTagName('td')[1].textContent.toLowerCase();
        const cantidad = rows[i].getElementsByTagName('td')[3].textContent.toLowerCase();

        if (categoria.includes(inputCategoria) || tamano.includes(inputCategoria) || cantidad.includes(inputCategoria)) {
            rows[i].style.display = ''; // Mostrar la fila si coincide con la búsqueda
        } else {
            rows[i].style.display = 'none'; // Ocultar la fila si no coincide con la búsqueda
        }
    }
}

// Eventos para buscar productos por categoría
document.getElementById('searchCategoria').addEventListener('keydown', function(event) {
    if (event.key === 'Enter') { // Al apretar la tecla ENTER se realiza la búsqueda
        buscarPorCriterios();
    }
});

// Evento para limpiar la búsqueda
document.getElementById('searchCategoria').addEventListener('input', function(event) {
    if (event.target.value === '') { // Si el campo de búsqueda está vacío, limpiar la búsqueda
        limpiarBusqueda();
    } else {
        buscarPorCriterios();
    }
});

// Funcion para limpiar la busqueda de productos
function limpiarBusqueda() {
    document.getElementById('searchCategoria').value = '';
    const tbody = document.getElementById('insumos-tbody');
    const rows = tbody.getElementsByTagName('tr');

    for (let i = 0; i < rows.length; i++) { // Recorrer cada fila de la tabla
        rows[i].style.display = '';
    }
}

// Funcion para dar formato a la moneda
function formatCurrencyy(value) {
    return `$${value.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ".")}`; // Formato de moneda
}

// Funcion para cambiar la cantidad de un producto a Sin Stock
function cambiarCantidad(id) {
    Swal.fire({
        title: '¿Estás seguro?',
        text: '¿Estás seguro de que deseas cambiar la cantidad del producto a Sin Stock?',
        icon: 'warning',
        showCancelButton: true,
        confirmButtonText: 'Sí, cambiar',
        cancelButtonText: 'No, cancelar',
        customClass: {
            confirmButton: 'swal2-confirm'
        }
    }).then((result) => {
        if (result.isConfirmed) {
            fetch(`/productos/${id}/cambiarCantidad`, { // Ruta para cambiar la cantidad de un producto a Sin Stock
                method: 'PUT',
                headers: {
                    'Content-Type': 'application/json'
                }
            })
                .then(response => {
                    if (response.ok) { // Si la respuesta es exitosa
                        Swal.fire({
                            icon: 'success',
                            title: 'Cambio registrado',
                            text: 'Cantidad del producto cambiada a Sin Stock.',
                            customClass: {
                                confirmButton: 'swal2-confirm'
                            }
                        }).then(() => {
                            location.reload(); // Recargar la página para actualizar los datos
                        });
                    } else {
                        response.json().then(data => { // Mostrar mensaje de error del backend
                            Swal.fire({
                                icon: 'error',
                                title: 'Error al cambiar la cantidad',
                                text: data.message,
                                customClass: {
                                    confirmButton: 'swal2-confirm'
                                }
                            });
                        }).catch(error => {
                            console.error('Error al procesar respuesta JSON:', error);
                        });
                    }
                })
                .catch(error => console.error('Error cambiando cantidad a Sin Stock del producto:', error));
        }
    });
}
