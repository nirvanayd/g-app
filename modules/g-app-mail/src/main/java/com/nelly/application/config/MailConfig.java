package com.nelly.application.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Properties;

@Configuration
@RequiredArgsConstructor
public class MailConfig {
    Properties properties = new Properties();
    private final MailProperties mailProperties;

    @Bean
    public JavaMailSender javaMailService() {
        JavaMailSenderImpl javaMailSender = new JavaMailSenderImpl();

        javaMailSender.setHost(mailProperties.getHost());
        javaMailSender.setPort(mailProperties.getPort());
        javaMailSender.setUsername(mailProperties.getUsername());
        javaMailSender.setPassword(mailProperties.getPassword());

        properties.put("mail.transport.protocol", mailProperties.getProtocol());
        properties.put("mail.smtp.auth", mailProperties.getSmtp().getAuth());
        properties.put("mail.smtp.starttls.enable", mailProperties.getSmtp().getStarttls());
        properties.put("mail.smtp.debug", mailProperties.getDebug());
//        properties.put("mail.smtp.ssl.enable", mailProperties.getSmtp().getSslenable());
        properties.put("mail.smtp.ssl.trust", mailProperties.getSmtp().getSsltrust());

        javaMailSender.setJavaMailProperties(properties);

        return javaMailSender;
    }
}
