package com.nelly.application.dto.response;

import com.nelly.application.domain.Contents;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

import javax.persistence.Column;
import java.util.List;
import java.util.stream.Collectors;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ContentResponse {
    private Long id;
    private String text;
    private Integer likeCount;
    private Integer markCount;
    private Integer replyCount;
    private List<ContentImageResponse> photoList;
    private ContentMemberResponse member;
    private String createdAt;
    private boolean liked;
    private boolean marked;

    public List<ContentResponse> toDtoList(List<Contents> contentList) {
        return contentList.stream().map(this::toDto).collect(Collectors.toList());
    }

    public ContentResponse toDto(Contents c) {
        ContentMemberResponse memberResponse = new ContentMemberResponse();
        return ContentResponse.builder().
                id(c.getId()).
                text(c.getContentText()).
                likeCount(c.getLikeCount()).
                markCount(c.getMarkCount()).
                replyCount(c.getReplyCount()).
                createdAt(c.getCreatedDate().toString()).
                photoList(c.getContentImages().stream().map(i -> ContentImageResponse.builder().
                        photoURL(i.getContentImageUrl()).
                        tagList(i.getTagsList().stream().map(t -> TagResponse.builder().
                                tag(t.getTag()).
                                x(t.getX()).y(t.getY()).
                                brandId(t.getBrand() == null ? null : t.getBrand().getId()).
                                build()).collect(Collectors.toList())).
                        build()).collect(Collectors.toList())).
                member(memberResponse.contentUserToResponse(c.getUser())).
                build();
    }
}
