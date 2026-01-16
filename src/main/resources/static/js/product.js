function actualizarPrecioTotal() {
    const inputHoras = document.getElementById('modal-input-horas');
    const displayPrecio = document.getElementById('precio-total-display');

    // Valor por defecto
    let horas = 1;

    // Solo intentamos leer el valor si el input existe (es un Servicio)
    if (inputHoras) {
        const valor = parseInt(inputHoras.value);
        // Si es un número válido y mayor que 0, lo usamos
        if (!isNaN(valor) && valor > 0) {
            horas = valor;
        }
    }

    // Calculamos el total
    const total = precioBase * horas;

    // Actualizamos el texto en pantalla si existe el elemento
    if (displayPrecio) {
        displayPrecio.innerText = total;
    }
}

async function contactarVendedor() {
    try {
        const response = await fetch('/chat/', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({
                dni: vendedorDNI,
                idProd: productoID
            })
        });

        if (response.ok) {
            // Caso 200 OK: Chat creado correctamente
            window.location.href = '/chat/';
        } else {
            // Si hay error, leemos el JSON
            const data = await response.json();

            // Verificamos el mensaje específico que pediste
            if (data.error === "Ya existe un chat con estos usuarios y producto") {
                // Si ya existe, redirigimos igualmente a la lista de chats
                window.location.href = '/chat/';
            } else {
                // Cualquier otro error (ej: contactarse a sí mismo) lo mostramos
                alert("No se pudo iniciar el chat: " + data.error);
            }
        }
    } catch (error) {
        console.error('Error en la petición:', error);
        alert("Hubo un error de conexión al intentar contactar.");
    }
}