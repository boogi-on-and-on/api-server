package boogi.apiserver.domain.member.dto;

import boogi.apiserver.domain.member.domain.Member;
import boogi.apiserver.domain.user.domain.User;
import boogi.apiserver.global.dto.PagnationDto;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class JoinedMembersPageDto extends PagnationDto {

    private List<HashMap<String, Object>> members = new ArrayList<>();

    @Data
    @NoArgsConstructor
    static class UserDto {
        private Long id;

        @JsonInclude(JsonInclude.Include.NON_NULL)
        private String profileImageUrl;

        private String tagNum;
        private String name;
        private String department;

        private UserDto(User user) {
            this.id = user.getId();
            this.profileImageUrl = user.getProfileImageUrl();
            this.tagNum = user.getTagNumber();
            this.name = user.getUsername();
            this.department = user.getDepartment();
        }

        public static UserDto of(User user) {
            return new UserDto(user);
        }
    }

    private JoinedMembersPageDto(Page<Member> page) {
        super(page);
        this.members = page.getContent().stream()
                .map(m -> {
                    HashMap<String, Object> map = new HashMap<>();
                    map.put("id", m.getId());
                    map.put("type", m.getMemberType().toString());
                    map.put("createdAt", m.getCreatedAt().toString());
                    map.put("user", UserDto.of(m.getUser()));

                    return map;
                })
                .collect(Collectors.toList());
    }


    public static JoinedMembersPageDto of(Page page) {
        return new JoinedMembersPageDto(page);
    }
}