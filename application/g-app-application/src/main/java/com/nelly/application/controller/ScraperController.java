package com.nelly.application.controller;

import com.nelly.application.domain.ScraperBrands;
import com.nelly.application.domain.Users;
import com.nelly.application.dto.Response;
import com.nelly.application.dto.request.SignupDataRequest;
import com.nelly.application.dto.request.WebviewRequest;
import com.nelly.application.dto.response.AppInitDataResponse;
import com.nelly.application.service.scraper.ScraperService;
import com.nelly.application.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.net.MalformedURLException;

@RestController
@RequiredArgsConstructor
public class ScraperController {

    private final Response response;
    private final UserService userService;
    private final ScraperService scraperService;

    @PostMapping("/scraper/web-url")
    public ResponseEntity<?> enterWebviewUrl(@RequestBody WebviewRequest dto) throws MalformedURLException {
        Users user = userService.getAppUser().orElse(null);
        scraperService.saveScrapRequest(dto, user);
        ScraperBrands brand = scraperService.searchScraperBrand(dto);
        scraperService.saveHistory(user, brand, dto.getUrl());
        return response.success(brand.getId());
    }

    @PostMapping("/scraper/add-cart")
    public ResponseEntity<?> addCart(@RequestBody WebviewRequest dto) throws MalformedURLException {
        Users user = userService.getAppUser().orElse(null);
        scraperService.saveScrapRequest(dto, user);
        return response.success(scraperService.searchScraperBrand(dto));
    }
}
