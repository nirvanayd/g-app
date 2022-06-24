package com.nelly.application.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "mail")
@Data
public class MailProperties {
    /**
     mail:
     host: smtp.naver.com
     port: 465
     username: nirvanayd@naver.com
     password: 220qjsxkrhrkwk!
     properties:
     mail.smtp.auth: true
     mail.smtp.ssl.enable: true
     mail.smtp.ssl.trust: smtp.naver.com
     */

    private String host;
    private int port;
    private String username;
    private String password;

    private String protocol;
    private String encoding;
    private String debug;

    Smtp smtp = new Smtp();

    @Data
    public static class Smtp {
        private String auth;
        private String starttls;
        private String ssltrust;
        private String sslenable;
    }


}
