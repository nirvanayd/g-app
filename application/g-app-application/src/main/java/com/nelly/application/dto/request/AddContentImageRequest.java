package com.nelly.application.dto.request;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.util.List;

@Data
public class AddContentImageRequest {
    @NotEmpty(message = "텍스트를 입력해 주세요.")
    private String contentText;

    private List<String> contentImageUrls;

    private List<String> contentHashTags;

}
