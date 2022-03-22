package boogi.apiserver.domain.community.joinrequest.application;

import boogi.apiserver.domain.community.community.application.CommunityQueryService;
import boogi.apiserver.domain.community.community.domain.Community;
import boogi.apiserver.domain.community.joinrequest.dao.JoinRequestRepository;
import boogi.apiserver.domain.community.joinrequest.domain.JoinRequest;
import boogi.apiserver.domain.community.joinrequest.domain.JoinRequestStatus;
import boogi.apiserver.domain.user.application.UserQueryService;
import boogi.apiserver.domain.user.domain.User;
import boogi.apiserver.global.error.exception.InvalidValueException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class JoinRequestCoreServiceTest {

    @InjectMocks
    JoinRequestCoreService joinRequestCoreService;

    @Mock
    JoinRequestRepository joinRequestRepository;

    @Mock
    JoinRequestQueryService joinRequestQueryService;

    @Mock
    UserQueryService userQueryService;

    @Mock
    CommunityQueryService communityQueryService;

    @Test
    void 이미_요청한_경우() {
        User user = User.builder().id(1L).build();
        given(userQueryService.getUser(anyLong()))
                .willReturn(user);

        Community community = Community.builder().id(1L).build();
        given(communityQueryService.getCommunity(anyLong()))
                .willReturn(community);

        JoinRequest request = JoinRequest.builder()
                .status(JoinRequestStatus.PENDING)
                .build();
        given(joinRequestRepository.getLatestJoinRequest(anyLong(), anyLong()))
                .willReturn(request);

        assertThatThrownBy(() -> {
            joinRequestCoreService.request(user.getId(), community.getId());
        })
                .isInstanceOf(InvalidValueException.class)
                .hasMessage("이미 요청한 커뮤니티입니다.");
    }

    @Test
    void 이미_가입한_경우() {
        User user = User.builder().id(1L).build();
        given(userQueryService.getUser(anyLong()))
                .willReturn(user);

        Community community = Community.builder().id(1L).build();
        given(communityQueryService.getCommunity(anyLong()))
                .willReturn(community);

        JoinRequest request = JoinRequest.builder()
                .status(JoinRequestStatus.CONFIRM)
                .build();
        given(joinRequestRepository.getLatestJoinRequest(anyLong(), anyLong()))
                .willReturn(request);

        assertThatThrownBy(() -> {
            joinRequestCoreService.request(user.getId(), community.getId());
        })
                .isInstanceOf(InvalidValueException.class)
                .hasMessage("이미 가입한 커뮤니티입니다.");
    }
}