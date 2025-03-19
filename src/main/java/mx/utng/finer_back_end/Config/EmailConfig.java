package mx.utng.finer_back_end.Config;

import jakarta.mail.Session;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.security.cert.X509Certificate;
import java.util.Properties;
import jakarta.mail.Authenticator;
import jakarta.mail.PasswordAuthentication;

@Configuration
public class EmailConfig {

    @Bean
    public JavaMailSender javaMailSender() {
        Properties properties = new Properties();
        properties.put("mail.smtp.host", "smtp.gmail.com");
        properties.put("mail.smtp.port", "587");
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");

        // TrustManager que acepta todos los certificados
        TrustManager[] trustAllCertificates = new TrustManager[]{
            new X509TrustManager() {
                public X509Certificate[] getAcceptedIssuers() {
                    return null;
                }

                public void checkClientTrusted(X509Certificate[] certs, String authType) {}

                public void checkServerTrusted(X509Certificate[] certs, String authType) {}
            }
        };

        try {
            SSLContext sc = SSLContext.getInstance("TLS");
            sc.init(null, trustAllCertificates, new java.security.SecureRandom());

            properties.put("mail.smtp.ssl.trust", "*");

            // Crear la sesión con el nuevo Authenticator
            Session session = Session.getInstance(properties, new Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication("finner.oficial.2025@gmail.com", "vwrk ujlb fnnl yrph");
                }
            });

            JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
            mailSender.setSession(session);
            return mailSender;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
