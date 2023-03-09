package boogi.apiserver.domain.member.vo;

import boogi.apiserver.domain.member.domain.Member;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@EqualsAndHashCode
public class NullMember extends Member {

    @Override
    public Long getId() {
        return null;
    }

    @Override
    public boolean isJoined() {
        return false;
    }
}
