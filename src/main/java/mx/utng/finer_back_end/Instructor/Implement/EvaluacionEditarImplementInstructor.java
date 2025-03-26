package mx.utng.finer_back_end.Instructor.Implement;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import mx.utng.finer_back_end.Instructor.Dao.EvaluacionEditarDaoInstructor;
import mx.utng.finer_back_end.Instructor.Services.EvaluacionEditarServiceInstructor;

@Service
public class EvaluacionEditarImplementInstructor implements EvaluacionEditarServiceInstructor {

    @Autowired
    private EvaluacionEditarDaoInstructor evaluacionEditarDao;

    @Override
public ResponseEntity<String> editarEvaluacion(Integer idEvaluacion, String tituloEvaluacion, 
                                               Integer idPregunta, String textoPregunta, 
                                               Integer idOpcion, String textoOpcion, 
                                               Boolean verificar) {
    try {
        // Si se proporciona el título de la evaluación, actualizarlo
        if (tituloEvaluacion != null) {
            boolean evalUpdated = evaluacionEditarDao.editarEvaluacion(idEvaluacion, tituloEvaluacion);
            if (!evalUpdated) {
                return ResponseEntity.status(400).body("Error: La evaluación con ID " + idEvaluacion + " no existe.");
            }
        }

        // Si se proporciona la pregunta, actualizarla
        if (idPregunta != null && textoPregunta != null) {
            boolean pregUpdated = evaluacionEditarDao.editarPregunta(idPregunta, textoPregunta);
            if (!pregUpdated) {
                return ResponseEntity.status(400).body("Error: La pregunta con ID " + idPregunta + " no existe.");
            }
        }

        // Si se proporciona la opción, actualizarla
        if (idOpcion != null && textoOpcion != null) {
            boolean opcionUpdated = evaluacionEditarDao.editarOpcion(idOpcion, textoOpcion, verificar);
            if (!opcionUpdated) {
                return ResponseEntity.status(400).body("Error: La opción con ID " + idOpcion + " no existe.");
            }
        }

        return ResponseEntity.ok("Evaluación, pregunta y/o opción modificados exitosamente.");
    } catch (Exception e) {
        return ResponseEntity.status(500).body("Error en la DB: " + e.getMessage());
    }
}

}
