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
@Table(name = "evaluacion") // Nombre de la tabla
public class EvaluacionDocumento {

    // Campos de la tabla
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_evaluacion")
    private Long idEvaluacion;

    @NotNull
    @Column(name = "id_curso")
    private Integer idCurso;

    @NotBlank
    @Column(name = "titulo_evaluacion")
    private String tituloEvaluacion;

    @Column(name = "fecha_creacion")
    private LocalDate fechaCreacion;

    // Getters and Setters de cada uno de los atributos de la tabla

    public Long getIdEvaluacion() {
        return idEvaluacion;
    }

    public void setIdEvaluacion(Long idEvaluacion) {
        this.idEvaluacion = idEvaluacion;
    }

    public Integer getIdCurso() {
        return idCurso;
    }

    public void setIdCurso(Integer idCurso) {
        this.idCurso = idCurso;
    }

    public String getTituloEvaluacion() {
        return tituloEvaluacion;
    }

    public void setTituloEvaluacion(String tituloEvaluacion) {
        this.tituloEvaluacion = tituloEvaluacion;
    }

    public LocalDate getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(LocalDate fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }
}
