package mx.utng.finer_back_end.Instructor.Controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import mx.utng.finer_back_end.Documentos.SolicitudTemaDocumento;
import mx.utng.finer_back_end.Instructor.Documentos.CursoSolicitadoDTOInstructor;
import mx.utng.finer_back_end.Instructor.Documentos.SolicitudCursoRequest;
import mx.utng.finer_back_end.Instructor.Services.SolicitudCursoServiceInstructor;

@RestController
@RequestMapping("/api/cursos")
public class SolicitudCursoControllerInstructor {
    @Autowired
    private SolicitudCursoServiceInstructor solicitudCursoService;

    /**
     * Endpoint para ver los cursos solicitados filtrados por estatus y el
     * instructor.
     * 
     * @param estatus      Estatus de la solicitud de curso ('aprobado',
     *                     'rechazado', 'en revision').
     * @param idInstructor ID del instructor cuya solicitud de cursos se quiere
     *                     consultar.
     * @return ResponseEntity con la lista de cursos solicitados o un mensaje de
     *         error.
     */
    @GetMapping("/ver-solicitudes")
    public ResponseEntity<?> verCursosSolicitados(@RequestParam String estatus, @RequestParam Integer idInstructor) {
        try {
            List<CursoSolicitadoDTOInstructor> cursosSolicitados = solicitudCursoService.verCursosSolicitados(estatus,
                    idInstructor);

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

    @PutMapping("/solicitud-curso/editar")
    public ResponseEntity<?> editarSolicitudCurso(@RequestBody SolicitudCursoRequest request) {
        Map<String, Object> response = solicitudCursoService.editarSolicitudCurso(request);
        if ("success".equals(response.get("estatus"))) {
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @PutMapping("/solicitud-curso/enviar-a-revision")
    public ResponseEntity<?> enviarARevision(@RequestParam Integer id_solicitud_curso) {
        Map<String, Object> response = solicitudCursoService.enviarARevision(id_solicitud_curso);
        if ("success".equals(response.get("estatus"))) {
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @GetMapping("/ver-solicitudes/tema")
    public ResponseEntity<?> verTemasSolicitadosByIdCurso(@RequestParam Integer idSolicitudCurso) {
        try {
            List<SolicitudTemaDocumento> temasSolicitados = solicitudCursoService
                    .verSolicitudTemaByIDCurso(idSolicitudCurso);

            if (temasSolicitados.isEmpty()) {
                Map<String, Object> response = new HashMap<>();
                response.put("mensaje", "No se encontraron temas solicitados para este curso.");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }

            return ResponseEntity.ok(temasSolicitados);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("mensaje", "Error al obtener los cursos solicitados: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

}
