package mx.utng.finer_back_end.Instructor.Documentos;

public class CategoriaSolicitudDTO {

    private String nombreCategoria;  // Nombre de la categoría solicitada
    private String descripcion;  // Motivo de la solicitud de la categoría
    private Integer idInstructor;    // ID del instructor solicitante
    /* private Integer idUsuarioAdmin; */  // ID del administrador 
    private String nombreInstructor;

    // Getters y Setters
    public String getNombreCategoria() {
        return nombreCategoria;
    }

    public void setNombreCategoria(String nombreCategoria) {
        this.nombreCategoria = nombreCategoria;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Integer getIdInstructor() {
        return idInstructor;
    }

    public void setIdInstructor(Integer idInstructor) {
        this.idInstructor = idInstructor;
    }

    /* public Integer getIdUsuarioAdmin() {
        return idUsuarioAdmin;
    }

    public void setIdUsuarioAdmin(Integer idUsuarioAdmin) {
        this.idUsuarioAdmin = idUsuarioAdmin;
    } */

    public String getNombreInstructor() {
        return nombreInstructor;
    }

    public void setNombreInstructor(String nombreInstructor) {
        this.nombreInstructor = nombreInstructor;
    }
}
