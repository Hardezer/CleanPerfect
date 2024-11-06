document.addEventListener('DOMContentLoaded', function() {
    cargarCategorias();
});

// Función para guardar una categoría //
function guardarCategoria() {
    var nombreCategoria = document.getElementById("nombreCategoria").value;

    // Ver que el formulario no esté vacío
    if (nombreCategoria.trim() === "") {
        alert("Por favor, ingresa un nombre para la categoría.");
        return;
    }
    var formData = new FormData();
    formData.append("nombreCategoria", nombreCategoria);

    // Envía la solicitud con AJAX
    var xhr = new XMLHttpRequest();
    xhr.open("POST", "/categoria/add", true);
    xhr.onreadystatechange = function () {
        if (xhr.readyState === XMLHttpRequest.DONE) {
            // Si se guardó la categoría
            if (xhr.status === 200) {
                if (xhr.responseText === "true"){
                    alert("Categoría guardada correctamente.");
                } else {
                    alert("La categoría ya existe.");
                }
            } else {
                alert("Hubo un error al guardar la categoría.");
            }
        }
    };
    xhr.send(formData);
}

// Función cargar categorías //
function cargarCategorias() {
    var select = document.getElementById('categoriaInsumo');

    // Limpia las opciones existentes excepto las predeterminadas
    select.innerHTML = '<option value="" disabled selected>Seleccione una categoría</option>';
    select.innerHTML += '<option value="nueva">--Crear Nueva Categoría--</option>';

    // Realiza la petición al servidor para obtener las categorías
    fetch('/categoria/all')
        .then(response => {
            if (response.ok) {
                return response.json(); // Devuelve una promesa con los datos en formato JSON
            } else {
                throw new Error('Failed to fetch categories');
            }
        })
        .then(categorias => {
            // Agrega cada categoría al select como nuevas opciones
            categorias.forEach(categoria => {
                const option = document.createElement('option');
                option.value = categoria.id;
                option.textContent = ` ${categoria.nombre}`;
                select.appendChild(option);
            });
        })
        .catch(error => {
            console.error('Error al cargar categorías:', error);
        });
}

// Funcion para verificar una nueva categoria //
function verificarNuevaCategoria() {
    const selectCategoria = document.getElementById('categoriaInsumo');
    const nuevaCategoriaDiv = document.getElementById('nuevaCategoria');
    const formatoContainer = document.getElementById('formatoContainer');
    const nuevoFormatoDiv = document.getElementById('nuevoFormato');

    if (selectCategoria.value === 'nueva') {
        nuevaCategoriaDiv.style.display = 'block';
        formatoContainer.style.display = 'none';
        nuevoFormatoDiv.style.display = 'none';
    } else {
        nuevaCategoriaDiv.style.display = 'none';
        formatoContainer.style.display = 'block';
        nuevoFormatoDiv.style.display = 'none';
        cargarFormatos(selectCategoria.value);
    }
}

// Función para cargar los formatos //
function cargarFormatos(categoriaId) {
    fetch(`/formato/byCategoria/${categoriaId}`)
        .then(response => { // Respuesta del servidor
            if (response.ok) {
                return response.json(); // Devuelve una promesa con los datos en formato JSON
            } else {
                throw new Error('Failed to fetch formats');
            }
        })
        .then(formatos => {
            const selectFormato = document.getElementById('formatoCategoria');
            selectFormato.innerHTML = '<option value="" disabled selected>Seleccione un formato</option>'; // Limpiar opciones anteriores
            selectFormato.innerHTML += '<option value="nuevoFormato">--Crear Nuevo Formato--</option>';
            formatos.forEach(formato => {
                const option = document.createElement('option');
                option.value = formato.id;
                option.textContent = ` ${formato.tamano} ${formato.unidadDeMedida}`;
                selectFormato.appendChild(option);
            });
        })
        .catch(error => { // Error al cargar los formatos
            console.error('Error al cargar los formatos:', error);
        });
}

// Función para verificar un nuevo formato // 
function verificarNuevoFormato() {
    const selectFormato = document.getElementById('formatoCategoria');
    const nuevoFormatoDiv = document.getElementById('nuevoFormato');

    if (selectFormato.value === 'nuevoFormato') {
        nuevoFormatoDiv.style.display = 'block';
    } else {
        nuevoFormatoDiv.style.display = 'none'; // Ocultar el formulario de nuevo formato
    }
}

