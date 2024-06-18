package com.digitalholics.consultationsservice.Shared.EmailService;

import com.digitalholics.consultationsservice.Consultation.domain.model.entity.External.Patient;
import com.digitalholics.consultationsservice.Consultation.domain.model.entity.External.User;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.web.client.RestTemplate;

@Component
public class MailSenderService {

    private final JavaMailSender mailSender;

    public MailSenderService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendNewMail(String to, String subject, String body) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message,true,"UTF-8");
        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(body, true);

        mailSender.send(message);
    }

    public String buildHtmlEmail(String userName, String consultationNumber, String dateTime, String physiotherapistNumber) {
        return "<!DOCTYPE html>" +
                "<html>" +
                "<head>" +
                "<style>" +
                "body { font-family: Arial, sans-serif; background-color: #f4f4f4; margin: 0; padding: 0; }" +
                ".container { max-width: 600px; margin: 20px auto; padding: 20px; background-color: #ffffff; border: 1px solid #e0e0e0; }" +
                ".header { background-color: #00a8e8; padding: 10px 20px; text-align: center; display: flex; align-items: center; justify-content: space-evenly; }" +
                ".header img { max-width: 60px; margin-right: 10px; }" +
                ".header h1 { color: #ffffff; font-size: 24px; margin: 0; }" +
                ".content { padding: 20px; }" +
                ".content p { font-size: 14px; color: #333333; }" +
                ".content h2 { font-size: 18px; color: #00a8e8; }" +
                ".footer { background-color: #00a8e8; padding: 10px 20px; text-align: center; color: #ffffff; font-size: 12px; }" +
                ".footer p { margin: 0; }" +
                "</style>" +
                "</head>" +
                "<body>" +
                "<div class='container'>" +
                "<div class='header'>" +
                "<img src='https://github.com/upc-pre-202302-IoTheraphy-SI572-SW71/ReportAssets/blob/main/logo-image.png?raw=true' alt='Theraphy'>" +
                "<h1>Theraphy</h1>" +
                "</div>" +
                "<div class='content'>" +
                "<p>Estimado/a " + userName + ",</p>" +
                "<p>Nos complace informarle que su consulta ha sido creada con éxito. Nuestro equipo está trabajando para brindarle la mejor respuesta en el menor tiempo posible.</p>" +
                "<h2>Detalles de su consulta:</h2>" +
                "<p><b>Motivo de Consulta:</b> " + consultationNumber + "</p>" +
                "<p><b>Fecha y Hora:</b> " + dateTime + "</p>" +
                "<p><b>Fisioterapeuta:</b> " + physiotherapistNumber + "</p>" +
                "<p>Si necesita información adicional o desea realizar alguna modificación en su consulta, por favor no dude en contactarnos.</p>" +
                "<p>Gracias por su paciencia y confianza en nuestros servicios.</p>" +
                "</div>" +
                "<div class='footer'>" +
                "<p>@DigitAlcoholics-Theraphy</p>" +
                "</div>" +
                "</div>" +
                "</body>" +
                "</html>";
    }
}
