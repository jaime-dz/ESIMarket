document.addEventListener('DOMContentLoaded',()=>{
    mostrarPerfil();
    redirectToEdit();
});
async function mostrarPerfil() {
    const name=document.getElementById('show-name');
    const surname=document.getElementById('show-surname');
    const username=document.getElementById('show-username');
    const email=document.getElementById('show-email');
    const degree=document.getElementById('show-degree');
    const money = document.getElementById('show-money');
    try {
        // 2. Hacer la petición al backend
        const response = await fetch('/profile/'); 

        if (!response.ok) {
            throw new Error('Error al obtener el perfil');
        }

        const data = await response.json();

        name.textContent = data.nombre;       
        surname.textContent = data.apellidos;
        username.textContent = data.usuario;
        email.textContent = data.email;
        degree.textContent = data.carrera;
        money.textContent = data.saldo;

    } catch (error) {
        console.error('Hubo un problema cargando el perfil:', error);
    }
}

function redirectToEdit()
{
    const buttonEdit = document.getElementById('edit-profile');
    buttonEdit.addEventListener('click',function()
    {
       window.location.href="/profile/edit";
    });
}