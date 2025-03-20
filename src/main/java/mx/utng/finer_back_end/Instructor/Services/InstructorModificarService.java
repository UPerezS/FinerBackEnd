package mx.utng.finer_back_end.Instructor.Services;

import java.sql.CallableStatement;
import java.sql.Connection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

@Service
public class InstructorModificarService {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    public ResponseEntity<String> actualizarPerfilInstuctor(Integer idUsuario, String nombre, String apellidoPaterno,
            String apellidoMaterno, String correo, String nombreUsuario, String telefono, String direccion) {
        try (Connection connection = jdbcTemplate.getDataSource().getConnection()) {

            String sql = "UPDATE Usuario SET nombre = ?, apellido_paterno=?, " +
                    "apellido_materno = ?, nombre_usuario = ?, correo =?," +
                    "telefono = ?, direccion = ? WHERE id_usuario = ?";
            try (CallableStatement statement = connection.prepareCall(sql)) {
                statement.setString(1, nombre);
                statement.setString(2, apellidoPaterno);
                statement.setString(3, apellidoMaterno);
                statement.setString(4, nombreUsuario);
                statement.setString(5, correo);
                statement.setString(6, telefono);
                statement.setString(7, direccion);
                statement.setInt(8, idUsuario);

                int rowsAffected = statement.executeUpdate();

                if (rowsAffected == 0) {
                    return ResponseEntity.status(404).body("Usuario no encontrado");
                }
            }
            return ResponseEntity.ok().body("Usuario editado");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error en la DB: " + e.getMessage());
        }

    }

}
