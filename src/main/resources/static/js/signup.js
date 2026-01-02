import { enviarFormularioComoJSON } from './common.js';

document.addEventListener('DOMContentLoaded', () => {
    const signupForm = document.getElementById('signup');
    if (!signupForm) return;

    // Elementos para la validación en vivo
    const passInput = signupForm.querySelector('input[name="password"]'); // Asegúrate que en HTML el name sea password
    const repPassInput = signupForm.querySelector('input[name="reppassword"]'); // Asegúrate que en HTML el name sea reppassword (o el que uses)
    // Si tus inputs en HTML no tienen name, usa getElementById como tenías antes:
    const passId = document.getElementById('contraseña');
    const repPassId = document.getElementById('repcontraseña');
    const errorDiv = document.getElementById('error-contraseña');

    // Función de validación visual
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

    // 1. EVENTO INPUT: Se dispara cada vez que escribes una letra (PUNTO 3)
    if (passId && repPassId) {
        passId.addEventListener('input', validarCoincidencia);
        repPassId.addEventListener('input', validarCoincidencia);
    }

    // 2. ENVÍO DEL FORMULARIO
    signupForm.addEventListener('submit', (evento) => {
        // Validación final antes de enviar
        if (passId.value !== repPassId.value) {
            evento.preventDefault();
            errorDiv.textContent = 'Las contraseñas deben coincidir para registrarse.';
            return;
        }

        enviarFormularioComoJSON(evento);
    });
});