package servicios;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDate;

import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule; // Importar este módulo

import dtos.PedidoDto;
import utilidades.Utilidades; // Asegúrate de que este sea el paquete correcto de la utilidad

/**
 * Servicio para gestionar pedidos en el sistema.
 * Incorpora la lógica de negocio (cálculos, validaciones, asignación de valores por defecto)
 * y se encarga de comunicarse con la API de persistencia.
 */
@Service
public class PedidoServicio {

    // No se utiliza la sesión en este caso, ya que se pasa null para que se imprima por pantalla
    // según la lógica implementada en la utilidad.
    // Si ya manejas el log en los controladores, podrías prescindir de estos logs aquí, 
    // aunque tener trazabilidad en los servicios puede ser útil para el diagnóstico.
    
    /**
     * URL de la API donde se persisten los pedidos.
     */
    private static final String API_URL = "https://tomcat.dmunoz.es/ApiEcommerceOrdenadores-0.0.1/api/pedidos";  // Asegúrate de usar la URL correcta

    // Instancia de ObjectMapper
    private final ObjectMapper objectMapper;

    public PedidoServicio() {
        // Inicializar el ObjectMapper y registrar el módulo JavaTimeModule
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule()); // Registra el módulo para LocalDate
    }

    /**
     * Crea un nuevo pedido aplicando la lógica de negocio (por ejemplo, cálculo del total)
     * y luego envía el objeto a la API.
     *
     * @param pedidoDto Objeto con los datos del pedido (sin el total calculado y otros valores por defecto)
     * @return Mensaje indicando el resultado de la operación.
     * @throws Exception Si ocurre un error durante la comunicación con la API.
     */
    public String crearPedido(PedidoDto pedidoDto) throws Exception {
        // Log [INFO] al inicio del método
        Utilidades.escribirLog(null, "[INFO]", "PedidoServicio", "crearPedido", "Inicio de creación de pedido");

        // Lógica de negocio: asignar fecha y estado, y calcular el total.
        pedidoDto.setFechaPedido(LocalDate.now());
        pedidoDto.setEstado("PENDIENTE");
        double totalCalculado = calcularTotalPedido(pedidoDto);
        pedidoDto.setTotal(totalCalculado);

        HttpURLConnection connection = null;
        try {
            URL url = new URL(API_URL);
            connection = (HttpURLConnection) url.openConnection();

            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setDoOutput(true);

            String jsonInputString = objectMapper.writeValueAsString(pedidoDto);

           
            System.out.println("JSON enviado: " + jsonInputString);

            try (OutputStream os = connection.getOutputStream()) {
                byte[] input = jsonInputString.getBytes("utf-8");
                os.write(input, 0, input.length);
            }

            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_CREATED) {
                // Log [INFO] de operación exitosa
                Utilidades.escribirLog(null, "[INFO]", "PedidoServicio", "crearPedido", "Pedido creado correctamente");
                return "Pedido creado correctamente";
            } else {
                InputStream errorStream = connection.getErrorStream();
                StringBuilder response = new StringBuilder();
                try (BufferedReader in = new BufferedReader(new InputStreamReader(errorStream))) {
                    String line;
                    while ((line = in.readLine()) != null) {
                        response.append(line);
                    }
                }
                String errorMsg = "Error al crear el pedido, código de respuesta: " + responseCode + ", detalle: " + response.toString();
                // Log [ERROR] en caso de error en la respuesta
                Utilidades.escribirLog(null, "[ERROR]", "PedidoServicio", "crearPedido", errorMsg);
                return errorMsg;
            }

        } catch (Exception e) {
            // Log [ERROR] en caso de excepción
            Utilidades.escribirLog(null, "[ERROR]", "PedidoServicio", "crearPedido", "Error al enviar la solicitud al servidor: " + e.getMessage());
            throw new Exception("Error al enviar la solicitud al servidor: " + e.getMessage(), e);
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
    }

    /**
     * Calcula el total del pedido sumando (cantidad * precio) de cada producto.
     *
     * @param pedidoDto Objeto que contiene la lista de productos.
     * @return Total calculado del pedido.
     */
    private double calcularTotalPedido(PedidoDto pedidoDto) {
        return pedidoDto.getProductos().stream()
                .mapToDouble(prod -> prod.getCantidad() * prod.getPrecio())
                .sum();
    }
}
