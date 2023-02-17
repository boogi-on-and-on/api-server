package boogi.apiserver.domain.community.community.dto.request;


import boogi.apiserver.domain.member.domain.MemberType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DelegateMemberRequest {

    @NotNull
    private Long memberId;

    @NotNull
    private MemberType type;
}
