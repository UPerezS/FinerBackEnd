package mx.utng.finer_back_end.Administrador.Controller;

import mx.utng.finer_back_end.Administrador.Services.SolicitudCursoService;
import mx.utng.finer_back_end.Documentos.SolicitudCursoDocumento;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/solicitudes-curso")
public class SolicitudCursoController {

    @Autowired
    private SolicitudCursoService solicitudCursoService;

    // Endpoint para que el administrador consulte las solicitudes de cursos pendientes de aprobación
    @GetMapping("/pendientes")
    public ResponseEntity<List<SolicitudCursoDocumento>> consultarSolicitudesPendientes() {
        List<SolicitudCursoDocumento> solicitudesPendientes = solicitudCursoService.consultarSolicitudesPendientes();
        return ResponseEntity.ok(solicitudesPendientes);
    }

     // Endpoint para ejecutar manualmente la eliminación de solicitudes rechazadas
     @PostMapping("/eliminar-rechazadas")
     public ResponseEntity<String> eliminarSolicitudesRechazadas() {
         solicitudCursoService.eliminarSolicitudesCursoRechazadasAntiguas();
         return ResponseEntity.ok("Solicitudes rechazadas antiguas eliminadas con éxito.");
     }
}
