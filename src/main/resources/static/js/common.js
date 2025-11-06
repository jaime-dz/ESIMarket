document.addEventListener("DOMContentLoaded", function() {

    const inputBusqueda = document.getElementById("search-input");
    const botonBorrar = document.getElementById("clearBtn");

    inputBusqueda.addEventListener("input", function() {
        if (inputBusqueda.value.length > 0) {
            botonBorrar.style.display = "block";
        } else {
            botonBorrar.style.display = "none";
        }
    });
    
    botonBorrar.addEventListener("click", function() {
        inputBusqueda.value = "";

        botonBorrar.style.display = "none";
        
        // Opcional: vuelve a poner el foco en el input
        inputBusqueda.focus();
    });

});