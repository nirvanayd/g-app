package com.nelly.application.service.content;

import com.nelly.application.config.AwsProperties;
import com.nelly.application.domain.*;
import com.nelly.application.dto.BrandTagDto;
import com.nelly.application.dto.UserTagDto;
import com.nelly.application.dto.request.*;
import com.nelly.application.dto.response.*;
import com.nelly.application.enums.DeleteStatus;
import com.nelly.application.enums.RoleType;
import com.nelly.application.enums.YesOrNoType;
import com.nelly.application.exception.NoContentException;
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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityManager;
import javax.swing.text.html.Option;
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
    private final EntityManager entityManager;
    private static final String DIRECTORY_SEPARATOR = "/";

    @Transactional
    public Contents addContent(AddContentRequest dto) {
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
        contentDomainService.saveContent(content);
        return content;
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

    public ContentResponse getContent(Long id) {
        entityManager.clear();
        Optional<Contents> selectContent = contentDomainService.selectContent(id);
        if (selectContent.isEmpty()) throw new SystemException("게시글이 정상적으로 등록되지 않았습니다.");
        Contents content = selectContent.get();

        ContentResponse response = new ContentResponse();
        return response.toDto(content);
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

    public List<GetContentLikeResponse> getContentLike(Long contentId, GetContentLikeRequest dto) {

        Optional<Users> user = userService.getAppUser();
        Page<ContentLikes> selectLikeList =
                contentDomainService.selectContentLikeList(contentId, dto.getPage(), dto.getSize());
        if (selectLikeList.isEmpty()) {
            throw new NoContentException();
        }

        List<ContentLikes> likeList = selectLikeList.getContent();
        GetContentLikeResponse responseDto = new GetContentLikeResponse();

        List<GetContentLikeResponse> responseDtoList = responseDto.toDtoList(likeList);

        if (user.isPresent()) {
//            responseDtoList.stream().forEach(l -> {
//
//            });
        }
        return responseDtoList;
    }

    public List<GetContentMarkResponse> getContentMark(Long contentId, GetContentMarkRequest dto) {

        Optional<Users> user = userService.getAppUser();
        Page<ContentMarks> selectMarkList =
                contentDomainService.selectContentMarkList(contentId, dto.getPage(), dto.getSize());
        if (selectMarkList.isEmpty()) {
            throw new NoContentException();
        }

        List<ContentMarks> markList = selectMarkList.getContent();
        GetContentMarkResponse responseDto = new GetContentMarkResponse();
        List<GetContentMarkResponse> responseDtoList = responseDto.toDtoList(markList);

        if (user.isPresent()) {
//            responseDtoList.stream().forEach(l -> {
//
//            });
        }

        return responseDtoList;
    }

    public void saveContentLike(SaveLikeRequest dto) {
        // get user id
        Users user = userService.getUser();

        Contents contents = contentDomainService.selectContent(dto.getContentId())
                .orElseThrow(() -> new RuntimeException("컨텐츠를 조회할 수 없습니다."));

        if (contents.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("작성자가 본인입니다.");
        }
        ContentLikes contentLike =
                contentDomainService.selectContentLike(dto.getContentId(), user.getId()).orElse(null);

        if (YesOrNoType.YES.getCode().equals(dto.getLikeYn())) {
            if (contentLike == null){
                contentDomainService.createContentLike(contents, user);
                cacheTemplate.incrValue(String.valueOf(dto.getContentId()), "like");
            }
        } else if (YesOrNoType.NO.getCode().equals(dto.getLikeYn())) {
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
        ContentMarks contentMark =
                contentDomainService.selectContentMark(dto.getContentId(), user.getId()).orElse(null);

        if (YesOrNoType.YES.getCode().equals(dto.getMarkYn())) {
            if (contentMark == null) {
                contentDomainService.createContentMark(contents, user);
                cacheTemplate.incrValue(String.valueOf(dto.getContentId()), "mark");
            }
        } else if (YesOrNoType.NO.getCode().equals(dto.getMarkYn())) {
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
        Optional<Users> selectUser = userService.getAppUser();
        if (contentList.isEmpty()) {
            throw new NoContentException();
        }

        ContentResponse response = new ContentResponse();
        List<ContentResponse> list = response.toDtoList(contentList.getContent());

        if (selectUser.isPresent()) {
            Users user = selectUser.get();
            // 좋아요, 마크 여부
            list.forEach(l -> {
                boolean liked = contentDomainService.selectContentLike(l.getId(), user.getId()).isPresent();
                boolean marked = contentDomainService.selectContentMark(l.getId(), user.getId()).isPresent();
                l.setLiked(liked);
                l.setMarked(marked);
            });
        }

        return list;
    }

    public CommentResponse addComment(AddCommentRequest dto) {
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

        Comments savedComment =
                contentDomainService.createComment(content, user, parentComment, dto.getComment());
        cacheTemplate.incrValue(String.valueOf(dto.getContentId()), "reply");

        CommentResponse commentResponse = new CommentResponse();
        return commentResponse.toDto(savedComment);
    }

    public void updateComment(Long id, UpdateCommentRequest dto) {
        Users user = userService.getUser();

        Optional<Comments> selectExistComment = contentDomainService.selectComment(id, user);
        if (selectExistComment.isEmpty()) throw new SystemException("댓글 정보를 조회할 수 없습니다.");
        Comments existComment = selectExistComment.get();
        contentDomainService.saveComment(existComment, dto.getComment());
    }

    public String removeComment(Long id) {
        Users user = userService.getUser();
        Optional<Comments> selectComment = contentDomainService.selectComment(id);
        // 관리자, 원글 작성자, 댓글 작성자 삭제 가능
        if (selectComment.isEmpty()) {
            throw new SystemException("댓글 정보를 조회활 수 없습니다.");
        }
        Comments comment = selectComment.get();
        Contents content = comment.getContent();

        DeleteStatus returnStatus = null;

        if (comment.getUser().equals(user)) {
            contentDomainService.saveCommentDelete(DeleteStatus.WRITER, comment);
            returnStatus = DeleteStatus.WRITER;
        } else if (content.getUser().equals(user)) {
            contentDomainService.saveCommentDelete(DeleteStatus.CONTENT_WRITER, comment);
            returnStatus = DeleteStatus.CONTENT_WRITER;
        } else {
            throw new SystemException("삭제 권한이 없습니다.");
        }
        cacheTemplate.incrValue(String.valueOf(content.getId()), "reply");
        return returnStatus.getCode();
    }

    public List<CommentResponse> getCommentList(Long contentId, GetCommentListRequest dto) {
        Optional<Contents> selectContent = contentDomainService.selectContent(contentId);
        if (selectContent.isEmpty()) throw new SystemException("컨텐츠 정보를 조회할 수 없습니다.");
        Contents content = selectContent.get();
        Page<Comments> selectComments = contentDomainService.selectCommentList(content, dto.getPage(), dto.getSize());

        List<CommentResponse> list = new ArrayList<>();
        if (selectComments.isEmpty()) {
            throw new NoContentException();
        }
        List<Comments> commentList = selectComments.getContent();
        CommentResponse commentResponse = new CommentResponse();
        return commentResponse.toDtoList(commentList);
    }

    public GetChildCommentListResponse getChildCommentList(Long parentId, GetCommentListRequest dto) {
        Optional<Comments> selectComment = contentDomainService.selectComment(parentId);
        if (selectComment.isEmpty()) throw new SystemException("댓글 정보를 조회할 수 없습니다.");
        Comments parent = selectComment.get();
        Page<Comments> childCommentList =
                contentDomainService.selectChildCommentList(parent, dto.getPage(), dto.getSize());

        List<Comments> commentList = childCommentList.getContent();
        long contentSize = childCommentList.getContent().size();
        ChildCommentResponse childCommentResponse = new ChildCommentResponse();
        List<ChildCommentResponse> childCommentResponseList = childCommentResponse.toDtoList(commentList);

        GetChildCommentListResponse getChildCommentListResponse = GetChildCommentListResponse.builder().
                list(childCommentResponseList).totalCount(childCommentList.getTotalElements()).build();
        getChildCommentListResponse.setEnded(contentSize == 0);

        return getChildCommentListResponse;
    }

    @Transactional
    public void scheduleContentReply() {
        Set<String> keys = cacheTemplate.getKeys("reply");
        for (String key : keys) {
            int value = Integer.parseInt(cacheTemplate.getValue(key));
            Long contentId = Long.parseLong(cacheTemplate.parseCashNameKey(key).get("key"));
            System.out.println("content ID : " + contentId + " , value : " + value);
            contentDomainService.updateContentReply(contentId, value);
            cacheTemplate.deleteCache(key);
        }
    }

    public List<GetUserLikeResponse> getUserLikeList(Users user, GetUserLikeRequest dto) {
        Page<ContentLikes> selectLikeList = contentDomainService.selectUserContentLike(user, dto.getPage(), dto.getSize());
        if (selectLikeList.isEmpty()) {
            throw new NoContentException();
        }
        List<ContentLikes> likeList = selectLikeList.getContent();
        GetUserLikeResponse getUserLikeResponse = new GetUserLikeResponse();
        return getUserLikeResponse.toDtoList(likeList);
    }

    public List<GetUserMarkResponse> getUserMarkList(Users user, GetUserLikeRequest dto) {
        Page<ContentMarks> selectMarkList = contentDomainService.selectUserContentMark(user, dto.getPage(), dto.getSize());
        if (selectMarkList.isEmpty()) {
            throw new NoContentException();
        }
        List<ContentMarks> markList = selectMarkList.getContent();
        GetUserMarkResponse getUserMarkResponse = new GetUserMarkResponse();
        return getUserMarkResponse.toDtoList(markList);
    }
}
