// Obtiene los productos de ropa desde el backend y los muestra en una tabla //
document.addEventListener('DOMContentLoaded', function() {
    fetch('/ropa/get')
        .then(response => response.json())
        .then(data => {
            const tbody = document.getElementById('tbodyProductosRopa');
            tbody.innerHTML = ''; // Limpiar cualquier contenido anterior
            data.forEach(producto => { // Recorrer la lista de productos de ropa
                const row = document.createElement('tr');
                const cantidad = producto.cantidad;

                row.innerHTML = `
                    <td>${producto.nombre}</td>
                    <td>${producto.talla}</td>
                    <td>${formatCurrencyyy(producto.precio)}</td>
                    <td style="${cantidad === 0 ? 'color: red;' : ''}">${cantidad === 0 ? 'Sin Stock' : cantidad}</td>
                    <td style="text-align: center;">
                        <button class="action-button edit-button" onclick="editarProductoRopa(${producto.id})">
                        <img src="/css/editar.png" alt="Editar">
                        </button>
                        <button class="action-button change-state-button" onclick="cambiaCantidadRopa(${producto.id})">
                        <img src="/css/cambiar.png" alt="Estado">
                        </button>
                    </td>
                `;
                tbody.appendChild(row);
            });
        })
        .catch(error => console.error('Error al cargar productos de ropa:', error));
});

// Funcion para editar un producto de ropa //
function editarProductoRopa(id) {
    fetch(`/ropa/update/${id}`) // Ruta para obtener un producto de ropa por su ID
        .then(response => response.json())
        .then(data => {
            document.getElementById('edit-id').value = data.id;
            document.getElementById('edit-nombre').value = data.nombre;
            document.getElementById('edit-talla').value = data.talla;
            document.getElementById('edit-precio').value = data.precio;
            document.getElementById('edit-cantidad').value = data.cantidad;
            document.getElementById('editModal').style.display = 'block';
        })
        .catch(error => console.error('Error al cargar producto de ropa para editar:', error));
}

// Funcion para guardar la edicion de un producto de ropa //
function guardarEdicion() {
    const id = document.getElementById('edit-id').value;
    const nombre = document.getElementById('edit-nombre').value;
    const talla = document.getElementById('edit-talla').value;
    const precio = document.getElementById('edit-precio').value;
    const cantidad = document.getElementById('edit-cantidad').value;

    const data = { id, nombre, talla, precio, cantidad };

    fetch(`/ropa/update`, {
        method: 'PUT',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(data)
    })
        .then(response => {
            if (response.ok) {
                Swal.fire({
                    icon: 'success',
                    title: 'Edición exitosa',
                    text: 'El producto de ropa ha sido actualizado correctamente.',
                    customClass: {
                        confirmButton: 'swal2-confirm'
                    }
                }).then(() => {
                    location.reload();
                });
            } else {
                Swal.fire({
                    icon: 'error',
                    title: 'Error al guardar edición',
                    text: 'Hubo un problema al guardar la edición del producto.',
                    customClass: {
                        confirmButton: 'swal2-confirm'
                    }
                });
            }
        })
        .catch(error => {
            Swal.fire({
                icon: 'error',
                title: 'Error al guardar edición',
                text: 'Hubo un problema al guardar la edición del producto.',
                customClass: {
                    confirmButton: 'swal2-confirm'
                }
            });
        });
}



// Funcion para buscar productos de ropa por nombre, talla o cantidad //
function buscarPorCriteriosRopa() {
    const textoBusqueda = document.getElementById('searchNombreTallaCantidad').value.toLowerCase();
    const filas = document.querySelectorAll('#tbodyProductosRopa tr');

    filas.forEach(fila => {
        const nombre = fila.getElementsByTagName('td')[0].innerText.toLowerCase();
        const talla = fila.getElementsByTagName('td')[1].innerText.toLowerCase();
        const cantidad = fila.getElementsByTagName('td')[3].innerText.toLowerCase();

        if (nombre.includes(textoBusqueda) || talla.includes(textoBusqueda) || cantidad.includes(textoBusqueda)) {
            fila.style.display = '';
        } else {
            fila.style.display = 'none';
        }
    });
}

// Funcion para buscar productos de ropa por nombre, talla o cantidad mediante la Tecla Enter //
document.getElementById('searchNombreTallaCantidad').addEventListener('keydown', function(event) { // Al apretar la tecla ENTER se realiza la búsqueda
    if (event.key === 'Enter') { // Al apretar la tecla ENTER se realiza la búsqueda
        buscarPorCriteriosRopa();
    }
});

// Funcion para buscar productos de ropa por nombre, talla o cantidad mediante el evento Input //
document.getElementById('searchNombreTallaCantidad').addEventListener('input', function(event) {
    if (event.target.value === '') { // Si el campo de búsqueda está vacío, limpiar la búsqueda
        limpiarBusqueda(); 
    } else {
        buscarPorCriteriosRopa();
    }
});

// Funcion para limpiar la busqueda de productos de ropa //
function limpiarBusqueda() {
    document.getElementById('searchNombreTallaCantidad').value = ''; // Limpiar el campo de búsqueda
    const tbody = document.getElementById('tbodyProductosRopa');
    const filas = tbody.getElementsByTagName('tr');

    for (let i = 0; i < filas.length; i++) {
        filas[i].style.display = ''; // Mostrar todas las filas
    }
}

// Funcion para dar formato a la moneda //
function formatCurrencyyy(value) {
    return `$${value.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ".")}`;
}

// Funcion para cambiar la cantidad de un producto de ropa a Sin Stock //
function cambiaCantidadRopa(id) {
    Swal.fire({
        title: '¿Estás seguro?',
        text: '¿Deseas cambiar la cantidad de esta ropa a Sin Stock?',
        icon: 'warning',
        showCancelButton: true,
        confirmButtonText: 'Sí, cambiar',
        cancelButtonText: 'No, cancelar',
        customClass: {
            confirmButton: 'swal2-confirm'
        }}).then((result) => {
        if (result.isConfirmed) {
            fetch(`/ropa/${id}/cambiaCantidad`, { // Ruta para cambiar la cantidad de un producto de ropa a Sin Stock
                method: 'PUT',
                headers: {
                    'Content-Type': 'application/json'
                }
            })
                .then(response => {
                    if (response.ok) {
                        Swal.fire({
                            icon: 'success',
                            title: 'Cambio registrado',
                            text: 'Cantidad del producto cambiada a Sin Stock.',
                            customClass: {
                                confirmButton: 'swal2-confirm'
                            }
                        })
                        location.reload(); // Recargar la página para actualizar los datos
                    } else {
                        response.json().then(data => {
                            Swal.fire({
                                icon: 'error',
                                title: 'Error al cambiar la cantidad',
                                text: data.message,
                                customClass: {
                                    confirmButton: 'swal2-confirm'
                                }

                            })
                        }).catch(error => {
                            console.error('Error al procesar respuesta JSON:', error); // Error al procesar la respuesta JSON
                        });
                    }
                })
                .catch(error => console.error('Error cambiando cantidad a Sin Stock del producto', error)); // Error al cambiar la cantidad del producto a Sin Stock
        }
    });
}
