document.addEventListener('DOMContentLoaded', function() {
    fetch('/empresa/all')
        .then(response => response.json())
        .then(data => {
            const tbody = document.getElementById('clientes-tbody');
            tbody.innerHTML = ''; // Limpiar cualquier contenido anterior
            data.forEach(cliente => {
                const row = document.createElement('tr');

                // Asignar color dependiendo del estado
                const estadoColor = cliente.estado === 'Inactivo' ? 'red' : 'black'; // Usa rojo para "Inactivo"

                row.innerHTML = `
                    <td style="color: ${estadoColor};">${cliente.nombre}</td>
                    <td style="color: ${estadoColor};">${cliente.estado}</td>
                    <td style="text-align: center;">
                        <button class="action-button edit-button" onclick="editarCliente(${cliente.id})">
                        <img src="/css/editar.png" alt="Editar">
                        </button>
                        <button class="action-button change-state-button" onclick="cambioEstado(${cliente.id})">
                        <img src="/css/cambiar.png" alt="Estado">
                        </button>
                    </td>
                `;
                tbody.appendChild(row);
            });
        })
        .catch(error => console.error('Error fetching clientes:', error)); // Error al obtener clientes
});

function editarCliente(id) {
    fetch(`/empresa/${id}`)
        .then(response => response.json())
        .then(data => {
            document.getElementById('edit-id').value = data.id;
            document.getElementById('edit-nombre').value = data.nombre;
            document.getElementById('editModal').style.display = 'block';
        })
        .catch(error => console.error('Error fetching cliente:', error));
}

function actualizarCliente() {
    var id = document.getElementById("edit-id").value;
    var nombre = document.getElementById("edit-nombre").value;
    var estado = document.getElementById("edit-estado").value;

    var empresa = {
        id: id,
        nombre: nombre,
        estado: estado
    };

    fetch(`/empresa/${id}`, {
        method: 'PUT',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(empresa)
    })
        .then(response => {
            if (response.ok) {
                // Mostrar alerta de éxito
                Swal.fire({
                    title: 'Edición exitosa',
                    text: 'El cliente ha sido actualizado correctamente.\'',
                    icon: 'success',
                    customClass: {
                        confirmButton: 'swal2-confirm'
                    }
                }).then(() => {
                    location.reload();
                });
            } else {
                // Mostrar alerta de error si la actualización falla
                Swal.fire({
                    title: 'Error',
                    text: 'Hubo un problema al actualizar el cliente.',
                    icon: 'error',
                    customClass: {
                        confirmButton: 'swal2-confirm'
                    }
                });
                console.error('Error al actualizar la empresa');
            }
        })
        .catch(error => {
            // Mostrar alerta de error si hay un error en la solicitud fetch
            Swal.fire({
                title: 'Error',
                text: 'Hubo un problema al actualizar el cliente.',
                icon: 'error',
                customClass: {
                    confirmButton: 'swal2-confirm'
                }
            });
            console.error('Error:', error);
        });
}


function buscarPorCriterios() {
    const inputBusqueda = document.getElementById('searchNombre').value.toLowerCase();
    const tbody = document.getElementById('clientes-tbody');
    const rows = tbody.getElementsByTagName('tr');

    for (let i = 0; i < rows.length; i++) {
        const nombre = rows[i].getElementsByTagName('td')[0].textContent.toLowerCase();

        if (nombre.includes(inputBusqueda)) {
            rows[i].style.display = '';
        } else {
            rows[i].style.display = 'none';
        }
    }
}

document.getElementById('searchNombre').addEventListener('keydown', function(event) {
    if (event.key === 'Enter') { // Si se presiona la tecla "Enter"
        buscarPorCriterios();
    }
});

document.getElementById('searchNombre').addEventListener('input', function(event) {
    if (event.target.value === '') { // Si el input está vacío
        limpiarBusqueda();
    } else {
        buscarPorCriterios();
    }
});

function limpiarBusqueda() {
    document.getElementById('searchNombre').value = '';
    const tbody = document.getElementById('clientes-tbody');
    const rows = tbody.getElementsByTagName('tr');

    for (let i = 0; i < rows.length; i++) { // Recorrer cada fila de la tabla
        rows[i].style.display = '';
    }
}

function cambioEstado(id) {
    Swal.fire({
        title: '¿Estás seguro?',
        text: '¿Estás seguro de que deseas cambiar el estado de este cliente?',
        icon: 'warning',
        showCancelButton: true,
        confirmButtonText: 'Sí, cambiar',
        cancelButtonText: 'No, cancelar',
        customClass: {
            confirmButton: 'swal2-confirm'
        }
    }).then((result) => {
        if (result.isConfirmed) {
            fetch(`/empresa/${id}/cambioEstado`, {
                method: 'PUT',
            })
                .then(response => {
                    if (response.ok) {
                        Swal.fire({
                            title: 'Estado cambiado',
                            text: 'El estado del cliente se ha cambiado exitosamente.',
                            icon: 'success',
                            customClass: {
                                confirmButton: 'swal2-confirm'
                            }
                        }).then(() => {
                            location.reload();
                        });
                    } else {
                        Swal.fire({
                            title: 'Error',
                            text: 'Hubo un error al cambiar el estado del cliente.',
                            icon: 'error',
                            customClass: {
                                confirmButton: 'swal2-confirm'
                            }
                        });
                    }
                })
                .catch(error => {
                    Swal.fire({
                        title: 'Error',
                        text: 'Hubo un error al cambiar el estado del cliente.',
                        icon: 'error',
                        customClass: {
                            confirmButton: 'swal2-confirm'
                        }
                    });
                });
        }
    });
}
