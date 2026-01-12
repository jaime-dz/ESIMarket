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

            formData.forEach((value, key) => {
                if (!value || value.trim() === "") {
                    datosParaEnviar[key] = null;
                } else {
                    datosParaEnviar[key] = value.trim();
                }
            });

            console.log("Enviando:", datosParaEnviar);

            try {
                const respuesta = await fetch('/profile/edit', {
                    method: 'PUT',
                    headers: { 'Content-Type': 'application/json' },
                    body: JSON.stringify(datosParaEnviar)
                });
                
                if (respuesta.ok) {
                    window.location.href = "/profile/";
                } else {
                    alert("Error al actualizar");
                }
            } catch (error) {
                console.error(error);
            }
        });
    }
});