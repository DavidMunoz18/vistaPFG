<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="java.util.List,dtos.ProductoDto"%>
<%@ page import="dtos.CarritoDto"%>
<!DOCTYPE html>
<html lang="es">
<head>
<meta charset="utf-8" />
<meta content="width=device-width, initial-scale=1.0" name="viewport" />
<title>Code Components</title>
<link
	href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css"
	rel="stylesheet"
	integrity="sha384-QWTKZyjpPEjISv5WaRU9OFeRpok6YctnYmDr5pNlyT2bRjXh0JMhjY6hW+ALEwIH"
	crossorigin="anonymous" />
<link
	href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.3/css/all.min.css"
	rel="stylesheet" />
<link rel="stylesheet"
	href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.min.css">
<link rel="stylesheet"
	href="<%=request.getContextPath()%>/css/estilo.css">
</head>
<body>
	<nav class="navbar navbar-expand-lg navbar-light bg-light fixed-top">
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
	<br>
	<br>
	<br>
	<br>


	<div id="carouselExampleAutoplaying" class="carousel slide"
		data-bs-ride="carousel">
		<div class="carousel-inner">
			<div class="carousel-item active">
				<img
					src="imagenes/freepik__the-style-is-candid-image-photography-with-natural__80280.jpg"
					class="d-block img-fluid mx-auto" alt="..." />
			</div>
			<div class="carousel-item">
				<img src="imagenes/andrey-matveev-0Sdxy9Ev6oQ-unsplash.jpg"
					class="d-block img-fluid w-100" alt="..." />
			</div>
			<div class="carousel-item">
				<img src="imagenes/onur-binay-4Ykxp_t4i08-unsplash.jpg"
					class="d-block img-fluid w-100" alt="" />
			</div>
		</div>
		<button class="carousel-control-prev" type="button"
			data-bs-target="#carouselExampleAutoplaying" data-bs-slide="prev">
			<span class="carousel-control-prev-icon" aria-hidden="true"></span> <span
				class="visually-hidden">Previous</span>
		</button>
		<button class="carousel-control-next" type="button"
			data-bs-target="#carouselExampleAutoplaying" data-bs-slide="next">
			<span class="carousel-control-next-icon" aria-hidden="true"></span> <span
				class="visually-hidden">Next</span>
		</button>
	</div>
	<div class="container my-5">
		<h2 class="mb-4">Nuevos productos</h2>
		<div class="row">
			<%
			List<ProductoDto> productos = (List<ProductoDto>) request.getAttribute("productos");
			if (productos != null && !productos.isEmpty()) {
				int contador = 0;
				for (ProductoDto producto : productos) {
					if (contador >= 6)
				break;
					contador++;
			%>
			<div class="col-12 col-sm-6 col-md-4 col-lg-2 mb-4">
				<div class="card product-card h-100">
					<a href="vistas.jsp?id=<%=producto.getId()%>"> <img
						src="data:image/png;base64,<%=producto.getImagenBase64()%>"
						alt="<%=producto.getNombre()%>" class="card-img-top img-fluid" />
					</a>
					<div class="card-body d-flex flex-column">
						<h5 class="card-title text-truncate"><%=producto.getNombre()%></h5>
						<p class="card-text mb-3">
							<span class="fw-bold">$<%=producto.getPrecio()%></span>
						</p>
						<form action="<%=request.getContextPath()%>/carrito" method="POST"
							class="mt-auto">
							<input type="hidden" name="action" value="agregar"> <input
								type="hidden" name="id" value="<%=producto.getId()%>"> <input
								type="hidden" name="nombre" value="<%=producto.getNombre()%>">
							<input type="hidden" name="precio"
								value="<%=producto.getPrecio()%>"> <input type="hidden"
								name="imagen" value="<%=producto.getImagen()%>">
							<div class="input-group mb-3">
								<input type="number" name="cantidad" value="1" min="1" required
									class="form-control text-center" style="max-width: 80px;">
							</div>
							<button type="submit" class="btn btn-primary w-100">Agregar
								al carrito</button>
						</form>
					</div>
				</div>
			</div>
			<%
			}
			} else {
			%>
			<div class="col-12">
				<p>No hay productos disponibles.</p>
			</div>
			<%
			}
			%>
		</div>
		<div class="text-end mt-3">
			<a class="btn btn-link"
				href="<%=request.getContextPath()%>/productos"> Ver Más
				Productos </a>
		</div>
	</div>

	<div class="container my-5">
		<div class="row justify-content-between">
			<div class="col-6 col-sm-4 col-md-3 col-lg-2 mb-4">
				<div class="card category-card h-100">
					<img alt="Complementos" class="card-img-top img-fluid"
						src="imagenes/Component 6.png" />
					<div class="card-body text-center">
						<a class="btn btn-link"
							href="https://tomcat.dmunoz.es/VistaCodeComponents/productos?categoria=Componentes">
							Ver todos </a>
					</div>
				</div>
			</div>
			<div class="col-6 col-sm-4 col-md-3 col-lg-2 mb-4">
				<div class="card category-card h-100">
					<img alt="Portátiles MSI" class="card-img-top img-fluid"
						src="imagenes/Component 7.png" />
					<div class="card-body text-center">
						<a class="btn btn-link"
							href="https://tomcat.dmunoz.es/VistaCodeComponents/productos?categoria=Port%C3%A1tiles">
							Ver todos </a>
					</div>
				</div>
			</div>
			<div class="col-6 col-sm-4 col-md-3 col-lg-2 mb-4">
				<div class="card category-card h-100">
					<img alt="Ordenadores de mesa" class="card-img-top img-fluid"
						src="imagenes/Component 10.png" />
					<div class="card-body text-center">
						<a class="btn btn-link"
							href="https://tomcat.dmunoz.es/VistaCodeComponents/productos?categoria=Ordenadores">
							Ver todos </a>
					</div>
				</div>
			</div>
			<div class="col-6 col-sm-4 col-md-3 col-lg-2 mb-4">
				<div class="card category-card h-100">
					<img alt="Monitores" class="card-img-top img-fluid"
						src="imagenes/Component 11.png" />
					<div class="card-body text-center">
						<a class="btn btn-link"
							href="https://tomcat.dmunoz.es/VistaCodeComponents/productos?categoria=Ofertas">
							Ver todos </a>
					</div>
				</div>
			</div>
		</div>
	</div>

	<div class="container my-5 text-center">
		<div class="row">
			<div class="col-6 col-md-2 mb-3">
				<img style="height: 100px;" alt="Marca Logo" class="img-fluid"
					src="imagenes/roccat.png" />
			</div>
			<div class="col-6 col-md-2 mb-3">
				<img style="height: 100px;" alt="Marca Logo" class="img-fluid"
					src="imagenes/logomsi.png" />
			</div>
			<div class="col-6 col-md-2 mb-3">
				<img style="height: 100px;" alt="Marca Logo" class="img-fluid"
					src="imagenes/logorazer.png" />
			</div>
			<div class="col-6 col-md-2 mb-3">
				<img style="height: 100px;" alt="Marca Logo" class="img-fluid"
					src="imagenes/logotherma.png" />
			</div>
			<div class="col-6 col-md-2 mb-3">
				<img style="height: 100px;" alt="Marca Logo" class="img-fluid"
					src="imagenes/logoadata.png" />
			</div>
			<div class="col-6 col-md-2 mb-3">
				<img style="height: 100px;" alt="Marca Logo" class="img-fluid"
					src="imagenes/logohp.png" />
			</div>
		</div>
	</div>

	<div class="container my-5">
		<h2 class="mb-4">Síguenos en Instagram para recibir noticias,
			ofertas y más</h2>
		<div class="row">
			<div class="col-12 col-sm-6 col-md-3 mb-4">
				<div class="card">
					<img alt="Instagram Publicación" class="card-img-top img-fluid"
						src="imagenes/image 29.png" />
					<div class="card-body">
						<p class="card-text">Descubre nuestras PCs de alto
							rendimiento, ideales para jugar sin interrupciones y disfrutar de
							la mejor experiencia posible en todos tus juegos favoritos.</p>
					</div>
				</div>
			</div>
			<div class="col-12 col-sm-6 col-md-3 mb-4">
				<div class="card">
					<img alt="Instagram Publicación" class="card-img-top img-fluid"
						src="imagenes/image 290.png" />
					<div class="card-body">
						<p class="card-text">Diseñadas para ofrecer potencia y
							estabilidad, nuestras computadoras brindan un rendimiento óptimo
							para quienes buscan calidad al mejor precio.</p>
					</div>
				</div>
			</div>
			<div class="col-12 col-sm-6 col-md-3 mb-4">
				<div class="card">
					<img alt="Instagram Publicación" class="card-img-top img-fluid"
						src="imagenes/image 291.png" />
					<div class="card-body">
						<p class="card-text">Renueva tu equipo con una PC a la altura
							de tus expectativas. Encuentra ofertas exclusivas y mejora tu
							experiencia gamer como nunca antes lo habías hecho.</p>
					</div>
				</div>
			</div>
			<div class="col-12 col-sm-6 col-md-3 mb-4">
				<div class="card">
					<img alt="Instagram Publicación" class="card-img-top img-fluid"
						src="imagenes/image 293.png" />
					<div class="card-body">
						<p class="card-text">Síguenos para conocer nuestras novedades,
							consejos sobre lanzamientos y promociones especiales
							que solo encontrarás en nuestra tienda online.</p>
					</div>
				</div>
			</div>
		</div>
	</div>

	<div class="container my-5 text-center">
		<div class="row">
			<div class="col-12 col-md-4 mb-4">
				<i class="fas fa-headset fa-3x mb-3"></i>
				<h5>Soporte técnico</h5>
				<p>Hasta 3 años de garantía disponible para su tranquilidad.</p>
			</div>
			<div class="col-12 col-md-4 mb-4">
				<i class="fas fa-user fa-3x mb-3"></i>
				<h5>Cuenta personal</h5>
				<p>Con grandes descuentos, entrega gratuita y acceso prioritario
					en soporte dedicado.</p>
			</div>
			<div class="col-12 col-md-4 mb-4">
				<i class="fas fa-tags fa-3x mb-3"></i>
				<h5>Ahorros increíbles</h5>
				<p>Hasta 70% de descuento en productos nuevos para estar seguro
					de su mejor precio.</p>
			</div>
		</div>
	</div>

	<footer class="footer text-light py-5" style="background-color: #000;">
		<div class="container">
			<div class="row">
				<div class="col-12 col-md-4 mb-4 text-center text-md-start">
					<h5>Suscríbete a nuestro boletín</h5>
					<p class="small">Sé el primero en enterarte de las últimas
						ofertas y noticias.</p>
					<form class="row g-2" onsubmit="window.location.href='inicio'; return false;">
						<div class="col-12 col-md-8">
							<input aria-label="Email" class="form-control"
								placeholder="Tu email" type="email" required
								style="border-radius: 50px;" />
						</div>
						<div class="col-12 col-md-4">
							<button class="btn w-100" type="submit"
								style="border-radius: 50px; background-color: #0156FF; color: #fff;">
								Suscribirse</button>
						</div>
					</form>
				</div>
				<div class="col-6 col-md-2 mb-4">
					<h5>Información</h5>
					<ul class="list-unstyled small">
						<li><a href="#" class="text-white">Sobre nosotros</a></li>
						<li><a href="#" class="text-white">Política de privacidad</a></li>
						<li><a href="#" class="text-white">Términos y condiciones</a></li>
						<li><a href="#" class="text-white">Devoluciones</a></li>
					</ul>
				</div>
				<div class="col-6 col-md-2 mb-4">
					<h5>Piezas de PC</h5>
					<ul class="list-unstyled small">
						<li><a href="#" class="text-white">Procesadores</a></li>
						<li><a href="#" class="text-white">Tarjetas gráficas</a></li>
						<li><a href="#" class="text-white">Placas base</a></li>
						<li><a href="#" class="text-white">Memoria RAM</a></li>
					</ul>
				</div>
				<div class="col-6 col-md-2 mb-4">
					<h5>Portátiles</h5>
					<ul class="list-unstyled small">
						<li><a href="#" class="text-white">Gaming</a></li>
						<li><a href="#" class="text-white">Trabajo</a></li>
						<li><a href="#" class="text-white">Estudiantes</a></li>
						<li><a href="#" class="text-white">Diseño gráfico</a></li>
					</ul>
				</div>
				<div class="col-6 col-md-2 mb-4 text-center text-md-end">
					<h5>Síguenos</h5>
					<div class="d-flex justify-content-center justify-content-md-end">
						<a class="text-white me-3" href="#"><i
							class="fab fa-facebook-f"></i></a> <a class="text-white me-3"
							href="#"><i class="fab fa-twitter"></i></a> <a
							class="text-white me-3" href="#"><i class="fab fa-instagram"></i></a>
						<a class="text-white" href="#"><i class="fab fa-linkedin-in"></i></a>
					</div>
				</div>
			</div>
			<div class="row mt-4">
				<div class="col text-center">
					<p class="text-white small mb-0">© 2025 Code Components Pty.
						Ltd. Todos los derechos reservados.</p>
				</div>
			</div>
		</div>
	</footer>

	<script
		src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"
		integrity="sha384-YvpcrYf0tY3lHB60NNkmXc5s9fDVZLESaAA55NDzOxhy9GkcIdslK1eN7N6jIeHz"
		crossorigin="anonymous"></script>
</body>
</html>
