document.addEventListener("DOMContentLoaded", function() {

    // 1. Verificamos el estado visual y redirecciones basado en LocalStorage
    verificarSesionLocal();
    if (typeof actualizarBarraNavegacion === 'function') {
        actualizarBarraNavegacion();
    }

    // 2. NUEVO: Gestionar visibilidad por Página Actual (Footer)
    ocultarEnlacePaginaActual();
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

        // Dentro del evento DOMContentLoaded en common.js
    const botonLogout = document.getElementById('btn-logout');
    if (botonLogout) {
        botonLogout.addEventListener('click', (e) => {
            e.preventDefault(); // Evita comportamientos por defecto si es un link <a>
            cerrarSesion();
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
            window.location.href = "/";
            
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
    const estaLogueado = localStorage.getItem('isLoggedIn') === 'true';

    const guestElements = document.querySelectorAll('.guest-view');

    const userElements = document.querySelectorAll('.user-view');

    if (estaLogueado) {
        guestElements.forEach(el => el.style.display = 'none');
        userElements.forEach(el => el.style.display = 'block');
    } else {
        guestElements.forEach(el => el.style.display = 'block');
        userElements.forEach(el => el.style.display = 'none');
    }
}

function verificarSesionLocal() {

    const estaLogueado = localStorage.getItem('isLoggedIn') === 'true';

    actualizarBarraNavegacion();

    if (estaLogueado) {
        const path = window.location.pathname.toLowerCase();

        if (path.includes("login") || path.includes("signup") || path.includes("registro") || path.includes("iniciar-sesion")) {
            console.log("Usuario aparentemente logueado. Redirigiendo a home...");
            window.location.href = "/"; 
        }
    }
    else
    {
        if (path.includes("profile"))
        {
            window.location.href="/";
        }
    }
}

/* Función para cerrar sesión en Backend y Frontend */
export async function cerrarSesion() {
    try {
        // 1. Petición al Backend para destruir la cookie de sesión
        // Ajusta el método ('POST' o 'GET') según lo requiera tu backend.
        // Lo estándar suele ser POST.
        const respuesta = await fetch('/auth/logout', {
            method: 'DELETE', 
            headers: {
                'Content-Type': 'application/json'
            },
            credentials: 'include' // ¡CRUCIAL! Para enviar la cookie que se va a borrar
        });

        if (respuesta.ok) {
            console.log("Sesión cerrada en el servidor correctamente.");
        } else {
            console.warn("El servidor respondió error al logout, pero cerraremos localmente.");
        }

    } catch (error) {
        console.error("Error de red al intentar cerrar sesión:", error);
    } finally {
        // 2. Limpieza Local y Redirección
        // Usamos 'finally' para asegurar que, aunque falle el servidor o la red,
        // al usuario se le cierre la sesión visualmente y se le redirija.
        
        localStorage.removeItem('isLoggedIn'); // Borramos la bandera local
        window.location.href = "/";            // Redirigimos a la raíz
    }
    localStorage.removeItem('isLoggedIn'); location.reload();
}

function ocultarEnlacePaginaActual() {
    // 1. Obtenemos la ruta actual (ej: "/home/" o "/home/about")
    const currentPath = window.location.pathname;

    // 2. Seleccionamos todos los enlaces del menú inferior
    const footerLinks = document.querySelectorAll('.menu-inferior ul li a');

    footerLinks.forEach(link => {
        // Ignoramos los botones de redes sociales (que tienen imágenes dentro)
        if (link.querySelector('img')) return;

        // 3. Obtenemos la ruta del enlace limpio (sin dominio)
        // Usamos new URL para asegurar que comparamos rutas absolutas
        const linkPath = new URL(link.href, window.location.origin).pathname;

        // 4. Normalizamos (quitamos la barra final para comparar mejor)
        // Ejemplo: "/home/" se convierte en "/home"
        const cleanCurrent = currentPath.endsWith('/') ? currentPath.slice(0, -1) : currentPath;
        const cleanLink = linkPath.endsWith('/') ? linkPath.slice(0, -1) : linkPath;

        // Comparamos. También manejamos el caso especial de la raíz "/"
        if (cleanCurrent === cleanLink || (cleanCurrent === "" && cleanLink === "/home")) {
            // Si coinciden, ocultamos el elemento de la lista (LI) entero
            if (link.parentElement.tagName === 'LI') {
                link.parentElement.style.display = 'none';
            }
        }
    });
}