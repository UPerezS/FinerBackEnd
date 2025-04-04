package mx.utng.finer_back_end.Alumnos.Controller;

import mx.utng.finer_back_end.Alumnos.Services.AlumnoModificarService;
import mx.utng.finer_back_end.Alumnos.Services.AlumnoService;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/alumno")
public class AlumnoController {

    @Autowired
    private AlumnoService alumnoService;
    @Autowired
    private AlumnoModificarService alumnoModificarService;

    /**
     * Este método se encarga de hacer un registro en la tabla Usuario utilizando la
     * función registrar_alumno.
     * Recibe los datos desde el frontend y los inserta en la base de datos.
     * 
     * @param nombre          Nombre del alumno
     * @param apellidoPaterno Apellido paterno del alumno
     * @param apellidoMaterno Apellido materno del alumno
     * @param correo          Correo electrónico del alumno
     * @param contrasenia     Contraseña del alumno
     * @param nombreUsuario   Nombre de usuario del alumno
     * @return Respuesta con el mensaje de éxito o error
     */
    @PostMapping("/crear-cuenta")
    public ResponseEntity<Map<String, Object>> crearCuentaAlumno(
            @RequestParam String nombre,
            @RequestParam String apellidoPaterno,
            @RequestParam String apellidoMaterno,
            @RequestParam String correo,
            @RequestParam String contrasenia,
            @RequestParam String nombreUsuario) {

        try {
            return alumnoService.registrarAlumno(nombre, apellidoPaterno, apellidoMaterno, correo, contrasenia,
                    nombreUsuario);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "Error de conexión: " + e.getMessage());
            return ResponseEntity.status(500).body(response);
        }
    }

    @PutMapping("/editar-cuenta")
    public ResponseEntity<Map<String, Object>> actualizarPerfilAlumno(
            @RequestParam Integer idUsuario,
            @RequestParam String nombre,
            @RequestParam String apellidoPaterno,
            @RequestParam String apellidoMaterno,
            @RequestParam String correo,
            @RequestParam String contrasenia,
            @RequestParam String nombreUsuario,
            @RequestParam Boolean actualizar_contrasenia) {

        try {
            return alumnoModificarService.actualizarPerfilAlumno(
                    idUsuario, nombre, apellidoPaterno, apellidoMaterno,
                    nombreUsuario, correo, contrasenia, actualizar_contrasenia);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "Error de conexión: " + e.getMessage());
            return ResponseEntity.status(500).body(response);
        }
    }

    /**
     * Este método permite actualizar la contraseña de un alumno utilizando su
     * correo electrónico.
     * 
     * @param correo           Correo electrónico del alumno
     * @param nuevaContrasenia Nueva contraseña del alumno
     * @return ResponseEntity<String> con el mensaje de éxito o error
     */
    @PutMapping("/actualizar-contrasenia")
    public ResponseEntity<String> actualizarContrasenia(
            @RequestParam String correo,
            @RequestParam String nuevaContrasenia) {
        try {
            String resultado = alumnoService.actualizarContrasenia(correo, nuevaContrasenia);
            return ResponseEntity.ok(resultado);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error al actualizar la contraseña: " + e.getMessage());
        }
    }

}
