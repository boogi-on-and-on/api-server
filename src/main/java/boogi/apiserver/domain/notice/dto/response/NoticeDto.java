package boogi.apiserver.domain.notice.dto.response;

import boogi.apiserver.domain.notice.domain.Notice;
import boogi.apiserver.global.util.time.TimePattern;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class NoticeDto {

    protected Long id;
    protected String title;

    @JsonFormat(pattern = TimePattern.BASIC_FORMAT_STRING)
    protected LocalDateTime createdAt;

    protected NoticeDto(Notice notice) {
        this.id = notice.getId();
        this.title = notice.getTitle();
        this.createdAt = notice.getCreatedAt();
    }

    public static NoticeDto of(Notice notice) {
        return new NoticeDto(notice.getId(), notice.getTitle(), notice.getCreatedAt());
    }

    public NoticeDto(final Long id, final String title, final LocalDateTime createdAt) {
        this.id = id;
        this.title = title;
        this.createdAt = createdAt;
    }
}
