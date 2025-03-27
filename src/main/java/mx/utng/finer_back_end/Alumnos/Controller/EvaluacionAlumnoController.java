package mx.utng.finer_back_end.Alumnos.Controller;

import mx.utng.finer_back_end.Alumnos.Documentos.EvaluacionAlumnoDTO;
import mx.utng.finer_back_end.Alumnos.Documentos.RespuestaDTO;
import mx.utng.finer_back_end.Alumnos.Services.EvaluacionAlumnoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("api/evaluacion/alumno")
public class EvaluacionAlumnoController {

    @Autowired
    private EvaluacionAlumnoService evaluacionAlumnoService;

    /**
     * Endpoint para obtener la evaluación de un alumno por su ID de evaluación.
     * 
     * Este método devuelve una lista de objetos `EvaluacionAlumnoDTO` que contienen
     * la información detallada de la evaluación de un estudiante, basada en el ID
     * de la evaluación.
     * Si no se encuentra la evaluación, puede retornar una lista vacía o un mensaje
     * adecuado.
     *
     * @param idEvaluacion El ID de la evaluación que se desea consultar.
     * @return ResponseEntity con la lista de evaluaciones o mensaje de error.
     * 
     *         Posibles respuestas:
     *         - `200 OK`: Lista de evaluaciones recuperada correctamente (puede
     *         estar vacía si no hay evaluaciones).
     *         - `400 Bad Request`: Si el ID de evaluación no es válido o no se
     *         encuentra la evaluación.
     *         - `500 Internal Server Error`: Si ocurre un error al procesar la
     *         solicitud.
     */

    @GetMapping("verEvaluacion/{idEvaluacion}")
    public ResponseEntity<?> obtenerEvaluacion(@PathVariable Integer idEvaluacion) {
        try {
            List<EvaluacionAlumnoDTO> evaluaciones = evaluacionAlumnoService.obtenerEvaluacion(idEvaluacion);

            if (evaluaciones.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("No se encontraron evaluaciones para el ID proporcionado");
            }

            return ResponseEntity.ok(evaluaciones);

        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("mensaje", "Error al obtener la evaluación");
            response.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * Endpoint para guardar las respuestas de un estudiante a un curso.
     * 
     * Este método procesa las respuestas proporcionadas por un estudiante para un
     * curso
     * específico. Si las respuestas se guardan correctamente, retorna el resultado.
     * Si el estudiante no está inscrito en el curso, se retorna un mensaje de
     * error.
     *
     * @param respuestaDTO Objeto que contiene los datos de las respuestas del
     *                     estudiante.
     * @return ResponseEntity con el resultado de la operación o mensaje de error.
     * 
     *         Posibles respuestas:
     *         - `200 OK`: Respuestas guardadas correctamente con el resultado
     *         obtenido.
     *         - `400 Bad Request`: Si el estudiante no está inscrito en el curso.
     *         - `500 Internal Server Error`: Si ocurre un error al guardar las
     *         respuestas.
     */
    @PostMapping("/guardarRespuesta")
    public ResponseEntity<?> guardarRespuestas(@RequestBody RespuestaDTO respuestaDTO) {

        try {
            Number resultado = evaluacionAlumnoService.guardarRespuestas(
                    respuestaDTO.getIdEstudiante(),
                    respuestaDTO.getIdCurso(),
                    respuestaDTO.getIdPreguntas(),
                    respuestaDTO.getIdOpciones());

            if (resultado != null) {
                return ResponseEntity.ok(resultado);
            }

            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("El usuario no está inscrito en este curso");

        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("mensaje", "Error al guardar las respuestas");
            response.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

}
