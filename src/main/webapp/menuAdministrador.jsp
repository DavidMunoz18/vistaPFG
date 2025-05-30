<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="java.util.List"%>
<%@ page import="dtos.ProductoDto"%>
<%@ page import="dtos.UsuarioDto"%>
<%@ page import="dtos.MarcaDto"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Administrador</title>
<link rel="stylesheet" href="css/administrador.css">
<link rel="stylesheet" href="css/estilo.css">
<link
	href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.3/css/all.min.css"
	rel="stylesheet" />
<!-- Bootstrap CSS -->
<link
	href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css"
	rel="stylesheet">
<script
	src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
<link rel="stylesheet"
	href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.min.css">
<style>
/* Si quieres personalizar el fondo o la línea */
mark {
	background-color: yellow;
	color: inherit;
}
</style>

<script>
   function mostrarSeccion(seccionId) {
       const secciones = document.querySelectorAll('.seccion');
       secciones.forEach(seccion => seccion.style.display = 'none');
       document.getElementById(seccionId).style.display = 'block';
   }

   // Funciones para Productos
   function editarProducto(id, nombre, descripcion, precio, stock) {
       mostrarSeccion('modificarProducto');
       document.getElementById('idProducto').value = id;
       document.getElementById('modNombreProducto').value = nombre;
       document.getElementById('modDescripcionProducto').value = descripcion;
       document.getElementById('modPrecioProducto').value = precio;
       document.getElementById('modStockProducto').value = stock;
   }

   function eliminarProducto(id) {
       if (confirm('¿Estás seguro de eliminar este producto?')) {
           var form = document.createElement('form');
           form.action = 'eliminarProducto';
           form.method = 'post';
           var input = document.createElement('input');
           input.type = 'hidden';
           input.name = 'productoId';
           input.value = id;
           form.appendChild(input);
           document.body.appendChild(form);
           form.submit();
       }
   }

   // Funciones para Usuarios
   function editarUsuario(id, nombre, telefono, rol) {
       mostrarSeccion('modificarUsuario');
       document.getElementById('idUsuario').value = id;
       document.getElementById('nuevoNombre').value = nombre;
       document.getElementById('nuevoTelefono').value = telefono;
       document.getElementById('nuevoRol').value = rol;
   }

   
   function eliminarUsuario(id) {
       if (confirm('¿Estás seguro de eliminar este usuario?')) {
           var form = document.createElement('form');
           form.action = 'eliminarUsuario';
           form.method = 'post';
           var input = document.createElement('input');
           input.type = 'hidden';
           input.name = 'idUsuario';
           input.value = id;
           form.appendChild(input);
           document.body.appendChild(form);
           form.submit();
       }
   }

   // Detectamos si se envió el parámetro para modificar marca
   var mostrarModificarMarca = <%=(request.getParameter("id") != null ? "true" : "false")%>;

   document.addEventListener('DOMContentLoaded', function () {
       if(mostrarModificarMarca) {
           mostrarSeccion('modificarMarca');
       } else {
           mostrarSeccion('verUsuarios');
       }
   });

   function mostrarToast(mensaje, tipo) {
       const toastEl = document.getElementById('liveToast');
       const toastBody = toastEl.querySelector('.toast-body');
       toastBody.innerText = mensaje;

       // Ajustar el color según el tipo
       toastEl.classList.remove('bg-success', 'bg-danger');
       if (tipo === 'exito') {
           toastEl.classList.add('bg-success');
       } else {
           toastEl.classList.add('bg-danger');
       }

       // Mostrar el toast
       const toast = new bootstrap.Toast(toastEl);
       toast.show();
   }

   document.addEventListener('DOMContentLoaded', function () {
       const urlParams = new URLSearchParams(window.location.search);

       // Toast para actualización de usuario
       if (urlParams.has('modificado')) {
           const modificado = urlParams.get('modificado').trim().toLowerCase();
           if (modificado === 'true') {
               mostrarToast('Usuario actualizado con éxito', 'exito');
           } else {
               mostrarToast('Error al actualizar el usuario', 'error');
           }
       }

       // Toast para agregar producto
       if (urlParams.has('productoAgregado')) {
           const agregado = urlParams.get('productoAgregado').trim().toLowerCase();
           if (agregado === 'true') {
               mostrarToast('Producto agregado con éxito', 'exito');
           } else {
               mostrarToast('Error al agregar producto', 'error');
           }
       }

       // Toast para eliminar producto
       if (urlParams.has('productoEliminado')) {
           const eliminado = urlParams.get('productoEliminado').trim().toLowerCase();
           if (eliminado === 'true') {
               mostrarToast('Producto eliminado con éxito', 'exito');
           } else {
               mostrarToast('Error al eliminar producto', 'error');
           }
       }

       // Toast para eliminar usuario
       if (urlParams.has('usuarioEliminado')) {
           const usuarioEliminado = urlParams.get('usuarioEliminado').trim().toLowerCase();
           if (usuarioEliminado === 'true') {
               mostrarToast('Usuario eliminado con éxito', 'exito');
           } else {
               mostrarToast('Error al eliminar usuario', 'error');
           }
       }

       // Toast para modificar usuario
       if (urlParams.has('usuarioModificado')) {
           const usuarioModificado = urlParams.get('usuarioModificado').trim().toLowerCase();
           if (usuarioModificado === 'true') {
               mostrarToast('Usuario actualizado con éxito', 'exito');
           } else {
               mostrarToast('Error al actualizar el usuario', 'error');
           }
       }

       // Toast para agregar marca
       if (urlParams.has('marcaAgregada')) {
           const marcaAgregada = urlParams.get('marcaAgregada').trim().toLowerCase();
           if (marcaAgregada === 'true') {
               mostrarToast('Marca agregada con éxito', 'exito');
           } else {
               mostrarToast('Error al agregar la marca', 'error');
           }
       }

       // Toast para modificar marca
       if (urlParams.has('marcaModificada')) {
           const marcaModificada = urlParams.get('marcaModificada').trim().toLowerCase();
           if (marcaModificada === 'true') {
               mostrarToast('Marca modificada con éxito', 'exito');
           } else {
               mostrarToast('Error al modificar la marca', 'error');
           }
       }

       // Toast para eliminar marca
       if (urlParams.has('marcaEliminada')) {
           const marcaEliminada = urlParams.get('marcaEliminada').trim().toLowerCase();
           if (marcaEliminada === 'true') {
               mostrarToast('Marca eliminada con éxito', 'exito');
           } else {
               mostrarToast('Error al eliminar la marca', 'error');
           }
       }

       // Toast para modificar producto
       if (urlParams.has('productoModificado')) {
           const modificado = urlParams.get('productoModificado').trim().toLowerCase();
           if (modificado === 'true') {
               mostrarToast('Producto modificado con éxito', 'exito');
           } else {
               mostrarToast('Error al modificar producto', 'error');
           }
       }
   });

   /**
    * Filtra filas si alguna de las columnas indicadas contiene el texto.
    * @param {string} tablaId — id del contenedor que envuelve la <table>
    * @param {number[]} columnas — índices de columnas a buscar
    * @param {string} texto — texto de búsqueda
    */
   function filtrarTabla(tablaId, columnas, texto) {
     texto = texto.toLowerCase();
     const seccion = document.getElementById(tablaId);
     if (!seccion) return;
     const table = seccion.querySelector('table');
     if (!table) return;
     const rows = table.tBodies[0].rows;

     for (let i = 0; i < rows.length; i++) {
       let muestra = false;
       for (let col of columnas) {
         const cell = rows[i].cells[col];
         if (cell && cell.textContent.toLowerCase().includes(texto)) {
           muestra = true;
           break;
         }
       }
       rows[i].style.display = muestra ? '' : 'none';
     }
   }

   /**
    * Resalta con <mark> las coincidencias en las columnas visibles.
    * Igual signature que filtrarTabla.
    */
   function resaltarCoincidencias(tablaId, columnas, texto) {
     texto = texto.trim();
     if (texto === '') {
       limpiarResaltados(tablaId, columnas);
       return;
     }
     const regex = new RegExp('(' + texto.replace(/[-\/\\^$*+?.()|[\]{}]/g,'\\$&') + ')', 'ig');
     const seccion = document.getElementById(tablaId);
     if (!seccion) return;
     const table = seccion.querySelector('table');
     if (!table) return;
     const rows = table.tBodies[0].rows;

     for (let i = 0; i < rows.length; i++) {
       if (rows[i].style.display === 'none') continue;
       for (let col of columnas) {
         const cell = rows[i].cells[col];
         if (!cell) continue;
         const plain = cell.textContent;
         cell.innerHTML = plain.replace(regex, '<mark>$1</mark>');
       }
     }
   }

   /**
    * Elimina cualquier <mark> previo devolviendo solo el textContent.
    */
   function limpiarResaltados(tablaId, columnas) {
     const seccion = document.getElementById(tablaId);
     if (!seccion) return;
     const table = seccion.querySelector('table');
     if (!table) return;
     const rows = table.tBodies[0].rows;

     for (let i = 0; i < rows.length; i++) {
       for (let col of columnas) {
         const cell = rows[i].cells[col];
         if (cell) cell.innerHTML = cell.textContent;
       }
     }
   }

   function mostrarDetallesMarca() {
       var marcaSelect = document.getElementById("marcaProducto");
       var selectedOption = marcaSelect.options[marcaSelect.selectedIndex];
       var pais = selectedOption.getAttribute("data-pais");
       var anio = selectedOption.getAttribute("data-anio");
       var nombre = selectedOption.textContent;
       console.log("Marca seleccionada: ", nombre, pais, anio);

       document.getElementById("paisMarca").textContent = pais || "";
       document.getElementById("anioFundacionMarca").textContent = anio || "";
       document.getElementById("paisMarcaInput").value = pais || "";
       document.getElementById("anioMarcaInput").value = anio || "";
       document.getElementById("marcaNombreInput").value = nombre || "";
   }

   
   function eliminarUsuario(id, email) {
       // Validación: no permitir eliminar al administrador principal
       if (email === "davidmpm05@gmail.com") {
           alert("No se puede eliminar el administrador principal.");
           return;
       }
       
       var confirmacion = prompt("Para confirmar la eliminación, por favor escriba 'ELIMINACION'");
       if (confirmacion === "ELIMINACION") {
           var form = document.createElement('form');
           form.action = 'eliminarUsuario';
           form.method = 'post';
           var input = document.createElement('input');
           input.type = 'hidden';
           input.name = 'idUsuario';
           input.value = id;
           form.appendChild(input);
           document.body.appendChild(form);
           form.submit();
       } else {
           alert("Texto incorrecto. La eliminación ha sido cancelada.");
       }
   }

   function eliminarProducto(id) {
       var confirmacion = prompt("Para confirmar la eliminación, por favor escriba 'ELIMINACION'");
       if (confirmacion === "ELIMINACION") {
           var form = document.createElement('form');
           form.action = 'eliminarProducto';
           form.method = 'post';
           var input = document.createElement('input');
           input.type = 'hidden';
           input.name = 'productoId';
           input.value = id;
           form.appendChild(input);
           document.body.appendChild(form);
           form.submit();
       } else {
           alert("Texto incorrecto. La eliminación ha sido cancelada.");
       }
   }
   
   
