package mx.utng.finer_back_end.Instructor.Controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import mx.utng.finer_back_end.Instructor.DTO.SolicitudCursoEditarDTO;
import mx.utng.finer_back_end.Instructor.Services.SolicitudCursoEditarService;

@RestController
@RequestMapping("/api/solicitud-curso")
@CrossOrigin(origins = "*")  // Add this to allow cross-origin requests
public class SolicitudCursoEditarController {

    @Autowired
    private SolicitudCursoEditarService solicitudCursoEditarService;
    
    @Autowired
    private JdbcTemplate jdbcTemplate;

    /**
     * Endpoint para editar una solicitud de curso existente.
     * 
     * Este método permite a un instructor editar una solicitud de curso previamente rechazada.
     * Si la solicitud estaba en estado 'rechazada', se cambiará a 'en revision'.
     */
    @PutMapping("/editar")
    public ResponseEntity<Map<String, Object>> editarSolicitudCurso(@RequestBody SolicitudCursoEditarDTO solicitudDTO) {
        Map<String, Object> response = new HashMap<>();
        
        try {
            // Validar que los datos necesarios estén presentes
            if (solicitudDTO.getIdSolicitudCurso() == null || 
                solicitudDTO.getIdUsuarioInstructor() == null ||
                solicitudDTO.getTituloCursoSolicitado() == null || 
                solicitudDTO.getTituloCursoSolicitado().isEmpty() ||
                solicitudDTO.getIdCategoria() == null) {
                
                response.put("mensaje", "Los campos ID de solicitud, ID de instructor, título del curso y categoría son obligatorios");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
            }
            
            // Verificar que la solicitud existe
            Integer count = jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM solicitudcurso WHERE id_solicitud_curso = ?", 
                Integer.class, 
                solicitudDTO.getIdSolicitudCurso()
            );
            
            if (count == null || count == 0) {
                response.put("mensaje", "No existe una solicitud de curso con el ID proporcionado");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }
            
            // Verificar que el instructor que intenta editar es el propietario de la solicitud
            Integer instructorId = jdbcTemplate.queryForObject(
                "SELECT id_usuario_instructor FROM solicitudcurso WHERE id_solicitud_curso = ?",
                Integer.class,
                solicitudDTO.getIdSolicitudCurso()
            );
            
            if (!solicitudDTO.getIdUsuarioInstructor().equals(instructorId)) {
                response.put("mensaje", "No tiene permisos para editar esta solicitud. Solo el instructor que creó la solicitud puede editarla.");
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
            }
            
            // Llamar al servicio para editar la solicitud
            System.out.println("Intentando editar solicitud: " + solicitudDTO.getIdSolicitudCurso());
            System.out.println("Instructor ID: " + solicitudDTO.getIdUsuarioInstructor());
            System.out.println("Título: " + solicitudDTO.getTituloCursoSolicitado());
            System.out.println("Categoría: " + solicitudDTO.getIdCategoria());
            
            Boolean resultado = solicitudCursoEditarService.editarSolicitudCurso(solicitudDTO);
            
            System.out.println("Resultado de la edición: " + resultado);
            
            if (resultado) {
                response.put("mensaje", "Solicitud de curso editada exitosamente");
                return ResponseEntity.ok(response);
            } else {
                // Try to get more information about why it failed
                String estatus = jdbcTemplate.queryForObject(
                    "SELECT estatus FROM solicitudcurso WHERE id_solicitud_curso = ?",
                    String.class,
                    solicitudDTO.getIdSolicitudCurso()
                );
                
                System.out.println("Estatus actual de la solicitud: " + estatus);
                
                response.put("mensaje", "Error al procesar la solicitud. Por favor, inténtelo de nuevo más tarde.");
                response.put("detalle", "Estatus actual: " + estatus);
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
            }
            
        } catch (Exception e) {
            response.put("mensaje", "Error al editar la solicitud de curso");
            response.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
}