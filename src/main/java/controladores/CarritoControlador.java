package controladores;

import java.io.IOException;
import java.util.List;

import dtos.CarritoDto;
import servicios.CarritoServicio;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import utilidades.Utilidades; 

/**
 * Controlador que maneja las operaciones del carrito de compras.
 * Permite agregar y eliminar productos del carrito en la sesión.
 * La lógica de negocio se maneja en el servicio {@link CarritoServicio}.
 * 
 * @author dmp
 */
@WebServlet("/carrito")
public class CarritoControlador extends HttpServlet {

    // Se asume que este servicio es el de la API (solo persistencia)
    private CarritoServicio carritoServicio = new CarritoServicio();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        HttpSession session = request.getSession();

        // Acción para agregar un producto al carrito
        if ("agregar".equals(action)) {
            try {
                // Log: inicio acción agregar
                Utilidades.escribirLog(session, "[INFO]", "CarritoControlador", "doPost", "Inicio acción agregar producto");

                long id = Long.parseLong(request.getParameter("id"));
                int cantidad = Integer.parseInt(request.getParameter("cantidad"));
                if (cantidad <= 0) {
                    response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Cantidad inválida");
                    return;
                }

                CarritoDto productoDetalles = carritoServicio.obtenerProductoPorId(id);
                if (productoDetalles == null) {
                    response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Producto no encontrado");
                    return;
                }

                CarritoDto carritoDto = new CarritoDto(
                        productoDetalles.getId(),
                        productoDetalles.getNombre(),
                        cantidad,
                        productoDetalles.getPrecio(),
                        productoDetalles.getImagen()
                );

                // Sustituido: gestión mediante servicio con sesión
                carritoServicio.agregarProducto(session, carritoDto);

                Utilidades.escribirLog(session, "[INFO]", "CarritoControlador", "doPost", "Producto agregado al carrito correctamente");

                session.setAttribute("productoAgregado", true);
                response.sendRedirect("productos");

            } catch (NumberFormatException e) {
                Utilidades.escribirLog(session, "[ERROR]", "CarritoControlador", "doPost", "Error al convertir datos: " + e.getMessage());
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Datos inválidos");
            } catch (Exception e) {
                Utilidades.escribirLog(session, "[ERROR]", "CarritoControlador", "doPost", "Error interno del servidor: " + e.getMessage());
                e.printStackTrace();
                response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error interno del servidor");
            }

        // Acción para eliminar un producto del carrito
        } else if ("eliminar".equals(action) && "DELETE".equals(request.getParameter("_method"))) {
            try {
                // Log: inicio acción eliminar
                Utilidades.escribirLog(session, "[INFO]", "CarritoControlador", "doPost", "Inicio acción eliminar producto del carrito");

                long id = Long.parseLong(request.getParameter("id"));

                // Sustituido: gestión mediante servicio con sesión
                carritoServicio.eliminarProducto(session, id);

                Utilidades.escribirLog(session, "[INFO]", "CarritoControlador", "doPost", "Producto eliminado del carrito correctamente");
                response.sendRedirect("carrito");

            } catch (NumberFormatException e) {
                Utilidades.escribirLog(session, "[ERROR]", "CarritoControlador", "doPost", "ID inválido: " + e.getMessage());
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "ID inválido");
            } catch (Exception e) {
                Utilidades.escribirLog(session, "[ERROR]", "CarritoControlador", "doPost", "Error al eliminar producto: " + e.getMessage());
                e.printStackTrace();
                response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error al eliminar producto");
            }
        } else {
            Utilidades.escribirLog(session, "[ERROR]", "CarritoControlador", "doPost", "Acción no soportada: " + action);
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "Acción no soportada");
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        Utilidades.escribirLog(session, "[INFO]", "CarritoControlador", "doGet", "Mostrando carrito de compras");

       
        List<CarritoDto> carrito = carritoServicio.obtenerCarrito(session);

        request.setAttribute("carrito", carrito);
        RequestDispatcher dispatcher = request.getRequestDispatcher("carrito.jsp");
        dispatcher.forward(request, response);
    }
}