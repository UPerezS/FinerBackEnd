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

import mx.utng.finer_back_end.Instructor.Documentos.CursoDetalleNombreDTO;
import mx.utng.finer_back_end.Instructor.Services.CursoNombreService;

@RestController
@RequestMapping("/api/cursos")
public class CursoNombreController {
    @Autowired
    private CursoNombreService cursoNombreService;

    /**
     * Endpoint para filtrar cursos por nombre.
     * 
     * @param busqueda término a buscar en el título del curso.
     * @return ResponseEntity con la lista de cursos que coinciden o un mensaje de error.
     */
    @GetMapping("/filtrar-nombre/{busqueda}")
    public ResponseEntity<?> filtrarCursoNombre(@PathVariable String busqueda) {
        try {
            List<CursoDetalleNombreDTO> cursos = cursoNombreService.filtrarCursoNombre(busqueda);

            if (cursos.isEmpty()) {
                Map<String, Object> response = new HashMap<>();
                response.put("mensaje", "No se encontraron cursos para el nombre: " + busqueda);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }

            return ResponseEntity.ok(cursos);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("mensaje", "Error al filtrar cursos: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
}
