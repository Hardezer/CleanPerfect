// Obtiene la lista de maquinaria y la muestra en la tabla
document.addEventListener('DOMContentLoaded', function() {
    fetch('/maquinaria')
        .then(response => response.json())
        .then(data => {
            const tbody = document.getElementById('maquinaria-tbody');
            tbody.innerHTML = ''; // Limpiar cualquier contenido anterior
            const currentDate = new Date(); // Fecha actual

            data.forEach(maquinaria => {
                fetch(`/empresa/${maquinaria.empresa}`) // Suponiendo que tienes una ruta para obtener la empresa por ID
                    .then(response => response.json())
                    .then(empresaData => {
                        const row = document.createElement('tr');
                        let estadoColor = maquinaria.estado === 'No disponible' ? 'red' : 'black';
                        const empresaNombre = empresaData && empresaData.nombre ? empresaData.nombre : 'En bodega';
                        let rowColor = '';

                        // Verificar si la fecha de reingreso es pasada o igual a la fecha actual
                        if (maquinaria.fechaReingreso) {
                            const fechaReingreso = new Date(maquinaria.fechaReingreso);
                            if (fechaReingreso <= currentDate) {
                                rowColor = 'red'; // Cambiar el color de la fila a rojo
                            }
                        }

                        row.innerHTML = `
                            <td>${maquinaria.nombre}</td>
                            <td>${formatCurrency(maquinaria.costoUso)}</td>
                            <td style="color: ${estadoColor};">${maquinaria.estado}</td>
                            <td>${maquinaria.fechaSalida}</td>
                            <td>${maquinaria.fechaReingreso}</td>
                            <td>${empresaNombre}</td>
                            <td style="text-align: center;">
                                <button class="action-button edit-button" onclick="editarMaquinaria(${maquinaria.id})">
                                    <img src="/css/editar.png" alt="Editar">
                                </button>
                                <button class="action-button change-state-button" onclick="cambiarEstado(${maquinaria.id})">
                                    <img src="/css/cambiar.png" alt="Estado">
                                </button>
                            </td>
                        `;
                        if (rowColor) {
                            row.style.color = rowColor;
                        }
                        tbody.appendChild(row);
                    })
                    .catch(error => console.error('Error fetching empresa:', error));
            });
        })
        .catch(error => console.error('Error fetching maquinaria:', error));
});

// Función para editar una maquinaria
function editarMaquinaria(id) {
    fetch(`/maquinaria/${id}`)
        .then(response => response.json())
        .then(data => {
            document.getElementById('edit-id').value = data.id;
            document.getElementById('edit-nombre').value = data.nombre;
            document.getElementById('edit-costoUso').value = data.costoUso;
            document.getElementById('edit-estado').value = data.estado;
            document.getElementById('edit-fechaSalida').value = data.fechaSalida ? data.fechaSalida.substring(0, 10) : '';
            document.getElementById('edit-fechaReingreso').value = data.fechaReingreso ? data.fechaReingreso.substring(0, 10) : '';
            document.getElementById('edit-empresa').value = data.empresa;  // Asegúrate de que este campo se llene correctamente
            document.getElementById('editModal').style.display = 'block';
        })
        .catch(error => console.error('Error fetching maquinaria:', error));
}

function actualizarMaquinaria() {
    const id = document.getElementById('edit-id').value;
    const maquinaria = {
        id: id,
        nombre: document.getElementById('edit-nombre').value,
        costoUso: document.getElementById('edit-costoUso').value,
        estado: document.getElementById('edit-estado').value,
        fechaSalida: document.getElementById('edit-fechaSalida').value,
        fechaReingreso: document.getElementById('edit-fechaReingreso').value,
        empresa: document.getElementById('edit-empresa').value  // Asegúrate de que el campo empresa esté presente
    };

    fetch(`/maquinaria/${id}`, { // Hace una petición PUT a la API para actualizar la maquinaria
        method: 'PUT',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(maquinaria)
    })
        .then(response => {
            if (response.ok) {
                Swal.fire({
                    icon: 'success',
                    title: 'Actualización exitosa',
                    text: 'La maquinaria ha sido actualizada correctamente.',
                    customClass: {
                        confirmButton: 'swal2-confirm'
                    }
                }).then(() => {
                    location.reload(); // Recargar la página después de la actualización
                });
            } else {
                Swal.fire({
                    icon: 'error',
                    title: 'Error al actualizar',
                    text: 'Hubo un problema al actualizar la maquinaria.',
                    customClass: {
                        confirmButton: 'swal2-confirm'
                    }
                });
                console.error('Error updating maquinaria');
            }
        })
        .catch(error => {
            Swal.fire({
                icon: 'error',
                title: 'Error al actualizar',
                text: 'Hubo un problema al actualizar la maquinaria.',
                customClass: {
                    confirmButton: 'swal2-confirm'
                }
            });
            console.error('Error updating maquinaria:', error);
        });
}

