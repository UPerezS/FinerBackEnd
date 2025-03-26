package mx.utng.finer_back_end.AlumnosInstructor.Services;

import mx.utng.finer_back_end.Documentos.UsuarioDocumento;

public interface UsuarioInstructorService {

    // Método para obtener los datos del usuario por su ID
    UsuarioDocumento obtenerDatosUsuario(Integer idUsuario);
}
