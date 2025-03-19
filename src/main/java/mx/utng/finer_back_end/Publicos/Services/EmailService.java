package mx.utng.finer_back_end.Publicos.Services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    private static final String APP_NAME = "Finer-BackEnd";
    private static final String INSTRUCCIONES = "Utiliza este código para verificar tu identidad.";

    public boolean mandarTokenNumerico(String email, String token) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(email);
            message.setSubject("Código de Verificación - " + APP_NAME);
            message.setText(
                    "Hola,\n\n"
                    + "Has solicitado un código de verificación para acceder a " + APP_NAME + ".\n\n"
                    + "📌 Código: " + token + "\n\n"
                    + INSTRUCCIONES + "\n\n"
                    + "Si no solicitaste este código, ignora este mensaje."
            );

            mailSender.send(message);
            return true; // Indica que el correo fue enviado con éxito
        } catch (Exception e) {
            e.printStackTrace();
            return false; // Indica un error al enviar el correo
        }
    }
}
