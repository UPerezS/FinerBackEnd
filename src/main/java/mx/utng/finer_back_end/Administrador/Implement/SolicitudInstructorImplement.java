package mx.utng.finer_back_end.Administrador.Implement;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.mail.internet.MimeMessage;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import jakarta.activation.FileDataSource;
import mx.utng.finer_back_end.Administrador.Dao.SolicitudInstructorDao;
import mx.utng.finer_back_end.Administrador.Services.SolicitudInstructorService;
import mx.utng.finer_back_end.Documentos.SolicitudInstructorDocumento;
import java.util.List;

@Service
public class SolicitudInstructorImplement implements SolicitudInstructorService {

    @Autowired
    private SolicitudInstructorDao solicitudInstructorDao;

    @Autowired
    private JavaMailSender javaMailSender;

    public SolicitudInstructorDocumento rechazarInstructor(Integer idSolicitudInstructor, String motivo) {
        List<Object[]> resultado = solicitudInstructorDao.rechazarInstructor(idSolicitudInstructor);

        if (resultado != null && !resultado.isEmpty()) {
            Object[] fila = resultado.get(0);

            if (fila != null && fila.length >= 12) {
                SolicitudInstructorDocumento solicitud = new SolicitudInstructorDocumento(
                        (String) fila[5]);
                enviarCorreoRechazoInstructor((String) fila[5], motivo);
                return solicitud;
            }
        }
        throw new RuntimeException("No se encontró la solicitud o los datos son inválidos.");
    }

    /**
     * Envía un correo electrónico al usuario notificando el rechazo de su solicitud
     * para ser instructor en Finer.
     * Informa al usuario que puede volver a intentar el proceso de creación de
     * cuenta si lo desea.
     * 
     * @param correoUsuario Correo del usuario al que se enviará la notificación
     * @param motivoRechazo Motivo por el cual se rechazó la solicitud
     */

     
     private void enviarCorreoRechazoInstructor(String correoUsuario, String motivoRechazo) {
         try {
             // Creamos el mensaje MIME para permitir el uso de HTML
             MimeMessage mensaje = javaMailSender.createMimeMessage();
             MimeMessageHelper helper = new MimeMessageHelper(mensaje, true); // 'true' para permitir HTML y adjuntos
     
             // Configuramos el remitente, destinatario y asunto
             helper.setFrom("finner.oficial.2025@gmail.com");
             helper.setTo(correoUsuario);
             helper.setSubject("Solicitud de instructor rechazada - Finer");
     
             // Ruta de la imagen (se obtiene desde el directorio resources)
             String logoPath = getClass().getClassLoader().getResource("finer_logo.png").getPath();
             
             // Cuerpo del mensaje con formato HTML
             String cuerpoMensaje = "<html>" +
                     "<body style=\"font-family: Arial, sans-serif; background-color: #f5f5f5; color: #333; padding: 20px;\">" +
                     "<div style=\"background-color: #ffffff; padding: 30px; border-radius: 8px; box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);\">" +
                     "<h2 style=\"color: #2d6a4f;\">Estimado/a usuario/a,</h2>" +
                     "<p>Lamentamos informarle que su solicitud para ser instructor dentro de la plataforma <strong>Finer</strong> ha sido rechazada.</p>" +
                     "<p><strong>Motivo del rechazo:</strong> " + motivoRechazo + "</p>" +
                     "<p style=\"color: #2d6a4f;\">Si desea continuar con el proceso para convertirse en parte de nuestra comunidad de instructores, le invitamos a realizar nuevamente el proceso de creación de cuenta en nuestro sistema. ¡Nos encantaría contar con su participación en el futuro!</p>" +
                     "<p>Recuerde que, al ser parte de <strong>Finer</strong>, podrá compartir su experiencia y conocimientos con miles de estudiantes, y generar un impacto positivo en la educación.</p>" +
                     "<p><strong>Finer</strong> es una plataforma dedicada a la enseñanza y formación de miles de usuarios. Nos importa profundamente la calidad educativa de nuestros instructores y la enseñanza que brindan. ¡Esperamos contar con su participación para que pueda contribuir con su experiencia en uno de nuestros cursos!</p>" +
                     "<p>Si tiene alguna duda o desea más información, no dude en ponerse en contacto con nuestro equipo administrativo.</p>" +
                     "<p><strong>¡Gracias por su interés en formar parte de Finer!</strong></p>" +
                     "<p>Atentamente,<br/>El equipo de Finer</p>" +
                     "<p style=\"font-style: italic; color: #6c757d;\">P.D. ¡Esperamos poder contar con su participación en una futura ocasión!</p>" +
                     
                     // Insertamos la imagen al final del correo
                     "<p style=\"text-align:center;\">" +
                     "<img src=\"cid:finerLogo\" alt=\"Finer Logo\" style=\"max-width: 200px;\" />" +
                     "</p>" +
                     "</div>" +
                     "</body>" +
                     "</html>";
     
             // Establecemos el contenido HTML en el mensaje
             helper.setText(cuerpoMensaje, true);  // El 'true' indica que el contenido es HTML
     
             // Adjuntamos la imagen al correo usando FileDataSource
             FileDataSource dataSource = new FileDataSource(logoPath);
             helper.addInline("finerLogo", dataSource);  // 'finerLogo' es el ID que utilizamos en el src del HTML
     
             // Enviamos el correo
             javaMailSender.send(mensaje);
     
         } catch (Exception e) {
             // Solo registramos la excepción pero no interrumpimos el flujo
             System.err.println("Error al enviar correo de rechazo al usuario: " + e.getMessage());
         }
     }
     
}
