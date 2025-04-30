package controladores;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.net.URLEncoder;

import com.fasterxml.jackson.databind.ObjectMapper;
import dtos.CarritoDto;
import dtos.PedidoDto;
import dtos.PedidoProductoDto;
import servicios.CarritoServicio;
import servicios.PedidoServicio;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import utilidades.Utilidades;

@WebServlet("/pedidos")
public class PedidoControlador extends HttpServlet {

    private PedidoServicio pedidoServicio = new PedidoServicio();
    // Servicio del carrito para llamar al método limpiarCarrito()
    private CarritoServicio carritoServicio = new CarritoServicio();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Obtener el parámetro transaccionPaypal (si viene por GET para el flujo de PayPal)
        String transaccionPaypal = request.getParameter("transaccionPaypal");

        if (transaccionPaypal != null) {
            HttpSession session = request.getSession();
            carritoServicio.limpiarCarrito();
            session.removeAttribute("carrito");
          
            session.setAttribute("mensaje", "Pago procesado con éxito.");
            session.setAttribute("tipoMensaje", "success");
            response.sendRedirect("carrito.jsp?pedidoExitoso=true");
        } else {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Falta el parámetro transaccionPaypal.");
        }
    }


    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();

        // Obtener datos del pedido
        String contacto = request.getParameter("contacto");
        String direccion = request.getParameter("direccion");
        String metodoPago = request.getParameter("metodoPago");
        String transaccionPaypal = request.getParameter("transaccionPaypal");

        // Datos de tarjeta (sólo para pago con tarjeta)
        String nombreTarjeta  = request.getParameter("nombreTarjeta");
        String numeroTarjeta  = request.getParameter("numeroTarjeta");
        String mesExpiracion  = request.getParameter("mesExpiracion");
        String anioExpiracion = request.getParameter("anioExpiracion");
        String cvc            = request.getParameter("cvv");

        // Si se paga con PayPal, limpiar datos de tarjeta y establecer valores por defecto
        if ("paypal".equalsIgnoreCase(metodoPago)) {
            nombreTarjeta = "";
            numeroTarjeta = "";
            mesExpiracion = "";
            anioExpiracion = "";
            cvc = "";
            contacto = "PayPal"; // Asignar contacto predeterminado para PayPal
            if (direccion == null || direccion.trim().isEmpty()) {
                direccion = "N/A"; // Dirección por defecto para PayPal
            }
        }

        // Verificar si el usuario está logueado
        Long idUsuario = (Long) session.getAttribute("idUsuario");
        if (idUsuario == null) {
            session.setAttribute("pedidoContacto", contacto);
            session.setAttribute("pedidoDireccion", direccion);
            session.setAttribute("pedidoMetodoPago", metodoPago);
            session.setAttribute("pedidoNombreTarjeta", nombreTarjeta);
            session.setAttribute("pedidoNumeroTarjeta", numeroTarjeta);
            session.setAttribute("pedidoMesExpiracion", mesExpiracion);
            session.setAttribute("pedidoAnioExpiracion", anioExpiracion);
            session.setAttribute("pedidoCvv", cvc);
            String returnURL = "carrito";
            response.sendRedirect("login.jsp?returnURL=" + URLEncoder.encode(returnURL, "UTF-8"));
            return;
        }

        // Validaciones para pago con tarjeta
        if (!"paypal".equalsIgnoreCase(metodoPago)) {
            if (!validarNumeroTarjeta(numeroTarjeta)) {
                session.setAttribute("mensaje", "El número de tarjeta no es válido. Debe contener entre 13 y 19 dígitos numéricos.");
                session.setAttribute("tipoMensaje", "error");
                response.sendRedirect("carrito.jsp");
                return;
            }
            if (!validarCvc(cvc)) {
                session.setAttribute("mensaje", "El código de seguridad (CVV) no es válido. Debe contener 3 o 4 dígitos.");
                session.setAttribute("tipoMensaje", "error");
                response.sendRedirect("carrito.jsp");
                return;
            }
            numeroTarjeta = encriptarDatos(numeroTarjeta);
            cvc = encriptarDatos(cvc);
        }

        // Recuperar el carrito desde la sesión
        List<CarritoDto> carrito = (List<CarritoDto>) session.getAttribute("carrito");
        // Si el carrito está vacío y estamos en flujo PayPal, intentar recuperarlo desde el parámetro "jsonCarrito"
        if (carrito == null || carrito.isEmpty()) {
            String jsonCarrito = request.getParameter("jsonCarrito");
            if (jsonCarrito != null && !jsonCarrito.trim().isEmpty()) {
                try {
                    ObjectMapper mapper = new ObjectMapper();
                    CarritoDto[] carritoArray = mapper.readValue(jsonCarrito, CarritoDto[].class);
                    carrito = Arrays.asList(carritoArray);
                    session.setAttribute("carrito", carrito);
                } catch (Exception e) {
                    Utilidades.escribirLog(session, "[ERROR]", "PedidoControlador", "doPost", "Error al parsear jsonCarrito: " + e.getMessage());
                }
            }
        }

        if (carrito == null || carrito.isEmpty()) {
            session.setAttribute("mensaje", "El carrito está vacío");
            session.setAttribute("tipoMensaje", "error");
            response.sendRedirect("carrito.jsp");
            return;
        }

        // Convertir el carrito en la lista de productos del pedido
        List<PedidoProductoDto> productosPedido = new ArrayList<>();
        for (CarritoDto carritoDto : carrito) {
            PedidoProductoDto productoPedidoDto = new PedidoProductoDto(
                    carritoDto.getId(),
                    carritoDto.getNombre(),
                    carritoDto.getCantidad(),
                    carritoDto.getPrecio()
            );
            productosPedido.add(productoPedidoDto);
        }

        // Crear el objeto PedidoDto con los datos recopilados
        PedidoDto pedidoDto = new PedidoDto();
        pedidoDto.setContacto(contacto);
        pedidoDto.setDireccion(direccion);
        pedidoDto.setMetodoPago(metodoPago);
        pedidoDto.setNombreTarjeta(nombreTarjeta);
        pedidoDto.setNumeroTarjeta(numeroTarjeta);
        if (!"paypal".equalsIgnoreCase(metodoPago)) {
            pedidoDto.setFechaExpiracion(mesExpiracion + "/" + anioExpiracion);
        } else {
            pedidoDto.setFechaExpiracion("");
        }
        pedidoDto.setCvc(cvc);
        pedidoDto.setIdUsuario(idUsuario);
        pedidoDto.setProductos(productosPedido);

        if ("paypal".equalsIgnoreCase(metodoPago) && transaccionPaypal != null) {
            pedidoDto.setTransaccionPaypal(transaccionPaypal);
        }

        String mensajeRespuesta = null;
        String tipoMensajeRespuesta = "success";

        try {
            mensajeRespuesta = pedidoServicio.crearPedido(pedidoDto);
            Utilidades.escribirLog(session, "[INFO]", "PedidoControlador", "doPost", "Respuesta del servicio: " + mensajeRespuesta);
            if (!"Pedido creado correctamente".equals(mensajeRespuesta)) {
                tipoMensajeRespuesta = "error";
            } else {
                // Llamar al servicio para limpiar el carrito en el backend
                carritoServicio.limpiarCarrito();
                // Eliminar el atributo "carrito" de la sesión para que se obtenga nuevamente al solicitarlo
                session.removeAttribute("carrito");
            }
        } catch (Exception e) {
            tipoMensajeRespuesta = "error";
            mensajeRespuesta = "Error al crear el pedido: " + e.getMessage();
            e.printStackTrace();
            Utilidades.escribirLog(session, "[ERROR]", "PedidoControlador", "doPost", "Error al crear el pedido: " + e.getMessage());
        }

        session.setAttribute("mensaje", mensajeRespuesta);
        session.setAttribute("tipoMensaje", tipoMensajeRespuesta);
        if ("Pedido creado correctamente".equals(mensajeRespuesta)) {
            response.sendRedirect("carrito.jsp?pedidoExitoso=true");
        } else {
            response.sendRedirect("carrito.jsp");
        }
    }

    private boolean validarNumeroTarjeta(String numeroTarjeta) {
        return numeroTarjeta != null && numeroTarjeta.matches("\\d{13,19}");
    }

    private boolean validarCvc(String cvc) {
        return cvc != null && cvc.matches("\\d{3,4}");
    }

    private String encriptarDatos(String datos) {
        return new String(java.util.Base64.getEncoder().encode(datos.getBytes()));
    }
}
