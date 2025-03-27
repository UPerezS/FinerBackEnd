
package mx.utng.finer_back_end.Publicos.Services;

import java.net.URL;
import java.net.URLDecoder;

import org.springframework.beans.factory.annotation.Autowired;
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
            // Crear el mensaje MIME para permitir el uso de HTML
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
    
            // Configurar el remitente, destinatario y asunto
            helper.setFrom("finner.oficial.2025@gmail.com");  // Dirección local establecida
            helper.setTo(email);
            helper.setSubject("Código de Verificación - " + APP_NAME);
    
            // Obtener la URL del recurso (imagen) y decodificar la ruta
            URL logoUrl = getClass().getClassLoader().getResource("finer_logo.png");
            String logoPath = "";
            if (logoUrl != null) {
                logoPath = URLDecoder.decode(logoUrl.getPath(), "UTF-8");
            } else {
                System.err.println("No se encontró el recurso finer_logo.png, se enviará el correo sin logo.");
            }
    
            // Construir el cuerpo del mensaje en formato HTML
            String cuerpoMensaje = "<html>" +
                    "<body style=\"font-family: Arial, sans-serif; background-color: #f5f5f5; color: #333; padding: 20px;\">" +
                    "<div style=\"background-color: #ffffff; padding: 30px; border-radius: 8px; box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);\">" +
                    "<h2 style=\"color: #2d6a4f;\">Hola,</h2>" +
                    "<p>Has solicitado un código de verificación para recuperar tu contrasenia a <strong>" + APP_NAME + "</strong>.</p>" +
                    "<p style=\"font-size: 18px; font-weight: bold; color: #2d6a4f;\">Código: <span style=\"color: #e63946;\">" + token + "</span></p>" +
                    "<p>" + INSTRUCCIONES + "</p>" +
                    "<p>Si no solicitaste este código, por favor ignora este mensaje.</p>" +
                    "<p>Atentamente,<br/>El equipo de <strong>" + APP_NAME + "</strong></p>" +
                    (logoUrl != null ? "<p style=\"text-align:center;\"><img src=\"cid:finerLogo\" alt=\"Finer Logo\" style=\"max-width: 200px;\" /></p>" : "") +
                    "</div>" +
                    "</body>" +
                    "</html>";
    
            // Establecer el contenido HTML del mensaje
            helper.setText(cuerpoMensaje, true);
    
            // Adjuntar la imagen solo si se encontró el recurso
            if (!logoPath.isEmpty()) {
                FileDataSource dataSource = new FileDataSource(logoPath);
                helper.addInline("finerLogo", dataSource);
            }
    
            // Enviar el correo
            javaMailSender.send(message);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    
}