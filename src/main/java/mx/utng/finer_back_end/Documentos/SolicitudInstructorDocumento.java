package mx.utng.finer_back_end.Documentos;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "solicitudinstructor") // Nombre de la tabla
public class SolicitudInstructorDocumento {

    // Campo: id_solicitud_instructor
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_solicitud_instructor")
    private Integer idSolicitudInstructor;

    // Campo: id_rol
    @NotNull
    @Column(name = "id_rol")
    private Integer idRol;

    // Campo: nombre
    @NotBlank
    @Column(name = "nombre")
    private String nombre;

    // Campo: apellido_paterno
    @NotBlank
    @Column(name = "apellido_paterno")
    private String apellidoPaterno;

    // Campo: apellido_materno
    @NotBlank
    @Column(name = "apellido_materno")
    private String apellidoMaterno;

    // Campo: correo
    @NotBlank
    @Email
    @Column(name = "correo")
    private String correo;

    // Campo: contrasenia
    @NotBlank
    @Column(name = "contrasenia")
    private String contrasenia;

    // Campo: nombre_usuario
    @NotBlank
    @Column(name = "nombre_usuario")
    private String nombreUsuario;

    // Campo: telefono
    @Column(name = "telefono")
    private String telefono;

    // Campo: direccion
    @Column(name = "direccion")
    private String direccion;

    // Campo: cedula_pdf
    @Column(name = "cedula_pdf")
    private byte[] cedulaPdf;

    @Column(name = "fecha_solicitud")
    private LocalDateTime fechaSolicitud;

    // Campo: estatus_solicitud
    @Column(name = "estatus_solicitud")
    private String estatusSolicitud;



    // Constructor con todos los parámetros
    public SolicitudInstructorDocumento(Integer idSolicitudInstructor, Integer idRol, String nombre,
            String apellidoPaterno,
            String apellidoMaterno, String correo, String contrasenia, String nombreUsuario,
            String telefono, String direccion, byte[] cedulaPdf, LocalDateTime fechaSolicitud,String estatusSolicitud) {
        this.idSolicitudInstructor = idSolicitudInstructor; // Mantener Integer
        this.idRol = idRol;
        this.nombre = nombre;
        this.apellidoPaterno = apellidoPaterno;
        this.apellidoMaterno = apellidoMaterno;
        this.correo = correo;
        this.contrasenia = contrasenia;
        this.nombreUsuario = nombreUsuario;
        this.telefono = telefono;
        this.direccion = direccion;
        this.cedulaPdf = cedulaPdf;
        this.fechaSolicitud =fechaSolicitud;
        this.estatusSolicitud = estatusSolicitud;
    }

    public SolicitudInstructorDocumento( String correo) {
        this.correo = correo;
    }
    // Getters y Setters

    public Integer getIdSolicitudInstructor() {
        return idSolicitudInstructor;
    }

    public void setIdSolicitudInstructor(Integer idSolicitudInstructor) {
        this.idSolicitudInstructor = idSolicitudInstructor;
    }

    public Integer getIdRol() {
        return idRol;
    }

    public void setIdRol(Integer idRol) {
        this.idRol = idRol;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellidoPaterno() {
        return apellidoPaterno;
    }

    public void setApellidoPaterno(String apellidoPaterno) {
        this.apellidoPaterno = apellidoPaterno;
    }

    public String getApellidoMaterno() {
        return apellidoMaterno;
    }

    public void setApellidoMaterno(String apellidoMaterno) {
        this.apellidoMaterno = apellidoMaterno;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getContrasenia() {
        return contrasenia;
    }

    public void setContrasenia(String contrasenia) {
        this.contrasenia = contrasenia;
    }

    public String getNombreUsuario() {
        return nombreUsuario;
    }

    public void setNombreUsuario(String nombreUsuario) {
        this.nombreUsuario = nombreUsuario;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public byte[] getCedulaPdf() {
        return cedulaPdf;
    }

    public void setCedulaPdf(byte[] cedulaPdf) {
        this.cedulaPdf = cedulaPdf;
    }

    public String getEstatusSolicitud() {
        return estatusSolicitud;
    }

    public void setEstatusSolicitud(String estatusSolicitud) {
        this.estatusSolicitud = estatusSolicitud;
    }

      public LocalDateTime getFechaSolicitud() {
        return fechaSolicitud;
    }

    public void setFechaSolicitud(LocalDateTime fechaSolicitud) {
        this.fechaSolicitud = fechaSolicitud;
    }
}
