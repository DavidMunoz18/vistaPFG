package controladores;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Arrays;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.auth.oauth2.TokenResponse;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.oauth2.Oauth2;
import com.google.api.services.oauth2.Oauth2Scopes;
import com.google.api.services.oauth2.model.Userinfoplus;

import dtos.UsuarioDto;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import servicios.LoginGoogleServicio;

/**
 * Controlador que maneja la autenticación de los usuarios a través de Google OAuth2.
 * Realiza el flujo de autenticación, obtiene el token de acceso y la información del usuario,
 * y delega en el servicio para registrar o hacer login.
 */
@WebServlet("/loginGoogle")
public class LoginGoogleControlador extends HttpServlet {
       
    private static final long serialVersionUID = 1L;

    // Credenciales de Google (recuerda proteger estos valores en producción)
    private static final String CLIENT_ID = "868357228953-9padqblpv73igf2gf1el580cbpree75p.apps.googleusercontent.com";
    private static final String CLIENT_SECRET = "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";
    private static final String REDIRECT_URI = "https://tomcat.dmunoz.es/VistaCodeComponents/loginGoogle";


    // Instancia del servicio de login con Google
    private LoginGoogleServicio loginGoogleServicio = new LoginGoogleServicio();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();
        session.removeAttribute("datos"); // Se limpian datos previos de la sesión

        String code = request.getParameter("code"); // Se obtiene el código de autorización

        if (code != null) {
            doPost(request, response); // Si se recibe el código, se procesa en doPost
        } else {
            try {
                // Se configura el flujo de autenticación de Google
                GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
                        GoogleNetHttpTransport.newTrustedTransport(),
                        JacksonFactory.getDefaultInstance(), 
                        CLIENT_ID,
                        CLIENT_SECRET,
                        Arrays.asList(Oauth2Scopes.USERINFO_EMAIL, Oauth2Scopes.USERINFO_PROFILE))
                        .setAccessType("offline")
                        .build();

                // Se construye la URL de autorización
                String authorizationUrl = flow.newAuthorizationUrl()
                        .setRedirectUri(REDIRECT_URI)
                        .set("prompt", "select_account")
                        .build();

                // Se redirige al usuario a la URL de Google para iniciar autenticación
                response.sendRedirect(authorizationUrl);
            } catch (GeneralSecurityException | IOException e) {
                e.printStackTrace();
                response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error en la autenticación.");
            }
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String code = request.getParameter("code"); // Se obtiene el código de autorización

        if (code == null) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Código de autorización no encontrado");
            return;
        }

        try {
            System.out.println("Iniciando flujo de OAuth2...");

            // Configuración del flujo de autenticación de Google
            GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
                    GoogleNetHttpTransport.newTrustedTransport(),
                    JacksonFactory.getDefaultInstance(), 
                    CLIENT_ID,
                    CLIENT_SECRET,
                    Arrays.asList(Oauth2Scopes.USERINFO_EMAIL, Oauth2Scopes.USERINFO_PROFILE))
                    .setAccessType("offline")
                    .build();

            // Se obtiene el token de acceso usando el código recibido
            TokenResponse tokenResponse = flow.newTokenRequest(code)
                    .setRedirectUri(REDIRECT_URI)
                    .execute();
            Credential credential = flow.createAndStoreCredential(tokenResponse, "user");

            // Se usa el token para obtener la información del usuario desde Google
            Oauth2 oauth2 = new Oauth2.Builder(
                    GoogleNetHttpTransport.newTrustedTransport(),
                    JacksonFactory.getDefaultInstance(), 
                    credential)
                    .setApplicationName("Google OAuth2 Login")
                    .build();

            Userinfoplus userinfo = oauth2.userinfo().get().execute();
            System.out.println("Información del usuario obtenida: " + userinfo);

            // Se crea y configura el objeto UsuarioDto
            UsuarioDto usuario = new UsuarioDto();
            usuario.setEmailUsuario(userinfo.getEmail()); // El email se usa como identificador único
            usuario.setNombreUsuario(userinfo.getGivenName());
            // Se establece la contraseña autogenerada
            usuario.setContrasena("google_autogenerated");
            // Se marca que el usuario proviene de Google (campo agregado en el DTO)
            usuario.setEsGoogle(true);

            // Se delega el proceso de login o registro en el servicio
            UsuarioDto usuarioFinal = loginGoogleServicio.loginGoogle(usuario);
            System.out.println("Usuario final: " + usuarioFinal);

            // Se guarda el usuario final (o los datos relevantes) en la sesión
            HttpSession session = request.getSession();
            session.setAttribute("datos", usuarioFinal);
            // En vez de guardar solo "datos", se establecen los mismos atributos que en el login normal
	         session.setAttribute("idUsuario", usuarioFinal.getIdUsuario());
	         session.setAttribute("nombreUsuario", usuarioFinal.getNombreUsuario());
	         session.setAttribute("rol", usuarioFinal.getRol());

            System.out.println("Usuario guardado en la sesión correctamente.");
            response.sendRedirect("inicio"); // Redirige a la página principal

        } catch (GeneralSecurityException | IOException e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error al procesar la autenticación.");
        }
    }
}  
