package com.nelly.application.mail;

import com.nelly.application.config.MailProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

@Service
@RequiredArgsConstructor
public class MailSender {

    private final JavaMailSender javaMailSender;
    private final MailProperties mailProperties;

    public void sendMail(String subject, String content, String receiver) {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        try {
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true, mailProperties.getEncoding());
            mimeMessageHelper.setText(content, true);
            mimeMessageHelper.setSubject(subject);
            mimeMessageHelper.setTo(new InternetAddress(receiver));
            mimeMessageHelper.setFrom(new InternetAddress(mailProperties.getUsername()));
            javaMailSender.send(mimeMessage);
        } catch (MessagingException e) {
            e.getStackTrace();
        }
    }
}
