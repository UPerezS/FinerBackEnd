package mx.utng.finer_back_end.Administrador.Controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import mx.utng.finer_back_end.Administrador.Services.AdministradorService;

@RestController
@RequestMapping("/api/administrador")
public class AdministradorController {

    @Autowired
    private AdministradorService administradorService;

    /**
     * Endpoint para eliminar un alumno de un curso específico.
     * 
     * Este método verifica si el alumno seleccionado está inscrito en algún curso.
     * En caso de que lo esté, permite su eliminación del curso. Para lograrlo, se elimina
     * el registro correspondiente en la tabla de inscripciones, utilizando la matrícula
     * y el id del curso como referencia. Como resultado, el alumno queda desvinculado del curso.
     *
     * @param obj Objeto que contiene la matrícula del alumno (matricula) y el ID del curso (idCurso)
     * @return ResponseEntity con el mensaje de éxito o error en formato JSON.
     * 
     *         Posibles respuestas:
     *         - `200 OK`: Eliminación completada correctamente.
     *         - `404 Not Found`: Si el alumno no está inscrito en el curso.
     *         - `500 Internal Server Error`: Si ocurre un error al procesar la eliminación.
     */
    @PostMapping("/eliminarCursoAlumno")
    public ResponseEntity<Map<String, Object>> eliminarCursoAlumno(@RequestBody Map<String, Object> obj) {
        Map<String, Object> response = new HashMap<>();
        
        try {
            // Extraer los valores del objeto recibido
            String matricula = (String) obj.get("matricula");
            Integer idCurso = Integer.parseInt(obj.get("idCurso").toString());
            
            // Validar que los datos necesarios estén presentes
            if (matricula == null || matricula.isEmpty() || idCurso == null) {
                response.put("mensaje", "La matrícula y el ID del curso son obligatorios");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
            }
            
            // Llamar al servicio para eliminar al alumno del curso
            String resultado = administradorService.eliminarAlumnoCurso(matricula, idCurso);
            
            // Verificar el resultado
            if (resultado.equals("El alumno no está inscrito en este curso.")) {
                response.put("mensaje", resultado);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            } else if (resultado.equals("Alumno eliminado exitosamente del curso.")) {
                response.put("mensaje", resultado);
                return ResponseEntity.ok(response);
            } else {
                response.put("mensaje", "Error al procesar la solicitud: " + resultado);
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
            }
            
        } catch (DataAccessException e) {
            response.put("mensaje", "Error en la base de datos al intentar eliminar al alumno del curso");
            response.put("error", e.getMessage() + ": " + e.getMostSpecificCause().getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        } catch (NumberFormatException e) {
            response.put("mensaje", "El ID del curso debe ser un número entero válido");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        } catch (Exception e) {
            response.put("mensaje", "Error al procesar la solicitud");
            response.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
    
    /**
     * Endpoint para rechazar una solicitud de curso.
     * 
     * Este método permite al administrador rechazar un curso de los que están listados
     * para revisión. Al rechazar el curso, se cambia su estatus a 'rechazado' en la base
     * de datos y se envía un correo al instructor con el mensaje correspondiente.
     *
     * @param obj Objeto que contiene el ID de la solicitud de curso (idSolicitudCurso),
     *            el correo del instructor (correoInstructor) y el motivo del rechazo (motivoRechazo)
     * @return ResponseEntity con el mensaje de éxito o error en formato JSON.
     * 
     *         Posibles respuestas:
     *         - `200 OK`: Rechazo completado correctamente.
     *         - `404 Not Found`: Si no se encuentra la solicitud de curso.
     *         - `400 Bad Request`: Si faltan datos requeridos.
     *         - `500 Internal Server Error`: Si ocurre un error al procesar el rechazo.
     */
    @PostMapping("/rechazarCurso")
    public ResponseEntity<Map<String, Object>> rechazarCurso(@RequestBody Map<String, Object> obj) {
        Map<String, Object> response = new HashMap<>();
        
        try {
            // Extraer los valores del objeto recibido
            Long idSolicitudCurso = Long.parseLong(obj.get("idSolicitudCurso").toString());
            String correoInstructor = (String) obj.get("correoInstructor");
            String motivoRechazo = (String) obj.get("motivoRechazo");
            String tituloCurso = (String) obj.get("tituloCurso");
            
            // Validar que los datos necesarios estén presentes
            if (idSolicitudCurso == null || correoInstructor == null || correoInstructor.isEmpty() || 
                motivoRechazo == null || motivoRechazo.isEmpty() || tituloCurso == null || tituloCurso.isEmpty()) {
                response.put("mensaje", "El ID de la solicitud, el correo del instructor, el título del curso y el motivo del rechazo son obligatorios");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
            }
            
            // Llamar al servicio para rechazar el curso
            String resultado = administradorService.rechazarCurso(idSolicitudCurso, correoInstructor, motivoRechazo, tituloCurso);
            
            // Verificar el resultado
            if (resultado.equals("No se encontraron datos")) {
                response.put("mensaje", "No se encontró la solicitud de curso con el ID proporcionado");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            } else if (resultado.equals("Rechazado")) {
                response.put("mensaje", "Solicitud de curso rechazada exitosamente");
                return ResponseEntity.ok(response);
            } else {
                response.put("mensaje", "Error al procesar la solicitud: " + resultado);
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
            }
            
        } catch (DataAccessException e) {
            response.put("mensaje", "Error en la base de datos al intentar rechazar el curso");
            response.put("error", e.getMessage() + ": " + e.getMostSpecificCause().getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        } catch (NumberFormatException e) {
            response.put("mensaje", "El ID de la solicitud de curso debe ser un número válido");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        } catch (Exception e) {
            response.put("mensaje", "Error al procesar la solicitud");
            response.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
    
    /**
     * Endpoint para solicitar la creación de una nueva categoría.
     * 
     * Este método se usa para registrar una nueva solicitud de categoría. Recibe los datos
     * necesarios como el ID del instructor, ID del administrador, nombre de la categoría y
     * descripción. La solicitud es procesada y almacenada en la tabla SolicitudCategoria.
     *
     * @param obj Objeto que contiene los datos de la solicitud (idUsuarioInstructor, 
     *            idUsuarioAdmin, nombreCategoria, descripcion)
     * @return ResponseEntity con el mensaje de éxito o error en formato JSON.
     * 
     *         Posibles respuestas:
     *         - `201 Created`: Solicitud creada correctamente.
     *         - `400 Bad Request`: Si faltan datos requeridos.
     *         - `500 Internal Server Error`: Si ocurre un error al procesar la solicitud.
     */
    @PostMapping("/crearCategoria")
    public ResponseEntity<Map<String, Object>> crearCategoria(@RequestBody Map<String, Object> obj) {
        Map<String, Object> response = new HashMap<>();
        
        try {
            // Extraer los valores del objeto recibido
            Integer idUsuarioInstructor = Integer.parseInt(obj.get("idUsuarioInstructor").toString());
            Integer idUsuarioAdmin = Integer.parseInt(obj.get("idUsuarioAdmin").toString());
            String nombreCategoria = (String) obj.get("nombreCategoria");
            String descripcion = (String) obj.get("descripcion");
            
            // Validar que los datos necesarios estén presentes
            if (idUsuarioInstructor == null || idUsuarioAdmin == null || 
                nombreCategoria == null || nombreCategoria.isEmpty()) {
                response.put("mensaje", "El ID del instructor, ID del administrador y nombre de la categoría son obligatorios");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
            }
            
            // Llamar al servicio para crear la solicitud de categoría
            String resultado = administradorService.crearCategoria(idUsuarioInstructor, idUsuarioAdmin, nombreCategoria, descripcion);
            
            // Verificar el resultado - MODIFICADO PARA RECONOCER EL MENSAJE DE ÉXITO CORRECTO
            if (resultado.contains("enviada correctamente")) {
                response.put("mensaje", resultado);
                return ResponseEntity.status(HttpStatus.CREATED).body(response);
            } else {
                response.put("mensaje", "Error al procesar la solicitud: " + resultado);
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
            }
            
        } catch (DataAccessException e) {
            response.put("mensaje", "Error en la base de datos al intentar crear la solicitud de categoría");
            response.put("error", e.getMessage() + ": " + e.getMostSpecificCause().getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        } catch (NumberFormatException e) {
            response.put("mensaje", "Los IDs deben ser números enteros válidos");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        } catch (Exception e) {
            response.put("mensaje", "Error al procesar la solicitud");
            response.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
}