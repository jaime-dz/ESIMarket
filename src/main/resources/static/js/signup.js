import { enviarFormularioComoJSON } from './common.js';

document.addEventListener('DOMContentLoaded', () => {
    const signupForm = document.getElementById('signup');
    if (!signupForm) return;

    const passInput = signupForm.querySelector('input[name="password"]');
    const repPassInput = signupForm.querySelector('input[name="reppassword"]');
    const passId = document.getElementById('contraseña');
    const repPassId = document.getElementById('repcontraseña');
    const errorDiv = document.getElementById('error-contraseña');

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

    signupForm.addEventListener('submit', (evento) => {
        if (passId.value !== repPassId.value) {
            evento.preventDefault();
            errorDiv.textContent = 'Las contraseñas deben coincidir para registrarse.';
            return;
        }

        enviarFormularioComoJSON(evento);
    });
});