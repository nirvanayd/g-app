package com.nelly.application;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@SpringBootApplication
@EnableWebMvc
public class GAppApplication {
    public static void main(String[] args) {
        SpringApplication.run(GAppApplication.class, args);
    }
}
