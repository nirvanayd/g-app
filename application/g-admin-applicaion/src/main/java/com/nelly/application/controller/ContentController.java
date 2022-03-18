package com.nelly.application.controller;

import com.nelly.application.domain.Contents;
import com.nelly.application.dto.Response;
import com.nelly.application.dto.request.AddBrandRequest;
import com.nelly.application.dto.request.AddContentRequest;
import com.nelly.application.dto.request.GetContentListRequest;
import com.nelly.application.dto.response.ContentDetailResponse;
import com.nelly.application.dto.response.ContentListResponse;
import com.nelly.application.dto.response.ContentResponse;
import com.nelly.application.service.content.ContentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
        Page<Contents> contentsPage = contentService.getContentList(requestDto);
        long totalCount = contentsPage.getTotalElements();
        long totalPage = contentsPage.getTotalPages();
        List<Contents> contentList = contentsPage.getContent();

        for (Contents content : contentList) {
            System.out.println(content.getUser().getLoginId());
        }

        List<ContentResponse> list = contentList.stream().
                map(u -> modelMapper.map(u, ContentResponse.class)).collect(Collectors.toList());

        // response make
        ContentListResponse contentListResponse = new ContentListResponse();
        contentListResponse.setTotalCount(totalCount);
        contentListResponse.setTotalPage(totalPage);
        contentListResponse.setList(list);
        return response.success(contentListResponse);
    }

    @GetMapping("/contents/{contentId}")
    public ResponseEntity<?> getContent(@PathVariable long contentId) {
        Contents content = contentService.getContent(contentId);

        ContentDetailResponse contentDetailResponse = new ContentDetailResponse();
        modelMapper.map(content, contentDetailResponse);

        return response.success(contentDetailResponse);
    }

    @PutMapping("/contents/{contentId}")
    public ResponseEntity<?> updateContent(@PathVariable long contentId,
                                         @RequestBody @Valid AddContentRequest requestDto) {


        return response.success("test");
    }
}
