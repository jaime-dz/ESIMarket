import { enviarFormularioComoJSON } from './common.js';

document.addEventListener('DOMContentLoaded', () => {

    const contactForm = document.getElementById('formulario');
    
    if (contactForm) {
        contactForm.addEventListener('submit', (evento) => {
            enviarFormularioComoJSON(evento);
        });
    }
});