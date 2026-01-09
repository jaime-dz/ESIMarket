/* common.js - Lógica Global y de Utilidad */

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

    function filtrarProductos(texto) {
        const busqueda = texto.toLowerCase();
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


/* FUNCIONES EXPORTABLES */

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

/*
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

*/
function renderizarListaPedidos(pedidos, container) {
    if (!pedidos || pedidos.length === 0) {
        container.innerHTML = '<p style="text-align:center;">No se encontraron pedidos con este filtro.</p>';
        return;
    }

    const htmlPedidos = pedidos.map(p => {
        const imagen = p.fotoBase64 ? p.fotoBase64 : '/Images/book.jpg';
        
        let imagenFinal;
        if (p.foto) {
            imagenFinal = 'data:image/jpeg;base64,' + p.foto;
        } else {
            imagenFinal = '/Images/book.jpg';
        }
        let botonesAccion = '';

        if (!p.esComprador && p.estado === 'PorEntregar') {
            botonesAccion = `
                <button onclick="accionEntregarPedido(${p.idPedido}, ${p.enTaquilla})" 
                        style="background:#E57200; color:white; border:none; padding:8px 15px; border-radius:5px; cursor:pointer; margin-top:10px;">
                    Marcar como Entregado
                </button>`;
        }
        
        if (p.esComprador && p.estado === 'Entregado') {
            botonesAccion = `
                <button onclick="accionRecogerPedido(${p.idPedido})" 
                        style="background:#28a745; color:white; border:none; padding:8px 15px; border-radius:5px; cursor:pointer; margin-top:10px;">
                    Confirmar Recogida
                </button>`;
        }

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

window.accionEntregarPedido = async function(idPedido, requiereTaquilla) {
    let numTaquilla = 0;
    
    
    const inputTaquilla = prompt("Introduce el número de taquilla (pon 0 si es entrega en mano):", "0");
    if (inputTaquilla === null) return; // Cancelado
    numTaquilla = parseInt(inputTaquilla) || 0;

    try {
        const response = await fetch(`/orders/deliver/${idPedido}/${numTaquilla}`, {
            method: 'PUT'
        });

        if (response.ok) {
            alert("Pedido marcado como entregado.");
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

window.toggleTruequeField = function() {
    const select = document.getElementById('select-pago');
    const field = document.getElementById('trueque-field');
    if (select && field) {
        if (select.value === 'Trueque') {
            field.style.display = 'block';
        } else {
            field.style.display = 'none';
            // Limpiamos el valor si se oculta
            const input = document.getElementById('input-trueque-id');
            if (input) input.value = '';
        }
    }
};

window.comprarProducto = async function(idProducto) {
    // 1. Recoger valores del DOM
    const pagoSelect = document.getElementById('select-pago');
    const recepcionSelect = document.getElementById('select-recepcion');
    const horasInput = document.getElementById('input-horas');
    const truequeInput = document.getElementById('input-trueque-id');

    // 2. Preparar variables
    const tipoPago = pagoSelect ? pagoSelect.value : "Monedas"; // Valor por defecto
    const recepcion = recepcionSelect ? recepcionSelect.value : "enMano";
    
    // Parseo de números
    const horas = horasInput ? parseInt(horasInput.value) : null;
    
    // Solo enviamos ID de trueque si el usuario seleccionó "Trueque"
    let idProdTrueque = null;
    if (tipoPago === 'Trueque' && truequeInput) {
        if (!truequeInput.value) {
            alert("Por favor, introduce el ID del producto que ofreces para el trueque.");
            return;
        }
        idProdTrueque = parseInt(truequeInput.value);
    }

    if (!confirm("¿Estás seguro de que deseas realizar esta compra?")) {
        return;
    }

    // 3. Construir el Payload (debe coincidir con CompraRequest.java)
    const payload = {
        idProd: idProducto,          // Integer
        tipoPago: tipoPago,          // Enum (Monedas, Trueque)
        recepcion: recepcion,        // Enum (enMano, enTaquilla)
        horas: horas,                // Long (puede ser null)
        idProdTrueque: idProdTrueque // Integer (puede ser null)
    };

    try {
        const response = await fetch(`/purchase/`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(payload)
        });

        if (response.ok) {
            alert("¡Compra realizada con éxito!");
            window.location.href = "/home/";
        } else {
            const errorText = await response.text();
            alert("Error al realizar la compra: " + (errorText || "Inténtalo de nuevo."));
        }
    } catch (error) {
        console.error("Error de conexión:", error);
        alert("Error de conexión con el servidor.");
    }
};


// 1. Abrir Modal
window.abrirModalCompra = function(idProducto) {
    const modal = document.getElementById('modalCompra');
    const inputId = document.getElementById('modal-product-id');
    
    if(modal && inputId) {
        inputId.value = idProducto; // Guardamos el ID para usarlo al enviar
        
        // Resetear formulario por si acaso
        const truequeInput = document.getElementById('modal-input-trueque-id');
        if(truequeInput) truequeInput.value = '';
        
        // Ejecutar la lógica de visualización del campo trueque inicial
        toggleTruequeModal();
        
        // Mostrar modal (flex para que centre)
        modal.style.display = 'flex';
    }
};

// 2. Cerrar Modal
window.cerrarModalCompra = function() {
    const modal = document.getElementById('modalCompra');
    if(modal) {
        modal.style.display = 'none';
    }
};

// 3. Controlar visibilidad del campo Trueque dentro del modal
window.toggleTruequeModal = function() {
    const select = document.getElementById('modal-select-pago');
    const field = document.getElementById('modal-trueque-field');
    
    if (select && field) {
        if (select.value === 'Trueque') {
            field.style.display = 'block';
        } else {
            field.style.display = 'none';
        }
    }
};

// 4. Enviar Compra (Fetch)
window.enviarCompra = async function() {
    const btnConfirmar = document.querySelector('.modal-footer .btn-estilo:last-child');
    const textoOriginal = btnConfirmar.innerText;
    btnConfirmar.innerText = "Procesando...";
    btnConfirmar.disabled = true;

    // 1. Obtener elementos (el de recepción puede no existir si es servicio)
    const inputPago = document.getElementById('select-pago');
    const inputRecepcion = document.getElementById('select-recepcion');
    const inputHoras = document.getElementById('modal-input-horas');
    const idProducto = document.getElementById('btn-comprar').getAttribute('data-id');

    // 2. Extraer valores con seguridad
    const tipoPago = inputPago ? inputPago.value : null;
    
    // IMPORTANTE: Si no existe el input o está vacío, enviamos null (no "")
    let recepcion = null;
    if (inputRecepcion && inputRecepcion.value !== "") {
        recepcion = inputRecepcion.value;
    }

    // Horas: si no existe input (no es servicio), null o 1 según prefieras
    let horas = inputHoras ? parseInt(inputHoras.value) : 1;
    if (isNaN(horas) || horas < 1) horas = 1;

    // 3. Preparar el objeto para enviar
    const payload = {
        idProd: parseInt(idProducto),
        tipoPago: tipoPago,
        recepcion: recepcion, // Ahora esto seguro que es null o un valor válido
        horas: horas,
        idProdTrueque: null 
    };

    try {
        const response = await fetch(`/purchase/`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(payload)
        });

        if (response.ok) {
            alert("¡Compra realizada con éxito!");
            window.location.href = "/home/";
        } else {
            let mensajeUsuario = "No se pudo completar la compra.";
            
            // Personalización de errores
            if (response.status === 400) mensajeUsuario = "Error en los datos (400). Revisa el método de pago.";
            if (response.status === 500) mensajeUsuario = "Error interno del servidor (500).";
            
            alert(mensajeUsuario);
        }
    } catch (error) {
        console.error("Error:", error);
        alert("Error de conexión.");
    } finally {
        btnConfirmar.innerText = textoOriginal;
        btnConfirmar.disabled = false;
    }
};

// Cerrar modal si hacen click fuera del contenido (en el fondo oscuro)
window.onclick = function(event) {
    const modal = document.getElementById('modalCompra');
    if (event.target == modal) {
        cerrarModalCompra();
    }
}