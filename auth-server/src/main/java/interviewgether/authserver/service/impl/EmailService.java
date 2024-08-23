package interviewgether.authserver.service.impl;

import interviewgether.authserver.notifications.EmailInstance;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    private final JavaMailSender javaMailSender;
    @Value("${spring.mail.username}")
    private String sender;

    public EmailService(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    @Async
    public void sendSimpleMessage(EmailInstance emailInstance){
        SimpleMailMessage email = new SimpleMailMessage();
        email.setFrom(sender);
        email.setTo(emailInstance.recipient());
        email.setSubject(emailInstance.subject());
        email.setText(emailInstance.body());

        javaMailSender.send(email);
    }
}
