document.getElementById('signup').addEventListener('submit', async function(e) {
            e.preventDefault();

            const username = document.getElementById('usuario').value.trim();
            const password = document.getElementById('contraseña').value.trim();
            const confirmPassword = document.getElementById('repcontraseña').value.trim();

            // Validación básica
            if (password !== confirmPassword) {
                alert('Las contraseñas no coinciden');
                return;
            }

            const userData = { username, password };

            try {
                const response = await fetch('/signup', {
                    method: 'POST',
                    headers: { 'Content-Type': 'application/json' },
                    body: JSON.stringify(userData)
                });

                if (response.ok) {
                    alert('Usuario registrado correctamente');
                    window.location.href = 'login.html'; // Redirigir al login
                } else {
                    const errorText = await response.text();
                    alert('Error al registrar usuario: ' + errorText);
                }
            } catch (error) {
                console.error('Error:', error);
                alert('No se pudo conectar con el servidor.');
            }
});