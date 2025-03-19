package mx.utng.finer_back_end.Documentos;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "tema") // Nombre de la tabla en la BD
public class TemaDocumento {
    // Campos de la tabla
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_tema")
    private Integer idTema;  // Usamos Integer para coincidir con el tipo INTEGER de la tabla

    @NotNull
    @Column(name = "id_curso")
    private Integer idCurso;

    @NotBlank
    @Column(name = "nombre_tema")
    private String nombreTema;

    @Column(name = "contenido")
    private String contenido;

    @Lob
    @Column(name = "imagen")
    private byte[] imagen;

    // Getters y Setters de cada uno de los atributos de la tabla
    public TemaDocumento() {
    }

    public TemaDocumento(Integer idTema, Integer idCurso, String nombreTema, String contenido, byte[] imagen) {
        this.idTema = idTema;
        this.idCurso = idCurso;
        this.nombreTema = nombreTema;
        this.contenido = contenido;
        this.imagen = imagen;
    }
    

    public Integer getIdTema() {
        return idTema;
    }

    public void setIdTema(Integer idTema) {
        this.idTema = idTema;
    }

    public Integer getIdCurso() {
        return idCurso;
    }

    public void setIdCurso(Integer idCurso) {
        this.idCurso = idCurso;
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
