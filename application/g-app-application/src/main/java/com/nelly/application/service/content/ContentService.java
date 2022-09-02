package com.nelly.application.service.content;

import com.nelly.application.config.AwsProperties;
import com.nelly.application.domain.*;
import com.nelly.application.dto.BrandTagDto;
import com.nelly.application.dto.UserTagDto;
import com.nelly.application.dto.request.*;
import com.nelly.application.dto.response.AddContentImageResponse;
import com.nelly.application.dto.response.CommentResponse;
import com.nelly.application.dto.response.ContentResponse;
import com.nelly.application.enums.YesOrNoType;
import com.nelly.application.exception.SystemException;
import com.nelly.application.service.ContentDomainService;
import com.nelly.application.service.brand.BrandService;
import com.nelly.application.service.user.UserService;
import com.nelly.application.util.CacheTemplate;
import com.nelly.application.util.S3Uploader;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RequiredArgsConstructor
@Service
@Slf4j
public class ContentService {
    private final AwsProperties awsProperties;
    private final CacheTemplate cacheTemplate;
    private final S3Uploader s3Uploader;
    private final UserService userService;
    private final BrandService brandService;
    private final ContentDomainService contentDomainService;
    private static final String DIRECTORY_SEPARATOR = "/";

    @Transactional
    public void addContent(AddContentRequest dto) {
        Users user = userService.getUser();

        Contents content = contentDomainService.createContent(user, dto.getText());
        int imageSequence = 0;
        for (AddImageRequest imageRequest : dto.getPhotoList()) {
            ContentImages contentImage = contentDomainService.createContentImage(content, imageRequest.getPhotoURL(), imageSequence);
            imageSequence++;

            for (AddTagRequest brandTag : imageRequest.getBrandHashTags()) {
                Brands tagBrand = null;
                if (brandTag.getId() != null) {
                    tagBrand = brandService.getBrand(brandTag.getId());
                }
                contentDomainService.createBrandTag(content, contentImage, tagBrand,
                        brandTag.getX(), brandTag.getY(), brandTag.getTag());
            }
        }

        // user mention
        Pattern userTagPattern = Pattern.compile("@(\\S+)");
        Matcher userTagMatcher = userTagPattern.matcher(dto.getText());

        // hash tag
        Pattern hashTagPattern = Pattern.compile("#(\\S+)");
        Matcher hashTagMatcher = hashTagPattern.matcher(dto.getText());
        List<String> tagList = new ArrayList<>();
        while(hashTagMatcher.find()) {
            // matcher 를 해시태그로
            String[] tags = hashTagMatcher.group().split("#");
            for (String s : tags) {
                String str = s.replaceAll("\\s+", "");
                if (!str.isEmpty()) {
                    tagList.add(str);
                }
            }
        }
        for (String s : tagList) {
            AppTags appTag = contentDomainService.selectAppTag(s).orElse(null);
            if (appTag == null) {
                appTag = contentDomainService.createAppTag(s);
            }
            contentDomainService.createContentHashTag(content, appTag, s);
        }
    }

    /**
     * content text 태그 파싱
     */
    private void parseTag(String contentText) {

    }

    @Transactional
    public void updateContent(Long contentId, UpdateContentRequest dto) {
        Users user = userService.getUser();
        Contents content = contentDomainService.selectContent(user, contentId)
                .orElseThrow(() -> new RuntimeException("컨텐츠 정보를 조회할 수 없습니다."));

        // 앱태그 초기화
        contentDomainService.deleteContentHashTag(content);

        // 브랜드태그 초기화
        contentDomainService.deleteBrandTag(content);

        // 이미지 초기화
        contentDomainService.deleteContentImage(content);

        content.setContentText(dto.getText());
        contentDomainService.saveContent(content);

        int imageSequence = 0;
        for (AddImageRequest imageRequest : dto.getPhotoList()) {
            ContentImages contentImage = contentDomainService.createContentImage(content, imageRequest.getPhotoURL(), imageSequence);
            imageSequence++;

            for (AddTagRequest brandTag : imageRequest.getBrandHashTags()) {
                Brands tagBrand = null;
                if (brandTag.getId() != null) {
                    tagBrand = brandService.getBrand(brandTag.getId());
                }
                contentDomainService.createBrandTag(content, contentImage, tagBrand,
                        brandTag.getX(), brandTag.getY(), brandTag.getTag());
            }
        }

        if (dto.getText() == null) return;
        // user mention
        Pattern userTagPattern = Pattern.compile("@(\\S+)");
        Matcher userTagMatcher = userTagPattern.matcher(dto.getText());

        // hash tag
        Pattern hashTagPattern = Pattern.compile("#(\\S+)");
        Matcher hashTagMatcher = hashTagPattern.matcher(dto.getText());
        List<String> tagList = new ArrayList<>();
        while(hashTagMatcher.find()) {
            // matcher 를 해시태그로
            String[] tags = hashTagMatcher.group().split("#");
            for (String s : tags) {
                String str = s.replaceAll("\\s+", "");
                if (!str.isEmpty()) {
                    tagList.add(str);
                }
            }
        }
        for (String s : tagList) {
            AppTags appTag = contentDomainService.selectAppTag(s).orElse(null);
            if (appTag == null) {
                appTag = contentDomainService.createAppTag(s);
            }
            contentDomainService.createContentHashTag(content, appTag, s);
        }
    }

