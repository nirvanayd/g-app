package com.nelly.application.dto.response;

import lombok.Builder;
import lombok.Data;

import java.util.ArrayList;

@Data
@Builder
public class AddContentImageResponse {
    ArrayList<String> imageUrlList;
}