// Funcion para registrar un producto //
function registrarProducto() {
    const selectCategoria = document.getElementById('categoriaInsumo');
    const selectFormato = document.getElementById('formatoCategoria');

    const categoriaId = selectCategoria.value;
    if (categoriaId === '') {
        document.getElementById('categoriaInsumo').classList.add('error');
    } else {
        document.getElementById('categoriaInsumo').classList.remove('error');
    }

    const formatoId = selectFormato.value;

    const codigoBarra = document.getElementById('codigoBarraInsumo').value;
    let costo = document.getElementById('costoInsumo').value;
    const tipo = document.getElementById('tipoProducto').value;

    costo = costo.replace(/\$/g, '').replace(/\./g, '');

    if (tipo === '') {
        Swal.fire({
            icon: 'warning',
            title: 'Campos incompletos',
            text: 'Por favor, seleccione un tipo de producto.',
            customClass: {
                confirmButton: 'swal2-confirm'
            }
        });
        document.getElementById('categoriaInsumo').classList.remove('error');
        return;

    } else if (tipo === "maquina") { // Lógica para registrar maquinaria
        const nombreMaquinaria = document.getElementById("nombreMaquinaria").value;
        let costoUsoMaquinaria = document.getElementById("costoUsoMaquinaria").value;

        if (nombreMaquinaria.trim() === "" && costoUsoMaquinaria.trim() === "") {
            Swal.fire({
                icon: 'warning',
                title: 'Campos incompletos',
                text: 'Por favor, completa todos los campos para la maquinaria.',
                customClass: {
                    confirmButton: 'swal2-confirm'
                }
            });
            document.getElementById("nombreMaquinaria").classList.add("error");
            document.getElementById("costoUsoMaquinaria").classList.add("error");
            return;
        } else if (nombreMaquinaria.trim() === "") {
            Swal.fire({
                icon: 'warning',
                title: 'Campos incompletos',
                text: 'Por favor, ingrese el nombre de la maquinaria.',
                customClass: {
                    confirmButton: 'swal2-confirm'
                }
            });
            document.getElementById("nombreMaquinaria").classList.add("error");
            document.getElementById("costoUsoMaquinaria").classList.remove("error");
            return;
        } else if (costoUsoMaquinaria.trim() === "") {
            Swal.fire({
                icon: 'warning',
                title: 'Campos incompletos',
                text: 'Por favor, ingrese el costo de uso de la maquinaria.',
                customClass: {
                    confirmButton: 'swal2-confirm'
                }
            });
            document.getElementById("costoUsoMaquinaria").classList.add("error");
            document.getElementById("nombreMaquinaria").classList.remove("error");
            return;
        } else {
            document.getElementById("nombreMaquinaria").classList.remove("error");
            document.getElementById("costoUsoMaquinaria").classList.remove("error");
        }
        costoUsoMaquinaria = costoUsoMaquinaria.replace(/\$/g, '').replace(/\./g, '');

        const data = {
            nombre: nombreMaquinaria,
            costoUso: costoUsoMaquinaria
        };
        // Aquí puedes añadir la lógica para registrar una maquinaria en el backend
        fetch('/maquinaria', { // Ruta para registrar maquinaria
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(data)
        })
            .then(response => response.json())
            .then(data => {
                console.log('Success:', data);
                document.getElementById('nombreMaquinaria').value = '';
                document.getElementById('costoUsoMaquinaria').value = '';
                Swal.fire({
                    icon: 'success',
                    title: 'Éxito',
                    text: 'La maquinaria se ha guardado exitosamente.',
                    customClass: {
                        confirmButton: 'swal2-confirm'
                    }
                }).then(() => {
                    location.reload(); // Recargar la página
                });
                closeModal('producto');
            })
            .catch((error) => {
                console.error('Error:', error);
                Swal.fire({
                    icon: 'error',
                    title: 'Error',
                    text: 'Ha ocurrido un error al intentar guardar la maquinaria.',
                    customClass: {
                        confirmButton: 'swal2-confirm'
                    }
                });
            });
    } else if (tipo === "ropa") {
        // Lógica para registrar ropa
        const nombreRopa = document.getElementById('nombreRopa').value;
        const tallaRopa = document.getElementById('tallaRopa').value;
        let precioRopa = document.getElementById('precioRopa').value;

        // Validar campos para ropa
        if (nombreRopa.trim() === "" || tallaRopa.trim() === "" || precioRopa.trim() === "") {
            Swal.fire({
                icon: 'warning',
                title: 'Campos incompletos',
                text: 'Por favor, complete todos los campos para la ropa.',
                customClass: {
                    confirmButton: 'swal2-confirm'
                }
            });
            if (nombreRopa.trim() === "") {
                document.getElementById("nombreRopa").classList.add("error");
            } else {
                document.getElementById("nombreRopa").classList.remove("error");
            }
            if (tallaRopa.trim() === "") {
                document.getElementById("tallaRopa").classList.add("error");
            } else {
                document.getElementById("tallaRopa").classList.remove("error");
            }
            if (precioRopa.trim() === "") {
                document.getElementById("precioRopa").classList.add("error");
            } else {
                document.getElementById("precioRopa").classList.remove("error");
            }
            return;
        } else {
            document.getElementById("nombreRopa").classList.remove("error");
            document.getElementById("tallaRopa").classList.remove("error");
            document.getElementById("precioRopa").classList.remove("error");
        }

        // Eliminar el signo de peso ($) y los puntos (.) del precioRopa
        precioRopa = precioRopa.replace(/\$/g, '').replace(/\./g, '');

        // Crear objeto de datos para la ropa
        const dataRopa = {
            nombre: nombreRopa,
            talla: tallaRopa,
            precio: parseFloat(precioRopa)
        };

        // Enviar datos de la ropa al backend
        fetch('/ropa/add', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(dataRopa)
        })
            .then(response => response.json())
            .then(data => {
                console.log('Success:', data);
                document.getElementById('nombreRopa').value = '';
                document.getElementById('tallaRopa').value = '';
                document.getElementById('precioRopa').value = '';
                Swal.fire({
                    icon: 'success',
                    title: 'Éxito',
                    text: 'La ropa se ha guardado exitosamente.',
                    customClass: {
                        confirmButton: 'swal2-confirm'
                    }
                }).then(() => {
                    location.reload(); // Recargar la página
                });
                closeModal('producto');
            })
            .catch((error) => {
                console.error('Error:', error);
                Swal.fire({
                    icon: 'error',
                    title: 'Error',
                    text: 'Ha ocurrido un error al intentar guardar la ropa.',
                    customClass: {
                        confirmButton: 'swal2-confirm'
                    }
                });
            });
    } else if (categoriaId === 'nueva') {
        // Caso 3: Crear nueva categoría (y opcionalmente nuevo formato)
        const nuevaCategoriaNombre = document.getElementById('nombreCategoria').value;
        const nuevaCategoriaUnidad = document.getElementById('unidadMedida').value;
        const nuevaCategoriaTamano = document.getElementById('tamañoCategoria').value;

        if (formatoId === 'nuevoFormato') {
            // Crear nuevo formato para la nueva categoría
            const nuevoFormatoUnidad = document.getElementById('unidadMedidaFormato').value;
            const nuevoFormatoTamano = document.getElementById('tamañoFormato').value;
        } else {
            if (codigoBarra === '' || costo === '' || tipo === '' || nuevaCategoriaNombre === '' || nuevaCategoriaUnidad === '' || nuevaCategoriaTamano === '') {
                Swal.fire({
                    icon: 'warning',
                    title: 'Campos incompletos',
                    text: 'Por favor, rellene todos los campos.',
                    customClass: {
                        confirmButton: 'swal2-confirm'
                    }
                });
                if (codigoBarra === '') {
                    document.getElementById('codigoBarraInsumo').classList.add('error');
                } else {
                    document.getElementById('codigoBarraInsumo').classList.remove('error');
                }
                if (costo === '') {
                    document.getElementById('costoInsumo').classList.add('error');
                } else {
                    document.getElementById('costoInsumo').classList.remove('error');
                }
                if (tipo === '') {
                    document.getElementById('tipoProducto').classList.add('error');
                } else {
                    document.getElementById('tipoProducto').classList.remove('error');
                }
                if (nuevaCategoriaNombre === '') {
                    document.getElementById('nombreCategoria').classList.add('error');
                } else {
                    document.getElementById('nombreCategoria').classList.remove('error');
                }
                if (nuevaCategoriaUnidad === '') {
                    document.getElementById('unidadMedida').classList.add('error');
                } else {
                    document.getElementById('unidadMedida').classList.remove('error');
                }
                if (nuevaCategoriaTamano === '') {
                    document.getElementById('tamañoCategoria').classList.add('error');
                } else {
                    document.getElementById('tamañoCategoria').classList.remove('error');
                }
                return;
            }
            generarCategoriayFormato(codigoBarra, costo, tipo, nuevaCategoriaNombre, nuevaCategoriaUnidad, nuevaCategoriaTamano);
        }
    } else {
        if (formatoId === 'nuevoFormato') {
            // Caso 2: Crear nuevo formato para la categoría existente
            const nuevoFormatoUnidad = document.getElementById('unidadMedidaFormato').value;
            const nuevoFormatoTamano = document.getElementById('tamañoFormato').value;
            if (codigoBarra === '' || costo === '' || tipo === '' || categoriaId === '' || nuevoFormatoUnidad === '' || nuevoFormatoTamano === '') {
                Swal.fire({
                    icon: 'warning',
                    title: 'Campos incompletos',
                    text: 'Por favor, rellene todos los campos.',
                    customClass: {
                        confirmButton: 'swal2-confirm'
                    }
                });
                if (codigoBarra === '') {
                    document.getElementById('codigoBarraInsumo').classList.add('error');
                } else {
                    document.getElementById('codigoBarraInsumo').classList.remove('error');
                }
                if (costo === '') {
                    document.getElementById('costoInsumo').classList.add('error');
                } else {
                    document.getElementById('costoInsumo').classList.remove('error');
                }
                if (tipo === '') {
                    document.getElementById('tipoProducto').classList.add('error');
                } else {
                    document.getElementById('tipoProducto').classList.remove('error');
                }
                if (categoriaId === '') {
                    document.getElementById('categoriaInsumo').classList.add('error');
                } else {
                    document.getElementById('categoriaInsumo').classList.remove('error');
                }
                if (nuevoFormatoUnidad === '') {
                    document.getElementById('unidadMedidaFormato').classList.add('error');
                } else {
                    document.getElementById('unidadMedidaFormato').classList.remove('error');
                }
                if (nuevoFormatoTamano === '') {
                    document.getElementById('tamañoFormato').classList.add('error');
                } else {
                    document.getElementById('tamañoFormato').classList.remove('error');
                }
                return;
            }
            CrearFormatoParaCategoria(codigoBarra, costo, tipo, categoriaId, nuevoFormatoUnidad, nuevoFormatoTamano);
        } else {
            // Caso 1: Categoría y formato existentes
            if (codigoBarra === '' || costo === '' || tipo === '' || categoriaId === '' || formatoId === '') {
                Swal.fire({
                    icon: 'warning',
                    title: 'Campos incompletos',
                    text: 'Por favor, completa todos los campos para el insumo.',
                    customClass: {
                        confirmButton: 'swal2-confirm'
                    }
                });
                if (codigoBarra === '') {
                    document.getElementById('codigoBarraInsumo').classList.add('error');
                } else {
                    document.getElementById('codigoBarraInsumo').classList.remove('error');
                }
                if (costo === '') {
                    document.getElementById('costoInsumo').classList.add('error');
                } else {
                    document.getElementById('costoInsumo').classList.remove('error');
                }
                if (tipo === '') {
                    document.getElementById('tipoProducto').classList.add('error');
                } else {
                    document.getElementById('tipoProducto').classList.remove('error');
                }
                if (categoriaId === '') {
                    document.getElementById('categoriaInsumo').classList.add('error');
                } else {
                    document.getElementById('categoriaInsumo').classList.remove('error');
                }
                if (formatoId === '') {
                    document.getElementById('formatoCategoria').classList.add('error');
                } else {
                    document.getElementById('formatoCategoria').classList.remove('error');
                }
                return;
            }
            usarCategoriayFormatoExistente(codigoBarra, costo, tipo, categoriaId, formatoId);
        }
    }
    document.getElementById('codigoBarraInsumo').value = '';
    document.getElementById('costoInsumo').value = '';
    document.getElementById('unidadMedida').value = '';
    document.getElementById('nombreCategoria').value = '';
    document.getElementById('tamañoCategoria').value = '';
    document.getElementById('unidadMedidaFormato').value = '';
    document.getElementById('tamañoFormato').value = '';
    cargarCategorias();

    // Cerrar el formulario y lo limpia
    closeModal('producto');
}

