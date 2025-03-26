package mx.utng.finer_back_end.Instructor.Documentos;

import java.util.List;

import mx.utng.finer_back_end.Alumnos.Documentos.EvaluacionAlumnoDTO.OpcionDTO;

public class PreguntaInstructorDTO {
 private Integer idPregunta;
    private String textoPregunta;
    private List<OpcionDTO> opciones;  // Opciones asociadas a esta pregunta

    // Getters y Setters
    public Integer getIdPregunta() {
        return idPregunta;
    }

    public void setIdPregunta(Integer idPregunta) {
        this.idPregunta = idPregunta;
    }

    public String getTextoPregunta() {
        return textoPregunta;
    }

    public void setTextoPregunta(String textoPregunta) {
        this.textoPregunta = textoPregunta;
    }

    public List<OpcionDTO> getOpciones() {
        return opciones;
    }

    public void setOpciones(List<OpcionDTO> opciones) {
        this.opciones = opciones;
    }
}
