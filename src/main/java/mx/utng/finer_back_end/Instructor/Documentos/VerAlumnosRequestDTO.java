package mx.utng.finer_back_end.Instructor.Documentos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO para la solicitud de ver alumnos inscritos en un curso.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class VerAlumnosRequestDTO {
    private Integer id_curso;

}
