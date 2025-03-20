package mx.utng.finer_back_end.Administrador.DTO;

/**
 * DTO para transferir datos de categoría entre capas de la aplicación.
 * Contiene los campos necesarios para actualizar una categoría.
 */
public class CategoriaDTO {
    private String descripcion;
    private String nombreCategoria;

    // Constructores
    public CategoriaDTO() {
    }

    public CategoriaDTO(String descripcion, String nombreCategoria) {
        this.descripcion = descripcion;
        this.nombreCategoria = nombreCategoria;
    }

    // Getters y Setters
    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getNombreCategoria() {
        return nombreCategoria;
    }

    public void setNombreCategoria(String nombreCategoria) {
        this.nombreCategoria = nombreCategoria;
    }
}