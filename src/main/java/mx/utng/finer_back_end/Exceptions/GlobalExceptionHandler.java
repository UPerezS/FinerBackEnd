package mx.utng.finer_back_end.Exceptions;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // Manejo de error de conversión de parámetros en la URL
    /**
     * Captura excepciones cuando un parámetro de la URL no es del tipo esperado.
     * 
     * Esta excepción ocurre, por ejemplo, cuando un `@PathVariable Integer id`
     * recibe un valor no numérico,
     * como "abc" en `/api/cursos/detalles/abc`, lo que provoca un
     * `MethodArgumentTypeMismatchException`.
     *
     * @param e Excepción lanzada cuando un parámetro de tipo incorrecto se pasa en
     *          la URL.
     * @return ResponseEntity con código 400 (Bad Request) y un mensaje indicando el
     *         error de conversión.
     */
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<String> handleTypeMismatchException(MethodArgumentTypeMismatchException e) {
        String mensaje = "El parámetro '" + e.getName() + "' debe ser un número entero válido.";
        return ResponseEntity.badRequest().body(mensaje);
    }

}
