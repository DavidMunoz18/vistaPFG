package servicios;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import dtos.MarcaDto;
import utilidades.Utilidades;

import java.util.ArrayList;
import java.util.List;

/**
 * Servicio que contiene la lógica de negocio para la gestión de marcas.
 * Se conecta a la API y aplica validaciones, comprobaciones y logging antes
 * y después de invocar los endpoints.
 */
public class MarcaServicio {

    private static final String API_BASE_URL = "https://tomcat.dmunoz.es/ApiEcommerceOrdenadores-0.0.1/api/";

    /**
     * Obtiene la lista completa de marcas desde la API.
     */
    public List<MarcaDto> obtenerMarcas() {
        Utilidades.escribirLog(null, "[INFO]", "MarcaServicio", "obtenerMarcas", "Iniciando obtención de marcas...");
        System.out.println("Iniciando obtención de marcas en MarcaServicio...");
        List<MarcaDto> marcas = new ArrayList<>();
        try {
            URL url = new URL(API_BASE_URL + "marcas");
            HttpURLConnection conexion = (HttpURLConnection) url.openConnection();
            conexion.setRequestMethod("GET");
            conexion.setRequestProperty("Content-Type", "application/json");

            int responseCode = conexion.getResponseCode();
            System.out.println("Código de respuesta de la API: " + responseCode);
            if (responseCode == HttpURLConnection.HTTP_OK) {
                try (BufferedReader in = new BufferedReader(new InputStreamReader(conexion.getInputStream()))) {
                    StringBuilder response = new StringBuilder();
                    String inputLine;
                    while ((inputLine = in.readLine()) != null) {
                        response.append(inputLine);
                    }
                    // Imprimir la respuesta completa en consola
                    System.out.println("Respuesta de la API: " + response.toString());
                    Utilidades.escribirLog(null, "[INFO]", "MarcaServicio", "obtenerMarcas", "Respuesta de la API: " + response.toString());

                    // Mapear el JSON a un arreglo de MarcaDto
                    ObjectMapper mapper = new ObjectMapper();
                    MarcaDto[] marcasArray = mapper.readValue(response.toString(), MarcaDto[].class);
                    
                    if (marcasArray == null || marcasArray.length == 0) {
                        Utilidades.escribirLog(null, "[INFO]", "MarcaServicio", "obtenerMarcas", "No se encontraron marcas en la API.");
                        System.out.println("No se encontraron marcas en la API.");
                    } else {
                        for (MarcaDto marca : marcasArray) {
                            if (marca.getNombre() == null || marca.getNombre().isBlank()) {
                                Utilidades.escribirLog(null, "[WARN]", "MarcaServicio", "obtenerMarcas", "Marca sin nombre encontrada.");
                                System.out.println("Marca sin nombre encontrada: " + marca);
                            } else {
                                Utilidades.escribirLog(null, "[INFO]", "MarcaServicio", "obtenerMarcas", "Marca obtenida: " + marca.getNombre());
                                System.out.println("Marca obtenida: " + marca.getNombre());
                            }
                            marcas.add(marca);
                        }
                    }
                }
            } else {
                Utilidades.escribirLog(null, "[ERROR]", "MarcaServicio", "obtenerMarcas", "Código de respuesta: " + responseCode);
                System.out.println("Código de respuesta de la API: " + responseCode);
            }
        } catch (Exception e) {
            Utilidades.escribirLog(null, "[ERROR]", "MarcaServicio", "obtenerMarcas", "Excepción: " + e);
            System.out.println("Excepción en MarcaServicio.obtenerMarcas: " + e);
            e.printStackTrace();
        }
        System.out.println("Total de marcas obtenidas: " + marcas.size());
        return marcas;
    }


