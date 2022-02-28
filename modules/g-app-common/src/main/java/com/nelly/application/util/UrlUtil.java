package com.nelly.application.util;

import com.nelly.application.dto.UrlInfoDto;

import java.net.MalformedURLException;
import java.net.URL;

public class UrlUtil {

    public static UrlInfoDto parseUrl(String url) throws MalformedURLException {
        URL inputUrl = new URL(url);
        return UrlInfoDto.builder()
                .protocol(inputUrl.getProtocol())
                .authority(inputUrl.getAuthority())
                .host(inputUrl.getHost())
                .port(inputUrl.getPort())
                .path(inputUrl.getPath())
                .query(inputUrl.getQuery())
                .file(inputUrl.getFile())
                .ref(inputUrl.getRef())
                .build();
    }
}
