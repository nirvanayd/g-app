package com.nelly.application.controller;

import com.nelly.application.domain.Users;
import com.nelly.application.dto.Response;
import com.nelly.application.dto.request.*;
import com.nelly.application.dto.response.*;
import com.nelly.application.exception.SystemException;
import com.nelly.application.service.brand.BrandService;
import com.nelly.application.service.user.UserService;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

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
    public ResponseEntity<?> getAppUserBrandList(SearchBrandRequest searchBrandRequest) {
        return response.success();
    }

    @GetMapping("/brands/intro/{id}")
    public ResponseEntity<?> getBrandDetail(@PathVariable Long id) {
        Users user = userService.getAppUser().orElse(null);
        BrandIntroResponse introDto = brandService.getBrandIntro(id, user);
        return response.success(introDto);
    }

    @GetMapping("/brands/favorite")
    public ResponseEntity<?> getUserBrands(GetUserBrandsRequest getUserBrandsRequest) {
        Optional<Users> user = userService.getAppUser();
        GetUserBrandsResponse getUserBrandsResponse = brandService.getUserBrandList(user.isEmpty() ? null : user.get().getId(), getUserBrandsRequest);
        return response.success(getUserBrandsResponse);
    }

    @PostMapping("/brands/favorite")
    public ResponseEntity<?> saveUserBrand(@RequestBody SaveUserBrandsRequest saveUserBrandsRequest) {
        Users user = userService.getAppUser().orElseThrow(() -> new SystemException("사용자 정보를 조회할 수 없습니다."));
        brandService.saveUserBrands(user.getId(), saveUserBrandsRequest);
        return response.success();
    }

    @GetMapping("/brands/keyword")
    public ResponseEntity<?> getBrandSearchKeyword() {
        // 임시 처리함.
        // 로직에 따라 문자열 배열 생성필요.
        List<String> keywordList = brandService.getBrandSearchKeyword();
        return response.success(keywordList);
    }

    @PostMapping("/brands/search")
    public ResponseEntity<?> searchBrand(SearchBrandRequest searchRequest) {
        brandService.brandBrandList(searchRequest);
        return response.success();
    }


}
