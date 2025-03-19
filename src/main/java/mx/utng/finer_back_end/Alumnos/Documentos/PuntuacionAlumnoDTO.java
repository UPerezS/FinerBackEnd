package mx.utng.finer_back_end.Alumnos.Documentos;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PuntuacionAlumnoDTO {
    private Integer preguntas_totales;
    private Integer aciertos;
    private BigDecimal califiacion;
    
}

