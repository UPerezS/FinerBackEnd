package mx.utng.finer_back_end.Instructor.Services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import mx.utng.finer_back_end.Instructor.Dao.CategoriaSolicitudDao;
import mx.utng.finer_back_end.Instructor.Documentos.CategoriaSolicitudDTO;

@Service
public class CategoriaSolicitudService {

    @Autowired
    private CategoriaSolicitudDao categoriaSolicitudDao;

    @Autowired
    private JavaMailSender mailSender;

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
            categoriaSolicitudDTO.getMotivoSolicitud(),
            categoriaSolicitudDTO.getIdInstructor(),
            categoriaSolicitudDTO.getIdUsuarioAdmin()
        );

        
            sendEmailToAdmin(categoriaSolicitudDTO);

        return "Solicitud registrada con éxito";
    }

    /**
     * Enviar correo electrónico al administrador con los detalles de la solicitud.
     *
     * @param categoriaSolicitudDTO datos de la solicitud.
     */
    private void sendEmailToAdmin(CategoriaSolicitudDTO categoriaSolicitudDTO) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo("carlosbrv251023@gmail.com");
        message.setSubject("Solicitud de nueva categoría: ");
        message.setText("El instructor con ID: " + categoriaSolicitudDTO.getIdInstructor() + 
                        " ha solicitado la creación de una nueva categoría.\n" +
                        "Nombre de la categoría solicitada: " + categoriaSolicitudDTO.getNombreCategoria() + 
                        "\nMotivo de la solicitud: " + categoriaSolicitudDTO.getMotivoSolicitud());

        mailSender.send(message);
    }
}
