package mx.utng.finer_back_end.Alumnos.Services;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.CallableStatement;
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
public class AlumnoModificarService {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    private String hashPassword(String contrasenia) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hashBytes = digest.digest(contrasenia.getBytes(StandardCharsets.UTF_8));
            // Convertir el hash a una cadena hexadecimal
            StringBuilder hexString = new StringBuilder();
            for (byte b : hashBytes) {
                hexString.append(String.format("%02x", b));
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Error al generar el hash de la contraseña", e);
        }
    }

    public ResponseEntity<Map<String, Object>> actualizarPerfilAlumno(
            Integer idUsuario, String nombre, String apellidoPaterno,
            String apellidoMaterno, String nombreUsuario, String correo,
            String contrasenia, Boolean actualizar_contrasenia) {

        Map<String, Object> response = new HashMap<>();

        try (Connection connection = jdbcTemplate.getDataSource().getConnection()) {
            String sql = "SELECT actualizar_perfil_alumno(?, ?, ?, ?, ?, ?, ?, ?)";

            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setInt(1, idUsuario);
                statement.setString(2, nombre);
                statement.setString(3, apellidoPaterno);
                statement.setString(4, apellidoMaterno);
                statement.setString(5, nombreUsuario);
                statement.setString(6, correo);
                statement.setString(7, contrasenia);
                statement.setBoolean(8, actualizar_contrasenia);

                statement.execute();

                response.put("success", true);
                response.put("message", "Perfil actualizado correctamente.");
                return ResponseEntity.ok(response);
            }
        } catch (SQLException e) {
            if (e.getMessage().contains("Usuario no encontrado")) {
                response.put("success", false);
                response.put("message", "Usuario no encontrado");
                return ResponseEntity.status(404).body(response);
            }
            response.put("success", false);
            response.put("message", "Error en la DB: " + e.getMessage());
            return ResponseEntity.status(500).body(response);
        }
    }

}