// Función para buscar maquinaria por criterios
function buscarPorCriteriosM() {
    const inputBusqueda = document.getElementById('searchNombre').value.toLowerCase();
    const tbody = document.getElementById('maquinaria-tbody');
    const rows = tbody.getElementsByTagName('tr');

    for (let i = 0; i < rows.length; i++) {
        const nombre = rows[i].getElementsByTagName('td')[0].textContent.toLowerCase();
        const estado = rows[i].getElementsByTagName('td')[2].textContent.toLowerCase();
        const fechaSalida = rows[i].getElementsByTagName('td')[3].textContent.toLowerCase();
        const fechaReingreso = rows[i].getElementsByTagName('td')[4].textContent.toLowerCase();

        if (nombre.includes(inputBusqueda) ||
            estado.includes(inputBusqueda) ||
            fechaSalida.includes(inputBusqueda) ||
            fechaReingreso.includes(inputBusqueda)) {
            rows[i].style.display = '';
        } else {
            rows[i].style.display = 'none';
        }
    }
}

// Eventos para buscar maquinaria por criterios
document.getElementById('searchNombre').addEventListener('keydown', function(event) {
    if (event.key === 'Enter') { // Al apretar la tecla ENTER se realiza la búsqueda
        buscarPorCriteriosM();
    }
});

// Evento para limpiar la búsqueda
document.getElementById('searchNombre').addEventListener('input', function(event) {
    if (event.target.value === '') { // Si el campo de búsqueda está vacío, limpia la búsqueda
        limpiarBusqueda();
    } else {
        buscarPorCriteriosM();
    }
});

// Función para limpiar la búsqueda
function limpiarBusqueda() {
    document.getElementById('searchNombre').value = '';
    const tbody = document.getElementById('maquinaria-tbody');
    const rows = tbody.getElementsByTagName('tr');

    for (let i = 0; i < rows.length; i++) {
        rows[i].style.display = '';
    }
}

// Función para dar formato a la moneda
function formatCurrency(value) {
    return `$${value.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ".")}`;
}

// Función para cambiar el estado de una maquinaria
function cambiarEstado(id) {
    Swal.fire({
        title: '¿Estás seguro?',
        text: '¿Estás seguro de que deseas cambiar el estado de esta maquinaria?',
        icon: 'warning',
        showCancelButton: true,
        confirmButtonText: 'Sí, cambiar',
        cancelButtonText: 'No, cancelar',
        customClass: {
            confirmButton: 'swal2-confirm'
        }
    }).then((result) => {
        if (result.isConfirmed) {
            fetch(`/maquinaria/${id}/estado`, {
                method: 'PUT' // Hace una petición PUT a la API para cambiar el estado de la maquinaria
            })
                .then(response => { // Si la petición se realizó correctamente, recarga la página
                    if (response.ok) {
                        Swal.fire({
                            icon: 'success',
                            title: 'Estado cambiado exitosamente',
                            text: 'El estado de la maquinaria ha sido cambiado.',
                            customClass: {
                                confirmButton: 'swal2-confirm'
                            }
                        }).then(() => {
                            location.reload(); // Recargar la página después del cambio de estado
                        });
                    } else {
                        Swal.fire({
                            icon: 'error',
                            title: 'Error al cambiar el estado',
                            text: 'No se pudo cambiar el estado de la maquinaria.',
                            customClass: {
                                confirmButton: 'swal2-confirm'
                            }
                        });
                        console.error('Error changing estado');
                    }
                })
                .catch(error => {
                    Swal.fire({
                        icon: 'error',
                        title: 'Error al cambiar el estado',
                        text: 'Hubo un problema al cambiar el estado de la maquinaria.',
                        customClass: {
                            confirmButton: 'swal2-confirm'
                        }
                    });
                    console.error('Error changing estado:', error);
                });
        }
    });
}
