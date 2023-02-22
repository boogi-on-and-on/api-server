package boogi.apiserver.domain.notice.domain;

import boogi.apiserver.domain.model.Content;
import boogi.apiserver.domain.notice.exception.InvalidNoticeContentException;
import boogi.apiserver.global.error.exception.InvalidValueException;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;

@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class NoticeContent extends Content {

    public static final int MIN_LENGTH = 10;
    public static final int MAX_LENGTH = 1000;

    public NoticeContent(String value) {
        super(value);
    }

    @Override
    protected int min_length() {
        return MIN_LENGTH;
    }

    @Override
    protected int max_length() {
        return MAX_LENGTH;
    }

    @Override
    protected InvalidValueException exception() {
        return new InvalidNoticeContentException();
    }
}
