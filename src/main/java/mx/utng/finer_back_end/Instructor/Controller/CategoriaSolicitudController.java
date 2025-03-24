package mx.utng.finer_back_end.Instructor.Controller;

import mx.utng.finer_back_end.Instructor.Documentos.CategoriaSolicitudDTO;
import mx.utng.finer_back_end.Instructor.Services.CategoriaSolicitudService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/categorias")
public class CategoriaSolicitudController {

    @Autowired
    private CategoriaSolicitudService categoriaSolicitudService;

    /**
     * Endpoint para solicitar la creación de una nueva categoría.
     *
     * @param categoriaSolicitudDTO datos de la solicitud de categoría.
     * @return ResponseEntity con el mensaje de la operación.
     */
    @PostMapping("/solicitar")
    public ResponseEntity<String> solicitarCategoria(@RequestBody CategoriaSolicitudDTO categoriaSolicitudDTO) {
        try {
            // Llamar al servicio para procesar la solicitud
            String mensaje = categoriaSolicitudService.solicitarCategoria(categoriaSolicitudDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(mensaje);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al procesar la solicitud: " + e.getMessage());
        }
    }
}
