package mx.utng.finer_back_end.Instructor.Documentos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO para mostrar los detalles de un curso filtrado por categoría.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CursoDetalleCategoriaDTO {
    private String tituloCurso;
    private String descripcion;
    private String nombreInstructor;
    private String apellidoPaterno;
    private String apellidoMaterno;
    private String nombreCategoria;

}
