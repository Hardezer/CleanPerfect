// Funcion para guardar una empresa en la base de datos //
function guardarEmpresa() {
    var nombreEmpresa = document.getElementById("nombreEmpresa");
    var nombreEmpresaValor = nombreEmpresa.value;

    // Ver que el formulario no este vacio
    if (nombreEmpresaValor.trim() === "") {
        Swal.fire({
            icon: 'warning',
            title: 'Campos incompletos',
            text: 'El nombre del cliente no puede estar vacío.',
            customClass: {
                confirmButton: 'swal2-confirm'
            }
        });
        nombreEmpresa.classList.add("error");
        return;
    } else {
        nombreEmpresa.classList.remove("error");
    }
    var formData = new FormData();
    formData.append("nombreEmpresa", nombreEmpresaValor);

    // Envía la solicitud con AJAX
    var xhr = new XMLHttpRequest();
    xhr.open("POST", "/empresa/add", true);
    xhr.onreadystatechange = function () {
        if (xhr.readyState === XMLHttpRequest.DONE) {
            //Si se guardo la empresa
            if (xhr.status === 200) {
                if (xhr.responseText === "true") {
                    document.getElementById("nombreEmpresa").value = "";
                    Swal.fire({
                        icon: 'success',
                        title: 'Éxito',
                        text: 'Cliente guardado correctamente.',
                        customClass: {
                            confirmButton: 'swal2-confirm'
                        }
                    });
                    closeModal('empresa'); // Cierra el formulario y lo limpia
                } else {
                    Swal.fire({
                        icon: 'error',
                        title: 'Error',
                        text: 'El cliente ya existe.',
                        customClass: {
                            confirmButton: 'swal2-confirm'
                        }
                    });
                }
                // si ocurrio error con la solicitud
            } else {
                Swal.fire({
                    icon: 'error',
                    title: 'Error',
                    text: 'Hubo un error al guardar al cliente.',
                    customClass: {
                        confirmButton: 'swal2-confirm'
                    }
                });
            }
        }
    };
    xhr.send(formData);
}
