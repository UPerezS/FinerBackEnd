package mx.utng.finer_back_end.Alumnos.Services;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

@Service
public class AlumnoService {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    /**
     * Registra un nuevo alumno en la base de datos utilizando la función
     * registrar_alumno.
     * 
     * @param nombre          Nombre del alumno
     * @param apellidoPaterno Apellido paterno del alumno
     * @param apellidoMaterno Apellido materno del alumno
     * @param correo          Correo electrónico del alumno
     * @param contrasenia     Contraseña del alumno
     * @param nombreUsuario   Nombre de usuario del alumno
     * @return Mensaje de éxito o error
     */
    public ResponseEntity<Map<String, Object>> registrarAlumno(String nombre, String apellidoPaterno,
            String apellidoMaterno,
            String correo, String contrasenia, String nombreUsuario) {
        Map<String, Object> response = new HashMap<>();
        try {
            String sql = "SELECT registrar_alumno(?, ?, ?, ?, ?, ?)";
            String result = jdbcTemplate.queryForObject(sql, String.class, nombre, apellidoPaterno,
                    apellidoMaterno, correo, contrasenia, nombreUsuario);
            response.put("success", true);
            response.put("message", result);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error en la DB: " + e.getMessage());
            return ResponseEntity.status(500).body(response);
        }
    }

    /**
     * Actualiza la contraseña de un alumno en la base de datos utilizando su correo
     * electrónico.
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
                    nuevaContrasenia);
        } catch (Exception e) {
            System.err.println("Error al actualizar contraseña: " + e.getMessage());
            return "Error al actualizar la contraseña: " + e.getMessage();
        }
    }

    public String completarTema(String idInscripcion, String idTema) {
        try {
            String sql = "SELECT completar_tema(?, ?)";
            return jdbcTemplate.queryForObject(
                    sql,
                    String.class,
                    Integer.parseInt(idInscripcion),
                    Integer.parseInt(idTema));
        } catch (NumberFormatException e) {
            return "Error: ID de inscripción o ID de tema no es un número válido.";
        } catch (Exception e) {
            System.err.println("Error al completar tema: " + e.getMessage());
            return "Error al completar el tema: " + e.getMessage();
        }
    }

}
