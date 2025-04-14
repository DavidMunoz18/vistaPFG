package controladores;

import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/cerrarSesion")
public class CerrarSesionControlador extends HttpServlet {

    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Invalida la sesión actual
        request.getSession().invalidate();
        // Redirige al usuario a la página de inicio (ajusta la URL según lo necesites)
        response.sendRedirect(request.getContextPath() + "/inicio");
    }
}
