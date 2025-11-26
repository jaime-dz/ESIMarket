document.addEventListener('DOMContentLoaded', () => {

    const loginForm = document.getElementById('login');
    const mensajeError = document.getElementById('error-login'); // Asegúrate que este ID exista en tu HTML o usa 'login-message'

    if (loginForm) {
        loginForm.addEventListener('submit', async (evento) => {
            // 1. Evitamos que el formulario recargue la página
            evento.preventDefault();

            // 2. Capturamos los datos
            const formData = new FormData(loginForm);
            const datos = Object.fromEntries(formData.entries());

            try {
                // 3. Hacemos la petición (Fetch)
                // Usamos loginForm.action para obtener la URL definida en el HTML (th:action)
                const respuesta = await fetch(loginForm.action, {
                    method: 'POST',
                    headers: {
                        'Content-Type': 'application/json'
                    },
                    body: JSON.stringify(datos)
                });

                const resultado = await respuesta.json();

                if (!respuesta.ok) {
                    throw new Error(resultado.message || 'Error al iniciar sesión');
                }

                // --- PASO CRÍTICO: GUARDAR EL TOKEN ---
                // Aquí asumimos que tu backend devuelve algo como { "token": "eyJhb..." }
                if (resultado.token) {
                    localStorage.setItem('token', resultado.token);
                    
                    // Opcional: Guardar usuario si viene en la respuesta
                    // localStorage.setItem('usuario', JSON.stringify(resultado.username));

                    // 4. Redirigir al perfil (o al home)
                    window.location.href = '/perfil.html'; // Cambia esto a tu ruta real de perfil
                } else {
                    throw new Error('El servidor no devolvió un token de acceso.');
                }

            } catch (error) {
                console.error(error);
                // Mostrar error en el HTML
                const divMensaje = document.getElementById('login-message');
                if (divMensaje) {
                    divMensaje.textContent = error.message;
                    divMensaje.style.color = 'red';
                }
            }
        });
    }
});