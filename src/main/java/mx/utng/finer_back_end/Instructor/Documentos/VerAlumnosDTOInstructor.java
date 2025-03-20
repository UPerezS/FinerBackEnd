package mx.utng.finer_back_end.Instructor.Documentos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO para el resultado de la función verAlumnos.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class VerAlumnosDTOInstructor {
    private Integer statusCode;
    private String message;
    /**
     * Se utiliza String para almacenar el JSON resultante, que contiene la lista de alumnos.
     */
    private String data;

}
