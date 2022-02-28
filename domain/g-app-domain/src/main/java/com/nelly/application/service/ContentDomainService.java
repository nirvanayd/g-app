package com.nelly.application.service;

import com.nelly.application.domain.*;
import com.nelly.application.enums.YesOrNoType;
import com.nelly.application.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.swing.text.html.Option;
import java.util.ArrayList;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class ContentDomainService {
    private final ContentsRepository contentsRepository;
    private final ContentImagesRepository contentImagesRepository;
    private final UserHashTagsRepository userHashTagsRepository;
    private final BrandHashTagsRepository brandHashTagsRepository;
    private final ContentLikesRepository contentLikesRepository;
    private final ContentMarksRepository contentMarksRepository;

    public Contents createContent(Users user, String contentText) {
        Contents contents = Contents.builder()
                .contentText(contentText)
                .user(user)
                .likeCount(0)
                .replyCount(0)
                .markCount(0)
                .viewCount(0)
                .deletedYn(YesOrNoType.NO)
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

    public UserHashTags createUserTag(Contents content, Users user, String tag) {
        UserHashTags userHashTags = UserHashTags.builder()
                .user(user)
                .content(content)
                .tag(tag)
                .build();
        return userHashTagsRepository.save(userHashTags);
    }

    public BrandHashTags createBrandTag(Contents content, Brands brand, String tag) {
        BrandHashTags.BrandHashTagsBuilder builder = BrandHashTags.builder()
                .content(content)
                .tag(tag);
        if (brand != null) builder.brand(brand);
        BrandHashTags brandHashTags = builder.build();
        return brandHashTagsRepository.save(brandHashTags);
    }

    public Optional<Contents> selectContent(Users user, Long id) {
        return contentsRepository.findByUserAndId(user, id);
    }

    public Optional<Contents> selectContent(Long id) {
        return contentsRepository.findById(id);
    }


    public void updateDeleted(Contents content, YesOrNoType deletedYn) {
        content.setDeletedYn(deletedYn);
        contentsRepository.save(content);
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

}
