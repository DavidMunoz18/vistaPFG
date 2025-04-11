package controladores;

import java.io.IOException;
import java.util.List;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import servicios.MarcaServicio;
import dtos.MarcaDto;
import utilidades.Utilidades;

/**
 * Servlet encargado de manejar la adición de nuevas marcas al sistema y de mostrar la lista de marcas.
 * Recibe los datos del formulario de marcas y los envía al servicio correspondiente.
 */
@WebServlet("/marcasAniadir")
@MultipartConfig
public class AniadirMarcasControlador extends HttpServlet {

    private MarcaServicio marcaServicio;

    @Override
    public void init() throws ServletException {
        // Asume que tienes un servicio MarcaServicio para manejar la lógica
        marcaServicio = new MarcaServicio();
    }

    /**
     * Método GET para obtener la lista de marcas y enviarlas al JSP.
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        List<MarcaDto> marcas = marcaServicio.obtenerMarcas();
        request.setAttribute("marcas", marcas);
        // Se asume que la página "admin.jsp" es la vista que contiene el formulario para agregar productos y marcas.
        request.getRequestDispatcher("menuAdministrador.jsp").forward(request, response);
    }

    /**
     * Método POST para agregar una nueva marca.
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        Utilidades.escribirLog(session, "[INFO]", "AniadirMarcasControlador", "doPost", "Inicio del proceso de agregar marca");

        try {
            // Obtener datos del formulario de marca
            String nombre = request.getParameter("nombreMarca");
            String paisOrigen = request.getParameter("paisOrigen");
            String anioFundacionStr = request.getParameter("anioFundacion");
            String descripcion = request.getParameter("descripcion");

            // Validar parámetros obligatorios
            if (nombre == null || paisOrigen == null || anioFundacionStr == null || descripcion == null ||
                nombre.isEmpty() || paisOrigen.isEmpty() || anioFundacionStr.isEmpty() || descripcion.isEmpty()) {
                response.sendRedirect("menuAdministrador.jsp?error=Todos los campos son obligatorios");
                return;
            }

            // Convertir valores
            int anioFundacion = Integer.parseInt(anioFundacionStr);

            // Construir el DTO de Marca (la marca se guarda en el DTO)
            MarcaDto marca = new MarcaDto(null, nombre, paisOrigen, anioFundacion, descripcion);

            // Llamar al servicio para agregar la marca
            boolean exito = marcaServicio.agregarMarca(marca);

            if (exito) {
                Utilidades.escribirLog(session, "[INFO]", "AniadirMarcasControlador", "doPost", "Marca agregada exitosamente.");
                response.sendRedirect("menuAdministrador.jsp?marcaAgregada=true");
            } else {
                Utilidades.escribirLog(session, "[INFO]", "AniadirMarcasControlador", "doPost", "Fallo al agregar marca.");
                response.sendRedirect("menuAdministrador.jsp?marcaAgregada=false");
            }
        } catch (NumberFormatException e) {
            Utilidades.escribirLog(session, "[ERROR]", "AniadirMarcasControlador", "doPost", "Error al convertir año de fundación: " + e.getMessage());
            e.printStackTrace();
            response.sendRedirect("menuAdministrador.jsp?marcaAgregada=false");
        } catch (Exception e) {
            Utilidades.escribirLog(session, "[ERROR]", "AniadirMarcasControlador", "doPost", "Error general: " + e.getMessage());
            e.printStackTrace();
            response.sendRedirect("menuAdministrador.jsp?marcaAgregada=false");
        }
    }
}
