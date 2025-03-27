package mx.utng.finer_back_end.Alumnos.Documentos;

public class RespuestaDTO {

    private Integer idEstudiante;
    private Integer idCurso;
    private Integer[] idPreguntas;
    private Integer[] idOpciones;

    // Getters y Setters
    public Integer getIdEstudiante() {
        return idEstudiante;
    }

    public void setIdEstudiante(Integer idEstudiante) {
        this.idEstudiante = idEstudiante;
    }

    public Integer getIdCurso() {
        return idCurso;
    }

    public void setIdCurso(Integer idCurso) {
        this.idCurso = idCurso;
    }

    public Integer[] getIdPreguntas() {
        return idPreguntas;
    }

    public void setIdPreguntas(Integer[] idPreguntas) {
        this.idPreguntas = idPreguntas;
    }

    public Integer[] getIdOpciones() {
        return idOpciones;
    }

    public void setIdOpciones(Integer[] idOpciones) {
        this.idOpciones = idOpciones;
    }
}
