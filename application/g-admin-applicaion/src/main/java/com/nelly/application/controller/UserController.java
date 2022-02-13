package com.nelly.application.controller;

import com.nelly.application.domain.Users;
import com.nelly.application.dto.Response;
import com.nelly.application.dto.request.GetAccountListRequest;
import com.nelly.application.dto.response.*;
import com.nelly.application.service.user.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@Slf4j
public class UserController {

    private final Response response;
    private final ModelMapper modelMapper;
    private final UserService userService;

    @GetMapping("/users")
    public ResponseEntity<?> getUserList(GetAccountListRequest requestDto) {
        Page<Users> usersPage = userService.getAccountList(requestDto);
        long totalCount = usersPage.getTotalElements();
        long totalPage = usersPage.getTotalPages();
        List<Users> accountList = usersPage.getContent();
        List<UserResponse> list = accountList.stream().
                map(u -> modelMapper.map(u, UserResponse.class)).collect(Collectors.toList());

        // response make
        UserListResponse userListResponse = new UserListResponse();
        userListResponse.setTotalCount(totalCount);
        userListResponse.setTotalPage(totalPage);
        userListResponse.setList(list);
        return response.success(userListResponse);
    }

    @GetMapping("/users/{userId}")
    public ResponseEntity<?> getUser(@PathVariable(required = false) Integer userId) {
        if (userId == null) throw new RuntimeException("사용자 정보를 조회할 수 없습니다.");
        Users user = userService.getAccountDetail(userId);
        UserResponse userResponse = modelMapper.map(user, UserResponse.class);
        return response.success(userResponse);
    }
}
