/* ==============================================
   common.js - Lógica Global y de Utilidad
   ============================================== */

document.addEventListener("DOMContentLoaded", async function() {

    // 1. GESTIÓN DE SESIÓN Y NAVEGACIÓN
    verificarSesionLocal();
    await validarSesionConServidor();
    if (typeof actualizarBarraNavegacion === 'function') {
        actualizarBarraNavegacion();
    }

    // 2. LOGOUT (Si existe el botón)
    const botonLogout = document.getElementById('btn-logout');
    if (botonLogout) {
        botonLogout.addEventListener('click', (e) => {
            e.preventDefault(); 
            cerrarSesion();
        });
    }

    // 3. BUSCADOR (Lógica completa)
    const inputBusqueda = document.getElementById("search-input");
    const botonBorrar = document.getElementById("clearBtn");

    // Función que filtra las tarjetas visualmente
    function filtrarProductos(texto) {
        const busqueda = texto.toLowerCase();
        // Seleccionamos todas las tarjetas que ya están pintadas en pantalla
        const tarjetas = document.querySelectorAll('.product-card');

        tarjetas.forEach(card => {
            // Buscamos el nombre dentro de la tarjeta
            const nombreProducto = card.querySelector('.product-name').textContent.toLowerCase();
            
            // Si coincide, mostramos (block/flex), si no, ocultamos (none)
            if (nombreProducto.includes(busqueda)) {
                card.style.display = 'flex'; // O 'block' según tu diseño original
            } else {
                card.style.display = 'none';
            }
        });
    }

    if(inputBusqueda && botonBorrar) {
        // Evento al escribir
        inputBusqueda.addEventListener("input", function() {
            const texto = inputBusqueda.value;
            
            // 1. Mostrar u ocultar la X
            if (texto.length > 0) {
                botonBorrar.style.display = "block";
            } else {
                botonBorrar.style.display = "none";
            }

            // 2. Filtrar los productos
            filtrarProductos(texto);
        });
        
        // Evento al borrar con la X
        botonBorrar.addEventListener("click", function() {
            inputBusqueda.value = "";
            botonBorrar.style.display = "none";
            
            // Importante: Volver a mostrar todos los productos
            filtrarProductos(""); 
            
            inputBusqueda.focus();
        });
    }

    // 4. GESTIÓN MENU FOOTER
    ocultarEnlacePaginaActual();

    // 5. CARGA DE PRODUCTOS (Solo si existe el contenedor .product-grid-container)
    const productContainer = document.querySelector('.product-grid-container');
    
    if (productContainer) {
        try {
            console.log("Iniciando carga de productos...");
            const response = await fetch('/products/'); 
            
            if(response.ok) {
                const products = await response.json();
                displayProductsItems(products, productContainer);
            } else {
                console.error("Error al cargar productos (Status):", response.status);
                productContainer.innerHTML = "<p>No se pudieron cargar los productos.</p>";
            }
        } catch (error) {
            console.error("Error de conexión al cargar productos:", error);
            productContainer.innerHTML = "<p>Error de conexión con el servidor.</p>";
        }
    }
});


/* ==============================================
   FUNCIONES EXPORTABLES (Para usar en otros JS)
   ============================================== */

export async function enviarFormularioComoJSON(evento) {
    evento.preventDefault();

    const form = evento.target;
    const url = form.action; 
    const method = form.method; 

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
        
        // 1. ÉXITO
        if (respuesta.ok) {
            console.log('Solicitud exitosa. Status:', respuesta.status);
            
            if (typeof actualizarBarraNavegacion === 'function') {
                actualizarBarraNavegacion();
            }
            window.location.href = "/home/";
            return;
        }

        // 2. ERROR
        let mensajeError = "Usuario o contraseña incorrectos";
        try {
            const errorData = await respuesta.json();
            if (errorData && errorData.message) {
                mensajeError = errorData.message;
            }
        } catch (jsonError) {
            console.warn("El servidor devolvió error sin JSON.");
            if (respuesta.status === 401 || respuesta.status === 403) {
                mensajeError = "Credenciales incorrectas o acceso denegado.";
            } else {
                mensajeError = `Error del servidor (${respuesta.status})`;
            }
        }

        console.error('Fallo en login/registro:', mensajeError);
        if (divMensaje) {
            divMensaje.textContent = mensajeError;
            divMensaje.style.color = 'red';
            divMensaje.style.display = 'block';
        }

    } catch (error) {
        console.error('Error de red crítico:', error);
        if (divMensaje) {
            divMensaje.textContent = 'Error de conexión. Verifica que el servidor esté encendido.';
            divMensaje.style.color = 'red';
            divMensaje.style.display = 'block';
        }
    }
}

