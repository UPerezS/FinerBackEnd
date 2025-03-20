package mx.utng.finer_back_end.Instructor.Documentos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO para mostrar los detalles de un alumno por su nombre
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AlumnoDetalleNombreDTO {
    private String nombre;
    private String apellidoPaterno;
    private String apellidoMaterno;
    private String correo;

    
    
}
