package mx.utng.finer_back_end.Instructor.Services;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

@Service
public class InstructorModificarService {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    public ResponseEntity<String> actualizarPerfilInstructor(Integer idUsuario, String nombre, String apellidoPaterno,
    String apellidoMaterno, String correo, String telefono, String direccion, String nombreUsuario) {
try (Connection connection = jdbcTemplate.getDataSource().getConnection()) {
    // Llamar a la función de PostgreSQL para actualizar el perfil del instructor
    String sql = "SELECT actualizar_perfil_instructor(?, ?, ?, ?, ?, ?, ?, ?)";
    
    try (PreparedStatement statement = connection.prepareStatement(sql)) {
        statement.setInt(1, idUsuario);
        statement.setString(2, nombre);
        statement.setString(3, apellidoPaterno);
        statement.setString(4, apellidoMaterno);
        statement.setString(5, correo);
        statement.setString(6, nombreUsuario);
        statement.setString(7, telefono);
        statement.setString(8, direccion);
        
        statement.execute();
        
        return ResponseEntity.ok().body("Perfil de instructor actualizado correctamente.");
    }
} catch (SQLException e) {
    return ResponseEntity.status(500).body("Error en la DB: " + e.getMessage());
}
}


}
