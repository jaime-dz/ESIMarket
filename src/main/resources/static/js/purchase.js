async function gestionarCargaCompras() {
    const container = document.getElementById('lista-compras');
    if (!container) return;

    container.innerHTML = `<div style="text-align:center; padding:20px;"><div class="loader"></div> <p>Cargando tus compras...</p></div>`;

    try {
        const response = await fetch('/purchase/user', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' }
        });

        if (!response.ok) throw new Error("Error de red");

        const compras = await response.json();

        if (!compras || compras.length === 0) {
            container.innerHTML = `
                <div style="grid-column: 1/-1; text-align: center; padding: 40px; color: #666;">
                    <h3>No tienes compras registradas</h3>
                    <p>¡Echa un vistazo al catálogo!</p>
                </div>`;
            return;
        }

        const html = compras.map(c => {
            let metodoPago = c.tipoPago ? c.tipoPago : 'Desconocido';

            // --- LÓGICA DE VISUALIZACIÓN DE ENTREGA ---
            let htmlEntrega = '';

            // Solo mostramos la fila de entrega si es un OBJETO
            if (c.tipoProd === 'Objeto') {
                let textoEntrega = c.recepcion ? c.recepcion : 'No especificado';
                // Embellecer texto (ej: enMano -> en Mano)
                textoEntrega = textoEntrega.replace(/([A-Z])/g, ' $1').trim();

                htmlEntrega = `
                    <div class="info-row">
                        <span class="label">📦 Entrega:</span>
                        <span>${textoEntrega}</span>
                    </div>
                `;
            }

            // Lógica del Trueque
            const esTrueque = (c.tipoPago === 'Trueque' || c.tipoPago === 'trueque');
            const bloqueTrueque = (esTrueque && c.nombreProdTrueque)
                ? `<div class="trueque-box">🔄 Cambiado por: <strong>${c.nombreProdTrueque}</strong></div>`
                : '';

            const urlProducto = `/products/view/${c.idProd}`;

            return `
            <div class="compra-card" onclick="window.location.href='${urlProducto}'">

                <div class="card-header">
                    <h3>${c.nombreProd || 'Producto desconocido'}</h3>
                    <span class="fecha-badge">${c.fecha ? c.fecha.split(' ')[0] : ''}</span>
                </div>

                <div class="card-body">
                    <div style="margin-bottom: 5px; font-size: 0.85em; color: #E57200; font-weight: bold; text-transform: uppercase;">
                        ${c.tipoProd || 'Item'}
                    </div>

                    <div class="info-row">
                        <span class="label">💳 Pago:</span>
                        <span>${metodoPago}</span>
                    </div>

                    ${htmlEntrega}

                    ${bloqueTrueque}
                </div>

                <div class="card-footer">
                    ID Ref: #${c.idCompra}
                </div>
            </div>
            `;
        }).join('');

        container.innerHTML = html;

    } catch (error) {
        console.error("Error:", error);
        container.innerHTML = "<p>Error al cargar historial.</p>";
    }
}

document.addEventListener("DOMContentLoaded", gestionarCargaCompras);