package com.nelly.application.controller;

import com.nelly.application.dto.Response;
import com.nelly.application.service.user.UserService;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;


@RestController
@AllArgsConstructor
@Service
public class ContentController {

    private final UserService userService;
    private final Response response;
    private final ModelMapper modelMapper;

    @PostMapping("/contents/")
    public ResponseEntity<?> createContent() {
        return response.success();
    }
}
