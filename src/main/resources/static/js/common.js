document.addEventListener("DOMContentLoaded", function() {

    // 1. Verificamos el estado visual y redirecciones basado en LocalStorage
    verificarSesionLocal();

    const inputBusqueda = document.getElementById("search-input");
    const botonBorrar = document.getElementById("clearBtn");

    if(inputBusqueda && botonBorrar) {
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
            inputBusqueda.focus();
        });
    }
});

/* * Esta función maneja TANTO el Login COMO el Registro.
 * Toma la URL del atributo 'action' del HTML (<form action="/auth/login"> o <form action="/auth/signup">)
 */
export async function enviarFormularioComoJSON(evento) {
    evento.preventDefault();

    const form = evento.target;
    const url = form.action; 
    const method = form.method; 

    // Limpiamos mensajes previos
    const divMensaje = document.getElementById('signup-message') || document.getElementById('login-message');
    if (divMensaje) divMensaje.style.display = 'none';

    try {
        const formData = new FormData(form);
        const data = Object.fromEntries(formData.entries());
        const jsonString = JSON.stringify(data);

        const respuesta = await fetch(url, {
            method: method,
            headers: {
                'Content-Type': 'application/json',
                'Accept': 'application/json'
            },
            body: jsonString,
            credentials: 'include' 
        });

        /* --------------------------------------------------------
           ZONA DE SEGURIDAD: LECTURA DE RESPUESTA
           -------------------------------------------------------- */
        
        // 1. CASO DE ÉXITO (Status 200-299)
        if (respuesta.ok) {
            console.log('Solicitud exitosa. Status:', respuesta.status);

            // NO intentamos leer JSON aquí porque tu backend devuelve Void (vacío).
            
            // a. Guardamos sesión
            localStorage.setItem('isLoggedIn', 'true');
            
            // b. Actualizamos UI (si tienes la función importada o disponible)
            if (typeof actualizarBarraNavegacion === 'function') {
                actualizarBarraNavegacion();
            }

            // c. Redirigimos
            window.location.href = "/home/";
            
            return; // ¡IMPORTANTE! Salimos de la función aquí.
        }

        /* --------------------------------------------------------
           2. CASO DE ERROR (Status 400, 401, 500...)
           -------------------------------------------------------- */
        
        // Aquí estaba el problema antes. Si el error venía vacío, .json() fallaba.
        // Ahora lo protegemos:
        let mensajeError = "Error al procesar la solicitud.";

        try {
            // Intentamos leer el cuerpo del error con cuidado
            const errorData = await respuesta.json();
            if (errorData && errorData.message) {
                mensajeError = errorData.message;
            }
        } catch (jsonError) {
            // Si entra aquí, es que el servidor devolvió un error SIN cuerpo JSON (ej: un 401 vacío).
            console.warn("El servidor devolvió error sin JSON detallado.");
            if (respuesta.status === 401 || respuesta.status === 403) {
                mensajeError = "Credenciales incorrectas o acceso denegado.";
            } else {
                mensajeError = `Error del servidor (${respuesta.status})`;
            }
        }

        // Mostramos el mensaje final en pantalla
        console.error('Fallo en login/registro:', mensajeError);
        
        if (divMensaje) {
            divMensaje.textContent = mensajeError;
            divMensaje.style.color = 'red';
            divMensaje.style.display = 'block';
        }

    } catch (error) {
        // Este catch captura errores de RED (servidor apagado, sin internet)
        console.error('Error de red crítico:', error);
        if (divMensaje) {
            divMensaje.textContent = 'Error de conexión. Verifica que el servidor esté encendido.';
            divMensaje.style.color = 'red';
            divMensaje.style.display = 'block';
        }
    }
}

function actualizarBarraNavegacion() {
    const loginLinks = document.querySelector('.login'); 
    const profileSquare = document.querySelector('.profile-square'); 

    // Confiamos en la variable local ya que no hay endpoint de check
    const estaLogueado = localStorage.getItem('isLoggedIn') === 'true';

    if (estaLogueado) {
        if(loginLinks) loginLinks.style.display = "none";
        if(profileSquare) profileSquare.style.display = "block"; 
    } else {
        if(loginLinks) loginLinks.style.display = "block"; 
        if(profileSquare) profileSquare.style.display = "none";
    }
}

/* * Verifica la sesión basándose SOLO en localStorage 
 * (ya que no existe endpoint /auth/check)
 */
function verificarSesionLocal() {
    
    // 1. Comprobamos la bandera local
    const estaLogueado = localStorage.getItem('isLoggedIn') === 'true';

    // 2. Actualizamos la UI
    actualizarBarraNavegacion();

    // 3. Lógica de REDIRECCIÓN
    // Si dice que estoy logueado...
    if (estaLogueado) {
        const path = window.location.pathname.toLowerCase();
        
        // ...y estoy intentando ver la página de login o registro...
        if (path.includes("login") || path.includes("signup") || path.includes("registro") || path.includes("iniciar-sesion")) {
            console.log("Usuario aparentemente logueado. Redirigiendo a home...");
            window.location.href = "/home/"; 
        }
    }
    // Nota: Sin endpoint de check, no podemos saber si la cookie expiró hasta que el usuario intente hacer algo en /home/
}

/* Función auxiliar para cerrar sesión (conéctala a tu botón de Logout) */
export function cerrarSesion() {
    localStorage.removeItem('isLoggedIn');
    window.location.href = "/home/";
}