package mx.utng.finer_back_end.Instructor.Controller;

import java.util.Map;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestParam;


import mx.utng.finer_back_end.Instructor.Documentos.AlumnoDetalleNombreDTO;
import mx.utng.finer_back_end.Instructor.Services.AlumnoNombreService;

@RestController
@RequestMapping("/api/usuario")
public class AlumnoNombreController {
    
    @Autowired
    private AlumnoNombreService alumnoNombreService;

    /**
     * Endpoint para filtrar a los Alumnos por nombre y orden.
     * @param nombre término de búsqueda para el nombre.
     * @param orden parámetro de orden (ascendente o descendente).
     * @return ResponseEntity con la lista de alumnos que coincidan o un mensaje de error.
     */
    @GetMapping("/filtrar-alumno-nombre")
    public ResponseEntity<?> filtrarAlumnoNombre(
            @RequestParam String nombre,
            @RequestParam String orden) {

        try {
            // Llamada al servicio pasando tanto nombre como orden
            List<AlumnoDetalleNombreDTO> alumnos = alumnoNombreService.filtrarAlumnoNombre(nombre, orden);

            if (alumnos.isEmpty()) {
                Map<String, Object> response = new HashMap<>();
                response.put("mensaje", "No se encontraron alumnos para el filtrado de nombre: " + nombre);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }

            // Devolver los alumnos encontrados en la respuesta
            return ResponseEntity.ok(alumnos);

        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("mensaje", "Error al filtrar alumnos: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
}