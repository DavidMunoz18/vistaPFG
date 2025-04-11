package controladores;

import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import servicios.MarcaServicio;
import utilidades.Utilidades;

@WebServlet("/eliminarMarca")
public class EliminarMarcaControlador extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private MarcaServicio marcaServicio;
    
    @Override
    public void init() throws ServletException {
        marcaServicio = new MarcaServicio();
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        Utilidades.escribirLog(session, "[INFO]", "EliminarMarcaControlador", "doPost", "Iniciando eliminación de marca");
        
        String idStr = request.getParameter("id");
        if (idStr == null || idStr.trim().isEmpty()) {
            Utilidades.escribirLog(session, "[ERROR]", "EliminarMarcaControlador", "doPost", "Falta el ID de la marca");
            response.sendRedirect("admin?marcaEliminada=false&error=Falta el ID");
            return;
        }
        
        try {
            Long marcaId = Long.parseLong(idStr);
            boolean exito = marcaServicio.eliminarMarca(marcaId);
            
            if (exito) {
                Utilidades.escribirLog(session, "[INFO]", "EliminarMarcaControlador", "doPost", "Marca eliminada exitosamente con ID: " + marcaId);
                response.sendRedirect("admin?marcaEliminada=true");
            } else {
                Utilidades.escribirLog(session, "[ERROR]", "EliminarMarcaControlador", "doPost", "No se pudo eliminar la marca con ID: " + marcaId);
                response.sendRedirect("admin?marcaEliminada=false");
            }
        } catch (NumberFormatException e) {
            Utilidades.escribirLog(session, "[ERROR]", "EliminarMarcaControlador", "doPost", "Formato inválido para el ID");
            response.sendRedirect("admin?marcaEliminada=false&error=Formato inválido para el ID");
        }
    }
}
