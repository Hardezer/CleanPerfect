//Funcion para mostrar el formulario de ingreso de productos //
function mostrarFormulario() {
    var tipo = document.getElementById("tipoProductoo").value;
    var formInsumo = document.getElementById("formInsumoo");
    var formRopa = document.getElementById("formRopa");

    formInsumo.classList.add("hidden");
    formRopa.classList.add("hidden");

    if (tipo === "insumo") { // Tipo insumo
        formInsumo.classList.remove("hidden");
    } else if (tipo === "ropa") { // Tipo ropa
        formRopa.classList.remove("hidden");
    }
}

// Funcion para buscar un producto de ropa //
function buscarProducto() {
    var nombre = document.getElementById('nombreProductoRopa').value.trim();
    var sugerenciasDiv = document.getElementById('sugerencias');

    if (nombre === "") { // Si el campo está vacío
        sugerenciasDiv.innerHTML = ''; // Limpiar sugerencias
        return;
    }

    fetch('/ropa/sugerencias?nombre=' + nombre) // Buscar sugerencias
        .then(response => response.json())
        .then(data => {
            sugerenciasDiv.innerHTML = '';
            if (data.length > 0) { 
                data.forEach(producto => {
                    var suggestion = document.createElement('div');
                    suggestion.textContent = `${producto.nombre} - ${producto.talla}`;
                    suggestion.classList.add('suggestion');
                    suggestion.onclick = function() {
                        document.getElementById('nombreProductoRopa').value = producto.nombre;
                        document.getElementById('tallaProductoRopa').value = producto.talla;
                        sugerenciasDiv.innerHTML = '';
                    };
                    sugerenciasDiv.appendChild(suggestion);
                });
            } else {
                sugerenciasDiv.innerHTML = '<div class="no-results">No se encontraron resultados</div>'; // No se encontraron resultados
            }
        })
        .catch(error => {
            console.error('Error al obtener sugerencias:', error); // Error al obtener sugerencias
        });
}

//Funcion para guardar en la base de datos lo que se ingreso en el formulario (ingreso de productos) //
function ingresoProducto() {
    var tipo = document.getElementById("tipoProductoo").value;
    var id, nombre, talla, cantidad;

    if (tipo === "insumo") { // Tipo insumo
        id = document.getElementById('codigoProductoInsumo').value;
        cantidad = document.getElementById('cantidadProductoInsumo').value;

        if (id.trim() === "" || cantidad.trim() === "") { // Validar que los campos no estén vacíos
            if (id.trim() === "") {
                document.getElementById("codigoProductoInsumo").classList.add("error");
            } else {
                document.getElementById("codigoProductoInsumo").classList.remove("error");
            }

            if (cantidad.trim() === "") {
                document.getElementById("cantidadProductoInsumo").classList.add("error");
            } else {
                document.getElementById("cantidadProductoInsumo").classList.remove("error");
            }
            Swal.fire({
                icon: 'warning',
                title: 'Campos incompletos',
                text: 'Por favor, complete todos los campos del producto.',
                customClass: {
                    confirmButton: 'swal2-confirm'
                }})
            return;
        }

        fetch('/productos/ingresar', { // Ingresar producto
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify({tipo: tipo, id: id, cantidad: cantidad})
        }).then(response => {
            if (response.ok) {
                document.getElementById('codigoProductoInsumo').value = '';
                document.getElementById('cantidadProductoInsumo').value = '';
                Swal.fire({
                    icon: 'success',
                    title: 'Producto ingresado',
                    text: 'Stock de insumo actualizado correctamente.',
                    customClass: {
                        confirmButton: 'swal2-confirm'
                    }
                }).then(() => {
                    location.reload(); // Recargar la página después de la actualización
                });
                closeModal('ingreso'); // Cerrar modal
            } else {
                Swal.fire({
                    icon: 'error',
                    title: 'Error al ingresar producto',
                    text: 'El producto no existe.',
                    customClass: {
                        confirmButton: 'swal2-confirm'
                    }

                })
            }
        }).catch(error => {
            console.error('Error al ingresar producto:', error);
        });

    } else if (tipo === "ropa") { // Tipo ropa
        nombre = document.getElementById('nombreProductoRopa').value;
        talla = document.getElementById('tallaProductoRopa').value;
        cantidad = document.getElementById('cantidadProductoRopa').value;

        if (nombre.trim() === "" || talla.trim() === "" || cantidad.trim() === "") {   // Validar que los campos no estén vacíos
            if (nombre.trim() === "") {
                document.getElementById("nombreProductoRopa").classList.add("error");
            } else {
                document.getElementById("nombreProductoRopa").classList.remove("error");
            }

            if (talla.trim() === "") {
                document.getElementById("tallaProductoRopa").classList.add("error");
            } else {
                document.getElementById("tallaProductoRopa").classList.remove("error");
            }

            if (cantidad.trim() === "") {
                document.getElementById("cantidadProductoRopa").classList.add("error");
            } else {
                document.getElementById("cantidadProductoRopa").classList.remove("error");
            }
            Swal.fire({
                icon: 'warning',
                title: 'Campos incompletos',
                text: 'Por favor, complete todos los campos.',
                customClass: {
                    confirmButton: 'swal2-confirm'
                }})
            return;
        }

        // Asumimos que nombre y talla únicos identifican el producto de ropa
        fetch(`/ropa/sugerencias?nombre=${nombre}&talla=${talla}`)
            .then(response => response.json())
            .then(data => {
                if (data.length === 1) {
                    var productoId = data[0].id;

                    fetch('/ropa/addStock', { // Actualizar stock
                        method: 'POST',
                        headers: {
                            'Content-Type': 'application/json',
                        },
                        body: JSON.stringify({id: productoId, cantidad: cantidad})
                    }).then(response => {
                        if (response.ok) {
                            document.getElementById('nombreProductoRopa').value = '';
                            document.getElementById('tallaProductoRopa').value = '';
                            document.getElementById('cantidadProductoRopa').value = '';
                            Swal.fire({
                                icon: 'success',
                                title: 'Producto ingresado',
                                text: 'Stock de ropa actualizado correctamente.',
                                customClass: {
                                    confirmButton: 'swal2-confirm'

                            }}).then(() => {
                                location.reload(); // Recargar la página después de la actualización
                            });
                            closeModal('ingreso');
                        } else {
                            Swal.fire({
                                icon: 'error',
                                title: 'Error',
                                text: 'Error al actualizar stock.',
                                customClass: {
                                    confirmButton: 'swal2-confirm'
                            }})
                            alert('Error al actualizar stock.');
                        }
                    }).catch(error => {
                        console.error('Error al actualizar stock:', error);
                    });
                } else {
                    Swal.fire({
                        icon: 'error',
                        title: 'Error',
                        text: 'Producto no encontrado o hay múltiples coincidencias.',
                        customClass: {
                            confirmButton: 'swal2-confirm'
                        }
                    })
                }
            }).catch(error => {
            console.error('Error al buscar el producto:', error);
        });
    }
    closeModal('ingreso'); // Cerrar modal
}
