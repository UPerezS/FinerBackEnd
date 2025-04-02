package mx.utng.finer_back_end.Administrador.Controller;

import mx.utng.finer_back_end.Administrador.Documentos.SolicitudCategoriaDatos;
import mx.utng.finer_back_end.Administrador.Implement.SolicitudCategoriaServiceImpl;
import mx.utng.finer_back_end.Documentos.SolicitudCategoriaDocumento;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("api/solicitudes")
public class SolicitudCategoriaController {

    @Autowired
    private SolicitudCategoriaServiceImpl solicitudCategoriaService;
    
     // Endpoint para obtener todas las solicitudes
     @GetMapping("/todas")
     public List<SolicitudCategoriaDatos> obtenerTodasLasSolicitudes() {
         return solicitudCategoriaService.obtenerTodasLasSolicitudes();
     }

     @PutMapping("/aprobar/{id}")
     public ResponseEntity<Map<String, Object>> aprobarCategoria(@PathVariable Integer id) {
         return solicitudCategoriaService.aprobarDesaprobarCategoria(id, true);
     }
     
     @PutMapping("/desaprobar/{id}")
     public ResponseEntity<Map<String, Object>> desaprobarCategoria(@PathVariable Integer id) {
         return solicitudCategoriaService.aprobarDesaprobarCategoria(id, false);
     }
    // Nuevo endpoint para obtener las solicitudes de un instructor
    @GetMapping("/instructor/{id}")
    public ResponseEntity<List<SolicitudCategoriaDocumento>> obtenerSolicitudesPorInstructor(@PathVariable Integer id) {
        List<SolicitudCategoriaDocumento> solicitudes = solicitudCategoriaService.obtenerSolicitudesPorInstructor(id);
        return ResponseEntity.ok(solicitudes);
    }

     // Endpoint para ejecutar manualmente la eliminación de solicitudes rechazadas
     @PostMapping("/eliminar-rechazadas")
     public ResponseEntity<String> eliminarSolicitudesRechazadas() {
         solicitudCategoriaService.eliminarSolicitudesCategoriaRechazadasAntiguas();
         return ResponseEntity.ok("Solicitudes rechazadas antiguas eliminadas con éxito.");
     }
}
