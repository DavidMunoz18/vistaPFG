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

@WebServlet("/modificarMarcas")
public class ModificarMarcasControlador extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private MarcaServicio marcaServicio;

    @Override
    public void init() throws ServletException {
        marcaServicio = new MarcaServicio();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Obtener la sesión para loggear y verificar permisos
        HttpSession session = request.getSession();
        Utilidades.escribirLog(session, "[INFO]", "ModificarMarcasControlador", "doPost", "Inicio del proceso de modificación de marca");

        // Obtener parámetros del formulario
        String idStr = request.getParameter("id");
        String nombre = request.getParameter("nombre");
        String paisOrigen = request.getParameter("paisOrigen");
        String anioFundacionStr = request.getParameter("anioFundacion");
        String descripcion = request.getParameter("descripcion");

        // Validar parámetros obligatorios
        if (idStr == null || idStr.isEmpty() ||
            nombre == null || nombre.isEmpty() ||
            paisOrigen == null || paisOrigen.isEmpty() ||
            anioFundacionStr == null || anioFundacionStr.isEmpty() ||
            descripcion == null || descripcion.isEmpty()) {
            Utilidades.escribirLog(session, "[ERROR]", "ModificarMarcasControlador", "doPost", "Faltan parámetros obligatorios");
            response.sendRedirect("admin?marcaModificada=false&error=Faltan campos obligatorios");
            return;
        }

        try {
            Long id = Long.parseLong(idStr);
            int anioFundacion = Integer.parseInt(anioFundacionStr);

            // Llamar al servicio para modificar la marca con todos los campos necesarios
            boolean exito = marcaServicio.modificarMarca(id, nombre, paisOrigen, anioFundacion, descripcion);

            if (exito) {
                Utilidades.escribirLog(session, "[INFO]", "ModificarMarcasControlador", "doPost", "Marca modificada exitosamente con ID: " + id);
                response.sendRedirect("admin?marcaModificada=true");
            } else {
                Utilidades.escribirLog(session, "[ERROR]", "ModificarMarcasControlador", "doPost", "Fallo al modificar la marca con ID: " + id);
                response.sendRedirect("admin?marcaModificada=false");
            }
        } catch (NumberFormatException e) {
            Utilidades.escribirLog(session, "[ERROR]", "ModificarMarcasControlador", "doPost", "Error en la conversión de ID o Año de Fundación: " + e.getMessage());
            e.printStackTrace();
            response.sendRedirect("admin?marcaModificada=false&error=Formato numérico inválido");
        } catch (Exception e) {
            Utilidades.escribirLog(session, "[ERROR]", "ModificarMarcasControlador", "doPost", "Excepción: " + e.getMessage());
            e.printStackTrace();
            response.sendRedirect("admin?marcaModificada=false&error=Error inesperado");
        }
    }
}
