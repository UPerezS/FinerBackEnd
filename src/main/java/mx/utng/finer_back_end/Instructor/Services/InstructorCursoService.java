package mx.utng.finer_back_end.Instructor.Services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
@Service
public class InstructorCursoService {
    @Autowired
    private JdbcTemplate jdbcTemplate;
    /** 
     * @param idUsuarioInstructor /int/ Id del usuario 
     * @param idCategoria /int/ id de la categoria correspondiente 
     * @param tituloCurso /String/ Titulo que recibirá el curso
     * @param descripcion /String/ Descripción del curso
     * @return Respuesta con el mensaje de éxito o error.
     */
    public ResponseEntity<String> registrarCursos(int idUsuarioInstructor, String tituloCurso, String descripcion,int idCategoria){
    try{
        String sql = "SELECT solicitar_creacion_curso(?,?,?,?)";
        Object result = jdbcTemplate.queryForObject(sql, Object.class, idUsuarioInstructor,  tituloCurso, descripcion,idCategoria);

        if (result instanceof String) {
            return ResponseEntity.ok((String) result);  // Si es un String
        } else if (result instanceof Integer) {
            return ResponseEntity.ok(result.toString());  // Si es un Integer
        } else {
            return ResponseEntity.status(500).body("Tipo de respuesta inesperado.");
        }
    }catch(Exception e){
        return ResponseEntity.status(500).body("Error en la DB"+ e.getMessage());

    }


    }


    }




 
