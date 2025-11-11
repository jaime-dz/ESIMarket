// Importamos la función genérica
import { enviarFormularioComoJSON } from './common.js';

// Esperamos a que el HTML esté cargado
document.addEventListener('DOMContentLoaded', () => {

    const signupForm = document.getElementById('signup');
    if (!signupForm) return;

    // 1. Añadimos el "listener" al evento 'submit'
    signupForm.addEventListener('submit', (evento) => {
        
        // 2. ¡VALIDACIÓN PRIMERO!
        // Aquí va tu lógica de 'validarRegistro()'
        const pass = document.getElementById('contraseña').value;
        const repPass = document.getElementById('repcontraseña').value;
        const errorDiv = document.getElementById('error-contraseña');

        if (pass !== repPass) {
            // Si la validación falla:
            evento.preventDefault(); // Detenemos el envío
            errorDiv.textContent = 'Las contraseñas no coinciden.';
            return; // No continuamos
        }

        // Si la validación SÍ pasa:
        errorDiv.textContent = ''; // Limpiamos errores previos
        
        // 3. Llamamos a nuestra nueva función para enviar como JSON
        // Le pasamos el 'evento' original
        enviarFormularioComoJSON(evento);
    });
});