/**
 * Servicio encargado de gestionar el inicio de sesión de usuarios mediante Google.
 * <p>
 * Utiliza la API REST externa para autenticar al usuario a través del endpoint
 * <code>/usuarios/loginGoogle</code>. Convierte el DTO {@link UsuarioDto} a JSON,
 * realiza la petición HTTP y deserializa la respuesta.
 * </p>
 *
 * <p>
 * Internamente usa Jackson para el mapeo JSON y soporte para fechas con {@link JavaTimeModule}.
 * </p>
 */
package servicios;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URI;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import dtos.UsuarioDto;

public class LoginGoogleServicio {

    
    private static final String API_BASE_URL = "https://tomcat.dmunoz.es/ApiEcommerceOrdenadores-0.0.1/api";
    private final ObjectMapper mapper;

    public LoginGoogleServicio() {
        this.mapper = new ObjectMapper()
                .registerModule(new JavaTimeModule())
                .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    }

    private String enviarPeticionHttp(String url, String metodo, String jsonInput) throws Exception {
        URI uri = new URI(url);
        HttpURLConnection conexion = (HttpURLConnection) uri.toURL().openConnection();
        conexion.setRequestMethod(metodo);
        conexion.setRequestProperty("Content-Type", "application/json");
        conexion.setDoOutput(true);

        if (jsonInput != null) {
            try (OutputStream os = conexion.getOutputStream()) {
                os.write(jsonInput.getBytes());
                os.flush();
            }
        }

        int codigoRespuesta = conexion.getResponseCode();
        if (codigoRespuesta == HttpURLConnection.HTTP_OK) {
            try (var in = new java.io.BufferedReader(new java.io.InputStreamReader(conexion.getInputStream()))) {
                StringBuilder respuesta = new StringBuilder();
                String linea;
                while ((linea = in.readLine()) != null) {
                    respuesta.append(linea);
                }
                return respuesta.toString();
            }
        }
        return null;
    }

    /**
     * Realiza el login de un usuario utilizando sus credenciales de Google.
     * Se envía un POST a la API en el endpoint "/usuarios/loginGoogle".
     *
     * @param usuDto El DTO que contiene los datos del usuario que intenta iniciar sesión.
     * @return La respuesta de la API en formato String.
     */
    public UsuarioDto loginGoogle(UsuarioDto usuDto) {
        try {
            String jsonInput = mapper.writeValueAsString(usuDto);
            String respuesta = enviarPeticionHttp(API_BASE_URL + "/usuarios/loginGoogle", "POST", jsonInput);
            
            if (respuesta != null) {
                // Deserializar la respuesta JSON a un objeto UsuarioDto
                return mapper.readValue(respuesta, UsuarioDto.class);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
