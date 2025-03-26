package mx.utng.finer_back_end.Instructor.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import mx.utng.finer_back_end.Instructor.Services.TemaEditarServiceInstructor;

@RestController
@RequestMapping("/api/temas")
public class TemaEditarControllerInstructor {

    @Autowired
    private TemaEditarServiceInstructor temaEditarService;

    /**
     * Endpoint para editar un tema existente.
     *
     * @param idUsuario identificador del instructor.
     * @param idTema identificador del tema a editar.
     * @param nombreTema nuevo nombre del tema.
     * @param contenido nuevo contenido del tema.
     * @param imagen nueva imagen del tema.
     * @return ResponseEntity con el mensaje de estado.
     */
    @PutMapping("/editar/{idUsuario}/{idTema}")
    public ResponseEntity<String> editarTema(@PathVariable Integer idUsuario, 
                                             @PathVariable Integer idTema,
                                             @RequestParam(required = false) String nombreTema,
                                             @RequestParam(required = false) String contenido,
                                             @RequestParam(required = false) byte[] imagen) {
        return temaEditarService.editarTema(idUsuario, idTema, nombreTema, contenido, imagen);
    }
}
