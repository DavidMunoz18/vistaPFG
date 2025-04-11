package dtos;

import java.util.Base64;

/**
 * Clase DTO que representa un producto en el sistema.
 * <p>
 * Este objeto se utiliza para enviar y recibir los detalles de un producto,
 * incluyendo su identificador, nombre, descripción, precio, imagen, stock, categoría
 * y la marca asociada.
 * </p>
 */
public class ProductoDto {

    /** Identificador del producto. */
    private Long id;
    
    /** Nombre del producto. */
    private String nombre;
    
    /** Descripción del producto. */
    private String descripcion;
    
    /** Precio del producto. */
    private double precio;
    
    /** Imagen del producto en formato byte array. */
    private byte[] imagen;
    
    /** Número de unidades disponibles en stock. */
    private int stock;
    
    /** Categoría a la que pertenece el producto. */
    private String categoria;
    
    /** Marca asociada al producto. */
    private MarcaDto marca;

    /**
     * Constructor predeterminado para crear un objeto ProductoDto vacío.
     */
    public ProductoDto() {
    }

    /**
     * Constructor que inicializa un producto con todos sus detalles.
     *
     * @param id          El identificador del producto.
     * @param nombre      El nombre del producto.
     * @param descripcion La descripción del producto.
     * @param precio      El precio del producto.
     * @param imagen      La imagen del producto en formato byte array.
     * @param stock       El número de unidades disponibles en stock.
     * @param categoria   La categoría del producto.
     * @param marca       La marca asociada al producto (objeto MarcaDto).
     */
    public ProductoDto(Long id, String nombre, String descripcion, double precio, byte[] imagen, int stock, String categoria, MarcaDto marca) {
        this.id = id;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.precio = precio;
        this.imagen = imagen;
        this.stock = stock;
        this.categoria = categoria;
        this.marca = marca;
    }

    /**
     * Obtiene el identificador del producto.
     *
     * @return El identificador del producto.
     */
    public Long getId() {
        return id;
    }

    /**
     * Establece el identificador del producto.
     *
     * @param id El identificador a establecer.
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Obtiene el nombre del producto.
     *
     * @return El nombre del producto.
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * Establece el nombre del producto.
     *
     * @param nombre El nombre a establecer.
     */
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    /**
     * Obtiene la descripción del producto.
     *
     * @return La descripción del producto.
     */
    public String getDescripcion() {
        return descripcion;
    }

    /**
     * Establece la descripción del producto.
     *
     * @param descripcion La descripción a establecer.
     */
    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    /**
     * Obtiene el precio del producto.
     *
     * @return El precio del producto.
     */
    public double getPrecio() {
        return precio;
    }

    /**
     * Establece el precio del producto.
     *
     * @param precio El precio a establecer.
     */
    public void setPrecio(double precio) {
        this.precio = precio;
    }

    /**
     * Obtiene la imagen del producto en formato byte array.
     *
     * @return La imagen del producto.
     */
    public byte[] getImagen() {
        return imagen;
    }

    /**
     * Establece la imagen del producto.
     *
     * @param imagen La imagen a establecer en formato byte array.
     */
    public void setImagen(byte[] imagen) {
        this.imagen = imagen;
    }

    /**
     * Obtiene el número de unidades disponibles en stock.
     *
     * @return El stock del producto.
     */
    public int getStock() {
        return stock;
    }

    /**
     * Establece el número de unidades disponibles en stock.
     *
     * @param stock El stock a establecer.
     */
    public void setStock(int stock) {
        this.stock = stock;
    }

    /**
     * Obtiene la categoría del producto.
     *
     * @return La categoría del producto.
     */
    public String getCategoria() {
        return categoria;
    }

    /**
     * Establece la categoría del producto.
     *
     * @param categoria La categoría a establecer.
     */
    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    /**
     * Obtiene la marca asociada al producto.
     *
     * @return Un objeto MarcaDto con la información de la marca.
     */
    public MarcaDto getMarca() {
        return marca;
    }

    /**
     * Establece la marca asociada al producto.
     *
     * @param marca Un objeto MarcaDto con la información de la marca.
     */
    public void setMarca(MarcaDto marca) {
        this.marca = marca;
    }

    /**
     * Convierte la imagen del producto a una cadena en formato Base64.
     *
     * @return La imagen en formato Base64 o null si la imagen es null.
     */
    public String getImagenBase64() {
        if (imagen != null) {
            return Base64.getEncoder().encodeToString(imagen);
        }
        return null;
    }
}
