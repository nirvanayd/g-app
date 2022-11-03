package com.nelly.application.controller;

import com.nelly.application.domain.ScraperBrands;
import com.nelly.application.domain.Users;
import com.nelly.application.dto.Response;
import com.nelly.application.dto.request.*;
import com.nelly.application.dto.response.AppInitDataResponse;
import com.nelly.application.dto.response.StoreCheckResponse;
import com.nelly.application.service.scraper.ScraperService;
import com.nelly.application.service.user.UserService;
import lombok.Getter;
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
        return response.success(scraperService.saveHistory(user, brand, dto.getUrl()));
    }

    @GetMapping("/scraper/store-check")
    public ResponseEntity<?> storeCheck(WebviewRequest dto) throws MalformedURLException {
        ScraperBrands brand = scraperService.searchScraperBrand(dto);
        StoreCheckResponse storeCheckResponse = new StoreCheckResponse();
        storeCheckResponse.setScrapId(brand.getId());
        storeCheckResponse.setBrandName(brand.getName());
        storeCheckResponse.setBrandId(brand.getBrandId());
        return response.success(storeCheckResponse);
    }

    @GetMapping("/scraper/current-item")
    public ResponseEntity<?> getCurrentItemList(GetUserCurrentScrapItemRequest dto) throws MalformedURLException {
        Users user = userService.getAppUser().orElse(null);
        return response.success(scraperService.getCurrentScrapItemList(user, dto));
    }

    @PostMapping("/scraper/cart")
    public ResponseEntity<?> addCart(@RequestBody WebviewRequest dto) throws MalformedURLException {
        Users user = userService.getAppUser().orElse(null);
        scraperService.addCart(dto, user);
        return response.success();
    }

    @GetMapping("/scraper/cart")
    public ResponseEntity<?> getUserCartList(GetUserCartRequest dto) {
        Users user = userService.getAppUser().orElse(null);
        return response.success(scraperService.getScrapCartList(user, dto));
    }
}
