var notifClient = null;

document.addEventListener("DOMContentLoaded", function() {
    conectarNotificaciones();
});


function conectarNotificaciones() {
    // Usamos el mismo endpoint que el chat
    var socket = new SockJS('/ws-notifications');
    notifClient = Stomp.over(socket);

    // Desactivar logs de debug para no ensuciar la consola
    notifClient.debug = null;

    notifClient.connect({}, function(frame) {
        // Suscripción a la cola privada del usuario
        // No hace falta poner el DNI en la URL, Spring sabe quién es el usuario logueado
        notifClient.subscribe('/user/queue/notifications', function(messageOutput) {
            mostrarNuevaNotificacion(JSON.parse(messageOutput.body));
        });
    }, function(error){
        console.log("Error en websockets notificaciones: " + error);
    });
}

function mostrarNuevaNotificacion(notificacion) {
    // 1. Sonido y Campana
    const bell = document.querySelector('.bell-icon');
    if (bell) {
        bell.classList.add('bell-shake-active');
        setTimeout(() => bell.classList.remove('bell-shake-active'), 600);
    }
    const redDot = document.getElementById('notif-red-dot');
    if (redDot) redDot.style.display = 'block';

    // 2. Lógica del Punto Rojo en el Chat (La que ya arreglamos)
    const listaChats = document.getElementById('chat-list-container');
    if (listaChats && notificacion.mensaje) {
        const regex = /Tienes mensajes sin leer de (.+) sobre (.+)/;
        const match = notificacion.mensaje.match(regex);

        if (match) {
            const nombreEmisorNotif = match[1].trim();
            const productoNotif = match[2].trim();
            const items = listaChats.getElementsByClassName('chat-item');
            let encontrado = false;

            console.log("Buscando chat -> Usuario:", nombreEmisorNotif, "| Producto:", productoNotif);

            for (let item of items) {
                const prodEl = item.querySelector('.chat-name');
                const nameEl = item.querySelector('.chat-product');
                if (nameEl && prodEl) {
                    if (nameEl.innerText === nombreEmisorNotif && prodEl.innerText === productoNotif) {
                        item.classList.add('has-unread');
                        encontrado = true;
                        console.log("¡Coincidencia encontrada!");
                        break;
                    }
                }
            }
            if (!encontrado && typeof cargarListaChats === "function"){
                console.log("No se encontró el chat en la lista visual. Recargando...");
                cargarListaChats();
            }
        }
    }

}

// Borrar una notificación individual
function borrarNotif(event, id) {

    if(event) event.stopPropagation();

    const element = document.getElementById(`notif-${id}`);
    if (element) {
        element.remove();
        actualizarEstadoCampana();
    }

    fetch(`/delete/${id}`, { method: 'DELETE' })
        .catch(err => console.error("Error al borrar:", err));
}

// NUEVA: Borrar todas las notificaciones
function borrarTodas() {
    const lista = document.getElementById('notif-list');

    // 1. Limpiar visualmente
    lista.innerHTML = "";
    actualizarEstadoCampana();

    // 2. Llamar al endpoint del Controller que ya tienes creado
    fetch('/delete-all/', {
        method: 'DELETE'
    })
    .then(response => {
        if (!response.ok) console.error("Error en el servidor al borrar todas");
    })
    .catch(err => console.error("Error de red:", err));
}

function actualizarEstadoCampana() {
    const lista = document.getElementById('notif-list');
    const redDot = document.getElementById('notif-red-dot');
    const noNotifMsg = document.getElementById('no-notif-msg');

    if (!lista || lista.children.length === 0) {
        if (redDot) redDot.style.display = 'none';
        if (noNotifMsg) noNotifMsg.style.display = 'block';
    }
}


function toggleNotificaciones(event) {
    // Evita que el clic se propague al documento (lo que cerraría el menú inmediatamente)
    event.stopPropagation();

    const dropdown = document.getElementById('notifDropdown');
    dropdown.classList.toggle('show');
}

// Cerrar el menú si el usuario hace clic en cualquier otro lugar de la pantalla
document.addEventListener('click', function(event) {
    const dropdown = document.getElementById('notifDropdown');
    const bell = document.querySelector('.bell-icon');

    // Si el menú está abierto y el clic no ha sido ni en la campana ni dentro del menú
    if (dropdown.classList.contains('show')) {
        if (!dropdown.contains(event.target) && event.target !== bell) {
            dropdown.classList.remove('show');
        }
    }
});