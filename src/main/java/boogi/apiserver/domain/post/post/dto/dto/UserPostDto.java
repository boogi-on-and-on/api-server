package boogi.apiserver.domain.post.post.dto.dto;

import boogi.apiserver.domain.community.community.domain.Community;
import boogi.apiserver.domain.hashtag.post.domain.PostHashtag;
import boogi.apiserver.domain.post.post.domain.Post;
import boogi.apiserver.domain.post.postmedia.domain.PostMedia;
import boogi.apiserver.domain.post.postmedia.dto.dto.PostMediaMetadataDto;
import boogi.apiserver.global.util.time.TimePattern;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Getter
public class UserPostDto {
    private Long id;
    private String content;
    private CommunityDto community;

    @JsonFormat(pattern = TimePattern.BASIC_FORMAT_STRING)
    private LocalDateTime createdAt;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<String> hashtags;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<PostMediaMetadataDto> postMedias;


    @Builder(access = AccessLevel.PRIVATE)
    public UserPostDto(Long id, String content, CommunityDto community, LocalDateTime createdAt,
                       List<String> hashtags, List<PostMediaMetadataDto> postMedias) {
        this.id = id;
        this.content = content;
        this.community = community;
        this.createdAt = createdAt;
        this.hashtags = hashtags;
        this.postMedias = postMedias;
    }

    public static UserPostDto from(Post post) {
        List<PostHashtag> postHashtags = post.getHashtags();
        List<String> hashtags = (postHashtags == null || postHashtags.size() == 0) ? null :
                postHashtags.stream()
                        .map(PostHashtag::getTag)
                        .collect(Collectors.toList());

        List<PostMedia> postMedias = post.getPostMedias();
        List<PostMediaMetadataDto> postMediaMetadataDtos = (postMedias == null || postMedias.size() == 0) ? null :
                postMedias.stream()
                        .map(PostMediaMetadataDto::from)
                        .collect(Collectors.toList());

        return UserPostDto.builder()
                .id(post.getId())
                .content(post.getContent())
                .community(CommunityDto.from(post.getCommunity()))
                .createdAt(post.getCreatedAt())
                .hashtags(hashtags)
                .postMedias(postMediaMetadataDtos)
                .build();
    }

    @Getter
    public static class CommunityDto {
        private Long id;

        private String name;

        public CommunityDto(Long id, String name) {
            this.id = id;
            this.name = name;
        }

        public static CommunityDto from(Community community) {
            return new CommunityDto(community.getId(), community.getCommunityName());
        }

    }
}
