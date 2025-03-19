package mx.utng.finer_back_end.Instructor.Services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.batch.BatchProperties.Jdbc;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

@Service
public class TemaService {
    @Autowired
    private JdbcTemplate jdbcTemplate;
    /**
     * @param idSolicitudCurso /int/ 
     * @param nombreTema /String/
     * @param contenido /String/
     * @param imagen /byte/
     * @return mensaje de éxito o error
     * 
     */
    public ResponseEntity<String> registrarTema(int idSolicitudCurso, String nombreTema, String contenido,
                                    byte[] imagen) {

    try{
        String sql = "SELECT agregar_tema_solicitud(?,?,?,?)";
        Object result = jdbcTemplate.queryForObject(sql, Object.class, idSolicitudCurso,
        nombreTema,contenido,imagen);
            if (result instanceof String) {
                return ResponseEntity.ok((String) result);  // Si es un String
            } else if (result instanceof Integer ) {
                return ResponseEntity.ok(result.toString());  // Si es un Integer
            } else {
                return ResponseEntity.status(500).body("Tipo de respuesta inesperado.");
            }


        }catch(Exception e){
            return ResponseEntity.status(500).body("Error en la DB"+e.getMessage());
        }


    }
                                    
}