//Funcion para generar categorias y formatos //
function generarCategoriayFormato(codigoBarra, costo, tipo, nombreCategoria, unidad, tamano) {
    const data = {
        codigoBarra: codigoBarra,
        costo: costo,
        tipo: tipo,
        nombreCategoria: nombreCategoria,
        unidad: unidad,
        tamano: tamano
    };
    console.log(data);

    fetch('/productos/addCaso3', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(data)
    })
        .then(response => {
            if (!response.ok) {
                console.log(response);
                throw new Error('Error en la creación del producto');
            }
            return response.json();
        })
        .then(data => {
            Swal.fire({
                icon: 'success',
                title: 'Éxito',
                text: 'Producto creado con éxito.',
                customClass: {
                    confirmButton: 'swal2-confirm'
                }
            }).then(() => {
                location.reload(); // Recargar la página
            });
            console.log('Producto creado con éxito:', data);
        })
        .catch(error => {
            Swal.fire({
                icon: 'error',
                title: 'Error',
                text: 'Error en la creación del producto.',
                customClass: {
                    confirmButton: 'swal2-confirm'
                }
            });
            console.error('Error:', error);
        });
}

//Funcion para usar categoria y formato existente //
function usarCategoriayFormatoExistente(codigoBarra, costo, tipo, categoriaId, formatoId) {
    const data = {
        codigoBarra: codigoBarra,
        costo: costo,
        tipo: tipo,
        idCategoria: categoriaId,
        idFormato: formatoId
    };
    console.log(data);
    fetch('/productos/addCaso1', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(data)
    })
        .then(response => {
            if (!response.ok) {
                console.log(response);
                throw new Error('Error en la creación del producto');
            }
            return response.json();
        })
        .then(data => {
            Swal.fire({
                icon: 'success',
                title: 'Éxito',
                text: 'Producto creado con éxito.',
                customClass: {
                    confirmButton: 'swal2-confirm'
                }
            }).then(() => {
                location.reload(); // Recargar la página
            });
            console.log('Producto creado con éxito:', data);
        })
        .catch(error => {
            Swal.fire({
                icon: 'error',
                title: 'Error',
                text: 'Error en la creación del producto.',
                customClass: {
                    confirmButton: 'swal2-confirm'
                }
            });
            console.error('Error:', error);
        });
}

