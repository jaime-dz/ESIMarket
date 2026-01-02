document.addEventListener("DOMContentLoaded", () => {

    const btnEditar = document.getElementById("edit-profile");
    if(btnEditar){
        btnEditar.addEventListener("click", () => {
            window.location.href = '/profile/edit';
        });
    }

    const editForm = document.getElementById("edit-form");
    if (editForm) {
        editForm.addEventListener("submit", async (event) => {
            event.preventDefault();

            const formData = new FormData(editForm);
            const datosParaEnviar = {};
            let hayCambios = false;

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
                const respuesta = await fetch('/profile/edit', {
                    method: 'PUT',
                    headers: {
                        'Content-Type': 'application/json'
                    },
                    body: JSON.stringify(datosParaEnviar)
                });

                if (respuesta.ok) {
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