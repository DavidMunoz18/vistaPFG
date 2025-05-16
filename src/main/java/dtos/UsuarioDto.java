package dtos;

/**
 * DTO que representa un usuario en el sistema.
 * Contiene información personal y de autenticación del usuario.
 */
public class UsuarioDto {

    private long idUsuario;
    private String nombreUsuario;
    private String telefonoUsuario;
    private String emailUsuario;
    private String contrasena;
    private String rol;
    private boolean esGoogle; 

    /**
     * Constructor vacío.
     */
    public UsuarioDto() {}

    /**
     * Constructor con todos los campos.
     *
     * @param idUsuario ID del usuario
     * @param nombreUsuario Nombre del usuario
     * @param telefonoUsuario Teléfono del usuario
     * @param emailUsuario Correo electrónico del usuario
     * @param contrasena Contraseña del usuario
     * @param rol Rol del usuario (admin, cliente, etc.)
     * @param esGoogle Indica si el usuario se ha autenticado mediante Google
     */
    public UsuarioDto(long idUsuario, String nombreUsuario, String telefonoUsuario, String emailUsuario,
                      String contrasena, String rol, boolean esGoogle) {
        this.idUsuario = idUsuario;
        this.nombreUsuario = nombreUsuario;
        this.telefonoUsuario = telefonoUsuario;
        this.emailUsuario = emailUsuario;
        this.contrasena = contrasena;
        this.rol = rol;
        this.esGoogle = esGoogle;
    }

    /**
     * @return ID del usuario
     */
    public long getIdUsuario() {
        return idUsuario;
    }

    /**
     * @param idUsuario ID del usuario
     */
    public void setIdUsuario(long idUsuario) {
        this.idUsuario = idUsuario;
    }

    /**
     * @return Nombre del usuario
     */
    public String getNombreUsuario() {
        return nombreUsuario;
    }

    /**
     * @param nombreUsuario Nombre del usuario
     */
    public void setNombreUsuario(String nombreUsuario) {
        this.nombreUsuario = nombreUsuario;
    }

    /**
     * @return Teléfono del usuario
     */
    public String getTelefonoUsuario() {
        return telefonoUsuario;
    }

    /**
     * @param telefonoUsuario Teléfono del usuario
     */
    public void setTelefonoUsuario(String telefonoUsuario) {
        this.telefonoUsuario = telefonoUsuario;
    }

    /**
     * @return Correo electrónico del usuario
     */
    public String getEmailUsuario() {
        return emailUsuario;
    }

    /**
     * @param emailUsuario Correo electrónico del usuario
     */
    public void setEmailUsuario(String emailUsuario) {
        this.emailUsuario = emailUsuario;
    }

    /**
     * @return Contraseña del usuario
     */
    public String getContrasena() {
        return contrasena;
    }

    /**
     * @param contrasena Contraseña del usuario
     */
    public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
    }

    /**
     * @return Rol del usuario (admin, cliente, etc.)
     */
    public String getRol() {
        return rol;
    }

    /**
     * @param rol Rol del usuario
     */
    public void setRol(String rol) {
        this.rol = rol;
    }

    /**
     * @return true si el usuario se ha autenticado mediante Google, false en caso contrario
     */
    public boolean isEsGoogle() {
        return esGoogle;
    }

    /**
     * @param esGoogle Indica si el usuario se ha autenticado mediante Google
     */
    public void setEsGoogle(boolean esGoogle) {
        this.esGoogle = esGoogle;
    }

    /**
     * Representación en texto del objeto UsuarioDto.
     *
     * @return Cadena que representa el objeto
     */
    @Override
    public String toString() {
        return "UsuarioDto{" +
                "idUsuario=" + idUsuario +
                ", nombreUsuario='" + nombreUsuario + '\'' +
                ", telefonoUsuario='" + telefonoUsuario + '\'' +
                ", emailUsuario='" + emailUsuario + '\'' +
                ", rol='" + rol + '\'' +
                ", esGoogle=" + esGoogle +
                '}';
    }
}
