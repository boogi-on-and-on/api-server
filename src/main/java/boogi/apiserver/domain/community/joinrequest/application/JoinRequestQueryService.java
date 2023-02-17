package boogi.apiserver.domain.community.joinrequest.application;

import boogi.apiserver.domain.community.joinrequest.dao.JoinRequestRepository;
import boogi.apiserver.domain.user.dto.response.UserBasicProfileDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class JoinRequestQueryService {
    private final JoinRequestRepository joinRequestRepository;

    public List<Map<String, Object>> getAllRequests(Long communityId) {
        return joinRequestRepository.getAllRequests(communityId)
                .stream()
                .map(r -> Map.of(
                        "user", UserBasicProfileDto.of(r.getUser()),
                        "id", r.getId())
                )
                .collect(Collectors.toList());
    }
}
