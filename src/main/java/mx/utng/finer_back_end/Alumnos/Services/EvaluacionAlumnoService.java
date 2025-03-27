package mx.utng.finer_back_end.Alumnos.Services;

import mx.utng.finer_back_end.Alumnos.Documentos.EvaluacionAlumnoDTO;

import java.util.List;


public interface EvaluacionAlumnoService {
    List<EvaluacionAlumnoDTO> obtenerEvaluacion(Integer idEvaluacion);

    Number guardarRespuestas( Integer idEstudiante, Integer idCurso, Integer[] idPreguntas, Integer[] idOpciones);
}
