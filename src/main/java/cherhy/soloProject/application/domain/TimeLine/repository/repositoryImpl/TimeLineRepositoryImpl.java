package cherhy.soloProject.application.domain.TimeLine.repository.repositoryImpl;

import cherhy.soloProject.Util.scrollDto.ScrollRequest;
import cherhy.soloProject.application.domain.member.entity.Member;
import cherhy.soloProject.application.domain.memberBlock.entity.QMemberBlock;
import cherhy.soloProject.application.domain.post.entity.Post;
import cherhy.soloProject.application.domain.TimeLine.repository.querydsl.TimeLineRepositoryCustom;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static cherhy.soloProject.application.domain.memberBlock.entity.QMemberBlock.*;
import static cherhy.soloProject.application.domain.post.entity.QPost.post;
import static cherhy.soloProject.application.domain.TimeLine.entity.QTimeLine.*;

@Repository
@RequiredArgsConstructor
public class TimeLineRepositoryImpl implements TimeLineRepositoryCustom {

    private final EntityManager em;
    private final JPAQueryFactory queryFactory;

    @Override
    public List<Post> findPostIdByMemberFromTimeLine(Member myMember, ScrollRequest scrollRequest) {
        List<Long> getPostIds = queryFactory.select(timeLine.post.id)
                .from(timeLine)
                .leftJoin(memberBlock)
                .on(timeLine.member.id.eq(memberBlock.member.id),
                        memberBlock.member.id.eq(myMember.getId()))
                .where(timeLine.member.eq(myMember),
                        memberBlock.member.id.isNull(),
                        keyCheck(scrollRequest))
                .orderBy(timeLine.lastModifiedDate.desc())
                .limit(ScrollRequest.size)
                .fetch();
        List<Post> postByCovering = getPostByCovering(getPostIds);
        return postByCovering;
    }

    @Override
    public List<LocalDateTime> getNextKey(Member paramMember, ScrollRequest scrollRequest) {
        List<LocalDateTime> keyOfLocalDateTimes = queryFactory.select(timeLine.lastModifiedDate)
                .from(timeLine)
                .where(timeLine.member.eq(paramMember), keyCheck(scrollRequest))
                .orderBy(timeLine.lastModifiedDate.desc())
                .limit(ScrollRequest.size)
                .fetch();
        return keyOfLocalDateTimes;
    }

    public List<Post> getPostByCovering(List<Long> getPostIds) {
        List<Post> result = queryFactory.select(post)
                .from(post)
                .where(post.id.in(getPostIds))
                .fetch();
        return result;
    }

    private BooleanExpression keyCheck(ScrollRequest scrollRequest) {
        return scrollRequest.hasKey() ? timeLine.lastModifiedDate.loe(parseKey(scrollRequest)) : null;
    }
    private LocalDateTime parseKey(ScrollRequest scrollRequest) {
        String key = Long.toString(scrollRequest.key());

        StringBuffer str = new StringBuffer().append(key);
        str.insert(14,".");
        str.insert(12,":");
        str.insert(10,":");
        str.insert(8," ");
        str.insert(6,"-");
        str.insert(4,"-");

        LocalDateTime parse = LocalDateTime.parse(str, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSSSS"));
        return parse;
    }
}

