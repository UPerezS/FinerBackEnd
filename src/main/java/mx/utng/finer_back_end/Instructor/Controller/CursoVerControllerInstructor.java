package mx.utng.finer_back_end.Instructor.Controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import mx.utng.finer_back_end.Instructor.Documentos.CursoVerDTO;
import mx.utng.finer_back_end.Instructor.Services.CursoVerServiceInstructor;

@RestController
@RequestMapping("/api/cursos")
public class CursoVerControllerInstructor {

    @Autowired
    private CursoVerServiceInstructor cursoVerService;

     /**
     * Endpoint para obtener todos los cursos de un instructor (aprobados y solicitudes) ..
     *
     * @param idInstructor identificador del instructor.
     * @return ResponseEntity con la lista de cursos o mensaje de error.
     */
    @GetMapping("/ver/{idInstructor}")
    public ResponseEntity<?> verCursos(@PathVariable Integer idInstructor) {
        try {
            List<CursoVerDTO> cursos = cursoVerService.verCursos(idInstructor);
            if (cursos.isEmpty()) {
                Map<String, Object> response = new HashMap<>();
                response.put("mensaje", "No se encontraron cursos para el instructor con id: " + idInstructor);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }
            return ResponseEntity.ok(cursos);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("mensaje", "Error al obtener los cursos: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
}
