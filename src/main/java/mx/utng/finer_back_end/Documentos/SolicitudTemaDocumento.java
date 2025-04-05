package mx.utng.finer_back_end.Documentos;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "SolicitudTema") // Nombre de la tabla
public class SolicitudTemaDocumento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_solicitud_tema")
    private Integer idSolicitudTema;

    @NotNull
    @Column(name = "id_solicitud_curso")
    private Integer idSolicitudCurso;

    @NotBlank
    @Column(name = "nombre_tema")
    private String nombreTema;

    @Column(name = "contenido", columnDefinition = "TEXT")
    private String contenido;


    // Constructor vacío
    public SolicitudTemaDocumento() {
    }
    // Constructor con todos los parámetros
    public SolicitudTemaDocumento(Integer idSolicitudTema, Integer idSolicitudCurso, String nombreTema, String contenido) {
        this.idSolicitudTema =idSolicitudTema;
        this.idSolicitudCurso = idSolicitudCurso;
        this.nombreTema = nombreTema;
        this.contenido = contenido;
    }
    // Getters y Setters

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
