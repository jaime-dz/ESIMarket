import { enviarFormularioComoJSON } from './common.js';

document.addEventListener("DOMContentLoaded", () => {
    const filterForm = document.getElementById("filterForm");
    
    if(filterForm)
    {
        filterForm.addEventListener('submit',function(e)
        {
            enviarFormularioComoJSON(e);
        });
    }
});