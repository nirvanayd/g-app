package com.nelly.application.dto;

import java.util.Map;

public class NaverUserInfo implements OAuth2UserInfo {

    private Map<String, Object> attributes;

    NaverUserInfo(Map<String, Object> attributes) {
        this.attributes = attributes;
    }

    NaverUserInfo() {}

    @Override
    public Map<String, Object> getAttributes() {
        return null;
    }

    @Override
    public String getProviderId() {
        return null;
    }

    @Override
    public String getProvider() {
        return null;
    }

    @Override
    public String getEmail() {
        return null;
    }

    @Override
    public void setAttributes(String uid, String email) {

    }
}
