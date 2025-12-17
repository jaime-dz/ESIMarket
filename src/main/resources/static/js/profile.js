document.addEventListener("DOMContentLoaded", () => {

    const btnEditar = document.getElementById("edit-profile");
    if(btnEditar){
        btnEditar.addEventListener("click", () => {
            window.location.href = '/profile/edit';
        });
    }

    //interceptar el formulario de edición y hacerlo PUT
    const editForm = document.getElementById("edit-form"); // Asegúrate que en HTML el form tenga este ID

    if (editForm) {
        editForm.addEventListener("submit", async (event) => {
            event.preventDefault(); // Detenemos el envío normal del HTML

            const formData = new FormData(editForm);
            const datosParaEnviar = {};
            let hayCambios = false;

            // Recorremos los datos y solo guardamos los que NO estén vacíos
            formData.forEach((value, key) => {
                if (value && value.trim() !== "") {
                    datosParaEnviar[key] = value.trim();
                    hayCambios = true;
                }
            });

            if (!hayCambios) {
                alert("No has introducido ningún cambio.");
                return;
            }

            try {
                // Enviamos petición PUT manual
                const respuesta = await fetch('/profile/edit', {
                    method: 'PUT',
                    headers: {
                        'Content-Type': 'application/json'
                    },
                    body: JSON.stringify(datosParaEnviar)
                });

                if (respuesta.ok) {
                    // Si sale bien, volvemos al perfil
                    window.location.href = "/profile/";
                } else {
                    alert("Error al actualizar el perfil. Inténtalo de nuevo.");
                }

            } catch (error) {
                console.error("Error de conexión:", error);
                alert("Error de conexión con el servidor.");
            }
        });
    }
});