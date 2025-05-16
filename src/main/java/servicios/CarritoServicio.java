package servicios;

import dtos.CarritoDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import jakarta.servlet.http.HttpSession;
import utilidades.Utilidades;

/**
 * Servicio encargado de manejar las operaciones relacionadas con el carrito de compras.
 * Permite obtener los productos del carrito, agregar productos, eliminar productos y 
 * obtener detalles de un producto por su ID.
 */
public class CarritoServicio {

    private static final String API_URL = "https://tomcat.dmunoz.es/ApiEcommerceOrdenadores-0.0.1/api/carrito";

    /**
     * Obtiene todos los productos del carrito desde sesión si existen,
     * si no, los obtiene de la API y los guarda en sesión.
     *
     * @param session Sesión actual del usuario.
     * @return Lista de productos en el carrito.
     */
    public List<CarritoDto> obtenerCarrito(HttpSession session) {
        @SuppressWarnings("unchecked")
        List<CarritoDto> carrito = (List<CarritoDto>) session.getAttribute("carrito");
        if (carrito != null) return carrito;

        carrito = obtenerCarrito();
        session.setAttribute("carrito", carrito);
        return carrito;
    }

    /**
     * Obtiene todos los productos que están actualmente en el carrito.
     * Realiza una petición HTTP GET a la API y mapea la respuesta a una lista de CarritoDto.
     *
     * @return Lista de productos en el carrito.
     */
    public List<CarritoDto> obtenerCarrito() {
        List<CarritoDto> carrito = new ArrayList<>();
        Utilidades.escribirLog(null, "[INFO]", "CarritoServicio", "obtenerCarrito", "Inicio de obtención del carrito");
        try {
            URL url = new URL(API_URL);
            HttpURLConnection conexion = (HttpURLConnection) url.openConnection();
            conexion.setRequestMethod("GET");
            conexion.setRequestProperty("Content-Type", "application/json");

            int responseCode = conexion.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                BufferedReader in = new BufferedReader(new InputStreamReader(conexion.getInputStream()));
                StringBuilder response = new StringBuilder();
                String inputLine;
                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();

                ObjectMapper mapper = new ObjectMapper();
                CarritoDto[] carritoArray = mapper.readValue(response.toString(), CarritoDto[].class);
                for (CarritoDto producto : carritoArray) {
                    carrito.add(producto);
                    System.out.println("Producto en el carrito: ID = " + producto.getId() + ", Nombre: " + producto.getNombre());
                }
                Utilidades.escribirLog(null, "[INFO]", "CarritoServicio", "obtenerCarrito", "Se obtuvieron " + carrito.size() + " productos del carrito");
            } else {
                Utilidades.escribirLog(null, "[ERROR]", "CarritoServicio", "obtenerCarrito", "Código de respuesta no OK: " + responseCode);
            }
        } catch (Exception e) {
            e.printStackTrace();
            Utilidades.escribirLog(null, "[ERROR]", "CarritoServicio", "obtenerCarrito", "Excepción: " + e.getMessage());
        }
        return carrito;
    }

    /**
     * Obtiene un producto del carrito dado su ID.
     * Realiza una petición HTTP GET a la API para obtener los detalles del producto.
     *
     * @param id El ID del producto a buscar.
     * @return El producto encontrado o null si no se encuentra.
     */
    public CarritoDto obtenerProductoPorId(long id) {
        Utilidades.escribirLog(null, "[INFO]", "CarritoServicio", "obtenerProductoPorId", "Inicio de búsqueda del producto con ID: " + id);
        try {
            System.out.println("Buscando producto con ID: " + id);

            URL url = new URL("https://tomcat.dmunoz.es/ApiEcommerceOrdenadores-0.0.1/api/productos/" + id);
            HttpURLConnection conexion = (HttpURLConnection) url.openConnection();
            conexion.setRequestMethod("GET");
            conexion.setRequestProperty("Content-Type", "application/json");

            BufferedReader in = new BufferedReader(new InputStreamReader(conexion.getInputStream()));
            StringBuilder response = new StringBuilder();
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            System.out.println("Respuesta de la API: " + response.toString());
            ObjectMapper mapper = new ObjectMapper();
            CarritoDto producto = mapper.readValue(response.toString(), CarritoDto.class);

            if (producto != null) {
                System.out.println("Producto encontrado: ID = " + producto.getId() + ", Nombre: " + producto.getNombre());
                Utilidades.escribirLog(null, "[INFO]", "CarritoServicio", "obtenerProductoPorId", "Producto encontrado: ID = " + producto.getId() + ", Nombre: " + producto.getNombre());
            } else {
                System.out.println("No se encontró el producto con ID: " + id);
                Utilidades.escribirLog(null, "[ERROR]", "CarritoServicio", "obtenerProductoPorId", "No se encontró el producto con ID: " + id);
            }

            return producto;

        } catch (Exception e) {
            e.printStackTrace();
            Utilidades.escribirLog(null, "[ERROR]", "CarritoServicio", "obtenerProductoPorId", "Excepción: " + e.getMessage());
            return null;
        }
    }

    /**
     * Agrega un producto al carrito haciendo una petición HTTP POST a la API.
     *
     * @param producto El producto a agregar.
     * @return true si se agregó correctamente, false en caso contrario.
     */
    public boolean agregarProducto(CarritoDto producto) {
        Utilidades.escribirLog(null, "[INFO]", "CarritoServicio", "agregarProducto", "Inicio de agregar producto: ID = " + producto.getId() + ", Nombre: " + producto.getNombre());
        try {
            System.out.println("Producto a agregar: ID = " + producto.getId() + ", Nombre: " + producto.getNombre());

            URL url = new URL(API_URL + "/agregar");
            HttpURLConnection conexion = (HttpURLConnection) url.openConnection();
            conexion.setRequestMethod("POST");
            conexion.setRequestProperty("Content-Type", "application/json");
            conexion.setDoOutput(true);

            ObjectMapper mapper = new ObjectMapper();
            String json = mapper.writeValueAsString(producto);

            System.out.println("JSON a enviar al carrito: " + json);
            conexion.getOutputStream().write(json.getBytes());

            int responseCode = conexion.getResponseCode();

            if (responseCode == HttpURLConnection.HTTP_OK) {
                System.out.println("Producto agregado correctamente al carrito.");
                Utilidades.escribirLog(null, "[INFO]", "CarritoServicio", "agregarProducto", "Producto agregado correctamente al carrito.");
            } else {
                System.out.println("Error al agregar producto al carrito, código de respuesta: " + responseCode);
                Utilidades.escribirLog(null, "[ERROR]", "CarritoServicio", "agregarProducto", "Error al agregar producto al carrito, código de respuesta: " + responseCode);
            }
            return responseCode == HttpURLConnection.HTTP_OK;
        } catch (Exception e) {
            e.printStackTrace();
            Utilidades.escribirLog(null, "[ERROR]", "CarritoServicio", "agregarProducto", "Excepción: " + e.getMessage());
        }
        return false;
    }

    /**
     * Agrega un producto al carrito y lo guarda también en la sesión del usuario.
     *
     * @param session Sesión actual del usuario.
     * @param producto El producto a agregar.
     * @return true si se agregó correctamente, false en caso contrario.
     */
    public boolean agregarProducto(HttpSession session, CarritoDto producto) {
        boolean ok = agregarProducto(producto);
        if (ok) {
            @SuppressWarnings("unchecked")
            List<CarritoDto> carrito = (List<CarritoDto>) session.getAttribute("carrito");
            if (carrito == null) carrito = new ArrayList<>();
            boolean existe = false;
            for (CarritoDto p : carrito) {
                if (p.getId() == producto.getId()) {
                    p.setCantidad(p.getCantidad() + producto.getCantidad());
                    existe = true;
                    break;
                }
            }
            if (!existe) carrito.add(producto);
            session.setAttribute("carrito", carrito);
        }
        return ok;
    }

    /**
     * Elimina un producto del carrito mediante petición HTTP DELETE.
     *
     * @param id ID del producto a eliminar.
     * @return true si se eliminó correctamente, false en caso contrario.
     */
    public boolean eliminarProducto(long id) {
        Utilidades.escribirLog(null, "[INFO]", "CarritoServicio", "eliminarProducto", "Inicio de eliminación del producto con ID: " + id);
        try {
            URL url = new URL(API_URL + "/eliminar/" + id);
            HttpURLConnection conexion = (HttpURLConnection) url.openConnection();
            conexion.setRequestMethod("DELETE");
            conexion.setRequestProperty("Content-Type", "application/json");

            int responseCode = conexion.getResponseCode();

            if (responseCode == HttpURLConnection.HTTP_OK) {
                System.out.println("Producto eliminado correctamente del carrito.");
                Utilidades.escribirLog(null, "[INFO]", "CarritoServicio", "eliminarProducto", "Producto eliminado correctamente del carrito.");
            } else {
                System.out.println("Error al eliminar producto del carrito, código de respuesta: " + responseCode);
                Utilidades.escribirLog(null, "[ERROR]", "CarritoServicio", "eliminarProducto", "Error al eliminar producto del carrito, código de respuesta: " + responseCode);
            }
            return responseCode == HttpURLConnection.HTTP_OK;
        } catch (Exception e) {
            e.printStackTrace();
            Utilidades.escribirLog(null, "[ERROR]", "CarritoServicio", "eliminarProducto", "Excepción: " + e.getMessage());
        }
        return false;
    }

    /**
     * Elimina un producto del carrito remoto y luego actualiza sesión.
     *
     * @param session Sesión HTTP del usuario.
     * @param id       ID del producto a eliminar.
     * @return {@code true} siempre que la sesión se haya actualizado.
     */
    public boolean eliminarProducto(HttpSession session, long id) {
        boolean okRemoto = eliminarProducto(id);  // tu llamada remota
        @SuppressWarnings("unchecked")
        List<CarritoDto> carrito = (List<CarritoDto>) session.getAttribute("carrito");
        if (carrito != null) {
            carrito.removeIf(p -> p.getId() == id);
            session.setAttribute("carrito", carrito);
        }
        return true;  // da siempre éxito para no bloquear la vista
    }


    /**
     * Limpia (elimina TODOS los productos) del carrito.
     * Realiza una petición HTTP DELETE a la API en la ruta /limpiar para vaciar el carrito.
     *
     * @return true si el carrito se limpió correctamente, false en caso contrario.
     */
    public boolean limpiarCarrito() {
        try {
            URL url = new URL(API_URL + "/limpiar");
            HttpURLConnection conexion = (HttpURLConnection) url.openConnection();
            conexion.setRequestMethod("DELETE");
            conexion.setRequestProperty("Content-Type", "application/json");

            int responseCode = conexion.getResponseCode();
            return responseCode == HttpURLConnection.HTTP_OK;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Limpia el carrito tanto en la API como en la sesión del usuario.
     *
     * @param session Sesión actual del usuario.
     * @return true si el carrito se limpió correctamente, false en caso contrario.
     */
    public boolean limpiarCarrito(HttpSession session) {
        boolean ok = limpiarCarrito();
        if (ok) session.setAttribute("carrito", new ArrayList<CarritoDto>());
        return ok;
    }
}
