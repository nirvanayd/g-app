package com.nelly.application.controller;

import com.nelly.application.domain.Contents;
import com.nelly.application.dto.Response;
import com.nelly.application.dto.request.AddContentRequest;
import com.nelly.application.dto.request.RemoveContentRequest;
import com.nelly.application.dto.request.SaveLikeRequest;
import com.nelly.application.dto.request.SaveMarkRequest;
import com.nelly.application.dto.response.AddContentImageResponse;
import com.nelly.application.service.content.ContentService;
import com.nelly.application.service.user.UserService;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.util.List;


@RestController
@AllArgsConstructor
public class ContentController {

    private final ContentService contentService;
    private final Response response;
    private final ModelMapper modelMapper;

    @PostMapping("/contents")
    public ResponseEntity<?> addContent(@RequestBody AddContentRequest dto) {
        contentService.addContent(dto);
        return response.success();
    }

    @DeleteMapping("/contents")
    public ResponseEntity<?> removeContent(@RequestBody RemoveContentRequest dto) {
        contentService.removeContent(dto);
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
}
