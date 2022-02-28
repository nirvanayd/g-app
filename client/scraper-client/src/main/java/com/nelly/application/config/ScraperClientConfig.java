package com.nelly.application.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;

import java.time.Duration;

@Configuration
public class ScraperClientConfig {

    HttpClient client = HttpClient.create()
            .responseTimeout(Duration.ofSeconds(5));

    @Bean
    public WebClient scraperClient() {
        return WebClient.builder()
                .baseUrl("http://localhost:3000")
                .clientConnector(new ReactorClientHttpConnector(client))
                .build();
    }
}
