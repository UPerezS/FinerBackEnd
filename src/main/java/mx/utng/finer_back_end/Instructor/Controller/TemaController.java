package mx.utng.finer_back_end.Instructor.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import mx.utng.finer_back_end.Instructor.Services.TemaService;

@RestController
@RequestMapping("/api/tema")
public class TemaController {

    @Autowired
    private TemaService temaService;

    /**
     * @param idSolicitudCurso /int/
     * @param nombreTema       /char/
     * @param contenido        /String/
     * @return mensaje de éxito o error
     * 
     * 
     * 
     * 
     */
    @PostMapping("/crear-tema")
    public ResponseEntity<String> crearTema(
            @RequestParam int idSolicitudCurso,
            @RequestParam String nombreTema,
            @RequestParam String contenido) {

        try {
            ResponseEntity<String> mensaje = temaService.registrarTema(idSolicitudCurso, nombreTema, contenido);
            return mensaje;
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error de conexión" + e.getMessage());
        }
    }

}
