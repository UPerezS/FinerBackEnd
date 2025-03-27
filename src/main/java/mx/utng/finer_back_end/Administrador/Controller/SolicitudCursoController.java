package mx.utng.finer_back_end.Administrador.Controller;

import mx.utng.finer_back_end.Administrador.Services.SolicitudCursoService;
import mx.utng.finer_back_end.Documentos.SolicitudCursoDocumento;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador REST que gestiona las operaciones relacionadas con las solicitudes de cursos.
 * Proporciona endpoints para que los administradores puedan consultar y gestionar
 * las solicitudes de cursos pendientes de aprobación.
 */
@RestController
@RequestMapping("/api/solicitudes-curso")
public class SolicitudCursoController {

    @Autowired
    private SolicitudCursoService solicitudCursoService;

    /**
     * Endpoint para que el administrador consulte las solicitudes de cursos pendientes de aprobación.
     * Recupera todas las solicitudes de cursos que tienen el estatus 'en revision'.
     *
     * @return ResponseEntity con una lista de documentos de solicitudes de cursos pendientes
     *         y código de estado HTTP 200 (OK) si la operación es exitosa.
     */
    @GetMapping("/pendientes")
    public ResponseEntity<List<SolicitudCursoDocumento>> consultarSolicitudesPendientes() {
        List<SolicitudCursoDocumento> solicitudesPendientes = solicitudCursoService.consultarSolicitudesPendientes();
        return ResponseEntity.ok(solicitudesPendientes);
    }

    /**
     * Endpoint para ejecutar manualmente la eliminación de solicitudes de cursos rechazadas.
     * Este método permite a los administradores eliminar manualmente las solicitudes de cursos
     * que han sido rechazadas y tienen más de 30 días de antigüedad.
     *
     * @return ResponseEntity con un mensaje de confirmación y código de estado HTTP 200 (OK)
     *         si la operación es exitosa.
     */
     @PostMapping("/eliminar-rechazadas")
     public ResponseEntity<String> eliminarSolicitudesRechazadas() {
         solicitudCursoService.eliminarSolicitudesCursoRechazadasAntiguas();
         return ResponseEntity.ok("Solicitudes rechazadas antiguas eliminadas con éxito.");
     }
}
