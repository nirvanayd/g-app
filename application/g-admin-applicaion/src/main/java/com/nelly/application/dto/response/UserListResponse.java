package com.nelly.application.dto.response;

import lombok.Data;

import java.util.List;

@Data
public class UserListResponse {
    List<UserResponse> list;
}
