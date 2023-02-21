package boogi.apiserver.domain.user.dto.dto;

import boogi.apiserver.domain.member.domain.Member;
import boogi.apiserver.domain.user.domain.User;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;

@Getter
public class UserBasicProfileDto {
    private Long id;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String profileImageUrl;
    private String tagNum;
    private String name;

    public UserBasicProfileDto(Long id, String profileImageUrl, String tagNum, String name) {
        this.id = id;
        this.profileImageUrl = profileImageUrl;
        this.tagNum = tagNum;
        this.name = name;
    }

    @QueryProjection
    public UserBasicProfileDto(User user) {
        this(user.getId(), user.getProfileImageUrl(), user.getTagNumber(), user.getUsername());
    }

    public static UserBasicProfileDto of(User user) {
        if (user == null) {
            return null;
        }
        return new UserBasicProfileDto(user);
    }

    public static UserBasicProfileDto of(Member member) {
        if (member == null || member.getUser() == null) {
            return null;
        }
        return new UserBasicProfileDto(member.getUser());
    }
}
