package mx.utng.finer_back_end.AlumnosInstructor.Controller;

import mx.utng.finer_back_end.AlumnosInstructor.Documentos.CursoDetalleProgresoDTO;
import mx.utng.finer_back_end.AlumnosInstructor.Services.AlumnoInstructorService;
import mx.utng.finer_back_end.AlumnosInstructor.Services.ProgresoAlumnoService;

import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/alumnos-instructor")
public class AlumnoInstructorController {

    @Autowired
    private AlumnoInstructorService alumnoInstructorService;

    @Autowired
    private ProgresoAlumnoService progresoAlumnoService;

    /**
     * Endpoint para verificar si el correo es válido.
     * 
     * @param correo Correo a verificar.
     * @return true si el correo es válido, false si no lo es.
     * @apiNote Este método se manda a llamar solo cuando el usuario-instructor
     *          creo su cuenta.
     */
    @GetMapping("/verificar-correo")
    public boolean verificarCorreo(@RequestParam String correo) {
        return alumnoInstructorService.verificarCorreo(correo);
    }

    /**
     * Este método se encarga de hacer un registro en la tabla usuario
     * 
     * @return
     */
    @GetMapping("/crear-cuenta/instructor")
    public ResponseEntity<String> crearCuentaInstructor() {
        try {

            return ResponseEntity.ok("Cuenta registrada");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error de conexión" + e);
        }
    }

    @GetMapping("/progresoCurso/{idEstudiante}/{idCurso}")
    public ResponseEntity<?> verProgresoAlumno(@PathVariable Integer idEstudiante, @PathVariable Integer idCurso) {
        try {
            List<CursoDetalleProgresoDTO> progreso = progresoAlumnoService.verProgresoAlumno(idEstudiante, idCurso);
    
            if (progreso.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No se encontró porcentaje");
            }
    
            return ResponseEntity.ok(progreso.get(0).getVPorcentaje());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error interno del servidor");
        }
    }
}