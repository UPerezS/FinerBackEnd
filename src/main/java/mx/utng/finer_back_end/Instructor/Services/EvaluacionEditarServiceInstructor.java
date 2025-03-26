package mx.utng.finer_back_end.Instructor.Services;

import org.springframework.http.ResponseEntity;

public interface EvaluacionEditarServiceInstructor {
    ResponseEntity<String> editarEvaluacion(Integer idEvaluacion, String tituloEvaluacion, 
                                             Integer idPregunta, String textoPregunta, 
                                             Integer idOpcion, String textoOpcion, 
                                             Boolean correcta);
}
