package mx.utng.finer_back_end.Administrador.Implement;

import mx.utng.finer_back_end.Administrador.Services.SolicitudCategoriaService;
import mx.utng.finer_back_end.Documentos.SolicitudCategoriaDocumento;
import mx.utng.finer_back_end.Administrador.Dao.CategoriaDao;
import mx.utng.finer_back_end.Administrador.Dao.SolicitudCategoriaDao;
import mx.utng.finer_back_end.Administrador.Documentos.SolicitudCategoriaDatos;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
//import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.activation.FileDataSource;
import jakarta.mail.internet.MimeMessage;

import java.sql.Timestamp;
//import java.sql.Date;
import java.time.LocalDate;
import java.util.HashMap;
//import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class SolicitudCategoriaServiceImpl implements SolicitudCategoriaService {

    @Autowired
    private SolicitudCategoriaDao solicitudCategoriaDao;

    @Autowired
    private CategoriaDao categoriaDao;

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override

    @Transactional
    public ResponseEntity<Map<String, Object>> aprobarDesaprobarCategoria(Integer idSolicitud, boolean aprobar) {
        Map<String, Object> response = new HashMap<>();
        try {
            // Verificar si la solicitud existe
            Integer count = jdbcTemplate.queryForObject(
                    "SELECT COUNT(*) FROM solicitudcategoria WHERE id_solicitud_categoria = ?",
                    Integer.class, idSolicitud);

            if (count == null || count == 0) {
                response.put("status", HttpStatus.NOT_FOUND.value());
                response.put("message", "No se encontró la solicitud de categoría con el ID proporcionado.");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }

            // Obtener estado actual de la solicitud
            String estadoActual = jdbcTemplate.queryForObject(
                    "SELECT estatus FROM solicitudcategoria WHERE id_solicitud_categoria = ?",
                    String.class, idSolicitud);

            if ("aprobado".equals(estadoActual)) {
                response.put("status", HttpStatus.BAD_REQUEST.value());
                response.put("message", "No se puede aprobar una solicitud que ya ha sido aprobada.");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
            }

            if ("rechazado".equals(estadoActual)) {
                response.put("status", HttpStatus.BAD_REQUEST.value());
                response.put("message", "La solicitud ya ha sido rechazada anteriormente.");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
            }

            // Obtener correo y nombre de la categoría desde la base de datos
            Map<String, Object> solicitudInfo = jdbcTemplate.queryForMap(
                    "SELECT id_usuario_instructor, nombre_categoria FROM solicitudcategoria WHERE id_solicitud_categoria = ?",
                    idSolicitud);

            String nombreCategoria = (String) solicitudInfo.get("nombre_categoria");
            Integer idUsuarioInstructor = (Integer) solicitudInfo.get("id_usuario_instructor");

            if (idUsuarioInstructor == null) {
                response.put("status", HttpStatus.BAD_REQUEST.value());
                response.put("message", "No se pudo obtener el usuario instructor.");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
            }

            // Determinar el mensaje y asunto dependiendo si es aprobado o rechazado
            String asunto, mensaje, motivoRechazo = "";
            if (aprobar) {
                asunto = "Solicitud de Categoría Aprobada";
                mensaje = "Su solicitud para la categoría '" + nombreCategoria + "' ha sido aprobada.";
            } else {
                asunto = "Solicitud de Categoría Rechazada";
                motivoRechazo = "Categoria No cumple con lo requerido"; // Cambia esto con el motivo real
                mensaje = "Su solicitud para la categoría '" + nombreCategoria + "' ha sido rechazada. "
                        + motivoRechazo;
            }

            // Se asume que hay un método para obtener el correo del usuario instructor
            String correoSolicitante = obtenerCorreoInstructor(idUsuarioInstructor);
            if (correoSolicitante != null) {
                enviarCorreoNotificacion(correoSolicitante, asunto, motivoRechazo, aprobar, nombreCategoria);
            }

            // Actualizar el estado de la solicitud en la base de datos
            int filasAfectadas = jdbcTemplate.update(
                    "UPDATE solicitudcategoria SET estatus = ? WHERE id_solicitud_categoria = ?",
                    aprobar ? "aprobada" : "rechazada", idSolicitud);

            if (filasAfectadas > 0) {
                // Si la solicitud fue aprobada, agregar la categoría
                if (aprobar) {
                    jdbcTemplate.update(
                            "INSERT INTO categoria (nombre_categoria) VALUES (?)",
                            nombreCategoria);
                }
                response.put("status", HttpStatus.OK.value());
                response.put("message",
                        "Solicitud " + (aprobar ? "aprobada y categoría creada" : "rechazada") + " con éxito.");
                return ResponseEntity.ok(response);
            } else {
                response.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
                response.put("message", "Error al actualizar el estado de la solicitud.");
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
            }

        } catch (Exception e) {
            e.printStackTrace();
            response.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
            response.put("message", "Error al procesar la solicitud: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * Envía un correo de notificación al solicitante.
     */
    private void enviarCorreoNotificacion(String destinatario, String asunto, String motivoRechazo, boolean aprobado,
            String nombreCategoria) {
        try {

            MimeMessage mensaje = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mensaje, true);
            helper.setFrom("finner.oficial.2025@gmail.com");
            helper.setTo(destinatario);
            helper.setSubject(asunto);

            // Ruta de la imagen desde resources
            String logoPath = getClass().getClassLoader().getResource("finer_logo.png").getPath();

            // Cuerpo del mensaje con formato HTML
            String cuerpoMensaje = "<html>" +
                    "<body style=\"font-family: Arial, sans-serif; background-color: #f5f5f5; color: #333; padding: 20px;\">"
                    +
                    "<div style=\"background-color: #ffffff; padding: 30px; border-radius: 8px; box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);\">"
                    +
                    "<h2 style=\"color: #2d6a4f;\">Estimado/a usuario/a,</h2>";

            // Mensaje dependiendo si fue aprobado o rechazado
            if (aprobado) {
                cuerpoMensaje += "<p>¡Felicidades! Su solicitud para la categoría <strong>" + nombreCategoria
                        + "</strong> ha sido aprobada.</p>";
            } else {
                cuerpoMensaje += "<p>Lamentamos informarle que su solicitud para la categoría <strong>"
                        + nombreCategoria + "</strong> ha sido rechazada.</p>" +
                        "<p><strong>Motivo del rechazo:</strong> " + motivoRechazo + "</p>";
            }

            cuerpoMensaje += "<p style=\"color: #2d6a4f;\">Gracias por su interés en Finer.</p>" +
                    "<p>Atentamente,<br/>El equipo de Finer</p>" +
                    "<p style=\"text-align:center;\">" +
                    "<img src=\"cid:finerLogo\" alt=\"Finer Logo\" style=\"max-width: 200px;\" />" +
                    "</p>" +
                    "</div>" +
                    "</body>" +
                    "</html>";

            helper.setText(cuerpoMensaje, true);

            // Adjuntar la imagen como recurso en línea
            FileDataSource dataSource = new FileDataSource(logoPath);
            helper.addInline("finerLogo", dataSource);

            // Enviar el correo
            mailSender.send(mensaje);

        } catch (Exception e) {
            System.err.println("Error al enviar correo: " + e.getMessage());
        }
    }

    /**
     * Obtiene el correo del instructor basado en el ID.
     */
    private String obtenerCorreoInstructor(Integer idUsuarioInstructor) {
        try {
            return jdbcTemplate.queryForObject(
                    "SELECT correo FROM usuario WHERE id_usuario = ?",
                    String.class, idUsuarioInstructor);
        } catch (Exception e) {
            return null;

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
