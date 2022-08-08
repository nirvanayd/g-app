package com.nelly.application.controller;

import com.nelly.application.domain.BrandRank;
import com.nelly.application.domain.Brands;
import com.nelly.application.domain.Users;
import com.nelly.application.dto.Response;
import com.nelly.application.dto.request.AddCurrentItemRequest;
import com.nelly.application.dto.request.GetRankRequest;
import com.nelly.application.dto.request.GetUserBrandsRequest;
import com.nelly.application.dto.request.SaveUserBrandsRequest;
import com.nelly.application.dto.response.BrandRankResponse;
import com.nelly.application.dto.response.BrandResponse;
import com.nelly.application.dto.response.GetRankResponse;
import com.nelly.application.exception.SystemException;
import com.nelly.application.service.brand.BrandService;
import com.nelly.application.service.user.UserService;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@AllArgsConstructor
public class BrandController {

    private final BrandService brandService;
    private final Response response;
    private final ModelMapper modelMapper;
    private final UserService userService;


    @PostMapping("/brands/add-item")
    public void addCurrentItem(@RequestBody AddCurrentItemRequest dto) {
        brandService.addCurrentItem(dto);
    }

    @GetMapping("/brands/download-image")
    public void downloadImage() {
        brandService.downloadImage();
    }

    @GetMapping("/brands/rank")
    public ResponseEntity<?> getAppBrandRank(GetRankRequest getRankRequest) {
        GetRankResponse rankResponse = brandService.getBrandRankList(getRankRequest);
        return response.success(rankResponse);
    }

    @GetMapping("/brands")
    public ResponseEntity<?> getAppUserBrandList(GetRankRequest getRankRequest) {
        return response.success();
    }

    @GetMapping("/brands/favorite")
    public ResponseEntity<?> getUserBrands(GetUserBrandsRequest getUserBrandsRequest) {
        Users user = userService.getAppUser().orElseThrow(() -> new SystemException("사용자 정보를 조회할 수 없습니다."));
        GetRankResponse getRankResponse = brandService.getUserBrandList(user.getId(), getUserBrandsRequest);
        return response.success(getRankResponse);
    }

    @PostMapping("/brands/favorite")
    public ResponseEntity<?> saveUserBrand(@RequestBody SaveUserBrandsRequest saveUserBrandsRequest) {
        Users user = userService.getAppUser().orElseThrow(() -> new SystemException("사용자 정보를 조회할 수 없습니다."));
        brandService.saveUserBrands(user.getId(), saveUserBrandsRequest);
        return response.success();
    }

}