//Funcion para crear formato para categoria //
function CrearFormatoParaCategoria(codigoBarra, costo, tipo, categoriaId, unidad, tamano) {
    const data = {
        codigoBarra: codigoBarra,
        costo: costo,
        tipo: tipo,
        idCategoria: categoriaId,
        unidad: unidad,
        tamano: tamano
    };
    console.log(data);
    fetch('/productos/addCaso2', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(data)
    })
        .then(response => {
            if (!response.ok) {
                console.log(response);
                throw new Error('Error en la creación del producto');
            }
            return response.json();
        })
        .then(data => {
            Swal.fire({
                icon: 'success',
                title: 'Éxito',
                text: 'Producto creado con éxito.',
                customClass: {
                    confirmButton: 'swal2-confirm'
                }
            }).then(() => {
                location.reload(); // Recargar la página
            });
            console.log('Producto creado con éxito:', data);
        })
        .catch(error => {
            Swal.fire({
                icon: 'error',
                title: 'Error',
                text: 'Error en la creación del producto.',
                customClass: {
                    confirmButton: 'swal2-confirm'
                }
            });
            console.error('Error:', error);
        });
}


document.addEventListener('DOMContentLoaded', function () {
    // Función reutilizable para formatear campos numéricos
    function formatNumericField(inputField) {
        inputField.addEventListener('input', function (event) {
            let value = event.target.value;

            // Eliminar caracteres no numéricos, excepto puntos y signos de peso
            value = value.replace(/[^0-9]/g, '');

            // Formatear el número con puntos de milesima
            if (value) {
                value = new Intl.NumberFormat('es-ES').format(value);
            }

            // Añadir el signo de peso
            if (value) {
                value = '$' + value;
            }

            // Actualizar el valor del campo de entrada con el formato
            event.target.value = value;
        });

        inputField.addEventListener('focus', function () {
            let value = inputField.value;

            // Eliminar el signo de peso y los puntos al obtener el foco
            if (value.startsWith('$')) {
                value = value.replace(/\$/g, '').replace(/\./g, '');
            }

            inputField.value = value;
        });

        inputField.addEventListener('blur', function () {
            let value = inputField.value;

            // Eliminar caracteres no numéricos al perder el foco
            value = value.replace(/[^0-9]/g, '');

            // Formatear el número con puntos de milesima
            if (value) {
                value = new Intl.NumberFormat('es-ES').format(value);
            }

            // Añadir el signo de peso
            if (value) {
                value = '$' + value;
            }

            inputField.value = value;
        });
    }

    // Aplicar la función a los campos deseados
    const precioRopaInput = document.getElementById('precioRopa');
    formatNumericField(precioRopaInput);

    const costoUsoMaquinariaInput = document.getElementById('costoUsoMaquinaria');
    formatNumericField(costoUsoMaquinariaInput);

    const costoInsumoInput = document.getElementById('costoInsumo');
    formatNumericField(costoInsumoInput);
});


