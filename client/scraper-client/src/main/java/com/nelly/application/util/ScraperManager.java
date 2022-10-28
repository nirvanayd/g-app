package com.nelly.application.util;

import com.google.gson.Gson;
import com.nelly.application.dto.ItemScrapDto;
import com.nelly.application.dto.ScraperErrorDto;
import com.nelly.application.exception.ScraperException;
import com.nelly.application.exception.enums.ExceptionCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.asynchttpclient.AsyncHttpClient;
import org.asynchttpclient.Dsl;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserter;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.awt.*;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
@Component
@Slf4j
public class ScraperManager {
    private final WebClient scraperClient;

    public ItemScrapDto addCurrentItem(String url, String moduleName) {
        if (url == null || url.isEmpty()) throw new RuntimeException("호출할 수 없는 url 양식입니다.");

        // 저장된 url 인지 검사
        Gson gson = new Gson();
        Map<String, String> map = new HashMap<>();
        map.put("url", url);
        map.put("moduleName", moduleName);

        log.info("module name : " + moduleName);
        log.info("url : " + url);

        Mono<String> scraperResponse = scraperClient
                .post()
                .uri("/add-item")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.ALL)
                .body(BodyInserters.fromValue(gson.toJson(map)))
                .retrieve()
                .onStatus(ExceptionCode.SCRAPER_EXCEPTION.getStatus()::equals, response ->
                    response.bodyToMono(String.class).map(ScraperException::new)
                )
                .bodyToMono(String.class);

        return gson.fromJson(scraperResponse.block(), ItemScrapDto.class);
    }

    public void getScrapImage(String url) {
        AsyncHttpClient client = Dsl.asyncHttpClient();

        client.prepareGet(url).execute();
    }
}