</script>
</head>
<body>
	<nav class="navbar navbar-expand-lg navbar-light bg-light ">
		<div class="container-fluid">
			<a class="navbar-brand" href="#"> <img
				src="imagenes/Code components-Photoroom.png" alt="Logo">
			</a>
			<button class="navbar-toggler" type="button"
				data-bs-toggle="collapse" data-bs-target="#navbarNavDropdown"
				aria-controls="navbarNavDropdown" aria-expanded="false"
				aria-label="Toggle navigation">
				<span class="navbar-toggler-icon"></span>
			</button>
			<div class="collapse navbar-collapse" id="navbarNavDropdown">
				<ul class="navbar-nav">
					<!-- Botón de Inicio siempre visible -->
					<li class="nav-item"><a class="nav-link"
						href="<%=request.getContextPath()%>/inicio">Inicio</a></li>
					<!-- Si el usuario tiene rol admin, se muestra un botón extra de Admin -->
					<%
					if (session.getAttribute("rol") != null && "admin".equals(session.getAttribute("rol"))) {
					%>
					<li class="nav-item"><a class="nav-link"
						href="<%=request.getContextPath()%>/admin">Admin</a></li>
					<%
					}
					%>
					<li class="nav-item"><a class="nav-link" href="nosotros.jsp">Nosotros</a>
					</li>
					<li class="nav-item"><a class="nav-link"
						href="<%=request.getContextPath()%>/productos">Productos</a></li>
					<%
					if (session.getAttribute("idUsuario") != null) {
					%>
					<li class="nav-item"><a class="nav-link"
						href="<%=request.getContextPath()%>/cerrarSesion">Cerrar
							Sesión</a></li>
					<%
					} else {
					%>
					<li class="nav-item"><a class="nav-link" href="login.jsp">Iniciar
							Sesión</a></li>
					<li class="nav-item"><a class="nav-link" href="registro.jsp">Registrarse</a>
					</li>
					<%
					}
					%>
					<li class="nav-item cart-container"><a class="nav-link"
						href="<%=request.getContextPath()%>/carrito"> <i
							class="bi bi-cart"></i>
					</a></li>
				</ul>
			</div>
		</div>
	</nav>

	<div class="container-fluid">
		<div class="row">
			<!-- Menú lateral -->
			<div class="col-md-3 bg-light py-4">
				<h5>Menú</h5>
				<ul class="nav flex-column">
					<li class="nav-item">
						<button class="btn btn-link nav-link"
							onclick="mostrarSeccion('agregarProducto')">Agregar
							Productos</button>
					</li>
					<li class="nav-item">
						<button class="btn btn-link nav-link"
							onclick="mostrarSeccion('verProductos')">Ver Productos</button>
					</li>
					<li class="nav-item">
						<button class="btn btn-link nav-link"
							onclick="mostrarSeccion('verUsuarios')">Ver Usuarios</button>
					</li>
					<li class="nav-item">
						<button class="btn btn-link nav-link"
							onclick="mostrarSeccion('agregarMarca')">Agregar Marca
							Colaboradora</button>
					</li>
					<li class="nav-item">
						<button class="btn btn-link nav-link"
							onclick="mostrarSeccion('verMarcas')">Ver Marcas</button>
					</li>
				</ul>
			</div>
			<!-- Contenido principal -->
			<div class="col-md-9 py-4">
				<!-- Sección: Modificar Usuario -->
				<div id="modificarUsuario" class="seccion">
					<h2>Modificar Usuario</h2>
					<form action="modificarUsuario" method="post"
						enctype="multipart/form-data">
						<label for="idUsuario">ID Usuario:</label> <input type="number"
							id="idUsuario" name="idUsuario" required> <label
							for="nuevoNombre">Nuevo Nombre:</label> <input type="text"
							id="nuevoNombre" name="nuevoNombre"> <label
							for="nuevoDni">Nuevo DNI:</label> <input type="text"
							id="nuevoDni" name="nuevoDni"> <label for="nuevoTelefono">Nuevo
							Teléfono:</label> <input type="text" id="nuevoTelefono"
							name="nuevoTelefono"> <label for="nuevoRol">Nuevo
							Rol:</label> <input type="text" id="nuevoRol" name="nuevoRol"> <label
							for="nuevaFoto">Nueva Foto:</label> <input type="file"
							id="nuevaFoto" name="nuevaFoto" accept="image/*">
						<button type="submit" class="btn btn-primary mt-3">Modificar
							Usuario</button>
					</form>
				</div>

				<!-- Sección: Eliminar Usuario -->
				<div id="eliminarUsuario" class="seccion" style="display: none;">
					<h2>Eliminar Usuario</h2>
					<form action="eliminarUsuario" method="post">
						<label for="idUsuario">ID Usuario:</label> <input type="number"
							id="idUsuario" name="idUsuario" required>
						<button type="submit" class="btn btn-danger mt-3">Eliminar
							Usuario</button>
					</form>
				</div>

				<!-- Sección: Ver Marcas -->
				<div id="verMarcas" class="seccion" style="display: block;">
					<h2>Listado de Marcas</h2>
					<!-- Buscador de Marcas -->
					<div class="mb-3">
						<input id="buscadorMarcas" type="text" class="form-control"
							placeholder="Buscar marca por nombre…"
							onkeyup="filtrarTabla('verMarcas', [1], this.value);
                  resaltarCoincidencias('verMarcas', [1], this.value)" />
					</div>

					<table class="table table-bordered">
						<thead>
							<tr>
								<th>ID</th>
								<th>Nombre</th>
								<th>País de Origen</th>
								<th>Año de Fundación</th>
								<th>Descripción</th>
								<th>Acciones</th>
							</tr>
						</thead>
						<tbody>
							<%
							List<MarcaDto> marcas = (List<MarcaDto>) request.getAttribute("marcas");
							if (marcas != null && !marcas.isEmpty()) {
								for (MarcaDto marca : marcas) {
							%>
							<tr>
								<td><%=marca.getId()%></td>
								<td><%=marca.getNombre()%></td>
								<td><%=marca.getPaisOrigen()%></td>
								<td><%=marca.getAnioFundacion()%></td>
								<td><%=marca.getDescripcion()%></td>
								<td>
									<!-- Formulario para cargar datos en el formulario de modificación -->
									<form method="get" style="display: inline;">
										<input type="hidden" name="id" value="<%=marca.getId()%>">
										<input type="hidden" name="nombre"
											value="<%=marca.getNombre()%>"> <input type="hidden"
											name="paisOrigen" value="<%=marca.getPaisOrigen()%>">
										<input type="hidden" name="anioFundacion"
											value="<%=marca.getAnioFundacion()%>"> <input
											type="hidden" name="descripcion"
											value="<%=marca.getDescripcion()%>">
										<button type="submit" title="Editar"
											style="border: none; background: none;">
											<i class="fas fa-edit" style="color: blue;"></i>
										</button>
									</form> <!-- Formulario para eliminar la marca -->
									<form action="eliminarMarca" method="post" style="display: inline;">
    <input type="hidden" name="id" value="<%=marca.getId()%>">
    <button type="submit"
            style="border: none; background: none;"
            title="Eliminar"
            onclick="
               var respuesta = prompt('Para confirmar la eliminación, por favor escriba ELIMINACION');
               if (respuesta === 'ELIMINACION') {
                   return true;
               } else {
                   alert('Texto incorrecto. La eliminación ha sido cancelada.');
                   return false;
               }
            ">
        <i class="fas fa-trash-alt" style="color: red;"></i>
    </button>
</form>

								</td>
							</tr>
							<%
							}
							} else {
							%>
							<tr>
								<td colspan="6">No hay marcas disponibles.</td>
							</tr>
							<%
							}
							%>
						</tbody>
					</table>
				</div>

				<!-- Sección: Modificar Marca (precargada si hay datos en el request) -->
				<%
				String id = request.getParameter("id");
				String nombre = request.getParameter("nombre");
				String paisOrigen = request.getParameter("paisOrigen");
				String anioFundacion = request.getParameter("anioFundacion");
				String descripcion = request.getParameter("descripcion");

				if (id != null) {
				%>
				<div id="modificarMarca" class="seccion" style="display: block;">
					<h2>Modificar Marca</h2>
					<form action="modificarMarcas" method="post">
						<div class="mb-3">
							<label for="idMarca" class="form-label">ID:</label> <input
								type="text" class="form-control" id="idMarca" name="id"
								value="<%=id%>" readonly>
						</div>
						<div class="mb-3">
							<label for="nombreMarca" class="form-label">Nombre:</label> <input
								type="text" class="form-control" id="nombreMarca" name="nombre"
								value="<%=nombre%>" required>
						</div>
						<div class="mb-3">
							<label for="paisOrigenMarca" class="form-label">País de
								Origen:</label> <input type="text" class="form-control"
								id="paisOrigenMarca" name="paisOrigen" value="<%=paisOrigen%>"
								required>
						</div>
						<div class="mb-3">
							<label for="anioFundacionMarca" class="form-label">Año de
								Fundación:</label> <input type="number" class="form-control"
								id="anioFundacionMarca" name="anioFundacion"
								value="<%=anioFundacion%>" required>
						</div>
						<div class="mb-3">
							<label for="descripcionMarca" class="form-label">Descripción:</label>
							<textarea class="form-control" id="descripcionMarca"
								name="descripcion" rows="3" required><%=descripcion%></textarea>
						</div>
						<button type="submit" class="btn btn-primary">Modificar
							Marca</button>
					</form>
				</div>
				<%
				}
				%>

				<div id="agregarProducto" class="seccion" style="display: none;">
					<h2>Agregar Producto</h2>
					<form action="productosAniadir" method="post"
						enctype="multipart/form-data">
						<div class="mb-3">
							<label for="nombreProducto" class="form-label">Nombre:</label> <input
								type="text" class="form-control" id="nombreProducto"
								name="nombre" required>
						</div>
						<div class="mb-3">
							<label for="descripcionProducto" class="form-label">Descripción:</label>
							<textarea class="form-control" id="descripcionProducto"
								name="descripcion" required></textarea>
						</div>
						<div class="mb-3">
							<label for="precioProducto" class="form-label">Precio:</label> <input
								type="number" class="form-control" id="precioProducto"
								name="precio" step="0.01" required>
						</div>
						<div class="mb-3">
							<label for="stockProducto" class="form-label">Stock:</label> <input
								type="number" class="form-control" id="stockProducto"
								name="stock" required>
						</div>
						<div class="mb-3">
							<label for="categoriaProducto" class="form-label">Categoría:</label>
							<select class="form-select" id="categoriaProducto"
								name="categoria" required>
								<option value="Ordenadores">Ordenadores</option>
								<option value="Componentes">Componentes</option>
								<option value="Periféricos">Periféricos</option>
								<option value="Portátiles">Portátiles</option>
								<option value="Ofertas">Ofertas</option>
							</select>
						</div>
						<div class="mb-3">
							<label for="marcaProducto" class="form-label">Marca:</label>
							<%
							if (marcas != null) {
							%>
							<p>
								Número de marcas:
								<%=marcas.size()%></p>
							<%
							}
							%>
							<select class="form-select" id="marcaProducto" name="idMarca"
								required onchange="mostrarDetallesMarca()">
								<option value="">Seleccionar Marca</option>
								<%
								if (marcas != null && !marcas.isEmpty()) {
									for (MarcaDto marca : marcas) {
								%>
								<option value="<%=marca.getId()%>"
									data-pais="<%=marca.getPaisOrigen()%>"
									data-anio="<%=marca.getAnioFundacion()%>">
									<%=marca.getNombre()%>
								</option>
								<%
								}
								} else {
								%>
								<option value="">No hay marcas disponibles</option>
								<%
								}
								%>
							</select>
						</div>
						<div class="mb-3" id="marcaDetalles">
							<p>
								<strong>País de Origen:</strong> <span id="paisMarca"></span>
							</p>
							<p>
								<strong>Año de Fundación:</strong> <span id="anioFundacionMarca"></span>
							</p>
						</div>
						<input type="hidden" id="paisMarcaInput" name="pais"> <input
							type="hidden" id="anioMarcaInput" name="anioCreacion"> <input
							type="hidden" id="marcaNombreInput" name="marcaNombre">
						<div class="mb-3">
							<label for="imagenProducto" class="form-label">Imagen:</label> <input
								type="file" class="form-control" id="imagenProducto"
								name="imagen" accept="image/*" required>
						</div>
						<button type="submit" class="btn btn-success">Agregar
							Producto</button>
					</form>
				</div>

				<!-- Sección: Agregar Marca -->
				<div id="agregarMarca" class="seccion" style="display: none;">
					<h2>Agregar Marca</h2>
					<form action="marcasAniadir" method="post">
						<label for="nombreMarca">Nombre:</label> <input type="text"
							id="nombreMarca" name="nombreMarca" required> <label
							for="paisOrigen">País de Origen:</label> <input type="text"
							id="paisOrigen" name="paisOrigen" required> <label
							for="anioFundacion">Año de Fundación:</label> <input
							type="number" id="anioFundacion" name="anioFundacion" required>
						<label for="descripcion">Descripción:</label>
						<textarea id="descripcion" name="descripcion" required></textarea>
						<button type="submit" class="btn btn-success mt-3">Agregar
							Marca</button>
					</form>
				</div>

				<!-- Sección: Eliminar Producto -->
				<div id="eliminarProducto" class="seccion" style="display: none;">
					<h2>Eliminar Producto</h2>
					<form action="eliminarProducto" method="post">
						<label for="productoId" class="form-label">ID del
							Producto:</label> <input type="number" id="productoId" name="productoId"
							class="form-control" required>
						<button type="submit" class="btn btn-danger mt-3">Eliminar
							Producto</button>
					</form>
				</div>

				<!-- Sección: Modificar Producto -->
				<div id="modificarProducto" class="seccion" style="display: none;">
					<h2>Modificar Producto</h2>
					<form action="modificarProducto" method="post"
						enctype="multipart/form-data">
						<div class="mb-3">
							<label for="idProducto" class="form-label">ID Producto:</label> <input
								type="number" id="idProducto" name="id" class="form-control"
								required readonly>
						</div>
						<div class="mb-3">
							<label for="modNombreProducto" class="form-label">Nombre
								(opcional):</label> <input type="text" id="modNombreProducto"
								name="nombre" class="form-control">
						</div>
						<div class="mb-3">
							<label for="modDescripcionProducto" class="form-label">Descripción
								(opcional):</label>
							<textarea id="modDescripcionProducto" name="descripcion"
								class="form-control" rows="3"></textarea>
						</div>
						<div class="mb-3">
							<label for="modPrecioProducto" class="form-label">Precio
								(opcional):</label> <input type="number" id="modPrecioProducto"
								name="precio" class="form-control" step="0.01">
						</div>
						<div class="mb-3">
							<label for="modStockProducto" class="form-label">Stock
								(opcional):</label> <input type="number" id="modStockProducto"
								name="stock" class="form-control">
						</div>
						<div class="mb-3">
							<label for="imagenProducto" class="form-label">Imagen
								(opcional):</label> <input type="file" id="imagenProducto" name="imagen"
								class="form-control" accept="image/*">
						</div>
						<button type="submit" class="btn btn-warning mt-3">Modificar
							Producto</button>
					</form>
				</div>

				<!-- Sección: Ver Productos -->
				<div id="verProductos" class="seccion" style="display: none;">
					<h2>Lista de Productos</h2>

					<!-- Buscador de Productos -->
					<div class="mb-3">
						<input id="buscadorProductos" type="text" class="form-control"
							placeholder="Buscar producto por nombre…"
							onkeyup="filtrarTabla('verProductos', [1], this.value);
                  resaltarCoincidencias('verProductos', [1], this.value)" />
					</div>

					<table class="table table-bordered">
						<thead>
							<tr>
								<th>ID</th>
								<th>Nombre</th>
								<th>Descripción</th>
								<th>Precio</th>
								<th>Stock</th>
								<th>Imagen</th>
								<th>Acciones</th>
							</tr>
						</thead>
						<tbody>
							<%
							List<ProductoDto> productos = (List<ProductoDto>) request.getAttribute("productos");
							if (productos != null && !productos.isEmpty()) {
								for (ProductoDto producto : productos) {
							%>
							<tr>
								<td><%=producto.getId()%></td>
								<td><%=producto.getNombre()%></td>
								<td><%=producto.getDescripcion()%></td>
								<td><%=producto.getPrecio()%>€</td>
								<td><%=producto.getStock()%></td>
								<td>
									<!-- Se puede mostrar la imagen aquí -->
								</td>
								<td><i class="fas fa-edit"
									style="cursor: pointer; color: blue;"
									onclick="editarProducto('<%=producto.getId()%>', '<%=producto.getNombre()%>', '<%=producto.getDescripcion()%>', '<%=producto.getPrecio()%>', '<%=producto.getStock()%>')"></i>
									<!-- Se actualiza la llamada a eliminarProducto para usar la nueva función -->
									<i class="fas fa-trash-alt"
									style="cursor: pointer; color: red;"
									onclick="eliminarProducto('<%=producto.getId()%>')"></i></td>
							</tr>
							<%
							}
							} else {
							out.println("<tr><td colspan='7'>No hay productos disponibles.</td></tr>");
							}
							%>
						</tbody>
					</table>
				</div>

				<!-- Sección: Ver Usuarios -->
				<div id="verUsuarios" class="seccion" style="display: none;">
					<h2>Lista de Usuarios</h2>

					<!-- Buscador de Usuarios (nombre + email) -->
					<div class="mb-3">
						<input id="buscadorUsuarios" type="text" class="form-control"
							placeholder="Buscar usuario por nombre o email…"
							onkeyup="filtrarTabla('verUsuarios', [1,3], this.value);
                  resaltarCoincidencias('verUsuarios', [1,3], this.value)" />
					</div>

					<!-- Botón para exportar PDF (sin JSTL) -->
					<div class="d-flex justify-content-end mb-2">
						<a href="<%=request.getContextPath()%>/exportarUsuariosPdf"
							class="btn btn-outline-primary"> <i
							class="bi bi-file-earmark-pdf"></i> Exportar PDF
						</a>
					</div>
					<table class="table table-bordered">
						<thead>
							<tr>
								<th>ID</th>
								<th>Nombre</th>
								<th>Teléfono</th>
								<th>Email</th>
								<th>Rol</th>
								<th>Acciones</th>
							</tr>
						</thead>
						<tbody>
							<%
							List<UsuarioDto> usuarios = (List<UsuarioDto>) request.getAttribute("usuarios");
							if (usuarios != null && !usuarios.isEmpty()) {
								for (UsuarioDto usuario : usuarios) {
							%>
							<tr>
								<td><%=usuario.getIdUsuario()%></td>
								<td><%=usuario.getNombreUsuario()%></td>
								<td><%=usuario.getTelefonoUsuario()%></td>
								<td><%=usuario.getEmailUsuario()%></td>
								<td><%=usuario.getRol()%></td>
								<td><i class="fas fa-edit"
									style="cursor: pointer; color: blue;"
									onclick="editarUsuario(
               '<%=usuario.getIdUsuario()%>',
               '<%=usuario.getNombreUsuario()%>',
               '<%=usuario.getTelefonoUsuario()%>',
               '<%=usuario.getRol()%>'
             )"></i>
									<i class="fas fa-trash-alt"
									style="cursor: pointer; color: red; margin-left: 8px;"
									onclick="eliminarUsuario(
               '<%=usuario.getIdUsuario()%>',
               '<%=usuario.getEmailUsuario()%>'
             )"></i>
								</td>
							</tr>
							<%
							}
							} else {
							%>
							<tr>
								<td colspan="6">No hay usuarios disponibles.</td>
							</tr>
							<%
							}
							%>
						</tbody>
					</table>
				</div>


			</div>
		</div>
	</div>

	<div class="position-fixed bottom-0 end-0 p-3" style="z-index: 11">
		<div id="liveToast"
			class="toast align-items-center text-white bg-success border-0"
			role="alert" aria-live="assertive" aria-atomic="true">
			<div class="d-flex">
				<div class="toast-body">Mensaje</div>
				<button type="button" class="btn-close btn-close-white me-2 m-auto"
					data-bs-dismiss="toast" aria-label="Close"></button>
			</div>
		</div>
	</div>
</body>
</html>
