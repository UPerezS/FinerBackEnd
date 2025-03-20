package mx.utng.finer_back_end.Administrador.Services;

import mx.utng.finer_back_end.Documentos.SolicitudCursoDocumento;
import java.util.List;

public interface SolicitudCursoService {
    // Método para consultar las solicitudes pendientes de aprobación
    List<SolicitudCursoDocumento> consultarSolicitudesPendientes();
    // Nuevo método para eliminar las solicitudes rechazadas con más de 30 días
    void eliminarSolicitudesCursoRechazadasAntiguas();
}