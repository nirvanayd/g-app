package com.nelly.application.controller;

import com.nelly.application.dto.Response;
import com.nelly.application.dto.request.AddCurrentItemRequest;
import com.nelly.application.dto.response.AppVersionResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AppController {

    private final Response response;

    @PostMapping("/version")
    public ResponseEntity<?> getAppVersionConf() {
        AppVersionResponse appVersionResponse = new AppVersionResponse();
        appVersionResponse.setAppVersion("1.0.0");
        appVersionResponse.setMinVersion("1.0.0");
        appVersionResponse.setLatestVersion("1.0.0");
        return response.success(appVersionResponse);
    }
}