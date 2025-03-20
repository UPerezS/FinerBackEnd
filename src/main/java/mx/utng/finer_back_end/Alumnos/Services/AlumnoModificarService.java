package mx.utng.finer_back_end.Alumnos.Services;



import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;

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
    public ResponseEntity<String> actualizarPerfilAlumno(Integer idUsuario,String nombre,String apellidoPaterno, String apellidoMaterno,
        String nombreUsuario, String correo,String contrasenia){
            
            try (Connection connection = jdbcTemplate.getDataSource().getConnection()) {
                // Convertir la contraseña a un hash SHA-256
                String contraseniaHashed = hashPassword(contrasenia);
                
                // Actualizamos directamente la tabla Usuario en lugar de llamar a la función SQL
                String sql = "UPDATE Usuario SET nombre = ?, apellido_paterno = ?, " +
                            "apellido_materno = ?, nombre_usuario = ?, correo = ?, " +
                            "contrasenia = ? WHERE id_usuario = ?";
                
                try (CallableStatement statement = connection.prepareCall(sql)) {
                    statement.setString(1, nombre);
                    statement.setString(2, apellidoPaterno);
                    statement.setString(3, apellidoMaterno);
                    statement.setString(4, nombreUsuario);
                    statement.setString(5, correo);
                    statement.setString(6, contraseniaHashed);
                    statement.setInt(7, idUsuario);
                    
                    int rowsAffected = statement.executeUpdate();
                    
                    if (rowsAffected == 0) {
                        return ResponseEntity.status(404).body("Usuario no encontrado");
                    }
                }
                
                return ResponseEntity.ok("Perfil actualizado correctamente.");
            } catch (SQLException e) {
                return ResponseEntity.status(500).body("Error en la DB: " + e.getMessage());
            }
        }
    }