package mx.utng.finer_back_end.Alumnos.Documentos;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EvaluacionAlumnoDTO {
    private Integer id_pregunta;
    private String texto_pregunta;
    private List<OpcionDTO> opciones;  

    // DTO para las opciones
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class OpcionDTO {
        private Integer id_opcion;
        private String texto_opcion;
    }
}
