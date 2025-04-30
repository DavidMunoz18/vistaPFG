/**
 * Servlet encargado de gestionar la eliminación de una marca.
 * <p>
 * Recibe peticiones POST con el identificador de la marca a eliminar,
 * delega la operación al servicio de marca y redirige al administrador
 * indicando éxito o error.</p>
 */
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
    /**
     * UID de versión para serialización.
     */
    private static final long serialVersionUID = 1L;

    /**
     * Servicio que gestiona la lógica de negocio relacionada con marcas.
     */
    private MarcaServicio marcaServicio;
    
    /**
     * Inicializa el servlet creando una instancia de MarcaServicio.
     *
     * @throws ServletException si ocurre un error durante la inicialización
     */
    @Override
    public void init() throws ServletException {
        marcaServicio = new MarcaServicio();
    }
    
    /**
     * Procesa la petición POST para eliminar una marca.
     * <p>
     * Obtiene el parámetro "id" de la solicitud, valida su formato,
     * invoca el servicio para eliminar la marca y redirige a la vista de administración
     * indicando si la operación fue exitosa o no.</p>
     *
     * @param request  objeto HttpServletRequest con los datos de la petición
     * @param response objeto HttpServletResponse para enviar la respuesta
     * @throws ServletException si ocurre un error en el servlet
     * @throws IOException      si ocurre un error de E/S durante la redirección
     */
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