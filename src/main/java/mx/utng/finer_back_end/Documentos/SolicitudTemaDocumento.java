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
    private Long idSolicitudTema;

    @NotNull
    @Column(name = "id_solicitud_curso")
    private Integer idSolicitudCurso;

    @NotBlank
    @Column(name = "nombre_tema")
    private String nombreTema;

    @Column(name = "contenido", columnDefinition = "TEXT")
    private String contenido;

    @Lob
    @Column(name = "imagen")
    private byte[] imagen;

    // Getters y Setters

    public Long getIdSolicitudTema() {
        return idSolicitudTema;
    }

    public void setIdSolicitudTema(Long idSolicitudTema) {
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

    public byte[] getImagen() {
        return imagen;
    }

    public void setImagen(byte[] imagen) {
        this.imagen = imagen;
    }
}
