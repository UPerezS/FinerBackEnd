package mx.utng.finer_back_end.Publicos.Documentos;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO para mostrar los detalles de un curso aprobado.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CursoDetalleDTO {
    private Integer idCurso;
    private String tituloCurso;
    private String descripcion;
    private String nombreInstructor;
    private String apellidoPaterno;
    private String apellidoMaterno;
    private String nombreCategoria;
    private String imagen;
}
