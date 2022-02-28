package com.nelly.application.controller;

import com.nelly.application.dto.request.AddCurrentItemRequest;
import com.nelly.application.service.brand.BrandService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
public class BrandController {

    private final BrandService brandService;

    @PostMapping("/brands/add-item")
    public void addCurrentItem(@RequestBody AddCurrentItemRequest dto) {
        brandService.addCurrentItem(dto);
    }
}
