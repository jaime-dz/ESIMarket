var stompClient = null;
var currentChatId = null;
var ultimoDiaGlobal = "";

document.addEventListener("DOMContentLoaded", function() {
    cargarListaChats();
});

document.addEventListener('keydown', function(event) {
    if (event.key === 'Escape') {
        cerrarChat();
    }
});

function cargarListaChats() {
    fetch('/chat/user', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' }
    })
    .then(response => {
        if (!response.ok) throw new Error("Error cargando chats");
        return response.json();
    })
    .then(data => {
        pintarListaChats(data);
    })
    .catch(err => {
        console.error(err);
        document.getElementById('chat-list-container').innerHTML =
            '<div class="empty-chats">No tienes chats activos.<br>¡Busca un producto y contacta al vendedor!</div>';
    });
}

function pintarListaChats(chats) {
    const container = document.getElementById('chat-list-container');
    container.innerHTML = '';

    if (chats.length === 0) {
        container.innerHTML = '<div class="empty-chats">No hay chats iniciados.</div>';
        return;
    }

    chats.forEach(chat => {
        const item = document.createElement('div');
        item.className = 'chat-item';
        item.id = 'chat-item-' + chat.id; // ID para manipular clases active
        item.onclick = () => abrirChat(chat, item);

        const imgUrl = chat.foto ? "data:image/jpeg;base64," + chat.foto : "/Images/logo64.png";

        item.innerHTML = `
            <img src="${imgUrl}" alt="Prod">
            <div class="chat-info">
                <div class="chat-name">${chat.nombreProducto}</div>
                <div class="chat-preview">${chat.nombreUsu}</div>
            </div>
        `;
        container.appendChild(item);
    });
}

function abrirChat(chat, element) {
    currentChatId = chat.id;

    // Gestionar clase active visualmente
    document.querySelectorAll('.chat-item').forEach(el => el.classList.remove('active'));
    if(element) element.classList.add('active');

    if (window.innerWidth <= 768) {
        document.getElementById('chat-sidebar').classList.add('hidden');
        document.getElementById('chat-main').classList.add('active');
    }

    document.getElementById('chat-header').style.display = 'flex';
    document.getElementById('input-area').style.display = 'flex';
    document.getElementById('current-chat-user').innerText = chat.nombreUsu;
    document.getElementById('current-chat-product').innerText = chat.nombreProducto;

    const imgUrl = chat.foto ? "data:image/jpeg;base64," + chat.foto : "/Images/logo64.png";
    document.getElementById('current-chat-img').src = imgUrl;

    const msgContainer = document.getElementById('chat-container');
    msgContainer.innerHTML = '<div style="text-align:center; padding:20px; color:#999;">Cargando historial...</div>';

    conectarSocket(chat.id);
}

function cerrarChat() {
    // 1. DESCONECTAR SOCKET (Importante para liberar memoria)
    if (stompClient !== null && stompClient.connected) {
        stompClient.disconnect(() => {
            console.log("Conexión de chat cerrada.");
        });
    }

    // 2. RESET DE VARIABLES
    currentChatId = null;

    // 3. CAMBIO VISUAL (Ocultar lo que abrirChat mostró)
    document.getElementById('chat-header').style.display = 'none';
    document.getElementById('input-area').style.display = 'none';


    const msgContainer = document.getElementById('chat-container');
    msgContainer.innerHTML = `
        <div class="no-chat-selected">
                <img src="/Images/logo64.png" style="opacity:0.5; margin-bottom:20px;">
                <p style="font-size:1.2em; color:#aaa;">Selecciona un chat para comenzar</p>
        </div>
    `;


    if (window.innerWidth <= 768) {
        document.getElementById('chat-sidebar').classList.remove('hidden');
        document.getElementById('chat-main').classList.remove('active');
    }


    document.querySelectorAll('.chat-item').forEach(el => el.classList.remove('active'));
}

function conectarSocket(chatId) {
    if (stompClient !== null) {
        stompClient.disconnect();
    }

    var socket = new SockJS('/ws');
    stompClient = Stomp.over(socket);
    stompClient.debug = null;

    stompClient.connect({}, function(frame) {
        // Suscripción al canal
        stompClient.subscribe('/topic/messages/' + chatId, function(messageOutput) {
            // Simplemente pasamos el mensaje a la función maestra y ella decide qué hacer
            mostrarMensajes(JSON.parse(messageOutput.body));
        });

        stompClient.subscribe('/user/queue/errors', function (msg) {
            const errorMsg = JSON.parse(msg.body);

            console.error("Error de toxicidad:", errorMsg.message);
            const tempDiv = document.getElementById(errorMsg.clientId);
            if (tempDiv) tempDiv.remove();
            alert(errorMsg.message);
        });

        // Pedir historial
        stompClient.send("/app/chat.history", {}, JSON.stringify({'chatId': chatId}));
    });
}

