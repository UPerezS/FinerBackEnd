package mx.utng.finer_back_end.Instructor.Controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import mx.utng.finer_back_end.Instructor.Documentos.VerAlumnosDTOInstructor;
import mx.utng.finer_back_end.Instructor.Documentos.VerAlumnosRequestDTO;
import mx.utng.finer_back_end.Instructor.Services.VerAlumnosServiceInstructor;


@RestController
@RequestMapping("/api/alumnos")
public class VerAlumnosControllerInstructor {

      @Autowired
    private VerAlumnosServiceInstructor verAlumnosService;

    @Autowired
    private ObjectMapper objectMapper;

    /**
     * Endpoint para obtener la lista de alumnos inscritos en un curso.
     * Recibe un objeto JSON con al menos el campo id_curso.
     */
    @PostMapping("/ver")
    public ResponseEntity<?> verAlumnos(@RequestBody VerAlumnosRequestDTO requestDTO) {
        try {
            VerAlumnosDTOInstructor resultado = verAlumnosService.verAlumnos(requestDTO);

            if (resultado.getStatusCode() == 200) {
                // Convertir la cadena JSON en una lista de mapas (o en una lista de DTOs si lo prefieres)
                List<Map<String, Object>> alumnos = objectMapper.readValue(
                        resultado.getData(), new TypeReference<List<Map<String, Object>>>() {});
                return ResponseEntity.ok(alumnos);
            } else if (resultado.getStatusCode() == 404) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(resultado.getMessage());
            } else {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(resultado.getMessage());
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al obtener los alumnos: " + e.getMessage());
        }
    }


}
