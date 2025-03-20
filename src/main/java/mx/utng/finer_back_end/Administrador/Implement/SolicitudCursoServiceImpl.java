package mx.utng.finer_back_end.Administrador.Implement;

import mx.utng.finer_back_end.Administrador.Dao.SolicitudCursoDao;
import mx.utng.finer_back_end.Administrador.Services.SolicitudCursoService;
import mx.utng.finer_back_end.Documentos.SolicitudCursoDocumento;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
public class SolicitudCursoServiceImpl implements SolicitudCursoService {

    @Autowired
    private SolicitudCursoDao solicitudCursoRepository;

    @Override
    public List<SolicitudCursoDocumento> consultarSolicitudesPendientes() {
        // Recupera todas las solicitudes con estatus 'en revision'
        return solicitudCursoRepository.findByEstatus("en revision");
    }
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

