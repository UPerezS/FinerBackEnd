package mx.utng.finer_back_end.Instructor.Controller;

import mx.utng.finer_back_end.Instructor.Documentos.CursoEditarDTO;
import mx.utng.finer_back_end.Instructor.Services.CursoEditarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/cursos")
public class CursoEditarController {

    @Autowired
    private CursoEditarService cursoEditarService;

    /**
     * Endpoint para editar un curso existente.
     *
     * @param cursoEditarDTO objeto con los datos del curso editado.
     * @return ResponseEntity con el mensaje de la operación.
     */
    @PutMapping("/editar")
    public ResponseEntity<String> editarCurso(@RequestBody CursoEditarDTO cursoEditarDTO) {
        try {
            // Llamar al servicio para editar el curso
            String mensaje = cursoEditarService.editarCurso(cursoEditarDTO);
            return ResponseEntity.status(HttpStatus.OK).body(mensaje);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al editar el curso: " + e.getMessage());
        }
    }
}
