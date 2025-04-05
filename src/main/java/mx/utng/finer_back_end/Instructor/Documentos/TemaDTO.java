package mx.utng.finer_back_end.Instructor.Documentos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TemaDTO {
    private Integer idSolicitudTema;
    private Integer idSolicitudCurso;
    private String nombre_tema;
    private String contenido;
}
