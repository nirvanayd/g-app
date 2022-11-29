package com.nelly.application.service.content;

import com.nelly.application.config.AwsProperties;
import com.nelly.application.domain.*;
import com.nelly.application.dto.BrandTagDto;
import com.nelly.application.dto.UserTagDto;
import com.nelly.application.dto.request.*;
import com.nelly.application.dto.response.*;
import com.nelly.application.enums.DeleteStatus;
import com.nelly.application.enums.RoleType;
import com.nelly.application.enums.UserStatus;
import com.nelly.application.enums.YesOrNoType;
import com.nelly.application.exception.NoContentException;
import com.nelly.application.exception.SystemException;
import com.nelly.application.service.AppDomainService;
import com.nelly.application.service.ContentDomainService;
import com.nelly.application.service.UserDomainService;
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
import java.util.stream.IntStream;

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
    private final UserDomainService userDomainService;
    private final AppDomainService appDomainService;
    private final EntityManager entityManager;
    private static final String DIRECTORY_SEPARATOR = "/";

    private static final int REPORT_COUNT  = 1;
    private static final int REPORT_USER_COUNT  = 2;

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

    public void checkContentStatus(long id, boolean allowOwner) {
        Optional<Contents> selectContent = contentDomainService.selectContent(id);
        if (selectContent.isEmpty()) return;
        Contents content = selectContent.get();
        // writer인 경우 허용
        Optional<Users> selectUser = userService.getAppUser();
        if (allowOwner && selectUser.isPresent() && content.getUser().equals(selectUser.get())) return;
        if (content.getIsDisplay() == 0) throw new SystemException("비공개 처리된 컨텐츠입니다.");
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
        Optional<Users> selectUser = userService.getAppUser();
        entityManager.clear();
        Optional<Contents> selectContent = contentDomainService.selectContent(id);
        if (selectContent.isEmpty()) throw new SystemException("게시글이 정상적으로 등록되지 않았습니다.");
        Contents content = selectContent.get();
        ContentResponse response = new ContentResponse();
        response = response.toDto(content);

        if (content.getIsDisplay() == 0) {
            if (selectUser.isPresent() && Objects.equals(selectUser.get().getId(), content.getUser().getId())) {
                response.setIsDisplay(content.getIsDisplay());
            } else {
                throw new SystemException("비공개 처리된 게시물입니다.");
            }
        }

        if (selectUser.isPresent()) {
            Users user = selectUser.get();
            boolean liked = contentDomainService.selectContentLike(content.getId(), user.getId()).isPresent();
            boolean marked = contentDomainService.selectContentMark(content.getId(), user.getId()).isPresent();
            response.setMarked(marked);
            response.setLiked(liked);
        }
        return response;
    }

    @Transactional
    public AddContentImageResponse saveImages(List<MultipartFile> images) throws IOException {
        ArrayList<String> imageUrlList = new ArrayList<>();
        // get user id
        Users user = userService.getUser();
        for (MultipartFile file: images) {
            String fileFormatName = Objects.requireNonNull(file.getContentType()).substring(file.getContentType().lastIndexOf("/") + 1);
            if (file.getOriginalFilename() == null || file.getOriginalFilename().isEmpty()) continue;
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

        user.ifPresent(users -> IntStream.range(0, likeList.size()).forEach(idx -> {
            Users contentUser = likeList.get(idx).getUser();
            Optional<UserFollow> selectFollow = userDomainService.selectUserFollow(users, contentUser);
            if (selectFollow.isPresent()) {
                responseDtoList.get(idx).setFollow(true);
            }
        }));
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

        user.ifPresent(users -> IntStream.range(0, markList.size()).forEach(idx -> {
            Users contentUser = markList.get(idx).getUser();
            Optional<UserFollow> selectFollow = userDomainService.selectUserFollow(users, contentUser);
            if (selectFollow.isPresent()) {
                responseDtoList.get(idx).setFollow(true);
            }
        }));

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
            contentDomainService.updateContentLike(contentId, value);
            cacheTemplate.deleteCache(key);
        }
    }

    public void scheduleContentMarks() {
        Set<String> keys = cacheTemplate.getKeys("mark");
        for (String key : keys) {
            int value = Integer.parseInt(cacheTemplate.getValue(key));
            Long contentId = Long.parseLong(cacheTemplate.parseCashNameKey(key).get("key"));
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
        cacheTemplate.decrValue(String.valueOf(content.getId()), "reply");
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

    public List<ChildCommentResponse> getChildCommentList(Long parentId, GetCommentListRequest dto) {
        Optional<Comments> selectComment = contentDomainService.selectComment(parentId);
        if (selectComment.isEmpty()) throw new SystemException("댓글 정보를 조회할 수 없습니다.");
        Comments parent = selectComment.get();
        Page<Comments> childCommentList =
                contentDomainService.selectChildCommentList(parent, dto.getPage(), dto.getSize());

        List<Comments> commentList = childCommentList.getContent();
        ChildCommentResponse childCommentResponse = new ChildCommentResponse();

        if (dto.getPage() == 0) {
            commentList = commentList.subList(2, commentList.size());
        }
        return childCommentResponse.toDtoList(commentList);
    }

    @Transactional
    public void scheduleContentReply() {
        Set<String> keys = cacheTemplate.getKeys("reply");
        for (String key : keys) {
            int value = Integer.parseInt(cacheTemplate.getValue(key));
            Long contentId = Long.parseLong(cacheTemplate.parseCashNameKey(key).get("key"));
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

    @Transactional
    public void addReport(AddContentReportRequest dto) {
        Users user = userService.getUser();
        // content 상태 확인
        Optional<Contents> selectContent = contentDomainService.selectContent(dto.getContentId());
        if (selectContent.isEmpty()) throw new SystemException("컨텐츠를 조회할 수 없습니다.");
        Contents content = selectContent.get();

        // 이미 신고한 컨텐츠인지 확인
        Optional<ReportContents> selectReportContent = contentDomainService.selectReportContents(content, user);
        if (selectReportContent.isPresent()) throw new SystemException("이미 신고된 컨텐츠입니다.");

        Optional<ReportItems> selectReportItem = appDomainService.selectReportItem(dto.getReportId());
        if (selectReportItem.isEmpty()) throw new SystemException("신고 유형이 올바르지 않습니다.");
        // 신고처리
        ReportContents reportContents = ReportContents.builder()
                .reportItem(selectReportItem.get())
                .content(content)
                .user(user).build();
        contentDomainService.saveContentReport(reportContents);

        if (content.getReportCount() + 1 >= REPORT_COUNT) {
            // 컨텐츠 블락 처리
            content.setIsDisplay(0);
            contentDomainService.saveContent(content);

            // 블락 컨텐츠가 3개인 경우 계정 정지 처리
            Users writer = content.getUser();
            long count = contentDomainService.countBlockContentCount(writer);
            if (count >= REPORT_USER_COUNT) {
                writer.setStatus(UserStatus.BLOCK);
                userDomainService.saveUser(writer);
                // 차단된 계정 로그아웃처리함.
                userService.removeUserToken(writer.getAuthId());
            }
        }
        content.setReportCount(content.getReportCount() + 1);
        contentDomainService.saveContent(content);
    }

    public void resetBlock() {
        List<Contents> blockContentList = contentDomainService.selectBlockContentList();
        blockContentList.forEach(l -> {
            l.setIsDisplay(1);
            contentDomainService.saveContent(l);
        });
    }
}