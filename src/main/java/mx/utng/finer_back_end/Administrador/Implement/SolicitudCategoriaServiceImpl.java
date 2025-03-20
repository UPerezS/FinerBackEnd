package mx.utng.finer_back_end.Administrador.Implement;

import mx.utng.finer_back_end.Administrador.Services.SolicitudCategoriaService;
import mx.utng.finer_back_end.Documentos.CategoriaDocumento;
import mx.utng.finer_back_end.Documentos.SolicitudCategoriaDocumento;
import mx.utng.finer_back_end.Administrador.Dao.CategoriaDao;
import mx.utng.finer_back_end.Administrador.Dao.SolicitudCategoriaDao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class SolicitudCategoriaServiceImpl implements SolicitudCategoriaService {

    @Autowired
    private SolicitudCategoriaDao solicitudCategoriaDao;

    @Autowired
    private CategoriaDao categoriaDao;

    @Override
    public ResponseEntity<String> aprobarDesaprobarCategoria(Integer idSolicitud, boolean aprobar) {
        SolicitudCategoriaDocumento solicitud = solicitudCategoriaDao.findById(idSolicitud).orElse(null);

        if (solicitud == null) {
            return new ResponseEntity<>("Solicitud no encontrada", HttpStatus.BAD_REQUEST);
        }

        if (aprobar) {
            solicitud.setEstatus("aprobado");
            // Crear nueva categoría si la solicitud es aprobada
            CategoriaDocumento nuevaCategoria = new CategoriaDocumento();
            nuevaCategoria.setNombreCategoria(solicitud.getNombreCategoria());
            try {
                categoriaDao.save(nuevaCategoria); // Guarda la nueva categoría en la tabla Categoria
            } catch (Exception e) {
                return new ResponseEntity<>("Error al crear la categoría", HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } else {
            solicitud.setEstatus("rechazado");
        }

        try {
            solicitudCategoriaDao.save(solicitud); // Actualiza el estado de la solicitud
            return new ResponseEntity<>("Solicitud " + (aprobar ? "aprobada y categoría creada" : "rechazada") + " con éxito", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Error al procesar la solicitud", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public List<SolicitudCategoriaDocumento> obtenerSolicitudesPorInstructor(Integer idUsuarioInstructor) {
        return solicitudCategoriaDao.findByIdUsuarioInstructor(idUsuarioInstructor);
    }

    @Override
    public void eliminarSolicitudesCategoriaRechazadasAntiguas() {
        // Fecha actual menos 30 días
        LocalDate fechaLimite = LocalDate.now().minusDays(30);

        // Buscar todas las solicitudes con estatus "rechazado" y fecha mayor a 30 días
        List<SolicitudCategoriaDocumento> solicitudesRechazadas = solicitudCategoriaDao.findByEstatusAndFechaSolicitudBefore("rechazado", fechaLimite);

        // Eliminar las solicitudes encontradas
        for (SolicitudCategoriaDocumento solicitud : solicitudesRechazadas) {
            solicitudCategoriaDao.delete(solicitud);
        }
    }
}
