package com.nelly.application.controller;

import com.nelly.application.dto.Response;
import com.nelly.application.dto.request.AddCurrentItemRequest;
import com.nelly.application.dto.request.SignupDataRequest;
import com.nelly.application.dto.response.AppInitDataResponse;
import com.nelly.application.dto.response.AppVersionResponse;
import com.nelly.application.service.app.AppService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AppController {

    private final AppService appService;
    private final Response response;

    @GetMapping("/version")
    public ResponseEntity<?> getAppVersionConf() {
        AppVersionResponse appVersionResponse = new AppVersionResponse();
        appVersionResponse.setAppVersion("1.0.0");
        appVersionResponse.setMinVersion("1.0.0");
        appVersionResponse.setLatestVersion("1.0.0");
        return response.success(appVersionResponse);
    }

    @GetMapping("/init-data")
    public ResponseEntity<?> getAppInitData(SignupDataRequest signupDataRequest) {
        AppInitDataResponse appInitData = appService.getAppInitData(signupDataRequest.getVersion());
        return response.success(appInitData);
    }
}
