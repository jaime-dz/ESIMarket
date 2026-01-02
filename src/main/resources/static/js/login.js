// login.js
import { enviarFormularioComoJSON } from './common.js';

document.addEventListener('DOMContentLoaded', () => {
    const loginForm = document.getElementById('login');

    if (loginForm) {
        loginForm.addEventListener('submit', (evento) => {
            // Usamos la función de common.js que ya soporta cookies y redirección
            enviarFormularioComoJSON(evento);
        });
    }
});