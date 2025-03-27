package mx.utng.finer_back_end.Administrador.Implement;

import mx.utng.finer_back_end.Administrador.Dao.SolicitudCursoDao;
import mx.utng.finer_back_end.Administrador.Services.SolicitudCursoService;
import mx.utng.finer_back_end.Documentos.SolicitudCursoDocumento;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

/**
 * Implementación del servicio que gestiona las operaciones relacionadas con las solicitudes de cursos.
 * Esta clase proporciona la lógica de negocio para consultar y gestionar las solicitudes de cursos
 * pendientes de aprobación y eliminar solicitudes rechazadas antiguas.
 */
@Service
public class SolicitudCursoServiceImpl implements SolicitudCursoService {

    @Autowired
    private SolicitudCursoDao solicitudCursoRepository;

    /**
     * {@inheritDoc}
     * 
     * Recupera todas las solicitudes de cursos que tienen el estatus 'en revision'.
     * Estas solicitudes son las que están pendientes de ser aprobadas o rechazadas
     * por un administrador.
     * 
     * @return Lista de documentos de solicitudes de cursos con estatus 'en revision'
     */
    @Override
    public List<SolicitudCursoDocumento> consultarSolicitudesPendientes() {
        // Recupera todas las solicitudes con estatus 'en revision'
        return solicitudCursoRepository.findByEstatus("en revision");
    }
    /**
     * {@inheritDoc}
     * 
     * Este método elimina las solicitudes de cursos que han sido rechazadas y tienen
     * más de 30 días de antigüedad. El proceso consiste en:
     * 1. Obtener la fecha actual
     * 2. Recuperar todas las solicitudes con estatus 'rechazado'
     * 3. Para cada solicitud rechazada, calcular la diferencia en días entre la fecha
     *    de solicitud y la fecha actual
     * 4. Si la diferencia es mayor a 30 días, eliminar la solicitud de la base de datos
     * 
     * Esta operación ayuda a mantener limpia la base de datos, eliminando registros
     * que ya no son relevantes para el sistema.
     */
    @Override
    public void eliminarSolicitudesCursoRechazadasAntiguas() {
        // Obtiene la fecha actual
        LocalDate today = LocalDate.now();

        // Encuentra todas las solicitudes de cursos con estatus "rechazado"
        List<SolicitudCursoDocumento> solicitudesRechazadas = solicitudCursoRepository.findByEstatus("rechazado");

        // Recorre las solicitudes rechazadas y elimina aquellas con más de 30 días de antigüedad
        for (SolicitudCursoDocumento solicitud : solicitudesRechazadas) {
            LocalDate fechaSolicitud = solicitud.getFechaSolicitud(); // Asumimos que tienes un campo de fecha en la solicitud

            // Si la solicitud tiene más de 30 días de antigüedad
            if (ChronoUnit.DAYS.between(fechaSolicitud, today) > 30) {
                solicitudCursoRepository.delete(solicitud); // Elimina la solicitud
            }
        }
    }
}

