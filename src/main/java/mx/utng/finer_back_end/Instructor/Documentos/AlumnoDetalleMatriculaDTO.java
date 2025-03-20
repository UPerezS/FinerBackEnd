package mx.utng.finer_back_end.Instructor.Documentos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * DTO para mostrar los detalles de un matricula por matricula
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AlumnoDetalleMatriculaDTO {
    private String nombre;
    private String apellidoPaterno;
    private String apellidoMaterno;
    private String correo;
    private String matricula;

}
