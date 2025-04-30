/**
 * Servlet encargado de gestionar la modificación de una marca.
 * <p>
 * Recibe peticiones POST con los datos de la marca a actualizar,
 * valida los parámetros, invoca el servicio de marca y redirige
 * al administrador indicando si la operación fue exitosa o hubo errores.</p>
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

/**
 * Mapea las solicitudes a /modificarMarcas para actualizar los datos de una marca.
 */
@WebServlet("/modificarMarcas")
public class ModificarMarcasControlador extends HttpServlet {
    /**
     * UID de versión para serialización.
     */
    private static final long serialVersionUID = 1L;

    /**
     * Servicio que contiene la lógica de negocio para operaciones sobre marcas.
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
     * Procesa la petición POST para modificar una marca.
     * <p>
     * Obtiene los parámetros del formulario (id, nombre, país de origen,
     * año de fundación y descripción), valida que no estén vacíos,
     * convierte los valores numéricos y llama al servicio para aplicar los cambios.
     * Finalmente, redirige a la vista de administración informando éxito o fallo.</p>
     *
     * @param request  objeto HttpServletRequest con los datos de la petición
     * @param response objeto HttpServletResponse para enviar la respuesta
     * @throws ServletException si ocurre un error en el servlet
     * @throws IOException      si ocurre un error de E/S durante la redirección
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Obtener la sesión para log y control de permisos
        HttpSession session = request.getSession();
        Utilidades.escribirLog(session, "[INFO]", "ModificarMarcasControlador", "doPost",
                "Inicio del proceso de modificación de marca");

        // Parámetros del formulario
        String idStr = request.getParameter("id");
        String nombre = request.getParameter("nombre");
        String paisOrigen = request.getParameter("paisOrigen");
        String anioFundacionStr = request.getParameter("anioFundacion");
        String descripcion = request.getParameter("descripcion");

        // Validación de parámetros obligatorios
        if (idStr == null || idStr.isEmpty() ||
            nombre == null || nombre.isEmpty() ||
            paisOrigen == null || paisOrigen.isEmpty() ||
            anioFundacionStr == null || anioFundacionStr.isEmpty() ||
            descripcion == null || descripcion.isEmpty()) {
            Utilidades.escribirLog(session, "[ERROR]", "ModificarMarcasControlador", "doPost",
                    "Faltan parámetros obligatorios");
            response.sendRedirect("admin?marcaModificada=false&error=Faltan campos obligatorios");
            return;
        }

        try {
            Long id = Long.parseLong(idStr);
            int anioFundacion = Integer.parseInt(anioFundacionStr);

            // Invoca el servicio para modificar la marca
            boolean exito = marcaServicio.modificarMarca(id, nombre, paisOrigen, anioFundacion, descripcion);

            if (exito) {
                Utilidades.escribirLog(session, "[INFO]", "ModificarMarcasControlador", "doPost",
                        "Marca modificada exitosamente con ID: " + id);
                response.sendRedirect("admin?marcaModificada=true");
            } else {
                Utilidades.escribirLog(session, "[ERROR]", "ModificarMarcasControlador", "doPost",
                        "Fallo al modificar la marca con ID: " + id);
                response.sendRedirect("admin?marcaModificada=false");
            }
        } catch (NumberFormatException e) {
            Utilidades.escribirLog(session, "[ERROR]", "ModificarMarcasControlador", "doPost",
                    "Error en la conversión de ID o Año de Fundación: " + e.getMessage());
            e.printStackTrace();
            response.sendRedirect("admin?marcaModificada=false&error=Formato numérico inválido");
        } catch (Exception e) {
            Utilidades.escribirLog(session, "[ERROR]", "ModificarMarcasControlador", "doPost",
                    "Excepción inesperada: " + e.getMessage());
            e.printStackTrace();
            response.sendRedirect("admin?marcaModificada=false&error=Error inesperado");
        }
    }
}