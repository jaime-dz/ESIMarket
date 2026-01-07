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
    const filtroSelect = document.getElementById('filtro-ped');
    const btnAplicarFiltro = document.querySelector('.submit-button button');
    
    if (filtroSelect && btnAplicarFiltro) {
        let containerPedidos = document.getElementById('lista-pedidos-container');
        if (!containerPedidos) {
            containerPedidos = document.createElement('div');
            containerPedidos.id = 'lista-pedidos-container';
            containerPedidos.style.marginTop = '20px';
            document.querySelector('.submit-button').after(containerPedidos);
        }

        gestionarCargaPedidos('todos');

        btnAplicarFiltro.addEventListener('click', (e) => {
            e.preventDefault();
            const valorFiltro = filtroSelect.value;
            gestionarCargaPedidos(valorFiltro);
        });
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
        let mensajeError = "Error";
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
            imagenFinal = 'data:image/jpeg;base64,' + item.foto;
        } else {
            // Si item.foto es null, usamos la ruta local por defecto
            imagenFinal = esServicio ? '/Images/engranaje.jpg' : '/Images/book.jpg';
        }
        // C. SUFIJO DE PRECIO
        const sufijoPrecio = esServicio ? '/h' : '';

        // D. LÓGICA DE ESTADO
        const htmlEstado = (!esServicio && item.estado) 
            ? `<p class="product-state">${item.estado.replace(/_/g, ' ')}</p>` 
            : ''; 

        return `
            <div class="product-card" data-category="${item.tipo}" data-seller="${item.nombreVendedor}">
                <img src="${imagenFinal}" alt="${item.nombre}" class="product-image">
                <h4 class="product-name">${item.nombre}</h4>
                <p class="product-price">${item.precio} ⚙️${sufijoPrecio}</p>
                ${htmlEstado}
                <p class="product-seller">Vendido por: ${item.nombreVendedor}</p>
                <a href="/products/view/${item.id}" class="btn-detail" style="display:block; text-align:center; margin-top:10px; background:#E57200; color:white; padding:5px; text-decoration:none; border-radius:5px;">
                    Ver detalle
                </a>
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

window.toggleMenu = function() {
    const menu = document.getElementById("sideMenu");
    if (menu.style.width === "250px") {
        menu.style.width = "0";
    } else {
        menu.style.width = "250px";
    }
}

async function gestionarCargaPedidos(filtro) {
    const container = document.getElementById('lista-pedidos-container');
    container.innerHTML = '<p style="text-align:center;">Cargando pedidos...</p>';

    try {
        const response = await fetch('/orders/filter', {
            method: 'GET',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({ filter: filtro })
        });

        if (response.ok) {
            const pedidos = await response.json();
            renderizarListaPedidos(pedidos, container);
        } else {
            console.error("Error status:", response.status);
            container.innerHTML = '<p style="color:red; text-align:center;">Error al cargar los pedidos.</p>';
        }
    } catch (error) {
        console.error("Error de conexión:", error);
        container.innerHTML = '<p style="color:red; text-align:center;">Error de conexión con el servidor.</p>';
    }
}

function renderizarListaPedidos(pedidos, container) {
    if (!pedidos || pedidos.length === 0) {
        container.innerHTML = '<p style="text-align:center;">No se encontraron pedidos con este filtro.</p>';
        return;
    }

    const htmlPedidos = pedidos.map(p => {
        // Preparar imagen
        const imagen = p.fotoBase64 ? p.fotoBase64 : '/Images/book.jpg';
        
        let imagenFinal;
        if (p.foto) {
            // Si el backend nos devuelve datos (el byte[]), es un string Base64 limpio.
            // Le agregamos la cabecera para que el navegador lo entienda como imagen.
            imagenFinal = 'data:image/jpeg;base64,' + p.foto;
        } else {
            // Si item.foto es null, usamos la ruta local por defecto
            imagenFinal = '/Images/book.jpg';
        }
        // Determinar rol y acciones
        let botonesAccion = '';

        // LÓGICA VENDEDOR: Si soy vendedor y estado es 'PorEntregar' -> Botón Entregar
        if (!p.esComprador && p.estado === 'PorEntregar') {
            botonesAccion = `
                <button onclick="accionEntregarPedido(${p.idPedido}, ${p.enTaquilla})" 
                        style="background:#E57200; color:white; border:none; padding:8px 15px; border-radius:5px; cursor:pointer; margin-top:10px;">
                    Marcar como Entregado
                </button>`;
        }
        
        // LÓGICA COMPRADOR: Si soy comprador y estado es 'Entregado' -> Botón Recoger
        if (p.esComprador && p.estado === 'Entregado') {
            botonesAccion = `
                <button onclick="accionRecogerPedido(${p.idPedido})" 
                        style="background:#28a745; color:white; border:none; padding:8px 15px; border-radius:5px; cursor:pointer; margin-top:10px;">
                    Confirmar Recogida
                </button>`;
        }

        // Info de taquilla si aplica
        const infoTaquilla = p.enTaquilla && p.nTaquilla > 0 
            ? `<p style="color:#E57200; font-weight:bold;">📍 En Taquilla Nº ${p.nTaquilla}</p>` 
            : '';

        return `
            <div class="product-card" style="display:flex; flex-direction:row; width:95%; max-width:800px; margin:10px auto; align-items:center; gap:15px; text-align:left;">
                <img src="${imagen}" alt="${p.nombreProd}" style="width:100px; height:100px; object-fit:cover; border-radius:8px;">
                <div style="flex:1;">
                    <h3 style="margin:0 0 5px 0;">${p.nombreProd}</h3>
                    <p style="margin:0; font-size:0.9em; color:#666;">
                        <strong>Estado:</strong> ${p.estado} <br>
                        <strong>Vendedor:</strong> ${p.nombreVendedor} | 
                        <strong>Comprador:</strong> ${p.nombreComprador}
                    </p>
                    ${infoTaquilla}
                </div>
                <div style="text-align:right;">
                    ${botonesAccion}
                </div>
            </div>
        `;
    }).join('');

    container.innerHTML = htmlPedidos;
}

// Hacemos las funciones globales para que el onclick del HTML las encuentre
window.accionEntregarPedido = async function(idPedido, requiereTaquilla) {
    let numTaquilla = 0;
    
    // Si el producto requiere taquilla (según tu lógica de negocio), pedimos el número
    // Nota: Tu DTO tiene 'enTaquilla' como boolean, asumimos que si es true, preguntamos.
    // Si en tu lógica siempre se puede poner taquilla, quita el 'if'.
    // Tu controlador espera un path variable: /deliver/{id}/{taquilla}
    
    const inputTaquilla = prompt("Introduce el número de taquilla (pon 0 si es entrega en mano):", "0");
    if (inputTaquilla === null) return; // Cancelado
    numTaquilla = parseInt(inputTaquilla) || 0;

    try {
        const response = await fetch(`/orders/deliver/${idPedido}/${numTaquilla}`, {
            method: 'PUT'
        });

        if (response.ok) {
            alert("Pedido marcado como entregado.");
            // Recargar la lista manteniendo el filtro actual
            const filtroActual = document.getElementById('filtro-ped').value;
            gestionarCargaPedidos(filtroActual);
        } else {
            alert("Error al actualizar el pedido.");
        }
    } catch (e) {
        console.error(e);
        alert("Error de conexión.");
    }
};

window.accionRecogerPedido = async function(idPedido) {
    if(!confirm("¿Confirmas que has recogido el producto?")) return;

    try {
        const response = await fetch(`/orders/pickup/${idPedido}`, {
            method: 'PUT'
        });

        if (response.ok) {
            alert("¡Pedido completado!");
            const filtroActual = document.getElementById('filtro-ped').value;
            gestionarCargaPedidos(filtroActual);
        } else {
            alert("Error al confirmar recogida.");
        }
    } catch (e) {
        console.error(e);
        alert("Error de conexión.");
    }
};