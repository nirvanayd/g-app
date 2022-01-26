package com.nelly.application.controller;

import com.nelly.application.domain.Users;
import com.nelly.application.dto.Response;
import com.nelly.application.dto.request.UserListRequest;
import com.nelly.application.dto.response.UserListResponse;
import com.nelly.application.dto.response.UserResponse;
import com.nelly.application.dto.response.UserTestResponse;
import com.nelly.application.enums.Authority;
import com.nelly.application.service.AppUserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/admin/")
@RequiredArgsConstructor
@Slf4j
public class UserController {

    private final Response response;
    private final ModelMapper modelMapper;
    private final AppUserService userService;

    @GetMapping("/users")
    public ResponseEntity<?> getUserList(@RequestParam Integer page) {
        log.info("request >> " + page);

        Page<Users> userPage = userService.getUserList(page, Authority.ROLE_USER);
        List<Users> userList = userPage.getContent();
        Integer total = userPage.getTotalPages();

        List<UserResponse> list = userList.stream().
                map(u -> modelMapper.map(u, UserResponse.class)).collect(Collectors.toList());
        UserListResponse userListResponse = new UserListResponse();
        userListResponse.setList(list);
        return response.success(userListResponse);
    }

    @GetMapping("/users/{userId}")
    public ResponseEntity<?> getUser(@PathVariable(required = false) Integer userId) {
        if (userId == null) throw new RuntimeException("사용자 정보를 조회할 수 없습니다.");
        Users user = userService.getUserDetail(userId);
        UserResponse userResponse = modelMapper.map(user, UserResponse.class);
        return response.success(userResponse);
    }

}
