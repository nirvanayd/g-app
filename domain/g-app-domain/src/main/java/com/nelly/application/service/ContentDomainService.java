package com.nelly.application.service;

import com.nelly.application.domain.*;
import com.nelly.application.enums.DeleteStatus;
import com.nelly.application.enums.YesOrNoType;
import com.nelly.application.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class ContentDomainService {
    private final ContentsRepository contentsRepository;
    private final ContentImagesRepository contentImagesRepository;
    private final UserHashTagsRepository userHashTagsRepository;
    private final BrandTagsRepository brandTagsRepository;
    private final ContentLikesRepository contentLikesRepository;
    private final ContentMarksRepository contentMarksRepository;
    private final AppTagsRepository appTagsRepository;
    private final ContentHashTagsRepository contentHashTagsRepository;
    private final CommentsRepository commentsRepository;

    public Contents createContent(Users user, String contentText) {
        Contents contents = Contents.builder()
                .contentText(contentText)
                .user(user)
                .likeCount(0)
                .replyCount(0)
                .markCount(0)
                .viewCount(0)
                .build();

        return contentsRepository.save(contents);
    }

    public ContentImages createContentImage(Contents content, String imageUrl, int seq) {
        ContentImages contentImages = ContentImages.builder()
                .contentImageUrl(imageUrl)
                .content(content)
                .seq(seq)
                .build();
        return contentImagesRepository.save(contentImages);
    }

    public void deleteContentImage(Contents contents) {
        contentImagesRepository.deleteAllByContent(contents);
    }

    public UserHashTags createUserTag(Contents content, Users user, String tag) {
        UserHashTags userHashTags = UserHashTags.builder()
                .user(user)
                .content(content)
                .tag(tag)
                .build();
        return userHashTagsRepository.save(userHashTags);
    }

    public BrandTags createBrandTag(Contents content, ContentImages contentImage, Brands brand,
                                    double x, double y, String tag) {
        BrandTags brandTag = BrandTags.builder().
                contentImage(contentImage).
                content(content).brand(brand).
                x(x).y(y).tag(tag).build();
        return brandTagsRepository.save(brandTag);
    }

    public void deleteBrandTag(Contents contents) {
        brandTagsRepository.deleteAllByContent(contents);
    }

    public Optional<Contents> selectContent(Users user, Long id) {
        return contentsRepository.findByUserAndId(user, id);
    }

    public void saveContent(Contents content) {
        contentsRepository.save(content);
    }

    public Optional<Contents> selectContent(Long id) {
        return contentsRepository.findById(id);
    }

    public Optional<Contents> selectContent(Long id, boolean flush) {
        return contentsRepository.findById(id);
    }

    public void removeContent(Long contentId) {
        contentsRepository.deleteById(contentId);
    }

    public void createContentLike(Long contentId, Long UserId) {
        ContentLikes contentLikes = ContentLikes.builder()
                .contentId(contentId)
                .userId(UserId)
                .build();
        contentLikesRepository.save(contentLikes);
    }

    public void deleteContentLike(Long id) {
        contentLikesRepository.deleteById(id);
    }

    public Optional<ContentLikes> selectContentLike(Long contentId, Long userId) {
        return contentLikesRepository.findByContentIdAndAndUserId(contentId, userId);
    }

    public void createContentMark(Long contentId, Long UserId) {
        ContentMarks contentMarks = ContentMarks.builder()
                .contentId(contentId)
                .userId(UserId)
                .build();
        contentMarksRepository.save(contentMarks);
    }

    public void deleteContentMark(Long id) {
        contentMarksRepository.deleteById(id);
    }

    public Optional<ContentMarks> selectContentMark(Long contentId, Long userId) {
        return contentMarksRepository.findByContentIdAndAndUserId(contentId, userId);
    }

    public void updateContentLike(Long contentId, int value) {
        contentsRepository.updateLikeCount(contentId, value);
    }

    public void updateContentMark(Long contentId, int value) {
        contentsRepository.updateMarkCount(contentId, value);
    }

    public AppTags createAppTag(String tag) {
        AppTags appTag = AppTags.builder()
                .tag(tag)
                .build();
        return appTagsRepository.save(appTag);
    }

    public AppTags selectAppTag(Long id) {
        return appTagsRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("태그 정보를 조회할 수 없습니다."));
    }

    public Optional<AppTags> selectAppTag(String tag) {
        return appTagsRepository.findFirstByTag(tag);
    }

    public void createContentHashTag(Contents content, AppTags appTag, String tag) {
        ContentHashTags contentHashTags = ContentHashTags.builder()
                .appTag(appTag)
                .content(content)
                .tag(tag)
                .build();
        contentHashTagsRepository.save(contentHashTags);
    }

    public void deleteContentHashTag(Contents content) {
        contentHashTagsRepository.deleteAllByContent(content);
    }

    public Page<Contents> selectContentList(Integer page, Integer size, List<Users> userList, String isDeleted) {
        PageRequest pageRequest = PageRequest.of(page, size, Sort.by("id").descending());
        if (userList == null && isDeleted == null) return selectContentList(page, size);
        return null;
    }

    public Page<Contents> selectContentList(Integer page, Integer size) {
        PageRequest pageRequest = PageRequest.of(page, size, Sort.by("id").descending());
        return contentsRepository.findAll(pageRequest);
    }

    public Optional<Comments> selectComment(Long commentId) {
        if (commentId == null) return null;
        return commentsRepository.findById(commentId);
    }

    public Optional<Comments> selectComment(Long commentId, Users user) {
         return commentsRepository.findByIdAndUser(commentId, user);
    }

    public void createComment(Contents content, Users user, Comments parentComment, String text) {
        Comments comment = Comments.builder().
                content(content).
                user(user).
                comment(text).
                status(DeleteStatus.NORMAL).
                parent(parentComment).build();
        commentsRepository.save(comment);
    }

    public void saveComment(Comments comments, String comment) {
        comments.setComment(comment);
        commentsRepository.save(comments);
    }

    public Page<Comments> selectCommentList(Contents content, Integer page, Integer size) {
        PageRequest pageRequest = PageRequest.of(page, size, Sort.by("id").descending());
        return commentsRepository.findAllByContentAndParentNull(content, pageRequest);
    }

    public void saveCommentDelete(DeleteStatus status, Comments comment) {
        comment.setStatus(status);
        comment.setDeletedDate(LocalDateTime.now());
        commentsRepository.save(comment);
    }
}
