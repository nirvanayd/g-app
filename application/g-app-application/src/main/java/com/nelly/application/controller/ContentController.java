package com.nelly.application.controller;

import com.nelly.application.domain.Comments;
import com.nelly.application.domain.Contents;
import com.nelly.application.dto.Response;
import com.nelly.application.dto.request.*;
import com.nelly.application.dto.response.AddContentImageResponse;
import com.nelly.application.dto.response.CommentResponse;
import com.nelly.application.dto.response.ContentResponse;
import com.nelly.application.dto.response.GetContentListResponse;
import com.nelly.application.service.content.ContentService;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.util.List;


@RestController
@AllArgsConstructor
public class ContentController {

    private final ContentService contentService;
    private final Response response;
    private final ModelMapper modelMapper;

    @GetMapping("/contents")
    public ResponseEntity<?> getContentList(GetContentListRequest dto) {
        List<ContentResponse> list = contentService.getContentList(dto);
        return response.success(list);
    }

    @PostMapping("/contents")
    public ResponseEntity<?> addContent(@RequestBody AddContentRequest dto) {
        contentService.addContent(dto);
        return response.success();
    }

    @PutMapping("/contents/{id}")
    public ResponseEntity<?> updateContent(@NotBlank @PathVariable("id") String id,
                                           @RequestBody UpdateContentRequest dto) {
        Long contentId = Long.parseLong(id);
        contentService.updateContent(contentId, dto);
        return response.success();
    }

    @DeleteMapping("/contents/{id}")
    public ResponseEntity<?> removeContent(@NotBlank @PathVariable("id") String id) {
        // contentId Long convert
        Long contentId = Long.parseLong(id);
        contentService.removeContent(contentId);
        return response.success();
    }

    @PostMapping("/contents/save-images")
    public ResponseEntity<?> saveContentImages(@NotNull @RequestParam("images") List<MultipartFile> images) throws IOException {
        AddContentImageResponse addContentImageResponse = contentService.saveImages(images);
        return response.success(addContentImageResponse);
    }

    @PostMapping("/contents/like")
    public ResponseEntity<?> saveContentLike(@RequestBody SaveLikeRequest dto) {
        contentService.saveContentLike(dto);
        return response.success();
    }

    @PostMapping("/contents/mark")
    public ResponseEntity<?> saveContentMark(@RequestBody SaveMarkRequest dto) {
        contentService.saveContentMark(dto);
        return response.success();
    }

    @PostMapping("/comments")
    public ResponseEntity<?> addComment(@RequestBody AddCommentRequest dto) {
        contentService.addComment(dto);
        return response.success();
    }

    @PutMapping("/comments/{id}")
    public ResponseEntity<?> updateComment(@NotBlank @PathVariable("id") String id,
                                           @RequestBody UpdateCommentRequest dto) {
        Long commentId = Long.parseLong(id);
        contentService.updateComment(commentId, dto);
        return response.success();
    }

    @GetMapping("/comments/{contentId}")
    public ResponseEntity<?> getContentCommentList(@NotBlank @PathVariable("contentId") String id,
                                           GetCommentListRequest dto
                                           ) {
        Long contentId = Long.parseLong(id);
        List<CommentResponse> commentList = contentService.getCommentList(contentId, dto);
        return response.success(commentList);
    }
}
