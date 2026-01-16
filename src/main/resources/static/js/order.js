async function accionEntregarPedido(idPedido, esTaquilla) {
    let nTaquilla = 0;

    // LÓGICA DIFERENCIADA
    if (esTaquilla) {
        // CASO 1: ES EN TAQUILLA -> Pedimos el número obligatoriamente
        const input = prompt("📦 ENTREGA EN TAQUILLA\n\nIntroduce el número de la taquilla donde has dejado el paquete:");

        // Si el usuario pulsa Cancelar, no hacemos nada
        if (input === null) return;

        nTaquilla = parseInt(input);

        // Validamos que sea un número válido
        if (isNaN(nTaquilla) || nTaquilla <= 0) {
            alert("⚠️ Error: El número de taquilla debe ser válido.");
            return;
        }

    } else {
        // CASO 2: ES EN MANO -> Solo confirmación simple, taquilla es 0
        if(!confirm("🤝 ¿Confirmas que has entregado el producto en mano al comprador?")) {
            return; // Si dice que no, cancelamos
        }
        nTaquilla = 0; // Por defecto para entregas en mano
    }

    // Enviamos la petición al servidor
    try {
        const response = await fetch(`/orders/deliver/${idPedido}/${nTaquilla}`, { method: 'PATCH' });

        if(response.ok) {
            gestionarCargaPedidos(); // Recargar la lista si todo fue bien
        } else {
            const textoError = await response.text();
            alert("❌ Error al marcar entregado: " + textoError);
        }
    } catch(e) {
        console.error(e);
        alert("❌ Error de conexión con el servidor");
    }
}

async function accionRecogerPedido(idPedido) {
    if(!confirm("📦 ¿Confirmas que has recogido el producto?")) return;
    try {
        const response = await fetch(`/orders/pickup/${idPedido}`, { method: 'PATCH' });
        if(response.ok) gestionarCargaPedidos();
        else alert("Error al confirmar recogida: " + await response.text());
    } catch(e) {
        console.error(e);
        alert("Error de conexión");
    }
}

// --- CARGA Y RENDERIZADO ---
async function gestionarCargaPedidos() {
    const container = document.getElementById("pedidos-container");
    const estadoFiltro = document.getElementById("filtroEstado").value;

    container.innerHTML = '<div style="text-align:center; padding:20px;"><div class="loader"></div><p>Cargando...</p></div>';

    try {
        const response = await fetch("/orders/filter", {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ filter: estadoFiltro })
        });

        if (!response.ok) throw new Error("Error cargando pedidos");
        const pedidos = await response.json();

        if (!pedidos || pedidos.length === 0) {
            container.innerHTML = `
                <div style="text-align: center; padding: 40px; color: #666;">
                    <h3>No hay pedidos</h3>
                    <p>No se encontraron pedidos con el filtro "${estadoFiltro}".</p>
                </div>`;
            return;
        }

        const html = pedidos.map(p => {
            const esComprador = p.esComprador;

            const esTaquillaSeguro = (p.enTaquilla === true);

            // 1. Badge de Rol
            let badgeRol = esComprador
                ? '<span class="role-badge badge-comprador">Eres Comprador</span>'
                : '<span class="role-badge badge-vendedor">Eres Vendedor</span>';

            let otroUsuario = esComprador
                ? `<p><strong>Vendedor:</strong> ${p.nombreVendedor}</p>`
                : `<p><strong>Comprador:</strong> ${p.nombreComprador}</p>`;

            // 2. Badge Tipo de Entrega
            let entregaBadge = "";

            if (p.enTaquilla) {
                if (p.nTaquilla && p.nTaquilla > 0) {
                    // Taquilla ya asignada (Verde)
                    entregaBadge = `
                    <div style="margin-top:8px; display:inline-flex; align-items:center; gap:5px; background-color:#e8f5e9; color:#2e7d32; padding:4px 10px; border-radius:15px; font-size:0.85rem; border:1px solid #c8e6c9;">
                        <span>📦</span> <span>Taquilla Nº <strong>${p.nTaquilla}</strong></span>
                    </div>`;
                } else {
                    // Taquilla pendiente (Naranja)
                    entregaBadge = `
                    <div style="margin-top:8px; display:inline-flex; align-items:center; gap:5px; background-color:#fff3e0; color:#ef6c00; padding:4px 10px; border-radius:15px; font-size:0.85rem; border:1px solid #ffe0b2;">
                        <span>⏳</span> <span>Taquilla pendiente de asignar</span>
                    </div>`;
                }
            } else {
                // En mano (Gris)
                entregaBadge = `
                <div style="margin-top:8px; display:inline-flex; align-items:center; gap:5px; background-color:#f5f5f5; color:#616161; padding:4px 10px; border-radius:15px; font-size:0.85rem; border:1px solid #e0e0e0;">
                    <span>🤝</span> <span>Entrega en mano</span>
                </div>`;
            }

            // 3. Botones Acción
            let botonHtml = "";

            if ( ( esComprador && p.estado === 'Entregado' ) || ( esComprador && p.estado === 'PorEntregar' && !p.enTaquilla ) ) {
                botonHtml = `<button class="btn-finalizar" onclick="accionRecogerPedido(${p.idPedido})">Confirmar Recogida</button>`;
            }
            else if (esComprador && p.estado === 'PorEntregar' && p.enTaquilla) {

                botonHtml = `<span style="color:#999; font-style:italic;">Esperando entrega...</span>`;
            }
            else if (!esComprador && p.estado === 'PorEntregar' && p.enTaquilla ) {
                botonHtml = `<button class="btn-finalizar" onclick="accionEntregarPedido(${p.idPedido}, ${esTaquillaSeguro})">Marcar Entregado</button>`;
            }
            else if ( ( !esComprador && p.estado == 'Entregado' ) || ( p.estado === 'PorEntregar' && !p.enTaquilla ) ){
                botonHtml = `<span style="color:#999; font-style:italic;">Esperando recogida...</span>`;
            }
            else if (p.estado === 'Recogido') {
                botonHtml = `<span style="color:green; font-weight:bold; font-size:1.1em;">✔ Completado</span>`;
            }
            else{
                 botonHtml = `<span style="color:#999; font-style:italic;">En proceso...</span>`;
            }

            return `
                <div class="servicio-card">
                    <div class="servicio-info">
                        ${badgeRol}
                        <h3 style="margin-top:5px;">${p.nombreProd}</h3>
                        <p style="margin-bottom:8px;"><strong>Estado:</strong> <span style="color:#00587C;">${p.estado}</span></p>
                        ${otroUsuario}
                        ${entregaBadge}
                    </div>
                    <div class="servicio-actions">
                        ${botonHtml}
                    </div>
                </div>
            `;
        }).join('');

        container.innerHTML = html;

    } catch (error) {
        console.error(error);
        container.innerHTML = "<p>Error al cargar los datos.</p>";
    }
}

document.addEventListener("DOMContentLoaded", () => gestionarCargaPedidos());