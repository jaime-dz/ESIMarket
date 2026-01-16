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

function abrirModalEliminar() {
    document.getElementById("modalEliminarPerfil").style.display = "flex";
}

function cerrarModalEliminar() {
    document.getElementById("modalEliminarPerfil").style.display = "none";
}

window.onclick = function(event) {
    const modal = document.getElementById("modalEliminarPerfil");
    if (event.target == modal) {
        modal.style.display = "none";
    }
}

function confirmarEliminarPerfil() {
    fetch('/profile/delete', {
        method: 'DELETE',
        headers: {
            'Content-Type': 'application/json'
        }
    })
    .then(response => {
        if (response.ok) {
            window.location.href = '/home/';
        } else {
            alert("Hubo un error al intentar eliminar el perfil.");
        }
    })
    .catch(error => {
        console.error('Error:', error);
        alert("Error de conexión.");
    });
}