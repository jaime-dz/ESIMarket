import { displayProductsItems } from './common.js';

document.addEventListener("DOMContentLoaded", async () => {

    let allProducts = [];
    const filterForm = document.getElementById("filterForm");
    const productContainer = document.querySelector('.product-grid-container');

    try {
        const response = await fetch('/products/');
        if (response.ok) {
            allProducts = await response.json();
        } else {
            console.error("Error cargando productos para el filtro");
        }
    } catch (error) {
        console.error("Error de red:", error);
    }

    if(filterForm) {
        filterForm.addEventListener('submit', function(e) {
            e.preventDefault();
            aplicarFiltros();
        });
    }

    function aplicarFiltros() {
        let resultados = [...allProducts];

        const categoria = document.getElementById('category-filter').value;
        if (categoria !== 'all') {
            resultados = resultados.filter(item => {
                const tipo = item.tipo ? item.tipo.toLowerCase() : '';
                if (categoria === 'services') return tipo === 'servicio';
                if (categoria === 'objects') return tipo !== 'servicio';
                return true;
            });
        }

        const checkboxes = document.querySelectorAll('.state-filter:checked');
        const estadosSeleccionados = Array.from(checkboxes).map(cb => cb.value.toLowerCase().replace('-',' '));

        if (estadosSeleccionados.length > 0) {
            resultados = resultados.filter(item => {
                if (!item.estado) return false; 

                return estadosSeleccionados.includes(item.estado.toLowerCase());
            });
        }

        const minPrice = parseFloat(document.getElementById('min-price').value) || 0;
        const maxPriceInput = document.getElementById('max-price').value;
        const maxPrice = maxPriceInput ? parseFloat(maxPriceInput) : Infinity;

        resultados = resultados.filter(item => {
            const precio = parseFloat(item.precio);
            return precio >= minPrice && precio <= maxPrice;
        });

        const orden = document.getElementById('sort-price').value;
        resultados.sort((a, b) => {
            if (orden === 'asc') {
                return a.precio - b.precio;
            } else {
                return b.precio - a.precio;
            }
        });

        displayProductsItems(resultados, productContainer);
    }
});