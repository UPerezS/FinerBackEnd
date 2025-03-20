package mx.utng.finer_back_end.Administrador.scheduler;

import mx.utng.finer_back_end.Administrador.Implement.SolicitudCategoriaServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class CustoTaskScheduler {

    @Autowired
    private SolicitudCategoriaServiceImpl solicitudCategoriaService;

    // Esta tarea se ejecutará todos los días a la medianoche
    @Scheduled(cron = "0 0 0 * * ?")
    public void eliminarSolicitudesRechazadasAntiguas() {
        solicitudCategoriaService.eliminarSolicitudesCategoriaRechazadasAntiguas();
    }
}

