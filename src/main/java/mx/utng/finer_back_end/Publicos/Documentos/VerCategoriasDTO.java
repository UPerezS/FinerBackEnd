package mx.utng.finer_back_end.Publicos.Documentos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VerCategoriasDTO {
    private Integer idCategoria;
    private String nombreCategoria;
    private String descripcion;
    
}
