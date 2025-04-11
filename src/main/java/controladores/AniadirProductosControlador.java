package controladores;

import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;

import dtos.MarcaDto;
import dtos.ProductoDto;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.Part;
import servicios.ProductoServicio;
import utilidades.Utilidades;

/**
 * Servlet encargado de manejar la adición de nuevos productos al sistema.
 * Recibe los datos del formulario de productos y los envía al servicio correspondiente.
 */
@WebServlet("/productosAniadir")
@MultipartConfig
public class AniadirProductosControlador extends HttpServlet {

    private ProductoServicio productoServicio;

    @Override
    public void init() throws ServletException {
        productoServicio = new ProductoServicio(); 
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        Utilidades.escribirLog(session, "[INFO]", "AniadirProductosControlador", "doPost", "Inicio del proceso de agregar producto");

        try {
            // Obtener datos del formulario
            String nombre = Optional.ofNullable(request.getParameter("nombre")).orElse("").trim();
            String descripcion = Optional.ofNullable(request.getParameter("descripcion")).orElse("").trim();
            String precioStr = Optional.ofNullable(request.getParameter("precio")).orElse("0").trim();
            String stockStr = Optional.ofNullable(request.getParameter("stock")).orElse("0").trim();
            String categoria = Optional.ofNullable(request.getParameter("categoria")).orElse("").trim();
            String idMarcaStr = Optional.ofNullable(request.getParameter("idMarca")).orElse("0").trim();
            String marcaNombre = Optional.ofNullable(request.getParameter("marcaNombre")).orElse("").trim();

            // Nuevos campos de Marca
            String paisOrigen = Optional.ofNullable(request.getParameter("paisOrigen")).orElse("Desconocido").trim();
            String anioFundacionStr = Optional.ofNullable(request.getParameter("anioFundacion")).orElse("0").trim();
            String descripcionMarca = Optional.ofNullable(request.getParameter("descripcionMarca")).orElse("No especificado").trim();

            // Validar parámetros obligatorios
            if (nombre.isEmpty() || descripcion.isEmpty() || categoria.isEmpty() || marcaNombre.isEmpty()) {
                response.sendRedirect("productos.jsp?error=Faltan campos obligatorios");
                return;
            }

            // Convertir valores con manejo de errores
            double precio;
            int stock;
            long idMarca;
            int anioFundacion;

            try {
                precio = Double.parseDouble(precioStr);
                stock = Integer.parseInt(stockStr);
                idMarca = Long.parseLong(idMarcaStr);
                anioFundacion = Integer.parseInt(anioFundacionStr);
            } catch (NumberFormatException e) {
                Utilidades.escribirLog(session, "[ERROR]", "AniadirProductosControlador", "doPost", 
                    "Error en conversión de números: " + e.getMessage());
                response.sendRedirect("productos.jsp?error=Formato numérico inválido");
                return;
            }

            // Procesar archivo de imagen
            Part imagenPart = request.getPart("imagen");
            byte[] imagenBytes = null;
            if (imagenPart != null && imagenPart.getSize() > 0) {
                try (InputStream inputStream = imagenPart.getInputStream()) {
                    imagenBytes = inputStream.readAllBytes();
                }
            } else {
                // Puedes asignar una imagen por defecto aquí si es necesario
                Utilidades.escribirLog(session, "[INFO]", "AniadirProductosControlador", "doPost", "No se subió imagen, se asignará predeterminada.");
            }

            // Construir el objeto MarcaDto con los nuevos atributos
            MarcaDto marca = new MarcaDto(idMarca, marcaNombre, paisOrigen, anioFundacion, descripcionMarca);

            // Construir el DTO de producto
            ProductoDto producto = new ProductoDto(null, nombre, descripcion, precio, imagenBytes, stock, categoria, marca);

            // Llamar al servicio para agregar el producto
            boolean exito = productoServicio.agregarProducto(producto);

            if (exito) {
                Utilidades.escribirLog(session, "[INFO]", "AniadirProductosControlador", "doPost", "Producto agregado exitosamente.");
                response.sendRedirect("admin?productoAgregado=true");
            } else {
                Utilidades.escribirLog(session, "[ERROR]", "AniadirProductosControlador", "doPost", "Error al agregar el producto.");
                response.sendRedirect("productos.jsp?error=No se pudo agregar el producto");
            }
        } catch (Exception e) {
            Utilidades.escribirLog(session, "[ERROR]", "AniadirProductosControlador", "doPost", "Error general: " + e.getMessage());
            e.printStackTrace();
            response.sendRedirect("productos.jsp?error=Error inesperado");
        }
    }
}
