package mx.utng.finer_back_end.Administrador.Controller;

import mx.utng.finer_back_end.Administrador.Services.SolicitudInstructorService;
import mx.utng.finer_back_end.Documentos.SolicitudInstructorDocumento;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/administrador/solicitudes") // La URL base para todas las solicitudes de este controlador
public class SolicitudInstructorController {

    @Autowired
    private SolicitudInstructorService solicitudInstructorService;

    /**
     * Endpoint para rechazar una solicitud de instructor.
     * 
     * Este método permite rechazar una solicitud de instructor, proporcionando un
     * motivo
     * para la acción. Si la solicitud se rechaza correctamente, se devuelve la
     * solicitud
     * actualizada. En caso de error, se retorna un mensaje adecuado dependiendo del
     * tipo de error.
     *
     * @param id     El ID de la solicitud que se desea rechazar.
     * @param motivo El motivo por el cual se rechaza la solicitud.
     * @return ResponseEntity con la solicitud rechazada o mensaje de error.
     * 
     *         Posibles respuestas:
     *         - `200 OK`: Solicitud rechazada correctamente.
     *         - `404 Not Found`: Si no se encuentra la solicitud con el ID
     *         proporcionado.
     *         - `500 Internal Server Error`: Si ocurre un error al procesar la
     *         solicitud.
     */
    @PostMapping("/rechazar/{id}")
    public ResponseEntity<Object> rechazarInstructor(@PathVariable Integer id,
            @RequestBody String motivo) {

        try {
            SolicitudInstructorDocumento solicitud = solicitudInstructorService.rechazarInstructor(id, motivo);
            return new ResponseEntity<>(solicitud, HttpStatus.OK);
        } catch (SolicitudNoEncontradaException e) {
            return new ResponseEntity<>(new ErrorResponse("Solicitud no encontrada", e.getMessage()),
                    HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(new ErrorResponse("Error interno", e.getMessage()),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Clase para la respuesta de error
    public static class ErrorResponse {
        private String error;
        private String message;

        public ErrorResponse(String error, String message) {
            this.error = error;
            this.message = message;
        }

        // Getters y Setters
        public String getError() {
            return error;
        }

        public void setError(String error) {
            this.error = error;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }
    }

    // Excepciones personalizadas para un mejor control de errores
    public static class SolicitudNoEncontradaException extends RuntimeException {
        public SolicitudNoEncontradaException(String message) {
            super(message);
        }
    }
}
