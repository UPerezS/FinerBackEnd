package mx.utng.finer_back_end.Instructor.Documentos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO para el resultado de la función seleccionarCurso.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CursoSeleccionadoDTO {
    private Integer statusCode;
    private String message;
    // Se usa String para representar el JSON devuelto; alternativamente podrías mapearlo a un objeto específico
    private String data;
}
