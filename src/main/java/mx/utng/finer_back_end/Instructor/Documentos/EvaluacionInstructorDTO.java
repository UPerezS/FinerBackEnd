package mx.utng.finer_back_end.Instructor.Documentos;

import java.util.List;

public class EvaluacionInstructorDTO {

    private Integer idEvaluacion;  // Debes tener un campo para el ID de la evaluación
    private Integer idCurso;
    private String tituloEvaluacion;
    private List<PreguntaEvaluacionInstructorDTO> preguntas;  // Lista de preguntas asociadas a la evaluación

    // Getters y Setters
    public Integer getIdEvaluacion() {
        return idEvaluacion;
    }

    public void setIdEvaluacion(Integer idEvaluacion) {
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

    public List<PreguntaEvaluacionInstructorDTO> getPreguntas() {
        return preguntas;
    }

    public void setPreguntas(List<PreguntaEvaluacionInstructorDTO> preguntas) {
        this.preguntas = preguntas;
    }
}
