package controladores;

import java.io.IOException;
import java.util.List;

import dtos.MarcaDto;
import dtos.ProductoDto;
import dtos.UsuarioDto;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import servicios.AutentificacionServicio;
import servicios.MarcaServicio;
import servicios.ProductoServicio;
import utilidades.Utilidades; 

/**
 * Controlador para manejar el menú principal del administrador.
 * <p>
 * Este servlet procesa solicitudes HTTP GET para mostrar el menú del administrador,
 * verificando que el usuario esté autenticado y tenga el rol adecuado de "admin".
 * Recupera las listas de productos, usuarios y marcas, y las envía a la vista para su visualización.
 * </p>
 */
@WebServlet("/admin")
public class MenuAdministradorControlador extends HttpServlet {
    private static final long serialVersionUID = 1L;

    /**
     * Maneja las solicitudes HTTP GET para mostrar el menú del administrador.
     * Verifica que el usuario esté autenticado y tenga rol de administrador,
     * y luego recupera y envía las listas de productos, usuarios y marcas a la vista.
     * 
     * @param solicitud La solicitud HTTP recibida.
     * @param respuesta La respuesta HTTP que será enviada al cliente.
     * @throws ServletException Si ocurre un error durante el procesamiento de la solicitud.
     * @throws IOException Si ocurre un error de entrada/salida durante el procesamiento.
     */
    @Override
    protected void doGet(HttpServletRequest solicitud, HttpServletResponse respuesta) throws ServletException, IOException {
        // Verificar que el usuario esté autenticado y tenga rol de administrador
        HttpSession sesion = solicitud.getSession(false);
        
        if (sesion == null || sesion.getAttribute("rol") == null || 
                !sesion.getAttribute("rol").toString().equalsIgnoreCase("admin")) {
            // Si no está autenticado o no tiene rol "admin", redirigir al login
            Utilidades.escribirLog(sesion, "[ERROR]", "MenuAdministradorControlador", "doGet", 
                    "Acceso denegado: Usuario no autenticado o sin rol de administrador.");
            respuesta.sendRedirect(solicitud.getContextPath() + "/login.jsp");
            return;
        }

        // Obtener listas de productos, usuarios y marcas desde los servicios correspondientes
        ProductoServicio servicioProducto = new ProductoServicio();
        AutentificacionServicio servicioUsuario = new AutentificacionServicio();
        MarcaServicio servicioMarca = new MarcaServicio();

        List<ProductoDto> listaProductos = servicioProducto.obtenerProductos();
        List<UsuarioDto> listaUsuarios = servicioUsuario.obtenerUsuarios();
        List<MarcaDto> listaMarcas = servicioMarca.obtenerMarcas();

        // Log de las listas obtenidas
        Utilidades.escribirLog(sesion, "[INFO]", "MenuAdministradorControlador", "doGet", 
                "Tamaño de la lista de productos: " + (listaProductos != null ? listaProductos.size() : "null"));
        Utilidades.escribirLog(sesion, "[INFO]", "MenuAdministradorControlador", "doGet", 
                "Tamaño de la lista de usuarios: " + (listaUsuarios != null ? listaUsuarios.size() : "null"));
        Utilidades.escribirLog(sesion, "[INFO]", "MenuAdministradorControlador", "doGet", 
                "Tamaño de la lista de marcas: " + (listaMarcas != null ? listaMarcas.size() : "null"));

        // Imprimir en consola para depuración
        System.out.println("Número de productos obtenidos: " + (listaProductos != null ? listaProductos.size() : "null"));
        System.out.println("Número de usuarios obtenidos: " + (listaUsuarios != null ? listaUsuarios.size() : "null"));
        System.out.println("Número de marcas obtenidas: " + (listaMarcas != null ? listaMarcas.size() : "null"));

        if (listaMarcas != null) {
            for (MarcaDto marca : listaMarcas) {
                System.out.println("Marca obtenida: ID=" + marca.getId() + ", Nombre=" + marca.getNombre() + 
                                   ", País=" + marca.getPaisOrigen() + ", Año=" + marca.getAnioFundacion());
            }
        }

        // Enviar las listas de productos, usuarios y marcas al JSP
        solicitud.setAttribute("productos", listaProductos);
        solicitud.setAttribute("usuarios", listaUsuarios);
        solicitud.setAttribute("marcas", listaMarcas);

        // Redirigir a la vista del menú de administrador
        solicitud.getRequestDispatcher("/menuAdministrador.jsp").forward(solicitud, respuesta); 
    }
}
