package mx.utng.finer_back_end.Alumnos.Documentos;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CursoInscritoDTO {
    private Integer idCurso;
    private String tituloCurso;
    private String descripcion;
    private Integer idCategoria;
    private String nombreCategoria;
    private LocalDateTime fechaInscripcion;
    private String estatusInscripcion;
}