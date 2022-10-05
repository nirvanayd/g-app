package com.nelly.application.dto.response;

import com.nelly.application.domain.ContentImages;
import com.nelly.application.domain.Contents;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ContentThumbResponse {
    private Long id;
    private String photo;

    public List<ContentThumbResponse> toDtoList(List<Contents> list) {
        return list.stream().map(this::toDto).collect(Collectors.toList());
    }

    public ContentThumbResponse toDto(Contents c) {
        ContentImages contentImage = c.getContentImages().stream().findFirst().orElse(null);
        return ContentThumbResponse.builder().
                id(c.getId()).
                photo(contentImage == null ? null : contentImage.getContentImageUrl()).
                build();
    }


}
