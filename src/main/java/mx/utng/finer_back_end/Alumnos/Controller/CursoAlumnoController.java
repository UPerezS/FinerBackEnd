package mx.utng.finer_back_end.Alumnos.Controller;

import org.springframework.http.HttpHeaders;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import mx.utng.finer_back_end.Alumnos.Documentos.CertificadoDetalleDTO;
import mx.utng.finer_back_end.Alumnos.Documentos.ContinuarCursoDTO;
import mx.utng.finer_back_end.Alumnos.Documentos.CursoDetalleAlumnoDTO;
import mx.utng.finer_back_end.Alumnos.Documentos.CursoNombreAlumnoDTO;
import mx.utng.finer_back_end.Alumnos.Documentos.PuntuacionAlumnoDTO;
import mx.utng.finer_back_end.Alumnos.Implement.CursoAlumnoImplement;
import mx.utng.finer_back_end.Alumnos.Services.CursoAlumnoService;
import mx.utng.finer_back_end.Alumnos.Services.PdfGenerationService;

import mx.utng.finer_back_end.Documentos.TemaDocumento;

import org.springframework.http.MediaType;

@RestController
@RequestMapping("/api/cursos/alumno")
public class CursoAlumnoController {

    @Autowired
    private CursoAlumnoService cursoService;

    @Autowired
    private CursoAlumnoImplement cursoAlumnoService;

    @Autowired
    private PdfGenerationService pdfGenerationService;

    /**
     * Endpoint para obtener los detalles de un curso por su ID.
     * 
     * Este método recibe un ID de curso como parámetro en la URL y retorna
     * la información detallada del curso en formato JSON.
     *
     * @param id ID del curso que se desea consultar. Debe ser un número entero
     *           positivo.
     * @return ResponseEntity con la información del curso si se encuentra,
     *         o un mensaje de error en caso de problemas.
     * 
     *         Posibles respuestas:
     *         - `200 OK`: Devuelve la información del curso en una lista de
     *         `CursoDetalleDTO`.
     *         - `400 Bad Request`: Si el ID proporcionado no es válido (nulo o
     *         menor o igual a 0).
     *         - `404 Not Found`: Si no existe un curso con el ID proporcionado.
     *         - `500 Internal Server Error`: Si ocurre un error inesperado en
     *         el servidor.
     */
    @GetMapping("/detalles/{id}")
    public ResponseEntity<?> obtenerDetalles(@PathVariable Integer id) {
        try {
            List<CursoDetalleAlumnoDTO> detalles = cursoService.getCurso(id);

            // Si la lista está vacía, significa que el curso no existe
            if (detalles.isEmpty()) {
                return ResponseEntity.status(404).body("No se encontró ningún curso con el ID " + id);
            }

            return ResponseEntity.ok(detalles);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error interno del servidor: " + e.getMessage());
        }
    }

