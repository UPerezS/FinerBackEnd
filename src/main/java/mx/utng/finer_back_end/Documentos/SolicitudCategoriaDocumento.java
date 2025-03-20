package mx.utng.finer_back_end.Documentos;

import java.time.LocalDate;

import io.micrometer.common.lang.NonNull;
import jakarta.persistence.*;

@Entity
@Table(name = "solicitudcategoria")
public class SolicitudCategoriaDocumento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_solicitud_categoria")
    private Integer idSolicitudCategoria;

    @Column(name = "id_usuario_instructor", nullable = false)
    private Integer idUsuarioInstructor;

    @Column(name = "id_usuario_admin", nullable = false)
    private Integer idUsuarioAdmin;

    @Column(name = "nombre_categoria", nullable = false)
    private String nombreCategoria;

    @Column(name = "descripcion")
    private String descripcion;

    @Column(name = "estatus", columnDefinition = "TEXT DEFAULT 'en revision'")
    private String estatus;

    @NonNull
    @Column(name = "fecha_solicitud")
    private LocalDate fechaSolicitud;

    public Integer getIdSolicitudCategoria() {
        return idSolicitudCategoria;
    }

    public void setIdSolicitudCategoria(Integer idSolicitudCategoria) {
        this.idSolicitudCategoria = idSolicitudCategoria;
    }

    public Integer getIdUsuarioInstructor() {
        return idUsuarioInstructor;
    }

    public void setIdUsuarioInstructor(Integer idUsuarioInstructor) {
        this.idUsuarioInstructor = idUsuarioInstructor;
    }

    public Integer getIdUsuarioAdmin() {
        return idUsuarioAdmin;
    }

    public void setIdUsuarioAdmin(Integer idUsuarioAdmin) {
        this.idUsuarioAdmin = idUsuarioAdmin;
    }

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

    public String getEstatus() {
        return estatus;
    }

    public void setEstatus(String estatus) {
        this.estatus = estatus;
    }

    // Getters y Setters
}

