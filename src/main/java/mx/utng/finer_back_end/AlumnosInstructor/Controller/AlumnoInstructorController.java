package mx.utng.finer_back_end.AlumnosInstructor.Controller;

import mx.utng.finer_back_end.AlumnosInstructor.Services.AlumnoInstructorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/alumnos-instructor")
public class AlumnoInstructorController {

    @Autowired
    private AlumnoInstructorService alumnoInstructorService;

    /**
     * Endpoint para verificar si el correo es válido.
     * @param correo Correo a verificar.
     * @return true si el correo es válido, false si no lo es.
     * @apiNote Este método se manda a llamar solo cuando el usuario-instructor
     * creo su cuenta.
     */ 
    @GetMapping("/verificar-correo")
    public boolean verificarCorreo(@RequestParam String correo) {
        return alumnoInstructorService.verificarCorreo(correo);
    }

   

    /**
     * Este método se encarga de hacer un registro en la tabla usuario
     * @return
     */
    @GetMapping("/crear-cuenta/instructor")
    public ResponseEntity<String> crearCuentaInstructor(){
        try {
            
            return ResponseEntity.ok("Cuenta registrada");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error de conexión" + e);
        }
    }
}
