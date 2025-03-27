package mx.utng.finer_back_end.Instructor.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import mx.utng.finer_back_end.Instructor.Documentos.CorreccionCursoDTO;
import mx.utng.finer_back_end.Instructor.Documentos.EdicionCategoriaDTO;

import java.util.Map;
import java.util.List;

@RestController
@RequestMapping("/api/solicitud-categoria")
public class SolicitudCategoriaInstructorController {
    
    @Autowired
    private JdbcTemplate jdbcTemplate;
    
    /**
     * Verifica el estado de una categoría solicitada por un instructor.
     * 
     * @param idInstructor ID del instructor que solicitó la categoría
     * @param estatus Estado a verificar ("aprobado", "rechazado" o "en revision")
     * @return ResponseEntity con el estado de la solicitud o mensaje de error
     */
    @GetMapping("/verificar/{idInstructor}/{estatus}")
    public ResponseEntity<?> verCategoriaSolicitada(
            @PathVariable("idInstructor") Integer idInstructor,
            @PathVariable("estatus") String estatus) {
        
        try {
            // Validar que el estatus sea válido según la constraint de la base de datos
            if (!estatus.equals("aprobada") && !estatus.equals("rechazada") && !estatus.equals("en revision")) {
                return ResponseEntity.badRequest()
                    .body(Map.of("error", "Estatus no válido. Debe ser 'aprobada', 'rechazada' o 'en revision'"));
            }
            
            String sql = "SELECT sc.id_solicitud_categoria, sc.estatus, sc.nombre_categoria, sc.descripcion " +
                        "FROM SolicitudCategoria sc " +
                        "WHERE sc.id_usuario_instructor = ? AND sc.estatus = ?";
            
            List<Map<String, Object>> results = jdbcTemplate.query(sql,
                (rs, rowNum) -> Map.of(
                    "idSolicitudCategoria", rs.getInt("id_solicitud_categoria"),
                    "estatus", rs.getString("estatus"),
                    "nombreCategoria", rs.getString("nombre_categoria"),
                    "descripcion", rs.getString("descripcion")
                ),
                idInstructor, estatus);
            
            // Si no hay resultados, devolver not found
            if (results.isEmpty()) {
                return ResponseEntity.notFound().build();
            }
            
            // Si hay resultados, devolver la lista
            return ResponseEntity.ok(results);
            
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                .body(Map.of("error", "Error al verificar la categoría: " + e.getMessage()));
        }
    }

/**
 * Procesa la corrección de un curso previamente rechazado.
 * 
 * @param idSolicitudCurso ID de la solicitud del curso
 * @param idInstructor ID del instructor que solicita la corrección
 * @param correccion DTO con los datos de corrección
 * @return ResponseEntity con el resultado de la operación
 */
@PostMapping("/corregir-curso/{idSolicitudCurso}/{idInstructor}")
public ResponseEntity<?> corregirCursoRechazado(
        @PathVariable("idSolicitudCurso") Integer idSolicitudCurso,
        @PathVariable("idInstructor") Integer idInstructor,
        @RequestBody CorreccionCursoDTO correccion) {
    
    try {
        // Verificamos que la solicitud existe y pertenece al instructor
        String verificarSolicitudSql = "SELECT estatus FROM SolicitudCurso WHERE id_solicitud_curso = ? AND id_usuario_instructor = ?";
        
        List<String> estatusResult = jdbcTemplate.query(verificarSolicitudSql,
                (rs, rowNum) -> rs.getString("estatus"),
                idSolicitudCurso, idInstructor);
        
        // Si no hay resultados, la solicitud no existe o no pertenece al instructor
        if (estatusResult.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(Map.of("error", "La solicitud de curso no existe o no pertenece al instructor indicado"));
        }
        
        // Obtener el estado de la solicitud
        String estatus = estatusResult.get(0);
        
        // Verificar que esté en estado "rechazada"
        if (!estatus.equals("rechazada")) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(Map.of("error", "La solicitud no está en estado 'rechazada'. Estado actual: " + estatus));
        }
        
        // Actualizamos la solicitud con las correcciones y cambiamos el estado
        String actualizarSql = "UPDATE SolicitudCurso " +
                             "SET titulo_curso_solicitado = ?, " +
                             "descripcion = ?, " +
                             "estatus = 'en revision' " +
                             "WHERE id_solicitud_curso = ? AND id_usuario_instructor = ?";
        
        int filasActualizadas = jdbcTemplate.update(
            actualizarSql,
            correccion.getTituloCurso(),
            correccion.getDescripcion(),
            idSolicitudCurso,
            idInstructor
        );
        
        if (filasActualizadas > 0) {
            return ResponseEntity.ok()
                .body(Map.of(
                    "mensaje", "Solicitud de curso actualizada correctamente y enviada a revisión",
                    "idSolicitud", idSolicitudCurso
                ));
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("error", "No se pudo actualizar la solicitud"));
        }
    } catch (Exception e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(Map.of("error", "Error al procesar la corrección: " + e.getMessage()));
    }
}
 /**
 * Permite editar una solicitud de categoría que fue rechazada.
 * 
 * @param edicion DTO con los datos de la edición
 * @return ResponseEntity con el resultado de la operación
 */
@PutMapping("/editar-categoria")
public ResponseEntity<?> editarCategoriaRechazada(@RequestBody EdicionCategoriaDTO edicion) {
    try {
        // Verificar que la solicitud existe y está en estado de rechazo
        String verificarSql = "SELECT estatus FROM SolicitudCategoria " +
                            "WHERE id_solicitud_categoria = ? AND id_usuario_instructor = ?";
        
        List<String> estatusResult = jdbcTemplate.query(verificarSql,
                (rs, rowNum) -> rs.getString("estatus"),
                edicion.getIdSolicitudCategoria(),
                edicion.getIdInstructor());
        
        // Si no hay resultados, la solicitud no existe o no pertenece al instructor
        if (estatusResult.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(Map.of("error", "La solicitud de categoría no existe o no pertenece al instructor indicado"));
        }
        
        // Obtener el estado de la solicitud
        String estatus = estatusResult.get(0);
        
        // Verificar que esté en estado "rechazada"
        if (!estatus.equals("rechazada")) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(Map.of("error", "La solicitud no está en estado 'rechazada'. Estado actual: " + estatus));
        }
        
        // Actualizar la solicitud con los nuevos datos
        String actualizarSql = "UPDATE SolicitudCategoria " +
                             "SET nombre_categoria = ?, " +
                             "descripcion = ?, " +
                             "estatus = 'en revision' " +
                             "WHERE id_solicitud_categoria = ? " +
                             "AND id_usuario_instructor = ?";
        
        int filasActualizadas = jdbcTemplate.update(
            actualizarSql,
            edicion.getNombreCategoria(),
            edicion.getDescripcion(),
            edicion.getIdSolicitudCategoria(),
            edicion.getIdInstructor()
        );
        
        if (filasActualizadas > 0) {
            return ResponseEntity.ok()
                .body(Map.of(
                    "mensaje", "Solicitud de categoría actualizada y enviada a revisión",
                    "idSolicitud", edicion.getIdSolicitudCategoria()
                ));
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("error", "No se pudo actualizar la solicitud de categoría"));
        }
    } catch (Exception e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(Map.of("error", "Error al procesar la edición: " + e.getMessage()));
    }
    
}
}