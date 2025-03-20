package mx.utng.finer_back_end.Alumnos.Documentos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AlumnoDetalleModificarDTO {
    

private Integer idUsuario; 
private String nombre;
private String apellidoPaterno;
private String apellidoMaterno;
private String nombreUsuario;
private String correo;
private String contrasenia;




}
