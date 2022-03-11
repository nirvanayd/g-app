package com.nelly.application.controller;

import com.nelly.application.domain.Brands;
import com.nelly.application.dto.Response;
import com.nelly.application.dto.request.AddBrandRequest;
import com.nelly.application.dto.request.GetBrandListRequest;
import com.nelly.application.dto.request.GetContentListRequest;
import com.nelly.application.dto.response.BrandListResponse;
import com.nelly.application.dto.response.BrandResponse;
import com.nelly.application.service.content.ContentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@Slf4j
public class ContentController {

    private final Response response;
    private final ModelMapper modelMapper;
    private final ContentService contentService;

    @GetMapping("/contents")
    public ResponseEntity<?> getContentList(@Valid GetContentListRequest requestDto) {

        contentService.getContentList(requestDto);
//        Page<Brands> brandsPage = brandService.getBrandList(requestDto);
//        long totalCount = brandsPage.getTotalElements();
//        long totalPage = brandsPage.getTotalPages();
//        List<Brands> brandList = brandsPage.getContent();
//        List<BrandResponse> list = brandList.stream().
//                map(u -> modelMapper.map(u, BrandResponse.class)).collect(Collectors.toList());
//
//        // response make
//        BrandListResponse brandListResponse = new BrandListResponse();
//        brandListResponse.setTotalCount(totalCount);
//        brandListResponse.setTotalPage(totalPage);
//        brandListResponse.setList(list);
        return response.success();
    }
}
