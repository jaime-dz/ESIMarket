document.addEventListener("DOMContentLoaded", () => {
    // Seleccionar controles de filtro
    const categoryFilter = document.getElementById("category-filter");
    const sellerFilters = document.querySelectorAll('input[name="seller"]'); 
    
    // CAMBIO: Seleccionamos las tarjetas de producto, no las filas
    const productCards = document.querySelectorAll(".product-card");

    // Función principal de filtrado
    function filterProducts() {
        // 1. Obtener valores actuales de los filtros
        const selectedCategory = categoryFilter.value;
        const selectedSellers = [];
        sellerFilters.forEach(checkbox => {
            if (checkbox.checked) {
                selectedSellers.push(checkbox.value);
            }
        });

        // 2. Recorrer todas las TARJETAS de productos
        for (const card of productCards) { // CAMBIO: 'card' en vez de 'row'
            // Obtener los datos de la tarjeta
            const cardCategory = card.dataset.category; // CAMBIO
            const cardSeller = card.dataset.seller;     // CAMBIO

            // 3. Comprobar si la tarjeta debe ser visible
            const categoryMatch = (selectedCategory === "all") || (selectedCategory === cardCategory);
            const sellerMatch = (selectedSellers.length === 0) || selectedSellers.includes(cardSeller);

            // 4. Mostrar u ocultar la tarjeta
            if (categoryMatch && sellerMatch) {
                card.style.display = ""; // CAMBIO: Mostrar tarjeta
            } else {
                card.style.display = "none"; // CAMBIO: Ocultar tarjeta
            }
        }
    }

    categoryFilter.addEventListener("change", filterProducts);
    sellerFilters.forEach(checkbox => {
        checkbox.addEventListener("change", filterProducts);
    });
});