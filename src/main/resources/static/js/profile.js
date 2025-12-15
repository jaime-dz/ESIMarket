document.addEventListener('DOMContentLoaded',()=>{
    redirectToEdit();
});

function redirectToEdit()
{
    const buttonEdit = document.getElementById('edit-profile');
    buttonEdit.addEventListener('click',function()
    {
       window.location.href="/profile/edit";
    });
}