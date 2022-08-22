package com.nelly.application.controller;

import com.nelly.application.domain.Users;
import com.nelly.application.dto.Response;
import com.nelly.application.dto.request.SignupDataRequest;
import com.nelly.application.dto.request.WebviewRequest;
import com.nelly.application.dto.response.AppInitDataResponse;
import com.nelly.application.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ScraperController {

    private final Response response;
    private final UserService userService;

    @PostMapping("/scraper/web-url")
    public ResponseEntity<?> enterWebviewUrl(WebviewRequest dto) {
        Users user = userService.getAppUser().orElse(null);



        return response.success();
    }
}
