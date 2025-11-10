document.addEventListener("DOMContentLoaded", function() {

    const inputBusqueda = document.getElementById("search-input");
    const botonBorrar = document.getElementById("clearBtn");

    inputBusqueda.addEventListener("input", function() {
        if (inputBusqueda.value.length > 0) {
            botonBorrar.style.display = "block";
        } else {
            botonBorrar.style.display = "none";
        }
    });
    
    botonBorrar.addEventListener("click", function() {
        inputBusqueda.value = "";

        botonBorrar.style.display = "none";
        
        // Opcional: vuelve a poner el foco en el input
        inputBusqueda.focus();
    });

});
/* * Esta función intercepta el envío de un formulario,
 * lo convierte a JSON y lo envía usando fetch.
 */
export async function enviarFormularioComoJSON(evento) {
    // 1. Prevenir el envío normal del formulario
    evento.preventDefault();

    const form = evento.target;
    const url = form.action; // La URL del atributo 'action' del form
    const method = form.method; // El método del atributo 'method' (POST)

    try {
        // 2. Obtener todos los datos del formulario
        const formData = new FormData(form);

        // 3. Convertir los datos a un objeto simple
        const data = Object.fromEntries(formData.entries());

        // 4. Convertir el objeto a un string JSON
        const jsonString = JSON.stringify(data);

        // 5. Enviar los datos usando fetch
        const respuesta = await fetch(url, {
            method: method,
            headers: {
                'Content-Type': 'application/json', // AVISAMOS que enviamos JSON
                'Accept': 'application/json' // PEDIMOS que nos respondan con JSON
            },
            body: jsonString // El cuerpo de la petición es nuestro string JSON
        });

        // 6. Procesar la respuesta del servidor
        const resultado = await respuesta.json();

        if (!respuesta.ok) {
            // Si el servidor responde con un error (ej: 400, 404, 500)
            console.error('Error del servidor:', resultado);
            // Mostramos el error en la interfaz (usando tu div #signup-message)
            const divMensaje = document.getElementById('signup-message') || document.getElementById('login-message');
            if (divMensaje) {
                divMensaje.textContent = resultado.message || 'Error al procesar la solicitud.';
                divMensaje.style.color = 'red';
            }
            return;
        }

        // 7. ÉXITO: El servidor respondió con un 200 (OK)
        console.log('Éxito:', resultado);

        // Ejemplo: Si el servidor responde con una URL a la que redirigir
        if (resultado.redirectTo) {
            window.location.href = resultado.redirectTo;
        } else {
            // O mostrar un mensaje de éxito
            const divMensaje = document.getElementById('signup-message');
            if (divMensaje) {
                divMensaje.textContent = resultado.message || '¡Registro completado!';
                divMensaje.style.color = 'green';
            }
        }

    } catch (error) {
        console.error('Error de red:', error);
        const divMensaje = document.getElementById('signup-message');
        if (divMensaje) {
            divMensaje.textContent = 'Error de conexión. Inténtalo de nuevo.';
            divMensaje.style.color = 'red';
        }
    }
}