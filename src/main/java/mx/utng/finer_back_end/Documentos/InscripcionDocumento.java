package mx.utng.finer_back_end.Documentos;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

@Entity
@Table(name = "inscripcion") // Nombre de la tabla
public class InscripcionDocumento {

    // Campo: id_inscripcion 
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_inscripcion")
    private Long idInscripcion;

    // Campo: id_curso 
    @NotNull  
    @Column(name = "id_curso")
    private Integer idCurso;

    // Campo: id_usuario_alumno 
    @NotNull
    @Column(name = "id_usuario_alumno")
    private Integer idUsuarioAlumno;

    // Campo: fecha_inscripcion 
    @Column(name = "fecha_inscripcion")
    private LocalDate fechaInscripcion;

    // Campo: estatus
    @Column(name = "estatus")
    private String estatus;

    // Campo: matricula 
    @NotBlank
    @Column(name = "matricula")
    private String matricula;

    // Getters y Setters

    public Long getIdInscripcion() {
        return idInscripcion;
    }

    public void setIdInscripcion(Long idInscripcion) {
        this.idInscripcion = idInscripcion;
    }

    public Integer getIdCurso() {
        return idCurso;
    }

    public void setIdCurso(Integer idCurso) {
        this.idCurso = idCurso;
    }

    public Integer getIdUsuarioAlumno() {
        return idUsuarioAlumno;
    }

    public void setIdUsuarioAlumno(Integer idUsuarioAlumno) {
        this.idUsuarioAlumno = idUsuarioAlumno;
    }

    public LocalDate getFechaInscripcion() {
        return fechaInscripcion;
    }

    public void setFechaInscripcion(LocalDate fechaInscripcion) {
        this.fechaInscripcion = fechaInscripcion;
    }

    public String getEstatus() {
        return estatus;
    }

    public void setEstatus(String estatus) {
        this.estatus = estatus;
    }

    public String getMatricula() {
        return matricula;
    }

    public void setMatricula(String matricula) {
        this.matricula = matricula;
    }
}
