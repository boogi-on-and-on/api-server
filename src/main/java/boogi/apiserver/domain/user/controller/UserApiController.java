package boogi.apiserver.domain.user.controller;

import boogi.apiserver.domain.alarm.alarmconfig.application.AlarmConfigService;
import boogi.apiserver.domain.alarm.alarmconfig.domain.AlarmConfig;
import boogi.apiserver.domain.alarm.alarmconfig.dto.request.AlarmConfigSettingRequest;
import boogi.apiserver.domain.alarm.alarmconfig.dto.response.AlarmConfigSettingInfo;
import boogi.apiserver.domain.community.community.application.CommunityService;
import boogi.apiserver.domain.community.community.dto.dto.JoinedCommunitiesDto;
import boogi.apiserver.domain.message.block.application.MessageBlockService;
import boogi.apiserver.domain.message.block.application.MessageBlockQueryService;
import boogi.apiserver.domain.message.block.dto.response.MessageBlockedUserDto;
import boogi.apiserver.domain.user.application.UserQueryService;
import boogi.apiserver.domain.user.domain.User;
import boogi.apiserver.domain.user.dto.request.BlockMessageUsersRequest;
import boogi.apiserver.domain.user.dto.response.UserDetailInfoResponse;
import boogi.apiserver.global.argument_resolver.session.Session;
import boogi.apiserver.global.constant.SessionInfoConst;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/users")
public class UserApiController {
    private final UserQueryService userQueryService;
    private final MessageBlockQueryService messageBlockQueryService;

    private final CommunityService communityService;

    private final MessageBlockService messageBlockService;
    private final AlarmConfigService alarmConfigService;

    @PostMapping("/token/{email}")
    public ResponseEntity<Void> issueToken(HttpServletRequest request, @PathVariable String email) {
        User user = userQueryService.getUserByEmail(email);

        HttpSession preSession = request.getSession(false);
        if (preSession != null) {
            preSession.invalidate();
        }

        HttpSession session = request.getSession();
        session.setMaxInactiveInterval(60 * 60 * 24 * 30 * 3); // 3개월
        session.setAttribute(SessionInfoConst.USER_ID, user.getId());

        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @PostMapping("/token/validation")
    public ResponseEntity<Object> validateToken(HttpServletRequest request) {
        HttpSession session = request.getSession(false);

        Boolean isValid = Objects.nonNull(session);

        return ResponseEntity.status(HttpStatus.OK).body(Map.of("isValid", isValid));
    }

    @GetMapping
    public ResponseEntity<Object> getUserProfileInfo(@RequestParam(required = false) Long userId, @Session Long sessionUserId) {
        Long id = Objects.requireNonNullElse(userId, sessionUserId);
        UserDetailInfoResponse userDetailDto = userQueryService.getUserDetailInfo(id);
        Boolean me = sessionUserId.equals(userDetailDto.getId());
        return ResponseEntity.status(HttpStatus.OK).body(Map.of("user", userDetailDto, "me", me));
    }

    @GetMapping("/communities/joined")
    public ResponseEntity<Object> getUserJoinedCommunitiesInfo(@Session Long userId) {
        JoinedCommunitiesDto joinedCommunitiesDto = communityService.getJoinedCommunitiesWithLatestPost(userId);

        return ResponseEntity.status(HttpStatus.OK).body(joinedCommunitiesDto);
    }

    @GetMapping("/messages/blocked")
    public ResponseEntity<Object> getBlockedUsers(@Session Long userId) {
        List<MessageBlockedUserDto> blockedUserDtos = messageBlockQueryService.getBlockedMembers(userId);

        return ResponseEntity.ok(Map.of("blocked", blockedUserDtos));
    }

    @PostMapping("/messages/unblock")
    public ResponseEntity<Void> releaseUser(@Session Long userId, @RequestBody HashMap<String, String> request) {
        Long blockedUserId = Long.getLong(request.get("blockedUserId"));
        messageBlockService.releaseUser(userId, blockedUserId);

        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @PostMapping("/messages/block")
    public ResponseEntity<Void> blockUsers(@Session Long userId, @Validated @RequestBody BlockMessageUsersRequest request) {
        messageBlockService.blockUsers(userId, request.getBlockUserIds());

        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @GetMapping("/config/notifications")
    public ResponseEntity<Object> getAlarmConfig(@Session Long userId) {
        AlarmConfig alarmConfig = alarmConfigService.findOrElseCreateAlarmConfig(userId);

        return ResponseEntity.ok(Map.of("alarmInfo", AlarmConfigSettingInfo.of(alarmConfig)));
    }

    @PostMapping("/config/notifications")
    public ResponseEntity<Object> configureAlarm(@Session Long userId, @RequestBody AlarmConfigSettingRequest request) {
        AlarmConfig alarmConfig = alarmConfigService.configureAlarm(userId, request);

        return ResponseEntity.ok(Map.of("alarmInfo", AlarmConfigSettingInfo.of(alarmConfig)));
    }
}
