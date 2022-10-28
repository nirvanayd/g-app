package com.nelly.application.dto.response;

import com.nelly.application.domain.ContentMarks;
import com.nelly.application.domain.ScrapItemImages;
import com.nelly.application.domain.UserScrapCart;
import com.nelly.application.domain.UserScrapHistory;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ScrapItemResponse {
    private String name;
    private int price;
    private String url;
    private String brandName;
    private String photoUrl;

    public ScrapItemResponse toDto(UserScrapHistory userScrapHistory) {
        List<ScrapItemImages> imageList = userScrapHistory.getScrapItem().getScrapItemImages();
        return ScrapItemResponse.builder()
            .name(userScrapHistory.getScrapItem().getName())
                .price(userScrapHistory.getScrapItem().getPrice())
                .url(userScrapHistory.getScrapItem().getUrl())
                .brandName(userScrapHistory.getScrapItem().getBrandName())
                .photoUrl(imageList.size() > 0 ? imageList.get(0).getOriginImageUrl() : null)
                .build();
    }

    public ScrapItemResponse cartToDto(UserScrapCart userScrapCart) {
        List<ScrapItemImages> imageList = userScrapCart.getScrapItem().getScrapItemImages();
        return ScrapItemResponse.builder()
                .name(userScrapCart.getScrapItem().getName())
                .price(userScrapCart.getScrapItem().getPrice())
                .url(userScrapCart.getScrapItem().getUrl())
                .brandName(userScrapCart.getScrapItem().getBrandName())
                .photoUrl(imageList.size() > 0 ? imageList.get(0).getOriginImageUrl() : null)
                .build();
    }

    public List<ScrapItemResponse> toDtoList(List<UserScrapHistory> userScrapHistoryList) {
        return userScrapHistoryList.stream().map(this::toDto).collect(Collectors.toList());
    }

    public List<ScrapItemResponse> cartToDtoList(List<UserScrapCart> userScrapCartList) {
        return userScrapCartList.stream().map(this::cartToDto).collect(Collectors.toList());
    }
}