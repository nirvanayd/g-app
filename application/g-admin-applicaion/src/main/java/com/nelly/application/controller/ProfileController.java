package com.nelly.application.controller;

import com.nelly.application.config.EnvProperties;
import com.nelly.application.enums.Profiles;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ProfileController {

    private final EnvProperties envProperties;

    @GetMapping("/prod-profile")
    public String prodProfile() {
        String onProfile = envProperties.getActivate().getOnProfile();
        return Profiles.hasProfile("prod", onProfile);
    }

    @GetMapping("/local-profile")
    public String localProfile() {
        String onProfile = envProperties.getActivate().getOnProfile();
        return Profiles.hasProfile("local", onProfile);
    }
}
