package mx.utng.finer_back_end.Instructor.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import mx.utng.finer_back_end.Instructor.Services.CursoEliminarServiceInstructor;

@RestController
@RequestMapping("/api/cursos")
public class CursoEliminarControllerInstructor {

    @Autowired
    private CursoEliminarServiceInstructor cursoEliminarService;

    /**
     * Endpoint para eliminar un curso.
     *
     * @param idUsuario identificador del instructor.
     * @param idCurso identificador del curso a eliminar.
     * @return ResponseEntity con el mensaje de estado.
     */
    @DeleteMapping("/eliminar/{idUsuario}/{idCurso}")
    public ResponseEntity<?> eliminarCurso(@PathVariable Integer idUsuario, @PathVariable Integer idCurso) {
        try {
            String resultado = cursoEliminarService.eliminarCurso(idUsuario, idCurso);
            if (resultado.contains("Error")) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(resultado);
            }
            return ResponseEntity.ok(resultado);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                 .body("Error al eliminar el curso: " + e.getMessage());
        }
    }
}
