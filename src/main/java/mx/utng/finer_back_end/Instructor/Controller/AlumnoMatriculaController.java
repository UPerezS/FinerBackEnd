package mx.utng.finer_back_end.Instructor.Controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import mx.utng.finer_back_end.Instructor.Documentos.AlumnoDetalleMatriculaDTO;
import mx.utng.finer_back_end.Instructor.Services.AlumnoMatriculaService;


@RestController
@RequestMapping("/api/usuario")
public class AlumnoMatriculaController {
    
    @Autowired
    private AlumnoMatriculaService alumnomatriculaService;

    /**
     * Endpoint para filtrar a los Alumnos por nombre y orden.
     * 
     * @param matricula término de búsqueda para el nombre.
     * @param orden     parámetro de orden (ascendente o descendente).
     * @return ResponseEntity con la lista de alumnos que coincidan o un mensaje de
     *         error.
     */

    @GetMapping("/filtrar-alumno-matricula")
    public ResponseEntity<?> filtrarAlumnoMatricula(
            @RequestParam String matricula,
            @RequestParam String orden) {
        try {
            List<AlumnoDetalleMatriculaDTO> alumn = alumnomatriculaService.filtrarAlumnosMatricula(matricula, orden);

            if (alumn.isEmpty()) {
                Map<String, Object> response = new HashMap<>();
                response.put("mensaje", "No se encontraron alumnos para el filtrado de matricula: " + matricula);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }
            return ResponseEntity.ok(alumn);

        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("mensaje", "Error al filtrar alumno por matricula:" + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }

    }

}