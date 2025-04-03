package mx.utng.finer_back_end.Instructor.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import mx.utng.finer_back_end.Instructor.Services.InstructorModificarService;
import mx.utng.finer_back_end.Instructor.Services.InstructorService;

@RestController
@RequestMapping("/api/instructor")
public class InstructorController {

    @Autowired
    private InstructorService instructorService;
    @Autowired
    InstructorModificarService instructorModificarService;

    /**
     * Este método se encarga de hacer un registro en la tabla Usuario utilizando la
     * función registrar_instructor.
     * Recibe los datos desde el frontend y los inserta en la base de datos.
     * 
     * @param nombre          Nombre del instructor
     * @param apellidoPaterno Apellido paterno del instructor
     * @param apellidoMaterno Apellido materno del instructor
     * @param correo          Correo electrónico del instructor
     * @param contrasenia     Contraseña del instructorgit
     * @param nombreUsuario   Nombre de usuario del instructor
     * @param telefono        Telefono de usuario del instructor
     * @param direccion       Dirección del instructor
     * @param cedula          Cédula del instructor
     * @return Respuesta con el mensaje de éxito o error
     */
    @PostMapping("/crear-cuenta")
    public ResponseEntity<String> crearCuentainstructor(@RequestParam String nombre,
            @RequestParam String apellidoPaterno,
            @RequestParam String apellidoMaterno,
            @RequestParam String correo,
            @RequestParam String contrasenia,
            @RequestParam String nombreUsuario,
            @RequestParam String telefono,
            @RequestParam String direccion,
            @RequestParam String cedula) {
        try {

            ResponseEntity<String> mensaje = instructorService.registrarInstructor(nombre, apellidoPaterno,
                    apellidoMaterno, correo, contrasenia, nombreUsuario, telefono, direccion, cedula);
            return mensaje;
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error de conexión: " + e.getMessage());
        }
    }

    @PutMapping("/editar-cuenta")
    public ResponseEntity<String> actualizarPerfilInstructor(
            @RequestParam Integer idUsuario,
            @RequestParam String nombre, 
            @RequestParam String apellidoPaterno,
            @RequestParam String apellidoMaterno,
            @RequestParam String correo,
            @RequestParam String nombreUsuario,
            @RequestParam String telefono,
            @RequestParam String direccion,
            @RequestParam String contrasenia,
            @RequestParam Boolean actualizar_contrasenia
            ) {
        try {
            ResponseEntity<String> mensaje = instructorModificarService.actualizarPerfilInstructor(idUsuario, nombre,
                    apellidoPaterno, apellidoMaterno,
                    nombreUsuario, correo, telefono, direccion, contrasenia,actualizar_contrasenia);
            return mensaje;
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error de conexión" + e.getMessage());
        }

    }

}
