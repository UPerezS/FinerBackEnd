package mx.utng.finer_back_end.Instructor.DTO;

public class SolicitudTemaEditarDTO {
    private Integer idSolicitudTema;
    private Integer idSolicitudCurso;
    private String nombreTema;
    private String contenido;

    // Constructors
    public SolicitudTemaEditarDTO() {
    }

    public SolicitudTemaEditarDTO(Integer idSolicitudTema, Integer idSolicitudCurso, String nombreTema, String contenido) {
        this.idSolicitudTema = idSolicitudTema;
        this.idSolicitudCurso = idSolicitudCurso;
        this.nombreTema = nombreTema;
        this.contenido = contenido;
    }

    // Getters and Setters
    public Integer getIdSolicitudTema() {
        return idSolicitudTema;
    }

    public void setIdSolicitudTema(Integer idSolicitudTema) {
        this.idSolicitudTema = idSolicitudTema;
    }

    public Integer getIdSolicitudCurso() {
        return idSolicitudCurso;
    }

    public void setIdSolicitudCurso(Integer idSolicitudCurso) {
        this.idSolicitudCurso = idSolicitudCurso;
    }

    public String getNombreTema() {
        return nombreTema;
    }

    public void setNombreTema(String nombreTema) {
        this.nombreTema = nombreTema;
    }

    public String getContenido() {
        return contenido;
    }

    public void setContenido(String contenido) {
        this.contenido = contenido;
    }
}