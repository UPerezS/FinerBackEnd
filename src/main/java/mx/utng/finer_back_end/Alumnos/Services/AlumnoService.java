package mx.utng.finer_back_end.Alumnos.Services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

@Service
public class AlumnoService {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    /**
     * Registra un nuevo alumno en la base de datos utilizando la función registrar_alumno.
     * 
     * @param nombre           Nombre del alumno
     * @param apellidoPaterno  Apellido paterno del alumno
     * @param apellidoMaterno  Apellido materno del alumno
     * @param correo           Correo electrónico del alumno
     * @param contrasenia      Contraseña del alumno
     * @param nombreUsuario    Nombre de usuario del alumno
     * @return Mensaje de éxito o error
     */
    public ResponseEntity<String> registrarAlumno(String nombre, String apellidoPaterno, String apellidoMaterno,
                                  String correo, String contrasenia, String nombreUsuario) {
        try {
            String sql = "SELECT registrar_alumno(?, ?, ?, ?, ?, ?)";
            String result = jdbcTemplate.queryForObject(sql, String.class, nombre, apellidoPaterno,
                    apellidoMaterno, correo, contrasenia, nombreUsuario);
            return ResponseEntity.ok(result); // Mensaje de respuesta de la función
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error en la DB " + e.getMessage());
        }
    }

     /**
     * Actualiza la contraseña de un alumno en la base de datos utilizando su correo electrónico.
     * 
     * @param correo           Correo electrónico del alumno
     * @param nuevaContrasenia Nueva contraseña a establecer
     * @return Mensaje indicando el resultado de la operación
     */
    public String actualizarContrasenia(String correo, String nuevaContrasenia) {
        try {
            String sql = "SELECT actualizar_contrasenia(?, ?)";
            return jdbcTemplate.queryForObject(
                sql, 
                String.class,
                correo,
                nuevaContrasenia
            );
        } catch (Exception e) {
            System.err.println("Error al actualizar contraseña: " + e.getMessage());
            return "Error al actualizar la contraseña: " + e.getMessage();
        }
    }
}
