package mx.utng.finer_back_end.Alumnos.Controller;

import mx.utng.finer_back_end.Alumnos.Services.AlumnoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/alumno")
public class AlumnoController {

    @Autowired
    private AlumnoService alumnoService;

    /**
     * Este método se encarga de hacer un registro en la tabla Usuario utilizando la función registrar_alumno.
     * Recibe los datos desde el frontend y los inserta en la base de datos.
     * 
     * @param nombre           Nombre del alumno
     * @param apellidoPaterno  Apellido paterno del alumno
     * @param apellidoMaterno  Apellido materno del alumno
     * @param correo           Correo electrónico del alumno
     * @param contrasenia      Contraseña del alumno
     * @param nombreUsuario    Nombre de usuario del alumno
     * @return Respuesta con el mensaje de éxito o error
     */
    @PostMapping("/crear-cuenta")
    public ResponseEntity<String> crearCuentaAlumno(@RequestParam String nombre,
                                                    @RequestParam String apellidoPaterno,
                                                    @RequestParam String apellidoMaterno,
                                                    @RequestParam String correo,
                                                    @RequestParam String contrasenia,
                                                    @RequestParam String nombreUsuario) {
        try {
            ResponseEntity<String> mensaje = alumnoService.registrarAlumno(nombre, apellidoPaterno, apellidoMaterno, correo, contrasenia, nombreUsuario);
            return mensaje;
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error de conexión: " + e.getMessage());
        }
    }
}
