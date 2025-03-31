package mx.utng.finer_back_end.Administrador.Implement;

import mx.utng.finer_back_end.Administrador.Services.SolicitudCategoriaService;
import mx.utng.finer_back_end.Documentos.CategoriaDocumento;
import mx.utng.finer_back_end.Documentos.SolicitudCategoriaDocumento;
import mx.utng.finer_back_end.Administrador.Dao.CategoriaDao;
import mx.utng.finer_back_end.Administrador.Dao.SolicitudCategoriaDao;
import mx.utng.finer_back_end.Administrador.Documentos.SolicitudCategoriaDatos;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.sql.Date;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class SolicitudCategoriaServiceImpl implements SolicitudCategoriaService {

    @Autowired
    private SolicitudCategoriaDao solicitudCategoriaDao;

    @Autowired
    private CategoriaDao categoriaDao;

    @Override
    public ResponseEntity<Map<String, Object>> aprobarDesaprobarCategoria(Integer idSolicitud, boolean aprobar) {
        Map<String, Object> response = new HashMap<>();
        SolicitudCategoriaDocumento solicitud = solicitudCategoriaDao.findById(idSolicitud).orElse(null);

        if (solicitud == null) {
            response.put("success", false);
            response.put("message", "Solicitud no encontrada");
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }

        if (aprobar) {
            solicitud.setEstatus("aprobada");
            CategoriaDocumento nuevaCategoria = new CategoriaDocumento();
            nuevaCategoria.setNombreCategoria(solicitud.getNombreCategoria());
            try {
                categoriaDao.save(nuevaCategoria);
            } catch (Exception e) {
                response.put("success", false);
                response.put("message", "Error al crear la categoría");
                return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } else {
            solicitud.setEstatus("rechazada");
        }

        try {
            solicitudCategoriaDao.save(solicitud);
            response.put("success", true);
            response.put("message", "Solicitud " + (aprobar ? "aprobada y categoría creada" : "rechazada") + " con éxito");
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error al procesar la solicitud");
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public List<SolicitudCategoriaDocumento> obtenerSolicitudesPorInstructor(Integer idUsuarioInstructor) {
        return solicitudCategoriaDao.findByIdUsuarioInstructor(idUsuarioInstructor);
    }

    @Override
    public List<SolicitudCategoriaDatos> obtenerTodasLasSolicitudes() {
        List<Object[]> resultados = solicitudCategoriaDao.obtenerSolicitudes();

        return resultados.stream().map(obj -> new SolicitudCategoriaDatos(
                (Integer) obj[0], // id_solicitud_categoria
                (Integer) obj[1], // id_usuario_instructor
                (String) obj[2], // nombre_categoria
                (String) obj[3], // descripcion
                (String) obj[4], // estatus
                (Timestamp) obj[5], // fecha_solicitud
                (String) obj[6], // nombre
                (String) obj[7], // apellido_paterno
                (String) obj[8] // apellido_materno
        )).collect(Collectors.toList());
    }

    @Override
    public void eliminarSolicitudesCategoriaRechazadasAntiguas() {
        // Fecha actual menos 30 días
        LocalDate fechaLimite = LocalDate.now().minusDays(30);

        // Buscar todas las solicitudes con estatus "rechazado" y fecha mayor a 30 días
        List<SolicitudCategoriaDocumento> solicitudesRechazadas = solicitudCategoriaDao
                .findByEstatusAndFechaSolicitudBefore("rechazado", fechaLimite);

        // Eliminar las solicitudes encontradas
        for (SolicitudCategoriaDocumento solicitud : solicitudesRechazadas) {
            solicitudCategoriaDao.delete(solicitud);
        }
    }
}
