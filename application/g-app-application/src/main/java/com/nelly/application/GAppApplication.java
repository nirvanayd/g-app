package com.nelly.application;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class GAppApplication {
    public static void main(String[] args) {
        SpringApplication.run(GAppApplication.class, args);
    }
}
