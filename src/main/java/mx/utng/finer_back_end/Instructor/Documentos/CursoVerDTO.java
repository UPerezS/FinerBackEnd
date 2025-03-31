package mx.utng.finer_back_end.Instructor.Documentos;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO para mostrar los cursos (aprobados y solicitudes) de un instructor.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CursoVerDTO {
    private Integer idCurso;
    private String titulo;
    private String descripcion;
    private Integer idCategoria;
    private String nombreCategoria;
    private String imagen;

}
