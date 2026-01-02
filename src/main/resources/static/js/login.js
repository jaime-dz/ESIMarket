import { enviarFormularioComoJSON } from './common.js';

document.addEventListener('DOMContentLoaded', () => {
    const loginForm = document.getElementById('login');

    if (loginForm) {
        loginForm.addEventListener('submit', (evento) => {
            enviarFormularioComoJSON(evento);
        });
    }
});