export async function cerrarSesion() {
    try {
        const respuesta = await fetch('/auth/logout', {
            method: 'DELETE', 
            headers: { 'Content-Type': 'application/json' },
            credentials: 'include'
        });

        if (respuesta.ok) {
            console.log("Sesión cerrada en servidor.");
        }
    } catch (error) {
        console.error("Error al intentar cerrar sesión:", error);
    } finally {
        localStorage.removeItem('isLoggedIn'); 
        window.location.href = "/home/";
    }
}


/* ==============================================
   FUNCIONES INTERNAS (Helpers)
   ============================================== */

function actualizarBarraNavegacion() {
    // CAMBIO: Leemos la cookie 'isLoggedIn' en lugar del localStorage
    // Asumimos que si la cookie existe y tiene valor 'true', el usuario está logueado
    const cookieVal = getCookie('isLoggedIn');
    const estaLogueado = cookieVal === 'true'; 

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
    // CAMBIO: Usamos getCookie
    const cookieVal = getCookie('isLoggedIn');
    const estaLogueado = cookieVal === 'true';

    const path = window.location.pathname.toLowerCase();

    actualizarBarraNavegacion();

    if (estaLogueado) {
        if (path.includes("login") || path.includes("signup") || path.includes("registro")) {
            console.log("Usuario logueado intentando acceder a auth. Redirigiendo...");
            window.location.href = "/home/"; 
        }
    } else {
        if (path.includes("profile")) {
            window.location.href="/home/";
        }
    }
}

function ocultarEnlacePaginaActual() {
    const currentPath = window.location.pathname;
    const footerLinks = document.querySelectorAll('.menu-inferior ul li a');

    footerLinks.forEach(link => {
        if (link.querySelector('img')) return;
        const linkPath = new URL(link.href, window.location.origin).pathname;
        const cleanCurrent = currentPath.endsWith('/') ? currentPath.slice(0, -1) : currentPath;
        const cleanLink = linkPath.endsWith('/') ? linkPath.slice(0, -1) : linkPath;

        if (cleanCurrent === cleanLink || (cleanCurrent === "" && cleanLink === "/home")) {
            if (link.parentElement.tagName === 'LI') {
                link.parentElement.style.display = 'none';
            }
        }
    });
}

// Función encargada de pintar el HTML de los productos
export function displayProductsItems(products, container) {
    if (!products || products.length === 0) {
        container.innerHTML = "<p>No hay productos disponibles.</p>";
        return;
    }

    const displayProducts = products.map(function(item) {
        // A. DETECTAR TIPO
        const esServicio = item.tipo && item.tipo.toLowerCase() === 'servicio';

        // B. LÓGICA DE FOTO
        let imagenFinal;
        if (item.foto) {
            // Si el backend nos devuelve datos (el byte[]), es un string Base64 limpio.
            // Le agregamos la cabecera para que el navegador lo entienda como imagen.
            imagenFinal = "data:image/jpeg;base64," + item.foto;
        } else {
            // Si item.foto es null, usamos la ruta local por defecto
            imagenFinal = esServicio ? '/Images/engranaje.jpg' : '/Images/book.jpg';
        }
        // C. SUFIJO DE PRECIO
        const sufijoPrecio = esServicio ? '/h' : '';

        // D. LÓGICA DE ESTADO
        const htmlEstado = (!esServicio && item.estado) 
            ? `<p class="product-state">${item.estado}</p>` 
            : ''; 

        return `
            <div class="product-card" data-category="${item.tipo}" data-seller="${item.nombreVendedor}">
                <img src="${imagenFinal}" alt="${item.nombre}" class="product-image">
                <h4 class="product-name">${item.nombre}</h4>
                <p class="product-price">${item.precio} ⚙️${sufijoPrecio}</p>
                ${htmlEstado}
                <p class="product-seller">Vendido por: ${item.nombreVendedor}</p>
            </div>
        `;
    });

    container.innerHTML = displayProducts.join("");
}

// Agrega esto en la sección de FUNCIONES INTERNAS
async function validarSesionConServidor() {
    // CAMBIO: Si no hay cookie, no hacemos la llamada extra al servidor
    if (getCookie('isLoggedIn') !== 'true') return;

    try {
        const response = await fetch('/auth/validate', { 
            method: 'GET',
            headers: { 'Accept': 'application/json' },
            credentials: 'include' 
        });

        if (response.status === 401 || response.status === 403) {
            console.warn("La sesión ha expirado en el servidor.");
            // Ya no borramos nada manualmente porque el navegador 
            // gestionará la expiración de la cookie si el servidor lo indica
            actualizarBarraNavegacion(); 
            window.location.href = "/home/";
        }
    } catch (error) {
        console.error("Error verificando estado de la sesión:", error);
    }
}

function getCookie(name) {
    const value = `; ${document.cookie}`;
    const parts = value.split(`; ${name}=`);
    if (parts.length === 2) return parts.pop().split(';').shift();
    return null;
}