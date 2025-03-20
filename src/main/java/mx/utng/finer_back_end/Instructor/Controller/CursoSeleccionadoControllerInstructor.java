package mx.utng.finer_back_end.Instructor.Controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import mx.utng.finer_back_end.Instructor.Documentos.CursoSeleccionadoDTO;
import mx.utng.finer_back_end.Instructor.Services.CursoSeleccionadoServiceInstructor;

@RestController
@RequestMapping("/api/cursos")
public class CursoSeleccionadoControllerInstructor {

    @Autowired
    private CursoSeleccionadoServiceInstructor cursoSeleccionadoService;

    @Autowired
    private ObjectMapper objectMapper;

    /**
     * Endpoint para seleccionar un curso específico.
     *
     * @param idCurso identificador del curso a seleccionar.
     * @return ResponseEntity con los datos del curso formateados en un objeto JSON.
     */
    @GetMapping("/seleccionar/{idCurso}")
    public ResponseEntity<?> seleccionarCurso(@PathVariable Integer idCurso) {
        try {
            CursoSeleccionadoDTO resultado = cursoSeleccionadoService.seleccionarCurso(idCurso);

            if (resultado.getStatusCode() == 200) {
                // Convertir la cadena JSON en un Map
                Map<String, Object> cursoData = objectMapper.readValue(
                        resultado.getData(), new TypeReference<Map<String, Object>>() {});
                
                // Envolver la respuesta en un objeto con la propiedad "data"
                Map<String, Object> response = new HashMap<>();
                response.put("data", cursoData);
                
                return ResponseEntity.ok(response);
            } else if (resultado.getStatusCode() == 404) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(resultado.getMessage());
            } else {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(resultado.getMessage());
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al seleccionar el curso: " + e.getMessage());
        }
    }
}