    public void removeContent(Long contentId) {
        Users user = userService.getUser();
        Contents content = contentDomainService.selectContent(user, contentId)
                .orElseThrow(() -> new RuntimeException("컨텐츠 정보를 조회할 수 없습니다."));
        if (content.getDeletedDate() != null) {
            throw new RuntimeException("이미 삭제된 컨텐츠입니다.");
        }
        contentDomainService.removeContent(contentId);
    }

    @Transactional
    public AddContentImageResponse saveImages(List<MultipartFile> images) throws IOException {
        ArrayList<String> imageUrlList = new ArrayList<>();
        // get user id
        Users user = userService.getUser();
        for (MultipartFile file: images) {
            String a = awsProperties.getS3().getBucket();

            imageUrlList.add(awsProperties.getCloudFront().getUrl() +
                    s3Uploader.upload(
                            awsProperties.getS3().getBucket(),
                            file,
                            getS3ContentPath() + DIRECTORY_SEPARATOR + user.getId()));
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
        return awsProperties.getCloudFront().getContentDir();
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

    public List<ContentResponse> getContentList(GetContentListRequest dto) {
        Page<Contents> contentList = contentDomainService.selectContentList(dto.getPage(), dto.getSize());
        Optional<Users> users = userService.getAppUser();
        if (!users.isEmpty()) {
            // 좋아요, 마크 여부
        }

        ContentResponse response = new ContentResponse();
        return response.toDtoList(contentList.getContent());
    }

    public void addComment(AddCommentRequest dto) {
        Optional<Users> selectUsers = userService.getAppUser();
        if (selectUsers.isEmpty()) throw new SystemException("사용자 정보를 조회할 수 없습니다.");
        Users user = selectUsers.get();

        Optional<Contents> selectContent = contentDomainService.selectContent(dto.getContentId());
        if (selectContent.isEmpty()) throw new SystemException("컨텐츠 정보를 조회할 수 없습니다.");
        Contents content = selectContent.get();

        // parent check
        Comments parentComment = null;
        if (dto.getParentId() != null) {
            Optional<Comments> selectComment = contentDomainService.selectComment(dto.getParentId());
            if (selectComment.isEmpty()) throw new SystemException("댓글 정보를 조회할 수 없습니다.");
            parentComment = selectComment.get();
        }

        contentDomainService.createComment(content, user, parentComment, dto.getComment());
    }

    public void updateComment(Long id, UpdateCommentRequest dto) {
        Users user = userService.getUser();

        Optional<Comments> selectExistComment = contentDomainService.selectComment(id, user);
        if (selectExistComment.isEmpty()) throw new SystemException("댓글 정보를 조회할 수 없습니다.");
        Comments existComment = selectExistComment.get();
        contentDomainService.saveComment(existComment, dto.getComment());
    }

    public List<CommentResponse> getCommentList(Long contentId, GetCommentListRequest dto) {
        Optional<Contents> selectContent = contentDomainService.selectContent(contentId);
        if (selectContent.isEmpty()) throw new SystemException("컨텐츠 정보를 조회할 수 없습니다.");
        Contents content = selectContent.get();
        Page<Comments> selectComments = contentDomainService.selectCommentList(content, dto.getPage(), dto.getSize());

        List<CommentResponse> list = new ArrayList<>();
        if (selectComments.isEmpty()) return list;
        List<Comments> commentList = selectComments.getContent();
        CommentResponse commentResponse = new CommentResponse();
        return commentResponse.toDtoList(commentList);
    }
}
