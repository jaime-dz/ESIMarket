async function gestionarCargaServicios() {
    const container = document.getElementById("servicios-container");

    try {
      const response = await fetch('/service/user', {
          method: 'POST',
          headers: { 'Content-Type': 'application/json' }
      });

      if (!response.ok) throw new Error("Error cargando servicios");

      const servicios = await response.json();

      if (!servicios || servicios.length === 0) {
          container.innerHTML = `
              <div style="text-align: center; padding: 40px; color: #666;">
                  <h3>No tienes servicios en curso</h3>
                  <p>Aquí aparecerán las clases, reparaciones u otros servicios que contrates.</p>
              </div>`;
          return;
      }

      const html = servicios.map(s => {
          // --- LÓGICA DE ROLES ---
          let esProveedor = false;
          let miRolBadge = '';
          let otraPersonaHTML = '';
          let botonFinalizar = '';

          if (s.nombreVendedor) {
              // Si hay nombre de vendedor, YO soy el cliente
              miRolBadge = '<span class="role-badge badge-comprador">Eres Cliente</span>';
              otraPersonaHTML = `<p><strong>Proveedor:</strong> ${s.nombreVendedor}</p>`;
          } else if (s.nombreComprador) {
              // Si hay nombre de comprador, YO soy el proveedor
              esProveedor = true;
              miRolBadge = '<span class="role-badge badge-vendedor">Eres Proveedor</span>';
              otraPersonaHTML = `<p><strong>Cliente:</strong> ${s.nombreComprador}</p>`;

              // Botón de finalizar solo para proveedor
              botonFinalizar = `
                  <button class="btn-finalizar" onclick="accionFinalizarServicio(${s.idProd})">
                      Finalizar Servicio
                  </button>
              `;
          }

          // --- LÓGICA DE FECHA (Modificada) ---
          let bloqueFecha = "";

          if (s.fecha) {
              // Si ya hay fecha, la mostramos formateada
              let fechaTexto = s.fecha.replace('T', ' ').substring(0, 16);
              bloqueFecha = `<p><strong>Fecha acordada:</strong> ${fechaTexto}</p>`;
          } else {
              // FECHA PENDIENTE (NULL)
              if (esProveedor) {
                  // Si soy proveedor, muestro el formulario para asignar fecha
                  bloqueFecha = `
                      <div style="margin-top: 10px; background: #f9f9f9; padding: 10px; border-radius: 5px;">
                          <label style="font-size: 0.9em; display:block; margin-bottom:5px; color:#555;">
                              ⚠ Fecha pendiente. Selecciona una:
                          </label>
                          <div style="display:flex; gap:5px;">
                              <input type="datetime-local" id="fecha-input-${s.idProd}"
                                     style="padding: 5px; border: 1px solid #ccc; border-radius: 4px;">
                              <button onclick="accionAsignarFecha(${s.idProd})"
                                      style="background-color: #00587C; color: white; border: none; padding: 5px 10px; border-radius: 4px; cursor: pointer;">
                                  Guardar
                              </button>
                          </div>
                      </div>
                  `;
              } else {
                  // Si soy cliente, solo aviso
                  bloqueFecha = `<p style="color: #E57200;"><strong>Fecha:</strong> Pendiente de asignación por el proveedor.</p>`;
              }
          }

          return `
          <div class="servicio-card">
              <div class="servicio-info">
                  ${miRolBadge}
                  <h3>${s.nombreProd || 'Servicio'}</h3>
                  ${otraPersonaHTML}
                  ${bloqueFecha}
              </div>

              <div class="servicio-actions">
                  ${botonFinalizar}
              </div>
          </div>
          `;
      }).join('');

      container.innerHTML = html;

    } catch (error) {
      console.error(error);
      container.innerHTML = "<p>Error al cargar los servicios.</p>";
    }
}

// --- NUEVA FUNCIÓN: ASIGNAR FECHA ---
window.accionAsignarFecha = async function(idProd) {
    const input = document.getElementById(`fecha-input-${idProd}`);
    const fechaValor = input.value; // Formato yyyy-MM-ddTHH:mm

    if (!fechaValor) {
      alert("Por favor, selecciona una fecha y hora válida.");
      return;
    }

    try {
      // IMPORTANTE: El backend espera @RequestBody String.
      // Enviamos texto plano, no JSON, para evitar conflictos de comillas.
      const response = await fetch(`/service/setdate/${idProd}`, {
          method: 'PATCH',
          headers: { 'Content-Type': 'text/plain' },
          body: fechaValor
      });

      if (response.ok) {
          alert("Fecha asignada correctamente.");
          gestionarCargaServicios(); // Recargar la lista
      } else {
          const errorMsg = await response.text();
          alert("Error al asignar fecha: " + errorMsg);
      }
    } catch (e) {
      console.error(e);
      alert("Error de conexión al asignar fecha.");
    }
};

// --- FUNCIÓN ACTUALIZADA (NUEVO CONTROLLER) ---
window.accionFinalizarServicio = async function(idProd) {
    if(!confirm("¿Confirmas que el servicio ha finalizado satisfactoriamente?")) return;

    try {
       // NUEVA URL: /service/end/{idProd}
       // Ya no pasamos parámetros extra, el backend coge tu usuario de la sesión
       const response = await fetch(`/service/end/${idProd}`, {
           method: 'PATCH'
       });

       if (response.ok) {
           // Refrescamos la lista visualmente
           const container = document.getElementById("servicios-container");
           container.innerHTML = '<div class="loader"></div><p style="text-align:center">Actualizando...</p>';

           // Pequeña espera para asegurar que la BD se actualice antes de pedir la lista de nuevo
           setTimeout(gestionarCargaServicios, 500);
       } else {
           const errorMsg = await response.text();
           alert("Error al finalizar: " + errorMsg);
       }
    } catch (e) {
       console.error(e);
       alert("Error de conexión.");
    }
};

document.addEventListener("DOMContentLoaded", gestionarCargaServicios);