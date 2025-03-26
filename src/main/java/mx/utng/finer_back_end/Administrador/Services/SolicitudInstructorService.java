package mx.utng.finer_back_end.Administrador.Services;

import mx.utng.finer_back_end.Documentos.SolicitudInstructorDocumento;

public interface SolicitudInstructorService {

    SolicitudInstructorDocumento rechazarInstructor(Integer idSolicitudInstructor, String motivo);
}
