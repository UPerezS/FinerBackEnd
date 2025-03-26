package mx.utng.finer_back_end.Instructor.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import mx.utng.finer_back_end.Instructor.Documentos.EvaluacionInstructorDTO;
import mx.utng.finer_back_end.Instructor.Services.EvaluacionService;

@RestController
@RequestMapping("/api/evaluaciones")
public class CrearEvaluacionController {

    @Autowired
    private EvaluacionService evaluacionService;

    /**
     * Endpoint para crear una nueva evaluación.
     *
     * @param evaluacionDTO objeto con los datos de la evaluación.
     * @return ResponseEntity con el ID de la evaluación creada.
     */
    @PostMapping("/crear")
    public ResponseEntity<?> crearEvaluacion(@RequestBody EvaluacionInstructorDTO evaluacionDTO) {
        try {
            // Crear la evaluación
            Integer idEvaluacion = evaluacionService.crearEvaluacion(evaluacionDTO);

            // Agregar preguntas y opciones
            evaluacionService.agregarPreguntasYOpciones(evaluacionDTO);

            return ResponseEntity.status(HttpStatus.CREATED).body("Evaluación creada con ID: " + idEvaluacion);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al crear la evaluación: " + e.getMessage());
        }
    }
}
