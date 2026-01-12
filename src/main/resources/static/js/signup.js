import { enviarFormularioComoJSON } from './common.js';

document.addEventListener('DOMContentLoaded', () => {
    const signupForm = document.getElementById('signup');
    if (!signupForm) return;

    const passId = document.getElementById('contraseña');
    const repPassId = document.getElementById('repcontraseña');
    const errorDiv = document.getElementById('error-contraseña');
    // Referencia al div donde mostraremos el error del backend
    const mensajeServidorDiv = document.getElementById('signup-message');

    // --- 1. Lógica de validación visual de contraseñas (Igual que antes) ---
    function validarCoincidencia() {
        const val1 = passId.value;
        const val2 = repPassId.value;

        if (val2.length > 0 && val1 !== val2) {
            errorDiv.textContent = 'Las contraseñas no coinciden';
            errorDiv.style.color = 'red';
        } else {
            errorDiv.textContent = '';
        }
    }

    if (passId && repPassId) {
        passId.addEventListener('input', validarCoincidencia);
        repPassId.addEventListener('input', validarCoincidencia);
    }

    // --- 2. Manejo del envío del formulario (Nuevo) ---
    signupForm.addEventListener('submit', async (evento) => {
        evento.preventDefault(); // Evita la recarga de página estándar

        // Limpiar errores previos
        if(mensajeServidorDiv) mensajeServidorDiv.textContent = '';

        // Validación final de contraseñas antes de enviar
        if (passId.value !== repPassId.value) {
            errorDiv.textContent = 'Las contraseñas deben coincidir para registrarse.';
            return;
        }

        // Preparamos los datos
        const formData = new FormData(signupForm);
        const data = Object.fromEntries(formData.entries());

        try {
            const response = await fetch(signupForm.action || window.location.href, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                    'Accept': 'application/json' // Importante para que Spring sepa devolver JSON
                },
                body: JSON.stringify(data)
            });

            if (response.ok) {
                // Registro exitoso: Redirigimos al login
                window.location.href = '/home/';
            } else {
                // --- AQUÍ CAPTURAMOS TU EXCEPCIÓN ---
                let textoError = "Error al registrarse.";

                try {
                    // Intentamos leer el JSON que envía tu backend (ej: GlobalExceptionHandler)
                    const errorJson = await response.json();

                    // Verificamos diferentes campos comunes de error en Java/Spring
                    textoError = errorJson.message || errorJson.error || JSON.stringify(errorJson);

                } catch (e) {
                    // Si el backend envió texto plano en vez de JSON
                    const textoPlano = await response.text();
                    if(textoPlano) textoError = textoPlano;
                }

                // Mostramos el mensaje exacto en el HTML
                if (mensajeServidorDiv) {
                    mensajeServidorDiv.textContent = textoError;
                    mensajeServidorDiv.style.color = 'red';
                    mensajeServidorDiv.style.display = 'block';
                    mensajeServidorDiv.style.marginBottom = '10px';
                } else {
                    alert(textoError); // Fallback por si no encuentra el div
                }
            }
        } catch (error) {
            console.error('Error de red:', error);
            if(mensajeServidorDiv) {
                mensajeServidorDiv.textContent = 'Error de conexión con el servidor.';
                mensajeServidorDiv.style.color = 'red';
            }
        }
    });
});