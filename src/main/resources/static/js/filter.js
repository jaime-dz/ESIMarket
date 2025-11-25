document.addEventListener("DOMContentLoaded", () => {
    const categoryFilter = document.getElementById("category-filter");
    const stateFilters = document.querySelectorAll(".state-filter");
    const productCards = document.querySelectorAll(".product-card");

    function filterProducts() {
        const selectedCategory = categoryFilter.value;
        const selectedStates = [];

        stateFilters.forEach(checkbox => {
            if (checkbox.checked) {
                // Convertimos a minúsculas para comparar sin problemas
                selectedStates.push(checkbox.value.toLowerCase());
            }
        });

        for (const card of productCards) {
            const cardCategory = card.dataset.category;
            const cardState = card.dataset.state ? card.dataset.state.toLowerCase() : "";

            const categoryMatch = (selectedCategory === "all") || (selectedCategory === cardCategory);
            
            // Si no hay ningún estado marcado, mostramos todos
            // Si hay marcados, miramos si el estado de la carta está en la lista
            const stateMatch = (selectedStates.length === 0) || selectedStates.includes(cardState);

            if (categoryMatch && stateMatch) {
                card.style.display = ""; // Flex o grid item default
            } else {
                card.style.display = "none";
            }
        }
    }

    categoryFilter.addEventListener("change", filterProducts);
    stateFilters.forEach(checkbox => {
        checkbox.addEventListener("change", filterProducts);
    });
});