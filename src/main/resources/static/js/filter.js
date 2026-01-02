import { displayProductsItems } from './common.js';

document.addEventListener("DOMContentLoaded", async () => {
    
    // 1. VARIABLES
    let allProducts = []; // Aquí guardaremos la "copia maestra" de los productos
    const filterForm = document.getElementById("filterForm");
    const productContainer = document.querySelector('.product-grid-container');

    // 2. CARGA INICIAL DE DATOS
    // Necesitamos descargar la lista completa para poder filtrarla en memoria
    try {
        const response = await fetch('/products/'); // Misma ruta que usa common.js
        if (response.ok) {
            allProducts = await response.json();
            // Nota: common.js ya pinta los productos al inicio, así que aquí
            // solo guardamos los datos en la variable 'allProducts' para usarlos luego.
        } else {
            console.error("Error cargando productos para el filtro");
        }
    } catch (error) {
        console.error("Error de red:", error);
    }

    // 3. EVENTO DEL BOTÓN "APLICAR"
    if(filterForm) {
        filterForm.addEventListener('submit', function(e) {
            e.preventDefault(); // IMPORTANTE: Evita que se envíe el formulario al servidor
            aplicarFiltros();   // Ejecuta nuestra lógica local
        });
    }

    // 4. LÓGICA DE FILTRADO
    function aplicarFiltros() {
        // Empezamos con todos los productos y vamos descartando
        let resultados = [...allProducts];

        // --- A. FILTRO CATEGORÍA ---
        const categoria = document.getElementById('category-filter').value;
        if (categoria !== 'all') {
            resultados = resultados.filter(item => {
                const tipo = item.tipo ? item.tipo.toLowerCase() : '';
                if (categoria === 'services') return tipo === 'servicio';
                if (categoria === 'objects') return tipo !== 'servicio';
                return true;
            });
        }

        // --- B. FILTRO ESTADO ---
        // Buscamos todos los checkboxes marcados
        const checkboxes = document.querySelectorAll('.state-filter:checked');
        const estadosSeleccionados = Array.from(checkboxes).map(cb => cb.value.toLowerCase().replace('-',' '));

        if (estadosSeleccionados.length > 0) {
            resultados = resultados.filter(item => {
                // Si es servicio (no tiene estado), y estamos filtrando por estado, lo ocultamos
                // Opcional: Si quieres que los servicios siempre aparezcan, cambia esta lógica.
                if (!item.estado) return false; 

                // Normalizamos el texto (quitamos mayúsculas) para comparar
                // Asegúrate que en tu BD los estados coincidan con los 'value' de los inputs
                return estadosSeleccionados.includes(item.estado.toLowerCase());
            });
        }

        // --- C. FILTRO PRECIO ---
        const minPrice = parseFloat(document.getElementById('min-price').value) || 0;
        const maxPriceInput = document.getElementById('max-price').value;
        // Si no hay maxPrice, usamos Infinity
        const maxPrice = maxPriceInput ? parseFloat(maxPriceInput) : Infinity;

        resultados = resultados.filter(item => {
            const precio = parseFloat(item.precio);
            return precio >= minPrice && precio <= maxPrice;
        });

        // --- D. ORDENAR ---
        const orden = document.getElementById('sort-price').value;
        resultados.sort((a, b) => {
            if (orden === 'asc') {
                return a.precio - b.precio;
            } else {
                return b.precio - a.precio;
            }
        });

        // 5. MOSTRAR RESULTADOS
        // Reutilizamos la función de common.js que ya sabe pintar las tarjetas
        displayProductsItems(resultados, productContainer);
    }
});