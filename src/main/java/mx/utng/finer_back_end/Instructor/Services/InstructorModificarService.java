package mx.utng.finer_back_end.Instructor.Services;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

@Service
public class InstructorModificarService {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    public ResponseEntity<Map<String, Object>> actualizarPerfilInstructor(
            Integer idUsuario, String nombre, String apellidoPaterno,
            String apellidoMaterno, String correo, String telefono, String direccion,
            String nombreUsuario, String contrasenia, Boolean actualizar_contrasenia) {

        Map<String, Object> response = new HashMap<>();

        try (Connection connection = jdbcTemplate.getDataSource().getConnection()) {
            String sql = "SELECT actualizar_perfil_instructor(?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setInt(1, idUsuario);
                statement.setString(2, nombre);
                statement.setString(3, apellidoPaterno);
                statement.setString(4, apellidoMaterno);
                statement.setString(5, correo);
                statement.setString(6, nombreUsuario);
                statement.setString(7, telefono);
                statement.setString(8, direccion);
                statement.setString(9, contrasenia);
                statement.setBoolean(10, actualizar_contrasenia);

                statement.execute();

                response.put("success", true);
                response.put("message", "Perfil de instructor actualizado correctamente.");
                return ResponseEntity.ok(response);
            }
        } catch (SQLException e) {
            response.put("success", false);
            response.put("message", "Error en la DB: " + e.getMessage());
            return ResponseEntity.status(500).body(response);
        }
    }

}
