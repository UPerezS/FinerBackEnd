package mx.utng.finer_back_end.Documentos;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "curso") // Nombre de la tabla en la BD
public class CursoDocumento {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_curso")
    private Integer idCurso;
    
    @NotNull
    @Column(name = "id_usuario_instructor")
    private Integer idUsuarioInstructor;
    
    @NotNull
    @Column(name = "id_categoria")
    private Integer idCategoria;
    
    @NotBlank
    @Column(name = "titulo_curso")
    private String tituloCurso;
    
    @NotBlank
    @Column(name = "descripcion", columnDefinition = "TEXT")
    private String descripcion;
    
    // Getters y Setters
    
    public Integer getIdCurso() {
        return idCurso;
    }

    public void setIdCurso(Integer idCurso) {
        this.idCurso = idCurso;
    }

    public Integer getIdUsuarioInstructor() {
        return idUsuarioInstructor;
    }

    public void setIdUsuarioInstructor(Integer idUsuarioInstructor) {
        this.idUsuarioInstructor = idUsuarioInstructor;
    }

    public Integer getIdCategoria() {
        return idCategoria;
    }

    public void setIdCategoria(Integer idCategoria) {
        this.idCategoria = idCategoria;
    }

    public String getTituloCurso() {
        return tituloCurso;
    }

    public void setTituloCurso(String tituloCurso) {
        this.tituloCurso = tituloCurso;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
}
