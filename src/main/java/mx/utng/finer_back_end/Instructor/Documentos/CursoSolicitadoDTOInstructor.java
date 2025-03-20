package mx.utng.finer_back_end.Instructor.Documentos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO para mostrar los detalles de un curso solicitado.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CursoSolicitadoDTOInstructor {
    private String tituloCursoSolicitado;
    private String descripcion;
    private String nombreInstructor;
    private String nombreCategoria;
    private String estatus;
}
