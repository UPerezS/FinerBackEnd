package mx.utng.finer_back_end.Publicos.Services;

import org.hibernate.annotations.processing.SQL;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import mx.utng.finer_back_end.Publicos.Repository.PublicosRepository;

@Service
public class PublicosService {

    private final JdbcTemplate jdbcTemplate;
    private final PublicosRepository usuarioRepository;

    // Constructor combinado para inyectar tanto JdbcTemplate como PublicosRepository
    public PublicosService(JdbcTemplate jdbcTemplate, PublicosRepository usuarioRepository) {
        this.jdbcTemplate = jdbcTemplate;
        this.usuarioRepository = usuarioRepository;
    }

    
   /**
     * Actualiza la contraseña del usuario a través de la función almacenada.
     * @param correoUsuario Correo del usuario.
     * @param nuevaContrasenia Nueva contraseña.
     * @return Mensaje resultado de la función almacenada.
     */
    public String actualizarContrasenia(String correoUsuario, String nuevaContrasenia) {
        String sql = "SELECT actualizar_contrasenia(?, ?)";
        try {
            String resultado = jdbcTemplate.queryForObject(sql, new Object[]{correoUsuario, nuevaContrasenia}, String.class);
            return resultado;
        } catch (Exception e) {
            return "Error al actualizar la contraseña: " + e.getMessage();
        }
    }

    /**
     * Obtiene el correo electrónico del usuario por su ID.
     * @param idUsuario ID del usuario.
     * @return Correo del usuario o null si no se encuentra.
     */
    public String obtenerCorreoPorId(Long idUsuario) {
        return usuarioRepository.findCorreoById(idUsuario);
    }

    /**
     * Método para autenticar al usuario utilizando la función de la base de datos.
     * @param nombreUsuario El nombre de usuario.
     * @param contrasenia La contraseña del usuario.
     * @return Un arreglo con el id del usuario, correo y autenticación.
     */
    public Object[] autenticarUsuario(String nombreUsuario, String contrasenia) {
        try {
            String sql = "SELECT * FROM autenticar_usuario(?, ?)";
            
            return jdbcTemplate.queryForObject(sql, new Object[]{nombreUsuario, contrasenia}, (rs, rowNum) -> {
                Integer idUsuario = rs.getInt("id_usuario");
                String correo = rs.getString("correo");
                Boolean autenticacion = rs.getBoolean("autenticacion");
                return new Object[]{idUsuario, correo, autenticacion};
            });
        } catch (Exception e) {
            System.err.println("Error al ejecutar la consulta SQL: " + e.getMessage());
            throw new RuntimeException("Error al autenticar usuario", e);
        }
    }


    
    
}