package mx.utng.finer_back_end.Administrador.Services;

import org.springframework.http.ResponseEntity;

import mx.utng.finer_back_end.Administrador.Documentos.SolicitudCategoriaDatos;
import mx.utng.finer_back_end.Documentos.SolicitudCategoriaDocumento;

import java.util.List;
import java.util.Map;

public interface SolicitudCategoriaService {
    ResponseEntity<String> aprobarDesaprobarCategoria(Integer idSolicitud, boolean aprobar);

    // Método para recuperar solicitudes por instructor
    List<SolicitudCategoriaDocumento> obtenerSolicitudesPorInstructor(Integer idUsuarioInstructor);

    List<SolicitudCategoriaDatos> obtenerTodasLasSolicitudes();

    void eliminarSolicitudesCategoriaRechazadasAntiguas();
}
