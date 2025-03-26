package mx.utng.finer_back_end.Instructor.Services;

import org.springframework.http.ResponseEntity;

public interface TemaEditarServiceInstructor {
    ResponseEntity<String> editarTema(Integer idUsuario, Integer idTema, String nombreTema, String contenido, byte[] imagen);
}
