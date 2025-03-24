package mx.utng.finer_back_end.Instructor.Documentos;

import java.util.List;

public class PreguntaEvaluacionInstructorDTO {

    private Integer idPregunta;  // Campo para almacenar el ID de la pregunta
    private String textoPregunta;
    private List<OpcionEvaluacionInstructorDTO> opciones;  // Opciones asociadas a esta pregunta

    // Getters y Setters
    public Integer getIdPregunta() {
        return idPregunta;
    }

    public void setIdPregunta(Integer idPregunta) {
        this.idPregunta = idPregunta;  // Método setter para establecer el ID de la pregunta
    }

    public String getTextoPregunta() {
        return textoPregunta;
    }

    public void setTextoPregunta(String textoPregunta) {
        this.textoPregunta = textoPregunta;
    }

    public List<OpcionEvaluacionInstructorDTO> getOpciones() {
        return opciones;
    }

    public void setOpciones(List<OpcionEvaluacionInstructorDTO> opciones) {
        this.opciones = opciones;
    }
}