    /**
     * Agrega una nueva marca a través de la API tras validaciones.
     */
    public boolean agregarMarca(MarcaDto marca) {
        if (marca == null || marca.getNombre() == null || marca.getNombre().isBlank()) {
            Utilidades.escribirLog(null, "[ERROR]", "MarcaServicio", "agregarMarca", "Marca inválida: se requiere un nombre.");
            return false;
        }
        Utilidades.escribirLog(null, "[INFO]", "MarcaServicio", "agregarMarca", "Agregando marca: " + marca.getNombre());
        boolean resultado = false;
        try {
            URL url = new URL(API_BASE_URL + "marcas");
            HttpURLConnection conexion = (HttpURLConnection) url.openConnection();
            conexion.setRequestMethod("POST");
            conexion.setRequestProperty("Content-Type", "application/json");
            conexion.setDoOutput(true);
            ObjectWriter writer = new ObjectMapper().writer().withDefaultPrettyPrinter();
            String jsonMarca = writer.writeValueAsString(marca);
            try (OutputStream os = conexion.getOutputStream()) {
                os.write(jsonMarca.getBytes(StandardCharsets.UTF_8));
                os.flush();
            }
            int responseCode = conexion.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_CREATED) {
                Utilidades.escribirLog(null, "[INFO]", "MarcaServicio", "agregarMarca", "Marca añadida exitosamente: " + marca.getNombre());
                resultado = true;
            } else {
                Utilidades.escribirLog(null, "[ERROR]", "MarcaServicio", "agregarMarca", "Código de respuesta: " + responseCode);
            }
        } catch (Exception e) {
            Utilidades.escribirLog(null, "[ERROR]", "MarcaServicio", "agregarMarca", "Excepción: " + e);
            e.printStackTrace();
        }
        return resultado;
    }

    /**
     * Modifica una marca existente a través de la API tras validaciones.
     * @param descripcion 
     * @param anioFundacion 
     * @param paisOrigen 
     */
    public boolean modificarMarca(Long id, String nombre, String paisOrigen, int anioFundacion, String descripcion) {
        Utilidades.escribirLog(null, "[INFO]", "MarcaServicio", "modificarMarca", "Iniciando modificación de la marca con ID: " + id);
        if (id == null || id <= 0) {
            Utilidades.escribirLog(null, "[ERROR]", "MarcaServicio", "modificarMarca", "ID inválido.");
            return false;
        }
        if (nombre == null || nombre.isBlank()) {
            Utilidades.escribirLog(null, "[ERROR]", "MarcaServicio", "modificarMarca", "El nombre de la marca es obligatorio.");
            return false;
        }
        // Construir la URL para la modificación
        String API_URL_MODIFICAR = API_BASE_URL + "marcas/";
        try {
            URL url = new URL(API_URL_MODIFICAR + id);
            HttpURLConnection conexion = (HttpURLConnection) url.openConnection();
            conexion.setRequestMethod("PUT");
            conexion.setRequestProperty("Content-Type", "application/json");
            conexion.setDoOutput(true);
            // Construir el JSON con todos los campos
            String jsonMarca = "{" +
                    "\"nombre\": \"" + nombre + "\"," +
                    "\"paisOrigen\": \"" + paisOrigen + "\"," +
                    "\"anioFundacion\": " + anioFundacion + "," +
                    "\"descripcion\": \"" + descripcion + "\"" +
                    "}";
            try (OutputStream os = conexion.getOutputStream()) {
                os.write(jsonMarca.getBytes(StandardCharsets.UTF_8));
                os.flush();
            }
            int responseCode = conexion.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                Utilidades.escribirLog(null, "[INFO]", "MarcaServicio", "modificarMarca", "Marca modificada exitosamente con ID: " + id);
                return true;
            } else {
                Utilidades.escribirLog(null, "[ERROR]", "MarcaServicio", "modificarMarca", "Código de respuesta: " + responseCode);
                return false;
            }
        } catch (Exception e) {
            Utilidades.escribirLog(null, "[ERROR]", "MarcaServicio", "modificarMarca", "Excepción: " + e);
            e.printStackTrace();
            return false;
        }
    }


    /**
     * Elimina una marca a través de la API tras validar el ID.
     */
    public boolean eliminarMarca(Long marcaId) {
        Utilidades.escribirLog(null, "[INFO]", "MarcaServicio", "eliminarMarca", "Iniciando eliminación de la marca con ID: " + marcaId);
        
        if (marcaId == null || marcaId <= 0) {
            Utilidades.escribirLog(null, "[ERROR]", "MarcaServicio", "eliminarMarca", "ID inválido.");
            return false;
        }

        boolean resultado = false;
        
        try {
            URL url = new URL(API_BASE_URL + "marcas/eliminar/" + marcaId);
            HttpURLConnection conexion = (HttpURLConnection) url.openConnection();
            conexion.setRequestMethod("DELETE");
            conexion.setRequestProperty("Content-Type", "application/json");
            
            int responseCode = conexion.getResponseCode();
            
            if (responseCode == HttpURLConnection.HTTP_OK || responseCode == HttpURLConnection.HTTP_NO_CONTENT) {
                Utilidades.escribirLog(null, "[INFO]", "MarcaServicio", "eliminarMarca", "Marca eliminada exitosamente con ID: " + marcaId);
                resultado = true;
            } else {
                Utilidades.escribirLog(null, "[ERROR]", "MarcaServicio", "eliminarMarca", "Código de respuesta inesperado: " + responseCode);
            }
        } catch (Exception e) {
            Utilidades.escribirLog(null, "[ERROR]", "MarcaServicio", "eliminarMarca", "Excepción: " + e.getMessage());
            e.printStackTrace();
        }
        
        return resultado;
    }

}
