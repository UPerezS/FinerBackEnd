package mx.utng.finer_back_end.Publicos.Services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import jakarta.activation.FileDataSource;
import jakarta.mail.internet.MimeMessage;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender javaMailSender;

    private static final String APP_NAME = "Finer-BackEnd";
    private static final String INSTRUCCIONES = "Utiliza este código para verificar tu identidad.";

    public boolean mandarTokenNumerico(String email, String token) {
        try {
            // Creamos el mensaje MIME para permitir el uso de HTML
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true); // 'true' para permitir HTML y adjuntos
    
            // Configuramos el destinatario y el asunto
            helper.setTo(email);
            helper.setSubject("Código de Verificación - " + APP_NAME);
            
            // Ruta de la imagen (asumimos que el archivo 'finer_logo.png' está en el classpath)
            String logoPath = getClass().getClassLoader().getResource("finer_logo.png").getPath();
            
            // Cuerpo del mensaje con formato HTML
            String cuerpoMensaje = "<html>" +
                    "<body style=\"font-family: Arial, sans-serif; background-color: #f5f5f5; color: #333; padding: 20px;\">" +
                    "<div style=\"background-color: #ffffff; padding: 30px; border-radius: 8px; box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);\">" +
                    "<h2 style=\"color: #2d6a4f;\">Hola,</h2>" +
                    "<p>Has solicitado un código de verificación para acceder a <strong>" + APP_NAME + "</strong>.</p>" +
                    "<p style=\"font-size: 18px; font-weight: bold; color: #2d6a4f;\">Código: <span style=\"color: #e63946;\">" + token + "</span></p>" +
                    "<p>" + INSTRUCCIONES + "</p>" +
                    "<p>Si no solicitaste este código, por favor ignora este mensaje.</p>" +
                    "<p>Atentamente,<br/>El equipo de <strong>" + APP_NAME + "</strong></p>" +
                    "<p style=\"text-align:center;\">" +
                         "<img src=\"cid:finerLogo\" alt=\"Finer Logo\" style=\"max-width: 200px;\" />" +
                         "</p>" +
                    "</div>" +
                    "</body>" +
                    "</html>";
    
            // Establecemos el contenido HTML en el mensaje
            helper.setText(cuerpoMensaje, true);  // El 'true' indica que el contenido es HTML
    
            // Adjuntamos la imagen al correo usando el CID
            FileDataSource dataSource = new FileDataSource(logoPath);
            helper.addInline("finerLogo", dataSource);  // 'finerLogo' es el ID que utilizamos en el src del HTML
    
            // Enviamos el correo
            javaMailSender.send(message);
            return true; // Indica que el correo fue enviado con éxito
        } catch (Exception e) {
            e.printStackTrace();
            return false; // Indica un error al enviar el correo
        }
    }
    

}
