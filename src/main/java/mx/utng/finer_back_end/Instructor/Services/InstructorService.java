package mx.utng.finer_back_end.Instructor.Services;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;




@Service
public class InstructorService {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    /**
     * Registra un nuevo instructor en la base de datos utilizando la función registrar_instructor.
     * 
     * @param nombre           Nombre del instructor
     * @param apellidoPaterno  Apellido paterno del instructor
     * @param apellidoMaterno  Apellido materno del instructor
     * @param correo           Correo electrónico del instructor
     * @param contrasenia      Contraseña del instructor
     * @param nombreUsuario    Nombre de usuario del instructor
     * @param telefono         Teléfono del instructor
     * @param direccion        Dirección del instructor
     * @param cedula           Cédula del instructor
     * @return Mensaje de éxito o error 
     */
    public ResponseEntity<String> registrarInstructor(String nombre, String apellidoPaterno, String apellidoMaterno,
                                  String correo, String contrasenia, String nombreUsuario, String telefono, String direccion,  String cedulaPdf) {
        try {
            String sql = "SELECT registrar_instructor(?, ?, ?, ?, ?, ?, ?, ?, ?)";
            String result = jdbcTemplate.queryForObject(sql, String.class, nombre, apellidoPaterno,
                    apellidoMaterno, correo, contrasenia, nombreUsuario, telefono, direccion, cedulaPdf);
            return ResponseEntity.ok(result); // Mensaje de respuesta de la función
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error en la DB: " + e.getMessage());
        }
    }

    public String obtenerNombreInstructor(int idInstructor) {
        try {
            // Modificamos la consulta para obtener el nombre completo solo si el usuario tiene rol = 2
            String sql = "SELECT CONCAT(nombre, ' ', apellido_paterno, ' ', apellido_materno) " +
                         "FROM usuario WHERE id_usuario = ? AND id_rol = 2";
            String nombreInstructor = jdbcTemplate.queryForObject(sql, String.class, idInstructor);
            
            // Agregar un log para verificar el valor obtenido
            System.out.println("Nombre del instructor: " + nombreInstructor);
            
            return nombreInstructor;
        } catch (Exception e) {
            System.err.println("Error al obtener el nombre del instructor: " + e.getMessage());
            return null;
        }
    }
    
    
 
}
