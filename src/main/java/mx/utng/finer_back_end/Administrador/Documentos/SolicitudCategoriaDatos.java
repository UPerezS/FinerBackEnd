package mx.utng.finer_back_end.Administrador.Documentos;

import java.sql.Timestamp;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SolicitudCategoriaDatos {
    private Integer id_solicitud_categoria;
    private Integer id_usuario_instructor;
    private String nombre_categoria;
    private String descripcion;
    private String estatus;
    private Timestamp fecha_solicitud;
    private String nombre;
    private String apellido_paterno;
    private String apellido_materno;
}
