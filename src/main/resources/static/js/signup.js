document.addEventListener('DOMContentLoaded', function() {

            // 1. Seleccionamos los elementos del DOM
            const passwordInput = document.getElementById('contraseña');
            const confirmPasswordInput = document.getElementById('repcontraseña');
            const errorSpan = document.getElementById('error-contraseña');

            // 2. Creamos la función de validación
            function validarContraseñas() {
                const pass = passwordInput.value;
                const confirmPass = confirmPasswordInput.value;

                // 3. Comparamos los valores
                if (pass !== confirmPass) {
                    // Si no coinciden:
                    // A. Mostramos un mensaje de error en tiempo real
                    errorSpan.textContent = "Las contraseñas no coinciden.";
                    // B. Usamos setCustomValidity para informar al navegador
                    //    que el campo no es válido. Esto previene el envío.
                    confirmPasswordInput.setCustomValidity("Las contraseñas no coinciden.");
                } else {
                    // Si coinciden:
                    // A. Borramos el mensaje de error en tiempo real
                    errorSpan.textContent = "";
                    // B. Borramos el error de setCustomValidity.
                    //    ¡Esto es crucial para permitir el envío del formulario!
                    confirmPasswordInput.setCustomValidity("");
                }
            }

            // 4. Añadimos los "escuchadores" de eventos
            // Queremos que la validación se ejecute cada vez que el usuario
            // escribe en CUALQUIERA de los dos campos.
            passwordInput.addEventListener('input', validarContraseñas);
            confirmPasswordInput.addEventListener('input', validarContraseñas);

            // Nota: No necesitamos un listener en el evento 'submit' del formulario
            // porque al usar `setCustomValidity()`, el navegador gestionará
            // automáticamente el bloqueo del envío y mostrará el mensaje de error
            // si el campo 'confirm-password' es inválido.
        });