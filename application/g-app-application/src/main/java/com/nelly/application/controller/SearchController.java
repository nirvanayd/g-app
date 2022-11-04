package com.nelly.application.controller;

import com.nelly.application.domain.ScraperBrands;
import com.nelly.application.domain.Users;
import com.nelly.application.dto.Response;
import com.nelly.application.dto.request.SearchRequest;
import com.nelly.application.dto.request.WebviewRequest;
import com.nelly.application.service.search.SearchService;
import com.nelly.application.service.user.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.net.MalformedURLException;

@RestController
@RequiredArgsConstructor
@Slf4j
public class SearchController {

    private final Response response;
    private final SearchService searchService;

    @GetMapping("/search/account")
    public ResponseEntity<?> searchAccount(SearchRequest dto) {
        return response.success(searchService.searchAccount(dto));
    }

    @GetMapping("/search/brand")
    public ResponseEntity<?> searchBrand(SearchRequest dto) {
        return response.success(searchService.searchBrandList(dto));
    }

    @GetMapping("/search/tag")
    public ResponseEntity<?> searchTag(SearchRequest dto) {
        searchService.searchContentList(dto);
        return response.success();
    }
}
