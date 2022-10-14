package com.nelly.application.dto.response;

import com.nelly.application.domain.ContentImages;
import com.nelly.application.domain.ContentMarks;
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
public class MarkContentThumbResponse {
    private Long id;
    private String photo;
    private String markName;

    public List<MarkContentThumbResponse> toDtoList(List<Contents> list) {
        return list.stream().map(this::toDto).collect(Collectors.toList());
    }

    public List<MarkContentThumbResponse> toDtoMarkList(List<ContentMarks> list) {
        return list.stream().map(this::toDtoByMark).collect(Collectors.toList());
    }

    public MarkContentThumbResponse toDto(Contents c) {
        ContentImages contentImage = c.getContentImages().stream().findFirst().orElse(null);
        return MarkContentThumbResponse.builder().
                id(c.getId()).
                photo(contentImage == null ? null : contentImage.getContentImageUrl()).
                build();
    }

    public MarkContentThumbResponse toDtoByMark(ContentMarks m) {
        Contents c = m.getContent();
        ContentImages contentImage = c.getContentImages().stream().findFirst().orElse(null);
        return MarkContentThumbResponse.builder().
                id(c.getId()).
                photo(contentImage == null ? null : contentImage.getContentImageUrl()).
                markName(c.getUser().getLoginId()).
                build();
    }
}
