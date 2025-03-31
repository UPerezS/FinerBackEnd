package mx.utng.finer_back_end.Instructor.Implement;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import mx.utng.finer_back_end.Instructor.Dao.TemaEditarDaoInstructor;
import mx.utng.finer_back_end.Instructor.Services.TemaEditarServiceInstructor;

@Service
public class TemaEditarImplementInstructor implements TemaEditarServiceInstructor {

    @Autowired
    private TemaEditarDaoInstructor temaEditarDao;

    @Override
    public ResponseEntity<String> editarTema(Integer idUsuario, Integer idTema, String nombreTema, String contenido) {
        try {
            String result = temaEditarDao.editarTema(idUsuario, idTema, nombreTema, contenido);
            if (result.contains("Error")) {
                return ResponseEntity.status(400).body(result); // Error en la base de datos
            }
            return ResponseEntity.ok(result); // Tema modificado con éxito
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error en la DB: " + e.getMessage());
        }
    }
}
