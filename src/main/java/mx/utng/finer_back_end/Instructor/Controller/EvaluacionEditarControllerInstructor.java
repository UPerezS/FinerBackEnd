package mx.utng.finer_back_end.Instructor.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import mx.utng.finer_back_end.Instructor.Services.EvaluacionEditarServiceInstructor;

@RestController
@RequestMapping("/api/evaluaciones")
public class EvaluacionEditarControllerInstructor {

    @Autowired
    private EvaluacionEditarServiceInstructor evaluacionEditarService;

    /**
     * Endpoint para editar una evaluación, pregunta y opción.
     *
     * @param idEvaluacion identificador de la evaluación.
     * @param tituloEvaluacion nuevo título de la evaluación.
     * @param idPregunta identificador de la pregunta.
     * @param textoPregunta nuevo texto de la pregunta.
     * @param idOpcion identificador de la opción.
     * @param textoOpcion nuevo texto de la opción.
     * @param correcta si la opción es correcta o no.
     * @return ResponseEntity con el mensaje de estado.
     */
    @PutMapping("/editar/{idEvaluacion}/{idPregunta}/{idOpcion}")
    public ResponseEntity<String> editarEvaluacion(@PathVariable Integer idEvaluacion, 
                                                   @RequestParam(required = false) String tituloEvaluacion, 
                                                   @RequestParam(required = false) Integer idPregunta, 
                                                   @RequestParam(required = false) String textoPregunta, 
                                                   @RequestParam(required = false) Integer idOpcion, 
                                                   @RequestParam(required = false) String textoOpcion, 
                                                   @RequestParam(required = false) Boolean verificar) {
        return evaluacionEditarService.editarEvaluacion(idEvaluacion, tituloEvaluacion, 
                                                        idPregunta, textoPregunta, 
                                                        idOpcion, textoOpcion, 
                                                        verificar);
    }
}
