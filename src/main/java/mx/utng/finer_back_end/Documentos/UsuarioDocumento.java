package mx.utng.finer_back_end.Documentos;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Email;

@Entity
@Table(name = "usuario") // Nombre De la tabla en la BD
public class UsuarioDocumento {
    // Campos de la tabla
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_usuario")
    private Integer idUsuario;

    @NotBlank
    @Column(name = "nombre")
    private String nombre;

    @NotNull
    @Column(name = "id_rol")
    private Integer idRol;

    @Column(name = "apellido_paterno")
    private String apellidoPaterno;

    @Column(name = "apellido_materno")
    private String apellidoMaterno;

    @NotBlank
    @Email
    @Column(name = "correo", unique = true)
    private String correo;

    @NotBlank
    @Column(name = "contrasenia")
    private String contrasenia;

    @NotBlank
    @Column(name = "nombre_usuario", unique = true)
    private String nombreUsuario;

    @Column(name = "telefono")
    private String telefono;

    @Column(name = "direccion")
    private String direccion;

    @Lob
    @Column(name = "cedula_pdf")
    private byte[] cedulaPdf; // Puedes descomentar esto si es necesario

    @Column(name = "estatus")
    private String estatus;

    public UsuarioDocumento(String nombre, Integer idRol, String apellidoPaterno,
            String apellidoMaterno, String correo, String contrasenia,
            String nombreUsuario, String telefono, String direccion,
            String estatus) {
        this.nombre = nombre;
        this.idRol = idRol;
        this.apellidoPaterno = apellidoPaterno;
        this.apellidoMaterno = apellidoMaterno;
        this.correo = correo;
        this.contrasenia = contrasenia;
        this.nombreUsuario = nombreUsuario;
        this.telefono = telefono;
        this.direccion = direccion;
        this.estatus = estatus;
    }


    // Getters and Setters de cada uno d elos atributos de la tabla

    public Integer getId() {
        return idUsuario;
    }

    public void setId(Integer idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Integer getIdRol() {
        return idRol;
    }

    public void setIdRol(Integer idRol) {
        this.idRol = idRol;
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

    public String getEstatus() {
        return estatus;
    }

    public void setEstatus(String estatus) {
        this.estatus = estatus;
    }
}
