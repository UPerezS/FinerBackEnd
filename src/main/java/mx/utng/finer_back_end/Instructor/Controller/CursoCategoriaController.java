package mx.utng.finer_back_end.Instructor.Controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import mx.utng.finer_back_end.Instructor.Documentos.CursoDetalleCategoriaDTO;
import mx.utng.finer_back_end.Instructor.Services.CursoCategoriaService;

@RestController
@RequestMapping("/api/cursos")
public class CursoCategoriaController {

    @Autowired
    private CursoCategoriaService cursoCategoriaService;

    /**
     * Endpoint para filtrar cursos por categoría.
     * 
     * @param categoria término de búsqueda para la categoría.
     * @return ResponseEntity con la lista de cursos que coinciden o un mensaje de error.
     */
    @GetMapping("/filtrar-categoria/{categoria}")
    public ResponseEntity<?> filtrarCursoCategoria(@PathVariable String categoria) {
        try {
            List<CursoDetalleCategoriaDTO> cursos = cursoCategoriaService.filtrarCursoCategoria(categoria);

            if (cursos.isEmpty()) {
                Map<String, Object> response = new HashMap<>();
                response.put("mensaje", "No se encontraron cursos para la categoría: " + categoria);
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
