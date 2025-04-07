package mx.utng.finer_back_end.Instructor.DTO;

public class SolicitudCursoEditarDTO {
    private Integer idSolicitudCurso;
    private Integer idUsuarioInstructor;
    private String tituloCursoSolicitado;
    private String descripcion;
    private Integer idCategoria;
    private String imagen;

    // Constructors
    public SolicitudCursoEditarDTO() {
    }

    public SolicitudCursoEditarDTO(Integer idSolicitudCurso, Integer idUsuarioInstructor, String tituloCursoSolicitado,
            String descripcion, Integer idCategoria, String imagen) {
        this.idSolicitudCurso = idSolicitudCurso;
        this.idUsuarioInstructor = idUsuarioInstructor;
        this.tituloCursoSolicitado = tituloCursoSolicitado;
        this.descripcion = descripcion;
        this.idCategoria = idCategoria;
        this.imagen = imagen;
    }

    // Getters and Setters
    public Integer getIdSolicitudCurso() {
        return idSolicitudCurso;
    }

    public void setIdSolicitudCurso(Integer idSolicitudCurso) {
        this.idSolicitudCurso = idSolicitudCurso;
    }

    public Integer getIdUsuarioInstructor() {
        return idUsuarioInstructor;
    }

    public void setIdUsuarioInstructor(Integer idUsuarioInstructor) {
        this.idUsuarioInstructor = idUsuarioInstructor;
    }

    public String getTituloCursoSolicitado() {
        return tituloCursoSolicitado;
    }

    public void setTituloCursoSolicitado(String tituloCursoSolicitado) {
        this.tituloCursoSolicitado = tituloCursoSolicitado;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Integer getIdCategoria() {
        return idCategoria;
    }

    public void setIdCategoria(Integer idCategoria) {
        this.idCategoria = idCategoria;
    }

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }
}