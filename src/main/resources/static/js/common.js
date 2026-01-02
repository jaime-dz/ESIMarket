/* Lógica Global y de Utilidad */

document.addEventListener("DOMContentLoaded", async function() {

    verificarSesionLocal();
    await validarSesionConServidor();
    if (typeof actualizarBarraNavegacion === 'function') {
        actualizarBarraNavegacion();
    }
    
    const botonLogout = document.getElementById('btn-logout');
    if (botonLogout) {
        botonLogout.addEventListener('click', (e) => {
            e.preventDefault(); 
            cerrarSesion();
        });
    }

    const inputBusqueda = document.getElementById("search-input");
    const botonBorrar = document.getElementById("clearBtn");

    // Función que filtra las tarjetas visualmente
    function filtrarProductos(texto) {
        const busqueda = texto.toLowerCase();
        // Seleccionamos todas las tarjetas que ya están en pantalla
        const tarjetas = document.querySelectorAll('.product-card');

        tarjetas.forEach(card => {
            const nombreProducto = card.querySelector('.product-name').textContent.toLowerCase();

            if (nombreProducto.includes(busqueda)) {
                card.style.display = 'flex';
            } else {
                card.style.display = 'none';
            }
        });
    }

    if(inputBusqueda && botonBorrar) {
        inputBusqueda.addEventListener("input", function() {
            const texto = inputBusqueda.value;
            
            if (texto.length > 0) {
                botonBorrar.style.display = "block";
            } else {
                botonBorrar.style.display = "none";
            }

            filtrarProductos(texto);
        });

        botonBorrar.addEventListener("click", function() {
            inputBusqueda.value = "";
            botonBorrar.style.display = "none";

            filtrarProductos(""); 
            
            inputBusqueda.focus();
        });
    }

    ocultarEnlacePaginaActual();

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

    const ordersContainer = document.querySelector('.desplegable-pedidos');
    if(ordersContainer){
        try{
            console.log("Carga de pedidos");
            const responseOrders = await fetch('/orders/filter', { // Apunta al método @PostMapping
                method: 'POST', 
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify({}) 
            });
            if(responseOrders.ok){
                const orders=await responseOrders.json();
                displayOrders(pedidos,ordersContainer);
            }
            else{
                console.error("Error al cargar pedidos (Status):", response.status);
                ordersContainer.innerHTML = "<p>No se pudieron cargar los pedidos.</p>";
            }
        } catch(error){
            console.error("Error de conexión al cargar pedidos:", error);
            ordersContainer.innerHTML = "<p>Error de conexión con el servidor.</p>";
        }
        

    }
});


/* FUNCIONES EXPORTABLES (Para usar en otros JS) */

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

        if (respuesta.ok) {
            console.log('Solicitud exitosa. Status:', respuesta.status);
            
            if (typeof actualizarBarraNavegacion === 'function') {
                actualizarBarraNavegacion();
            }
            window.location.href = "/home/";
            return;
        }

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


/* FUNCIONES INTERNAS */

function actualizarBarraNavegacion() {
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

export function displayOrders(pedidos, container) {
    if (!pedidos || pedidos.length === 0) {
        container.innerHTML = "<p>No hay pedidos</p>";
        return;
    }

    const listaHtml = pedidos.map(function(item) {
        const esServicio = item.tipo && item.tipo.toLowerCase() === 'servicio';

        let imagenFinal;
        if (item.foto) {
            imagenFinal = 'data:image/jpeg;base64,' + item.foto;
        } else {
            imagenFinal = esServicio ? '/Images/engranaje.jpg' : '/Images/book.jpg';
        }

        let etiquetaRol;
        let nombrePersona;

        if (item.esComprador) {
            etiquetaRol = "Vendedor";
            nombrePersona = item.nombreVendedor; 
        } else {
            etiquetaRol = "Comprador";
            nombrePersona = item.nombreComprador;
        }

        const estadoFormateado = item.estado ? item.estado.replace(/_/g, ' ').toLowerCase() : 'desconocido';
        
        let infoTaquilla = '';
        if (item.nTaquilla) {
            infoTaquilla = `
                <div style="background-color: #e3f2fd; padding: 5px; border-radius: 4px; margin: 5px 0; font-size: 0.9em;">
                    <strong>Taquilla:</strong> ${item.nTaquilla}<br>
                </div>`;
        }

        return `
            <div class="product-card" style="border-left: 5px solid #00587C;">
                <img src="${imagenFinal}" alt="${item.nombreProd}" class="product-image">
                
                <div class="product-info" style="padding: 10px;">
                    <h4 class="product-name">${item.nombreProd || 'Pedido sin nombre'}</h4>

                    <p class="product-state" style="text-transform: capitalize;">
                        Estado: <strong>${estadoFormateado}</strong>
                    </p>

                    ${infoTaquilla}

                    <p class="product-seller" style="font-size: 0.9em; color: #333; margin-top: 5px;">
                        <strong>${etiquetaRol}:</strong> ${nombrePersona || 'Desconocido'}
                    </p>
                    
                    <a href="/orders/track/${item.id}" class="btn-detail" 
                       style="display:block; text-align:center; margin-top:10px; background:#00587C; color:white; padding:5px; text-decoration:none; border-radius:5px;">
                       Ver estado
                    </a>
                </div>
            </div>`;
    });
    container.innerHTML = listaHtml.join("");
}
export function displayProductsItems(products, container) {
    if (!products || products.length === 0) {
        container.innerHTML = "<p>No hay productos disponibles.</p>";
        return;
    }

    const displayProducts = products.map(function(item) {
        const esServicio = item.tipo && item.tipo.toLowerCase() === 'servicio';

        let imagenFinal;
        if (item.foto) {
            imagenFinal = 'data:image/jpeg;base64,' + item.foto;
        } else {
            imagenFinal = esServicio ? '/Images/engranaje.jpg' : '/Images/book.jpg';
        }
        const sufijoPrecio = esServicio ? '/h' : '';

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

async function validarSesionConServidor() {
    if (getCookie('isLoggedIn') !== 'true') return;

    try {
        const response = await fetch('/auth/validate', { 
            method: 'GET',
            headers: { 'Accept': 'application/json' },
            credentials: 'include' 
        });

        if (response.status === 401 || response.status === 403) {
            console.warn("La sesión ha expirado en el servidor.");
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