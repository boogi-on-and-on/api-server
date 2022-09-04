package boogi.apiserver.domain.message.message.dao;


import boogi.apiserver.domain.message.message.domain.Message;
import boogi.apiserver.global.util.PageableUtil;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

import java.util.List;

import static boogi.apiserver.domain.message.message.domain.QMessage.message;


@RequiredArgsConstructor
public class MessageRepositoryImpl implements MessageRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public Slice<Message> findMessagesByOpponentIdAndMyId(Long opponentId, Long myId, Pageable pageable) {
        List<Message> messages = queryFactory.selectFrom(message)
                .where(
                        message.sender.id.eq(opponentId).and(message.receiver.id.eq(myId))
                                .or(message.receiver.id.eq(opponentId).and(message.sender.id.eq(myId))),
                        message.blocked_message.isFalse()
                )
                .orderBy(message.createdAt.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize() + 1)
                .fetch();

        return PageableUtil.getSlice(messages, pageable);
    }
}