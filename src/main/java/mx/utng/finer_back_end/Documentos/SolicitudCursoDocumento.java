package mx.utng.finer_back_end.Documentos;

import java.time.LocalDate;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "solicitudcurso") // Nombre de la tabla
public class SolicitudCursoDocumento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_solicitud_curso")
    private Long idSolicitudCurso;

    @NotNull
    @Column(name = "fecha_solicitud")
    private LocalDate fechaSolicitud;

    @NotNull
    @Column(name = "id_usuario_instructor")
    private Integer idUsuarioInstructor;


    @NotBlank
    @Column(name = "titulo_curso_solicitado")
    private String tituloCursoSolicitado;

    @Column(name = "descripcion", columnDefinition = "TEXT")
    private String descripcion;

    @Column(name = "estatus", columnDefinition = "TEXT DEFAULT 'en revision'")
    private String estatus;

    @NotNull
    @Column(name = "id_categoria")
    private Integer idCategoria;

    // @Column(name = "id_curso")
    // private Integer idCurso;

    // Getters y Setters

    public Long getIdSolicitudCurso() {
        return idSolicitudCurso;
    }

    public void setIdSolicitudCurso(Long idSolicitudCurso) {
        this.idSolicitudCurso = idSolicitudCurso;
    }

    public LocalDate getFechaSolicitud() {
        return fechaSolicitud;
    }

    public void setFechaSolicitud(LocalDate fechaSolicitud) {
        this.fechaSolicitud = fechaSolicitud;
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

    public String getEstatus() {
        return estatus;
    }

    public void setEstatus(String estatus) {
        this.estatus = estatus;
    }

    public Integer getIdCategoria() {
        return idCategoria;
    }

    public void setIdCategoria(Integer idCategoria) {
        this.idCategoria = idCategoria;
    }

    // public Integer getIdCurso() {
    //     return idCurso;
    // }

    // public void setIdCurso(Integer idCurso) {
    //     this.idCurso = idCurso;
    // }
}
