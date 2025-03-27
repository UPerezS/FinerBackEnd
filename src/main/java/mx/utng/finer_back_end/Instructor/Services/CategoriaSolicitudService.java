package mx.utng.finer_back_end.Instructor.Services;

import java.net.URL;
import java.net.URLDecoder;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import jakarta.activation.FileDataSource;
import jakarta.mail.internet.MimeMessage;
import mx.utng.finer_back_end.Instructor.Dao.CategoriaSolicitudDao;
import mx.utng.finer_back_end.Instructor.Documentos.CategoriaSolicitudDTO;

@Service
public class CategoriaSolicitudService {

    @Autowired
    private CategoriaSolicitudDao categoriaSolicitudDao;

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private InstructorService InstructorService;

    /**
     * Procesar la solicitud de una nueva categoría.
     *
     * @param categoriaSolicitudDTO datos de la solicitud de categoría.
     * @return Mensaje de éxito o error.
     */
    public String solicitarCategoria(CategoriaSolicitudDTO categoriaSolicitudDTO) {
        // Registrar la solicitud en la base de datos
            categoriaSolicitudDao.registrarSolicitudCategoria(
            categoriaSolicitudDTO.getNombreCategoria(),
            categoriaSolicitudDTO.getDescripcion(),
            categoriaSolicitudDTO.getIdInstructor()
            /* categoriaSolicitudDTO.getIdUsuarioAdmin() */
        );

        String nombreInstructor = InstructorService.obtenerNombreInstructor(categoriaSolicitudDTO.getIdInstructor());
        
        // Log para verificar el nombre del instructor
        System.out.println("Nombre del instructor obtenido: " + nombreInstructor);

        sendEmailToAdmin(categoriaSolicitudDTO, nombreInstructor);

        return "Solicitud registrada con éxito";
    }

    /**
     * Enviar correo electrónico al administrador con los detalles de la solicitud.
     *
     * @param categoriaSolicitudDTO datos de la solicitud.
     */
    private void sendEmailToAdmin(CategoriaSolicitudDTO categoriaSolicitudDTO, String nombreInstructor) {
    try {
        MimeMessage mensaje = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mensaje, true);

        helper.setFrom("finner.oficial.2025@gmail.com");
        helper.setTo("carlosbrv251023@gmail.com");
        helper.setSubject("Solicitud de nueva categoría: ");

        // Intentar obtener la URL del logo desde el classpath (src/main/resources)
        URL logoUrl = getClass().getClassLoader().getResource("finer_logo.png");
        String logoPath = "";
        if (logoUrl != null) {
            // Decodificar la ruta para eliminar caracteres codificados (como %20)
            logoPath = URLDecoder.decode(logoUrl.getPath(), "UTF-8");
            System.out.println("Ruta decodificada del logo: " + logoPath);
        } else {
            System.err.println("No se encontró el recurso finer_logo.png, se enviará sin logo.");
        }

        // Construir el contenido HTML del mensaje
        String cuerpoMensaje = "<html>" +
            "<body style=\"font-family: Arial, sans-serif; background-color: #f5f5f5; color: #333; padding: 20px;\">" +
            "<div style=\"background-color: #ffffff; padding: 30px; border-radius: 8px; box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);\">" +
            "<h2 style=\"color: #2d6a4f;\">Solicitud de Nueva Categoría</h2>" +
            "<p>El instructor <strong>" + nombreInstructor+ "</strong> ha solicitado la creación de una nueva categoría.</p>" +
            "<p>El instructor con ID: <strong>" + categoriaSolicitudDTO.getIdInstructor() + "</strong> ha solicitado la creación de una nueva categoría.</p>" +
            "<p><strong>Nombre de la categoría solicitada:</strong> " + categoriaSolicitudDTO.getNombreCategoria() + "</p>" +
            "<p><strong>Motivo de la solicitud:</strong> " + categoriaSolicitudDTO.getDescripcion() + "</p>" +
            // Incluir la imagen solo si se encontró el recurso
            (logoUrl != null ? "<p style=\"text-align:center;\"><img src=\"cid:finerLogo\" alt=\"Finer Logo\" style=\"max-width: 200px;\" /></p>" : "") +
            "</div>" +
            "</body>" +
            "</html>";

        helper.setText(cuerpoMensaje, true);

        // Adjuntar la imagen del logo usando la ruta decodificada
        if (logoUrl != null) {
            FileDataSource dataSource = new FileDataSource(logoPath);
            helper.addInline("finerLogo", dataSource);
        }

        mailSender.send(mensaje);
        System.out.println("Correo enviado al administrador correctamente.");
    } catch (Exception e) {
        System.err.println("Error al enviar correo al administrador: " + e.getMessage());
        e.printStackTrace();
    }
}


}
