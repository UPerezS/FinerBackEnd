package mx.utng.finer_back_end.Instructor.Documentos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SolicitudCursoDTO {
    private Integer idSolicitudCurso;
    private Integer idUsuarioInstructor;
    private String tituloCursoSolicitado;
    private String descripcion;
    private Integer idCategoria;
    private Integer idCurso;
    private String imagen;
    private String temasJson; // Este es el JSON en String de los temas
}
