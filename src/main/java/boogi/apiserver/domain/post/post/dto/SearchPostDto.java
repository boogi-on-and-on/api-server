package boogi.apiserver.domain.post.post.dto;

import boogi.apiserver.domain.community.community.domain.Community;
import boogi.apiserver.domain.hashtag.post.domain.PostHashtag;
import boogi.apiserver.domain.post.post.domain.Post;
import boogi.apiserver.domain.user.domain.User;
import boogi.apiserver.domain.user.dto.UserBasicProfileDto;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.querydsl.core.annotations.QueryProjection;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SearchPostDto {

    private Long id;
    private UserBasicProfileDto user;
    private Long communityId;
    private String communityName;
    private String createdAt;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<String> hashtags;

    private int commentCount;
    private int likeCount;

    private String content;

    public SearchPostDto(Post post) {
        this.id = post.getId();

        User user = post.getMember().getUser();
        this.user = UserBasicProfileDto.of(user);

        Community community = post.getCommunity();
        this.communityId = community.getId();
        this.communityName = community.getCommunityName();

        this.createdAt = post.getCreatedAt().toString();
        this.commentCount = post.getCommentCount();
        this.likeCount = post.getLikeCount();
        this.content = post.getContent();

        List<PostHashtag> hashtags = post.getHashtags();
        if (hashtags != null && hashtags.size() > 0) {
            this.hashtags = hashtags.stream()
                    .map(PostHashtag::getTag)
                    .collect(Collectors.toList());
        }
    }

}

