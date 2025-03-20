package mx.utng.finer_back_end.AlumnosInstructor.Documentos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO para mostrar el progreso de un curso.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CursoDetalleProgresoDTO {
    private Integer vIdInscripcion;
    private Integer vTemasCompletados;
    private Integer vTotalTemas;
    private Float vPorcentaje;
}