    /**
     * Endpoint para inscribir a un alumno en un curso.
     * 
     * Este método recibe un ID de curso y un ID de alumno para procesar la
     * inscripción de un alumno en el curso.
     *
     * @param idCurso  ID del curso al que el alumno desea inscribirse.
     * @param idAlumno ID del alumno que desea inscribirse en el curso.
     * @return ResponseEntity con el mensaje de éxito o error en formato JSON.
     * 
     *         Posibles respuestas:
     *         - `200 OK`: Inscripción completada.
     *         - `400 Bad Request`: Si el alumno ya está inscrito o ha completado
     *         el curso.
     *         - `500 Internal Server Error`: Si ocurre un error al procesar
     *         la inscripción.
     */
    @GetMapping("/inscripcionCurso/{idCurso}/{idAlumno}")
    public ResponseEntity<Map<String, Object>> inscribirseCurso(@PathVariable Integer idCurso,
            @PathVariable Integer idAlumno) {
        Map<String, Object> response = new HashMap<>();

        Boolean inscripcionCurso = cursoService.inscribirseCurso(idAlumno, idCurso);

        if (inscripcionCurso != null && inscripcionCurso) {
            response.put("mensaje", "Inscripción al curso completada");
            return ResponseEntity.ok(response);
        } else if (inscripcionCurso != null && !inscripcionCurso) {
            response.put("mensaje", "El alumno ya está inscrito en este curso y/o acaba de completar el curso");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        } else {
            response.put("mensaje", "Error al procesar la inscripción");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * Endpoint para ver la puntuación de un alumno en un curso.
     * 
     * Este método recibe el ID de inscripción y devuelve las puntuaciones
     * obtenidas por el alumno.
     *
     * @param idInscricpion ID de inscripción del alumno en el curso.
     * @return ResponseEntity con la puntuación obtenida o un mensaje de error
     *         en caso de problemas.
     * 
     *         Posibles respuestas:
     *         - `200 OK`: Devuelve la lista de puntuaciones del alumno.
     *         - `404 Not Found`: Si no se encontraron puntuaciones para el alumno.
     *         - `500 Internal Server Error`: Si ocurre un error interno en
     *         el servidor.
     *         - `400 Bad Request`: Si la solicitud es incorrecta.
     */
    @GetMapping("/resultadoEvaluacion/{idInscricpion}")
    public ResponseEntity<?> verPuntuacion(@PathVariable Integer idInscricpion) {
        try {
            List<PuntuacionAlumnoDTO> puntuacionAlumnoDTOs = cursoService.verPuntuacion(idInscricpion);

            if (puntuacionAlumnoDTOs.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("No se encontraron puntuaciones para el alumno con la inscripción: " + idInscricpion);
            }

            return ResponseEntity.ok(puntuacionAlumnoDTOs);

        } catch (DataAccessException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error interno al procesar la solicitud: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Solicitud incorrecta: " + e.getMessage());
        }
    }

    /**
     * Endpoint para dar de baja a un alumno de un curso.
     * 
     * Este método recibe el ID de inscripción de un alumno y procesa la baja.
     *
     * @param idInscripcion ID de la inscripción del alumno que desea darse de baja.
     * @return ResponseEntity con el mensaje de éxito o error en formato JSON.
     * 
     *         Posibles respuestas:
     *         - `200 OK`: Baja procesada correctamente.
     *         - `404 Not Found`: Si el ID de inscripción no es válido o no se
     *         encuentra en los registros.
     *         - `500 Internal Server Error`: Si ocurre un error inesperado.
     */
    @GetMapping("bajaCurso/{idInscripcion}")
    public ResponseEntity<Map<String, Object>> bajaCursoAlumno(@PathVariable Integer idInscripcion) {
        Map<String, Object> response = new HashMap<>();

        try {
            String bajaCurso = cursoService.bajaCursoAlumno(idInscripcion);
            response.put("mensaje", bajaCurso);
            return ResponseEntity.ok(response);

        } catch (IllegalArgumentException e) {
            response.put("mensaje", "El ID de inscripción no es válido o no se encuentra en nuestros registros.");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        } catch (DataAccessException e) {
            response.put("mensaje", "Error en la base de datos al intentar procesar la baja.");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        } catch (Exception e) {
            response.put("mensaje", "Ocurrió un error inesperado.");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * Endpoint para generar el certificado de un alumno.
     * 
     * Este método recibe el ID de inscripción y genera un certificado en PDF
     * con los detalles del alumno y el curso.
     *
     * @param idInscripcion ID de la inscripción del alumno para el cual se desea
     *                      generar el certificado.
     * @return ResponseEntity con el archivo PDF del certificado o un mensaje
     *         de error en caso de problemas.
     * 
     *         Posibles respuestas:
     *         - `200 OK`: El archivo PDF del certificado se genera y se descarga.
     *         - `404 Not Found`: Si no se encontraron detalles para el certificado.
     *         - `500 Internal Server Error`: Si ocurre un error al generar el
     *         certificado.
     */
    @GetMapping("/certificado/{idInscripcion}")
    public ResponseEntity<byte[]> generarCertificado(@PathVariable Integer idInscripcion) {
        try {
            CertificadoDetalleDTO certificadoDetalles = cursoService.obtenerDetallesCertificado(idInscripcion);

            if (certificadoDetalles == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(("No se encontraron detalles para el certificado del alumno con la inscripción: "
                                + idInscripcion).getBytes());
            }

            byte[] pdfContent = pdfGenerationService.generarCertificado(certificadoDetalles);

            HttpHeaders headers = new HttpHeaders();
            headers.add("Content-Disposition", "attachment; filename=certificado_" + idInscripcion + ".pdf");

            return ResponseEntity.ok()
                    .headers(headers)
                    .contentType(MediaType.APPLICATION_PDF)
                    .body(pdfContent);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(("Error al generar el certificado: " + e.getMessage()).getBytes());
        }

    }

    /**
     * Endpoint para obtener los temas de un curso.
     * 
     * Este método recibe el ID de un curso y devuelve una lista de los temas
     * disponibles en ese curso.
     *
     * @param idCurso ID del curso cuyos temas se desean obtener.
     * @return ResponseEntity con los temas del curso o un mensaje de error
     *         en caso de problemas.
     * 
     *         Posibles respuestas:
     *         - `200 OK`: Devuelve la lista de temas en formato JSON.
     *         - `404 Not Found`: Si no se encontraron temas para el curso con el ID
     *         proporcionado.
     *         - `500 Internal Server Error`: Si ocurre un error al acceder a la
     *         base de datos.
     */
    @GetMapping("/temasCurso/{idCurso}")
    public ResponseEntity<?> getTemas(@PathVariable Integer idCurso) {
        try {
            List<TemaDocumento> temas = cursoService.getTemas(idCurso);

            if (temas == null || temas.isEmpty()) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "No se encontraron temas para el curso con ID: " + idCurso);
            }
            return ResponseEntity.ok(temas);
        } catch (DataAccessException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error al acceder a la base de datos",
                    e);
        } catch (ResponseStatusException e) {
            throw e;
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error inesperado", e);
        }
    }


    @GetMapping("/curso/nombre/{nombreCurso}")
    public ResponseEntity<?> buscarCursoNombre(@PathVariable String nombreCurso) {
        try {
            List<CursoNombreAlumnoDTO> cursos = cursoAlumnoService.getCursoNombre(nombreCurso);
            if (cursos.isEmpty()) {
                return ResponseEntity.status(404).body("Curso no encontrado");
            }
            return ResponseEntity.ok(cursos);  // Si se encuentran cursos, retornamos los datos
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error en la conexión: " + e.getMessage());
        }
    }

    /**
     * Endpoint para buscar un curso por su categoria.
     * @param nombreCurso
     * @return
     */
    @GetMapping("/curso/categoria/{categoria}")
    public ResponseEntity<?> buscarCursoCateogria(@PathVariable String categoria) {
        try {
            List<CursoNombreAlumnoDTO> cursos = cursoAlumnoService.getCursoCategoria(categoria);
            if (cursos.isEmpty()) {
                return ResponseEntity.status(404).body("Curso no encontrado");
            }
            return ResponseEntity.ok(cursos);  // Si se encuentran cursos, retornamos los datos
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error en la conexión: " + e.getMessage());
        }
    }



        @GetMapping("/continuarCurso/{idCurso}/{idUsuarioAlumno}")
    public ResponseEntity<List<ContinuarCursoDTO>> continuarCurso(
        @PathVariable Integer idCurso, 
        @PathVariable Integer idUsuarioAlumno
    ) {
        try{
        List<ContinuarCursoDTO> temasPendientes = cursoService.continuarCurso(idCurso, idUsuarioAlumno);
        if (temasPendientes == null || temasPendientes.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    "No se encontró el curso: " + idCurso);
        }
        return ResponseEntity.ok(temasPendientes);

    } catch (DataAccessException e) {
        throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error al acceder a la base de datos",
                e);
    } catch (ResponseStatusException e) {
        throw e;
    } catch (Exception e) {
        throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error inesperado", e);
    }
}
}
