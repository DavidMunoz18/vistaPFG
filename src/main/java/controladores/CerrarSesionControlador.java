package controladores;

import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Servlet encargado de cerrar la sesión del usuario.
 * Invalida la sesión actual y redirige al usuario a la página de inicio.
 */
@WebServlet("/cerrarSesion")
public class CerrarSesionControlador extends HttpServlet {

    private static final long serialVersionUID = 1L;

    /**
     * Maneja las peticiones GET para cerrar la sesión del usuario.
     * Invalida la sesión y redirige al inicio.
     *
     * @param request  Petición HTTP recibida
     * @param response Respuesta HTTP enviada
     * @throws ServletException Si ocurre un error relacionado con el servlet
     * @throws IOException      Si ocurre un error de entrada/salida
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getSession().invalidate();
        response.sendRedirect(request.getContextPath() + "/inicio");
    }
}
