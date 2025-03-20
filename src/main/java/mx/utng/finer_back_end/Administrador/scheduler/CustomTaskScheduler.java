package mx.utng.finer_back_end.Administrador.scheduler;

import mx.utng.finer_back_end.Administrador.Implement.SolicitudCursoServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class CustomTaskScheduler {

    @Autowired
    private SolicitudCursoServiceImpl solicitudCursoService;

    // Método que se ejecutará automáticamente una vez al día
    @Scheduled(cron = "0 0 0 * * ?")  // Esto ejecutará el método todos los días a la medianoche
    public void eliminarSolicitudesRechazadasAntiguas() {
        solicitudCursoService.eliminarSolicitudesCursoRechazadasAntiguas();
    }
}
