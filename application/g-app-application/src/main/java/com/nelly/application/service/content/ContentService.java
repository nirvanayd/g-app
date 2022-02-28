package com.nelly.application.service.content;

import com.nelly.application.domain.*;
import com.nelly.application.dto.BrandTagDto;
import com.nelly.application.dto.UserTagDto;
import com.nelly.application.dto.request.AddContentRequest;
import com.nelly.application.dto.request.RemoveContentRequest;
import com.nelly.application.dto.request.SaveLikeRequest;
import com.nelly.application.dto.request.SaveMarkRequest;
import com.nelly.application.dto.response.AddContentImageResponse;
import com.nelly.application.enums.YesOrNoType;
import com.nelly.application.service.ContentDomainService;
import com.nelly.application.service.UserDomainService;
import com.nelly.application.service.brand.BrandService;
import com.nelly.application.service.user.UserService;
import com.nelly.application.util.CacheTemplate;
import com.nelly.application.util.S3Uploader;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;

@RequiredArgsConstructor
@Service
@Slf4j
public class ContentService {

    @Value("${cloud.aws.s3.content-dir}")
    public String contentDirectory;

    @Value("${cloud.aws.s3.cloudfront-url}")
    public String cloudFrontUrl;

    private final CacheTemplate cacheTemplate;
    private final S3Uploader s3Uploader;
    private final UserService userService;
    private final BrandService brandService;

    private final ContentDomainService contentDomainService;
    private final UserDomainService userDomainService;

    private static final String DIRECTORY_SEPARATOR = "/";

    @Transactional
    public void addContent(AddContentRequest dto) {
        Users user = userService.getUser();

        Contents content = contentDomainService.createContent(user, dto.getContentText());

        int imageSequence = 0;
        for (String imageUrl : dto.getImageUrlList()) {

            contentDomainService.createContentImage(content, imageUrl, imageSequence);
            imageSequence++;
        }

        for (UserTagDto userTag : dto.getUserHashTags()) {
            Users tagUser = userService.getUser(userTag.getId());
            contentDomainService.createUserTag(content, tagUser, userTag.getTag());
        }

        for (BrandTagDto brandTag : dto.getBrandHashTags()) {
            Brands tagBrand = null;
            if (brandTag.getId() != null) {
                tagBrand = brandService.getBrand(brandTag.getId());
            }
            contentDomainService.createBrandTag(content, tagBrand, brandTag.getTag());
        }
    }

    public void removeContent(RemoveContentRequest dto) {
        Users user = userService.getUser();
        Contents content = contentDomainService.selectContent(user, dto.getId())
                .orElseThrow(() -> new RuntimeException("컨텐츠 정보를 조회할 수 없습니다."));

        if (content.getDeletedYn().equals(YesOrNoType.YES)) {
            throw new RuntimeException("이미 삭제된 컨텐츠입니다.");
        }
        contentDomainService.updateDeleted(content, YesOrNoType.YES);
    }

    @Transactional
    public AddContentImageResponse saveImages(List<MultipartFile> images) throws IOException {
        ArrayList<String> imageUrlList = new ArrayList<>();
        // get user id
        Users user = userService.getUser();
        for (MultipartFile file: images) {
            imageUrlList.add(s3Uploader.upload(file, getS3ContentPath() + DIRECTORY_SEPARATOR + user.getId()));
        }
        return AddContentImageResponse.builder()
                .imageUrlList(imageUrlList)
                .build();
    }

    public void saveContentLike(SaveLikeRequest dto) {
        // get user id
        Users user = userService.getUser();

        Contents contents = contentDomainService.selectContent(dto.getContentId())
                .orElseThrow(() -> new RuntimeException("컨텐츠를 조회할 수 없습니다."));

        if (contents.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("작성자가 본인입니다.");
        }

        if (YesOrNoType.YES.getCode().equals(dto.getLikeYn())) {
            contentDomainService.createContentLike(dto.getContentId(), user.getId());
            cacheTemplate.incrValue(String.valueOf(dto.getContentId()), "like");
        } else if (YesOrNoType.NO.getCode().equals(dto.getLikeYn())) {
            ContentLikes contentLike = contentDomainService.selectContentLike(dto.getContentId(), user.getId()).orElse(null);
            if (contentLike != null) {
                contentDomainService.deleteContentLike(contentLike.getId());
                cacheTemplate.decrValue(String.valueOf(dto.getContentId()), "like");
            }
        }
    }

    public void saveContentMark(SaveMarkRequest dto) {
        // get user id
        Users user = userService.getUser();

        Contents contents = contentDomainService.selectContent(dto.getContentId())
                .orElseThrow(() -> new RuntimeException("컨텐츠를 조회할 수 없습니다."));

        if (contents.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("작성자가 본인입니다.");
        }

        if (YesOrNoType.YES.getCode().equals(dto.getMarkYn())) {
            contentDomainService.createContentMark(dto.getContentId(), user.getId());
            cacheTemplate.incrValue(String.valueOf(dto.getContentId()), "mark");
        } else if (YesOrNoType.NO.getCode().equals(dto.getMarkYn())) {
            ContentMarks contentMark = contentDomainService.selectContentMark(dto.getContentId(), user.getId()).orElse(null);
            if (contentMark != null) {
                contentDomainService.deleteContentMark(contentMark.getId());
                cacheTemplate.decrValue(String.valueOf(dto.getContentId()), "mark");
            }
        }
    }

    public String getS3ContentPath() {
        return contentDirectory;
    }

    @Transactional
    public void scheduleContentLikes() {
        Set<String> keys = cacheTemplate.getKeys("like");
        for (String key : keys) {
            int value = Integer.parseInt(cacheTemplate.getValue(key));
            Long contentId = Long.parseLong(cacheTemplate.parseCashNameKey(key).get("key"));
            System.out.println("content ID : " + contentId + " , value : " + value);
            contentDomainService.updateContentLike(contentId, value);
            cacheTemplate.deleteCache(key);
        }
    }

    public void scheduleContentMarks() {
        Set<String> keys = cacheTemplate.getKeys("mark");
        for (String key : keys) {
            int value = Integer.parseInt(cacheTemplate.getValue(key));
            Long contentId = Long.parseLong(cacheTemplate.parseCashNameKey(key).get("key"));
            System.out.println("content ID : " + contentId + " , value : " + value);
            contentDomainService.updateContentMark(contentId, value);
            cacheTemplate.deleteCache(key);
        }
    }
}