function enviarMensaje() {
    const input = document.getElementById('mensajeInput');
    const texto = input.value.trim();
    const tempId = "temp-" + Date.now();
    let separadorFecha = null;

    if (texto.length > 129) {
        alert("Mensaje demasiado largo.");
        return;
    }

    if (texto && stompClient && currentChatId) {

        // --- NUEVO: GESTIÓN DE FECHA LOCAL ---
        // Si es el primer mensaje que enviamos hoy, pintamos "Hoy" manualmente
        // para que aparezca ANTES de nuestra burbuja.
        if (ultimoDiaGlobal !== "Hoy") {
            separadorFecha ="Hoy";
            ultimoDiaGlobal = "Hoy";
        }

        agregarMensajeAlDOM(texto, true, tempId,separadorFecha);

        const msgObj = {
            'chatId': currentChatId,
            'message': texto,
            'senderID': currentUserDni,
            'clientId' : tempId,
            'hour': new Date().toLocaleTimeString([], {hour: '2-digit', minute:'2-digit'})
        };

        stompClient.send("/app/chat.sendMessage", {}, JSON.stringify(msgObj));
        input.value = '';
        scrollToBottom();
    }
}



function mostrarMensajes(payload) {
    const container = document.getElementById('chat-container');

    // --- CASO 1: HISTORIAL ---
    if (Array.isArray(payload)) {
        container.innerHTML = '';
        ultimoDiaGlobal = ""; // Reseteamos al cargar historial nuevo

        if(payload.length === 0) {
             container.innerHTML = '<div style="text-align:center; margin-top:20px; color:#aaa;">No hay mensajes previos.</div>';
        }

        payload.forEach(msg => {

            let fechaParaEsteMensaje = null;

            if (msg.day !== ultimoDiaGlobal) {
                fechaParaEsteMensaje = msg.day;
                ultimoDiaGlobal = msg.day; // Actualizamos la última fecha vista
            }

            var esMio = (msg.senderID === currentUserDni);
            agregarMensajeAlDOM(msg, esMio, false,fechaParaEsteMensaje);
        });
        scrollToBottom();

    }else {

        var msg = payload;
        const esMio = msg.senderID === currentUserDni;

        let fechaParaEsteMensaje = null;
        if (msg.day && msg.day !== ultimoDiaGlobal) {
             fechaParaEsteMensaje = msg.day;
             ultimoDiaGlobal = msg.day;
        }

        if (msg.clientId) {
            const elementoExistente = document.getElementById(msg.clientId);
            if (elementoExistente) {
                console.log("Mensaje confirmado. ID actualizado.");
                elementoExistente.id = "msg-" + msg.id;
                return;
            }
        }


        agregarMensajeAlDOM(msg, false, "msg-" + msg.id,fechaParaEsteMensaje);
        scrollToBottom();
    }
}

function agregarSeparadorFecha(textoFecha) {
    const div = document.createElement('div');
    div.className = 'date-separator'; // Clase CSS que crearemos luego
    div.innerText = textoFecha;

    const container = document.getElementById('chat-container');
    container.appendChild(div);
}

function agregarMensajeAlDOM(msgObjOrText, esMioForzado, esTemporal, separadorFecha) {

    const container = document.getElementById('chat-container');

    if(container.innerText.includes("Cargando") || container.innerText.includes("No hay mensajes")) {
        container.innerHTML = '';
    }

    if ( separadorFecha ){
        agregarSeparadorFecha(separadorFecha);
    }

    let texto = "";
    let hora = "";
    let esMio = esMioForzado;


    if (typeof msgObjOrText === 'string') {
        texto = msgObjOrText;
        hora = new Date().toLocaleTimeString([], {hour: '2-digit', minute:'2-digit'});
    } else {
        texto = msgObjOrText.message;
        hora = msgObjOrText.hour;
    }

    const div = document.createElement('div');

    if (esTemporal && typeof esTemporal === 'string') {
        div.id = esTemporal;
    }

    div.className = esMio ? 'message-right' : 'message-left';
    div.innerHTML = `
        ${texto}
        <span class="message-time">${hora}</span>
    `;


    if(container.innerText.includes("Cargando") || container.innerText.includes("No hay mensajes")) {
        container.innerHTML = '';
    }
    container.appendChild(div);
}

function scrollToBottom() {
    const container = document.getElementById('chat-container');
    container.scrollTop = container.scrollHeight;
}

window.cerrarChatMovil = function() {
    document.getElementById('chat-sidebar').classList.remove('hidden');
    document.getElementById('chat-main').classList.remove('active');
};