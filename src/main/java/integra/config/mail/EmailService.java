package integra.config.mail;

import jakarta.mail.internet.MimeMessage;
import lombok.AllArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class EmailService {
    private JavaMailSender mailSender;
    public void sendHtmlEmail(String to, String subject, String htmlBody) throws Exception {
        MimeMessage message = mailSender.createMimeMessage();
        // true = multipart, necesario si luego quieres adjuntos o imágenes inline
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

        helper.setTo(to);
        helper.setFrom("sci.integra@gmail.com", "Integra");
        helper.setSubject(subject);
        helper.setText(htmlBody, true);   // true = body es HTML
        mailSender.send(message);
    }
}
