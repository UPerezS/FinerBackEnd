package mx.utng.finer_back_end.Instructor.Documentos;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SolicitudCursoRequest {
    private Integer idSolicitudCurso;
    private Integer idUsuarioInstructor;
    private String tituloCursoSolicitado;
    private String descripcion;
    private Integer idCategoria;
    private Integer idCurso;
    private String imagen;
    private List<TemaDTO> temas; // ✅ CORRECTO
}
