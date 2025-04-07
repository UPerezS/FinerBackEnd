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

import mx.utng.finer_back_end.Instructor.DTO.SolicitudTemaEditarDTO;
import mx.utng.finer_back_end.Instructor.Services.SolicitudTemaEditarService;

@RestController
@RequestMapping("/api/solicitud-tema")
@CrossOrigin(origins = "*")
public class SolicitudTemaEditarController {

    @Autowired
    private SolicitudTemaEditarService solicitudTemaEditarService;
    
    @Autowired
    private JdbcTemplate jdbcTemplate;

    /**
     * Endpoint para editar un tema de solicitud de curso.
     * 
     * Este método permite a un instructor editar un tema existente o crear uno nuevo
     * si el ID proporcionado no existe.
     */
    @PutMapping("/editar")
    public ResponseEntity<Map<String, Object>> editarSolicitudTema(@RequestBody SolicitudTemaEditarDTO solicitudTemaDTO) {
        Map<String, Object> response = new HashMap<>();
        
        try {
            // Validar que los datos necesarios estén presentes
            if (solicitudTemaDTO.getIdSolicitudTema() == null || 
                solicitudTemaDTO.getIdSolicitudCurso() == null ||
                solicitudTemaDTO.getNombreTema() == null || 
                solicitudTemaDTO.getNombreTema().isEmpty() ||
                solicitudTemaDTO.getContenido() == null || 
                solicitudTemaDTO.getContenido().isEmpty()) {
                
                response.put("mensaje", "Los campos ID de tema, ID de solicitud de curso, nombre del tema y contenido son obligatorios");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
            }
            
            // Verificar que la solicitud de curso existe
            Integer count = jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM solicitudcurso WHERE id_solicitud_curso = ?", 
                Integer.class, 
                solicitudTemaDTO.getIdSolicitudCurso()
            );
            
            if (count == null || count == 0) {
                response.put("mensaje", "No existe una solicitud de curso con el ID proporcionado");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }
            
            // Llamar al servicio para editar el tema
            Boolean resultado = solicitudTemaEditarService.editarSolicitudTema(solicitudTemaDTO);
            
            if (resultado) {
                response.put("mensaje", "Tema editado exitosamente");
                return ResponseEntity.ok(response);
            } else {
                response.put("mensaje", "Error al procesar la solicitud. Por favor, inténtelo de nuevo más tarde.");
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
            }
            
        } catch (Exception e) {
            response.put("mensaje", "Error al editar el tema");
            response.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
}