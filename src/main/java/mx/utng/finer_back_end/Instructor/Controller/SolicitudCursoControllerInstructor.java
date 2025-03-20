package mx.utng.finer_back_end.Instructor.Controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import mx.utng.finer_back_end.Instructor.Documentos.CursoSolicitadoDTOInstructor;
import mx.utng.finer_back_end.Instructor.Services.SolicitudCursoServiceInstructor;

@RestController
@RequestMapping("/api/cursos")
public class SolicitudCursoControllerInstructor {
    @Autowired
    private SolicitudCursoServiceInstructor solicitudCursoService;

    /**
     * Endpoint para ver los cursos solicitados filtrados por estatus y el instructor.
     * 
     * @param estatus Estatus de la solicitud de curso ('aprobado', 'rechazado', 'en revision').
     * @param idInstructor ID del instructor cuya solicitud de cursos se quiere consultar.
     * @return ResponseEntity con la lista de cursos solicitados o un mensaje de error.
     */
    @GetMapping("/ver-solicitudes")
    public ResponseEntity<?> verCursosSolicitados(@RequestParam String estatus, @RequestParam Integer idInstructor) {
        try {
            List<CursoSolicitadoDTOInstructor> cursosSolicitados = solicitudCursoService.verCursosSolicitados(estatus, idInstructor);

            if (cursosSolicitados.isEmpty()) {
                Map<String, Object> response = new HashMap<>();
                response.put("mensaje", "No se encontraron cursos solicitados con ese estatus para el instructor.");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }

            return ResponseEntity.ok(cursosSolicitados);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("mensaje", "Error al obtener los cursos solicitados: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

}
