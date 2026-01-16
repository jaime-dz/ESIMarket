document.addEventListener("DOMContentLoaded", function() {
    const counterElement = document.getElementById("current-count");
    const subtitleElement = document.getElementById("subtitle-count");

    // --- CAMBIO AQUÍ: URL nueva y método POST ---
    fetch('/donations/donationByUser', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
    })
    .then(response => {
        if (!response.ok) throw new Error("Error en la petición");
        return response.json(); // Esperamos que devuelva un número (ej: 3)
    })
    .then(numero => {
        // Animación del número
        animateValue(counterElement, 0, numero, 1500);

        // Texto descriptivo (singular/plural)
        const vecesTexto = numero === 1 ? "vez" : "veces";
        subtitleElement.innerText = `${numero} ${vecesTexto}`;
    })
    .catch(error => {
        console.error('Error cargando donaciones:', error);
        counterElement.innerText = "0";
        subtitleElement.innerText = "0 veces";
    });
});

function animateValue(obj, start, end, duration) {
    if (start === end) {
        obj.innerHTML = end;
        return;
    }
    let startTimestamp = null;
    const step = (timestamp) => {
        if (!startTimestamp) startTimestamp = timestamp;
        const progress = Math.min((timestamp - startTimestamp) / duration, 1);
        obj.innerHTML = Math.floor(progress * (end - start) + start);
        if (progress < 1) {
            window.requestAnimationFrame(step);
        } else {
            obj.innerHTML = end;
        }
    };
    window.requestAnimationFrame(step);
}


async function realizarDonacionManual() {
    const dniUsuario = document.getElementById('inputDniUsuario').value.trim();
    let cantidad = document.getElementById('inputCantidad').value;

    // Si está vacío, asumimos 0
    if (!cantidad) cantidad = 0;

    // Validaciones
    if (cantidad < 0) {
        alert("⚠️ La cantidad no puede ser negativa.");
        return;
    }
    if (!dniUsuario) {
         alert("⚠️ Falta el DNI.");
         return;
    }

    if (!confirm(`¿Registrar donación de ${cantidad}€ a ${dniUsuario}?`)) return;

    try {
        const url = `/donations/make/${encodeURIComponent(dniUsuario)}/${encodeURIComponent(cantidad)}`;

        const response = await fetch(url, {
            method: 'PUT', // Este sigue siendo PUT según tu controlador Java anterior
            headers: { 'Content-Type': 'application/json' }
        });

        if (response.ok) {
            alert("✅ Donación registrada correctamente.");
            // Limpiamos campos
            document.getElementById('inputDniUsuario').value = '';
            document.getElementById('inputCantidad').value = '';
            // Opcional: Recargar la página para ver cambios si el admin se donó a sí mismo
            // window.location.reload();
        } else {
            const text = await response.text();
            alert("❌ Error: " + (text || "No se pudo realizar la donación."));
        }
    } catch (e) {
        console.error(e);
        alert("❌ Error de conexión");
    }
}