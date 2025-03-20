package mx.utng.finer_back_end.Instructor.Documentos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EdicionCategoriaDTO {
    private Integer idSolicitudCategoria;
    private Integer idInstructor;
    private String nombreCategoria;
    private String descripcion;
}