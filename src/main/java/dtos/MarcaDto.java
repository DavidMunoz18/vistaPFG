package dtos;

/**
 * Clase DTO que representa una marca en el sistema.
 * <p>
 * Este objeto se utiliza para transferir los detalles de una marca,
 * incluyendo su identificador, nombre, país de origen, año de fundación y descripción.
 * </p>
 */
public class MarcaDto {

    /** Identificador de la marca. */
    private Long id;
    
    /** Nombre de la marca. */
    private String nombre;
    
    /** País de origen de la marca. */
    private String paisOrigen;
    
    /** Año de fundación de la marca. */
    private int anioFundacion;
    
    /** Descripción de la marca. */
    private String descripcion;

    /**
     * Constructor predeterminado para crear un objeto MarcaDto vacío.
     */
    public MarcaDto() {
    }

    /**
     * Constructor que inicializa una marca con todos sus detalles.
     *
     * @param id            El identificador de la marca.
     * @param nombre        El nombre de la marca.
     * @param paisOrigen    El país de origen de la marca.
     * @param anioFundacion El año de fundación de la marca.
     * @param descripcion   La descripción de la marca.
     */
    public MarcaDto(Long id, String nombre, String paisOrigen, int anioFundacion, String descripcion) {
        this.id = id;
        this.nombre = nombre;
        this.paisOrigen = paisOrigen;
        this.anioFundacion = anioFundacion;
        this.descripcion = descripcion;
    }

    /**
     * Obtiene el identificador de la marca.
     *
     * @return El identificador de la marca.
     */
    public Long getId() {
        return id;
    }

    /**
     * Establece el identificador de la marca.
     *
     * @param id El identificador a establecer.
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Obtiene el nombre de la marca.
     *
     * @return El nombre de la marca.
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * Establece el nombre de la marca.
     *
     * @param nombre El nombre a establecer.
     */
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    /**
     * Obtiene el país de origen de la marca.
     *
     * @return El país de origen de la marca.
     */
    public String getPaisOrigen() {
        return paisOrigen;
    }

    /**
     * Establece el país de origen de la marca.
     *
     * @param paisOrigen El país de origen a establecer.
     */
    public void setPaisOrigen(String paisOrigen) {
        this.paisOrigen = paisOrigen;
    }

    /**
     * Obtiene el año de fundación de la marca.
     *
     * @return El año de fundación de la marca.
     */
    public int getAnioFundacion() {
        return anioFundacion;
    }

    /**
     * Establece el año de fundación de la marca.
     *
     * @param anioFundacion El año de fundación a establecer.
     */
    public void setAnioFundacion(int anioFundacion) {
        this.anioFundacion = anioFundacion;
    }

    /**
     * Obtiene la descripción de la marca.
     *
     * @return La descripción de la marca.
     */
    public String getDescripcion() {
        return descripcion;
    }

    /**
     * Establece la descripción de la marca.
     *
     * @param descripcion La descripción a establecer.
     */
    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
}
