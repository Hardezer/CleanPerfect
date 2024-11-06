function verificarTipoProductoSal() {
    const tipoProducto = document.getElementById('tipoProductoSalida').value;
    const formInsumo = document.getElementById('formInsumoSal');
    const formRopa = document.getElementById('formRopaSal');
    const formMaquinaria = document.getElementById('formMaquinariaSal');

    // Ocultar ambos formularios al inicio
    formInsumo.style.display = 'none';
    formRopa.style.display = 'none';
    formMaquinaria.style.display = 'none';


    // Mostrar el formulario correspondiente según el tipo de producto seleccionado
    if (tipoProducto === 'insumo') {
        formInsumo.style.display = 'block';
    } else if (tipoProducto === 'ropa') {
        formRopa.style.display = 'block';
    }else if (tipoProducto === 'maquinaria') {
        formMaquinaria.style.display = 'block';

    }
}

function buscarEmpresaActiva(input) {
    var nombre = input.value.trim();
    var suggestionsContainer = input.parentElement.querySelector('.suggestions');
    var sugerenciasDiv = suggestionsContainer;

    // Limpiar sugerencias al inicio de la búsqueda
    sugerenciasDiv.innerHTML = '';

    if (nombre === "") { // Si el campo está vacío
        return;
    }

    fetch('/empresa/activas') // Llamar al nuevo endpoint para obtener las empresas activas
        .then(response => response.json())
        .then(data => {
            var empresasFiltradas = data.filter(empresa => empresa.nombre.toLowerCase().includes(nombre.toLowerCase()));

            if (empresasFiltradas.length > 0) {
                empresasFiltradas.forEach(empresa => {
                    // Crear un elemento de sugerencia
                    var suggestion = document.createElement('div');
                    suggestion.textContent = empresa.nombre;
                    suggestion.classList.add('suggestion');

                    // Manejar clic en la sugerencia
                    suggestion.onclick = function() {
                        // Actualizar campo de entrada con la sugerencia seleccionada
                        input.value = empresa.nombre;

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

// Función para buscar un producto de ropa para la salida //
function buscarProductooS(input) {
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

                        // Asignar la talla al campo correspondiente para salida
                        document.getElementById('tallaProductoSal').value = producto.talla;

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

document.addEventListener("DOMContentLoaded", function() {
    // Evento de entrada para buscar maquinaria
    document.getElementById("idProductoMaquinariaSal").addEventListener("input", function() {
        buscarMaquinariaS(this);
    });
});

// Función para buscar maquinaria disponible por nombre
function buscarMaquinariaS(input) {
    var nombre = input.value.trim();
    var suggestionsContainer = input.parentElement.querySelector('.suggestions');
    var sugerenciasDiv = suggestionsContainer;

    // Limpiar sugerencias al inicio de la búsqueda
    sugerenciasDiv.innerHTML = '';

    if (nombre === "") { // Si el campo está vacío
        return;
    }

    fetch('/maquinaria/sugerencias?nombre=' + nombre) // Buscar sugerencias
        .then(response => response.json())
        .then(data => {
            // Limpiar sugerencias antes de agregar nuevas
            sugerenciasDiv.innerHTML = '';

            if (data.length > 0) {
                data.forEach(producto => {
                    // Crear un elemento de sugerencia
                    var suggestion = document.createElement('div');
                    suggestion.textContent = `${producto.nombre} - ${producto.estado}`;
                    suggestion.classList.add('suggestion');

                    // Manejar clic en la sugerencia
                    suggestion.onclick = function() {
                        // Actualizar campo de entrada con la sugerencia seleccionada
                        input.value = producto.nombre;

                        // Limpiar sugerencias después de seleccionar
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


function registrarSalida() {
    const table = document.getElementById('productosTableSal').getElementsByTagName('tbody')[0];
    const rows = table.rows;
    const empresa = document.getElementById('empresaSalida').value;

    if (rows.length === 0) { // Validar que haya productos para registrar
        Swal.fire({
            icon: 'warning',
            title: 'Campos incompletos',
            text: 'No hay productos para registrar.',
            customClass: {
                confirmButton: 'swal2-confirm'
            }
        })
        return;
    }

    if (empresa.trim() === '') {
        Swal.fire({
            icon: 'warning',
            title: 'Campos incompletos',
            text: 'Por favor, complete el campo de empresa.',
            customClass: {
                confirmButton: 'swal2-confirm'

            }})
        if (empresa.trim() === '') {
            document.getElementById('empresaSalida').classList.add('error');
        } else {
            document.getElementById('empresaSalida').classList.remove('error');
        }
        return;
    }

    const productos = [];
    for (let i = 0; i < rows.length; i++) {
        const cells = rows[i].cells;
        const producto = {
            nombre: cells[0].textContent.trim(),
            idTalla: cells[1].textContent.trim(),
            cantidad: cells[2].textContent.trim(),
            fechaSalida: cells[3].textContent.trim(),
            fechaVuelta: cells[4].textContent.trim()
        };

        if (!producto.nombre || !producto.idTalla || !producto.cantidad) {
            Swal.fire({
                icon: 'warning',
                title: 'Campos incompletos',
                text: 'Por favor, complete todos los campos del producto.',
                customClass: {
                    confirmButton: 'swal2-confirm'
                }
            })
            return;
        }

        productos.push(producto);
    }

    fetch('/salida/', { // Asegúrate de cambiar '/ruta-al-backend' a la ruta correcta en tu backend
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify({
            nombreEmpresa: empresa,
            detalle: productos
        })
    }).then(response => {

        if (response.ok) {
            return response.json(); // Suponiendo que el ID de salida se devuelve en la respuesta
        } else {
            throw new Error('Error al registrar la salida.');
        }
    }).then(data => {
        if(data.msg){
            Swal.fire({
                icon: 'error',
                title: 'Error',
                text: data.msg,
                customClass: {
                    confirmButton: 'swal2-confirm'
                }
            })
            return;
        }
        Swal.fire({
            icon: 'success',
            title: 'Salida registrada',
            text: 'La salida se ha registrado correctamente.',
            customClass: {
                confirmButton: 'swal2-confirm'
            }
        }).then(() => {
            location.reload(); // Recargar la página después de la actualización
        });
        console.log('Salida registrada:', data);
        const salidaId = data.salida.id;
        table.innerHTML = '';
        document.getElementById("empresaSalida").value = '';
        closeModal('salida'); // Cerrar el modal de salida

        // Descargar el PDF
        const link = document.createElement('a');
        link.href = '/pdf/generate/' + salidaId;
        link.download = 'salida_' + empresa + '_' + new Date().toISOString().split('T')[0] + '.pdf';
        document.body.appendChild(link);
        link.click();
        document.body.removeChild(link);

    }).catch(error => {
        console.error('Error al registrar la salida:', error);
        Swal.fire({
            icon: 'error',
            title: 'Error',
            text: 'Error al registrar la salida.',
            customClass: {
                confirmButton: 'swal2-confirm'
            }
        })});
}

//Verifica la cantidad de la ropa escogida //
async function verificarCantidadDisponible(nombre, talla, cantidad) {
    const response = await fetch(`/ropa/verificarCantidad?nombre=${nombre}&talla=${talla}&cantidad=${cantidad}`);
    if (response.ok) {
        const data = await response.json();
        return data;
    }
    return { disponible: false, cantidadDisponible: 0 };
}

function eliminarProducto(button, event) {
    // Confirmar si el usuario está seguro de eliminar el producto
    event.preventDefault();
    Swal.fire({
        title: '¿Estás seguro?',
        text: '¿Estás seguro deseas eliminar este item?',
        icon: 'warning',
        showCancelButton: true,
        confirmButtonText: 'Sí, eliminar',
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
        }
    });
}

async function agregarProductoSal(tipo) {
    const table = document.getElementById('productosTableSal').getElementsByTagName('tbody')[0];
    let producto, idTalla, cantidad, fechaSalida, fechaReingreso;

    if (tipo === 'insumo') {
        const idProductoInsumo = document.getElementById('idProductoInsumoSal').value.trim();
        const cantidadInsumo = parseInt(document.getElementById('cantidadInsumoSal').value.trim());

        if (idProductoInsumo === '' || isNaN(cantidadInsumo) || cantidadInsumo <= 0) {
            Swal.fire({
                icon: 'warning',
                title: 'Campos incompletos',
                text: 'Por favor, complete todos los campos del insumo.',
                customClass: {
                    confirmButton: 'swal2-confirm'
                }})
            toggleErrorClass('idProductoInsumoSal', idProductoInsumo === '');
            toggleErrorClass('cantidadInsumoSal', isNaN(cantidadInsumo) || cantidadInsumo <= 0);
            return;
        }

        // Mostrar la cantidad disponible
        const { disponible, cantidadDisponible } = await verificarCantidadDisponibleInsumo(idProductoInsumo, cantidadInsumo);
        console.log(`Cantidad Disponible para el insumo con ID ${idProductoInsumo}: ${cantidadDisponible}`);
        document.getElementById('cantidadDisponibleInsumo').textContent = `Cantidad disponible: ${cantidadDisponible}`;

        if (!disponible) {
            Swal.fire({
                icon: 'warning',
                title: 'Stock insuficiente',
                text: 'No hay suficiente stock disponible para este producto.',
                customClass: {
                    confirmButton: 'swal2-confirm'
                }

            })
            return;
        }

        producto = "Insumo";
        idTalla = idProductoInsumo;
        cantidad = cantidadInsumo;

    } else if (tipo === 'ropa') {
        const nombre = document.getElementById('productoRopaSal').value.trim();
        const talla = document.getElementById('tallaProductoSal').value.trim();
        const cantidadRopa = parseInt(document.getElementById('cantidadRopaSal').value.trim());

        if (nombre === '' || talla === '' || isNaN(cantidadRopa) || cantidadRopa <= 0) {
            Swal.fire({
                icon: 'warning',
                title: 'Campos incompletos',
                text: 'Por favor, complete todos los campos de la ropa correctamente.',
                customClass: {
                    confirmButton: 'swal2-confirm'}
            })
            toggleErrorClass('productoRopaSal', nombre === '');
            toggleErrorClass('tallaProductoSal', talla === '');
            toggleErrorClass('cantidadRopaSal', isNaN(cantidadRopa) || cantidadRopa <= 0);
            return;
        }

        // Mostrar la cantidad disponible
        const { disponible, cantidadDisponible } = await verificarCantidadDisponible(nombre, talla, cantidadRopa);
        document.getElementById('cantidadDisponibleRopa').textContent = `Cantidad disponible: ${cantidadDisponible}`;

        if (!disponible) {
            Swal.fire({
                icon: 'warning',
                title: 'Stock insuficiente',
                text: 'No hay suficiente stock disponible para este producto.',
                customClass: {
                    confirmButton: 'swal2-confirm'
                }

            })
            return;
        }
        producto = nombre;
        idTalla = talla;
        cantidad = cantidadRopa;

    } else if (tipo === 'maquinaria') {
        const productoMaquinaria = document.getElementById('idProductoMaquinariaSal').value.trim();
        const fechaSalidaa = document.getElementById('fechaSalida').value.trim();
        const fechaVuelta = document.getElementById('fechaVuelta').value.trim();

        if (productoMaquinaria === '' || fechaSalidaa === '' || fechaVuelta === '') {
            Swal.fire({
                icon: 'warning',
                title: 'Campos incompletos',
                text: 'Por favor, complete todos los campos de la maquinaria.',
                customClass: {
                    confirmButton: 'swal2-confirm'
                }
            })
            toggleErrorClass('idProductoMaquinariaSal', productoMaquinaria === '');
            toggleErrorClass('fechaSalida', fechaSalidaa === '');
            toggleErrorClass('fechaVuelta', fechaVuelta === '');
            return;
        }
        producto = "Maquinaria";
        idTalla = productoMaquinaria;
        cantidad = 1;
        fechaReingreso = fechaVuelta;
        fechaSalida = fechaSalidaa;
    }

    // Agregar el producto a la tabla
    const newRow = table.insertRow();
    newRow.insertCell(0).textContent = producto;
    newRow.insertCell(1).textContent = idTalla;
    newRow.insertCell(2).textContent = cantidad;
    newRow.insertCell(3).textContent = fechaSalida || '';
    newRow.insertCell(4).textContent = fechaReingreso || '';
    const accionesCell = newRow.insertCell(5);

    // Botón de eliminar
    const eliminarBtn = document.createElement('button');
    eliminarBtn.classList.add('action-button', 'delete-button');
    eliminarBtn.style.backgroundColor = '#f44336';
    eliminarBtn.innerHTML = '<img src="/css/eliminar.png" alt="Eliminar">';
    eliminarBtn.onclick = (event) => eliminarProducto(eliminarBtn, event);
    accionesCell.appendChild(eliminarBtn);

    // Limpiar los campos después de agregar el producto
    limpiarCampos(tipo);

    // Limpiar el campo de cantidad disponible
    if (tipo === 'ropa') {
        document.getElementById('cantidadDisponibleRopa').textContent = '';
    } else if (tipo === 'insumo') {
        document.getElementById('cantidadDisponibleInsumo').textContent = '';
    }
    // Restablecer el formulario para ocultar ambos formularios y mostrar solo el selector
    document.getElementById('tipoProductoSalida').value = '';
    verificarTipoProductoSal();
}

async function verificarCantidadDisponibleInsumo(codigoBarra, cantidad) {
    const response = await fetch(`/productos/verificarCantidadInsumo?codigoBarra=${codigoBarra}&cantidad=${cantidad}`);
    if (response.ok) {
        const data = await response.json();
        console.log(`Verificar cantidad disponible insumo - Código de Barra: ${codigoBarra}, Cantidad: ${data.cantidadDisponible}`);
        return data;
    }
    return { disponible: false, cantidadDisponible: 0 };
}


function toggleErrorClass(elementId, isError) {
    const element = document.getElementById(elementId);
    if (isError) {
        element.classList.add('error');
    } else {
        element.classList.remove('error');
    }
}

function limpiarCampos(tipo) {
    if (tipo === 'insumo') {
        document.getElementById('idProductoInsumoSal').value = '';
        document.getElementById('cantidadInsumoSal').value = '';
    } else if (tipo === 'ropa') {
        document.getElementById('productoRopaSal').value = '';
        document.getElementById('tallaProductoSal').value = '';
        document.getElementById('cantidadRopaSal').value = '';
    } else if (tipo === 'maquinaria') {
        document.getElementById('idProductoMaquinariaSal').value = '';
        document.getElementById('fechaSalida').value = '';
        document.getElementById('fechaVuelta').value = '';
    }
}

