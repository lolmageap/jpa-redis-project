package cherhy.soloProject.domain.TimeLine.repository.repositoryImpl;

import cherhy.soloProject.Util.scrollDto.ScrollRequest;
import cherhy.soloProject.domain.TimeLine.repository.querydsl.TimeLineRepositoryCustom;
import cherhy.soloProject.domain.member.entity.Member;
import cherhy.soloProject.domain.post.entity.Post;
import com.querydsl.core.types.SubQueryExpression;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static cherhy.soloProject.domain.TimeLine.entity.QTimeLine.timeLine;
import static cherhy.soloProject.domain.memberBlock.entity.QMemberBlock.memberBlock;
import static cherhy.soloProject.domain.post.entity.QPost.post;

@Repository
@RequiredArgsConstructor
public class TimeLineRepositoryImpl implements TimeLineRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    @Override
    public List<Post> getTimeLine(Member myMember, ScrollRequest scrollRequest) {

        SubQueryExpression<Long> subQuery1 = JPAExpressions
                .select(timeLine.post.id)
                .from(timeLine)
                .where(timeLine.member.id.eq(1L));

        SubQueryExpression<Long> subQuery2 = JPAExpressions
                .select(post.id)
                .from(memberBlock)
                .innerJoin(post.member)
                .on(memberBlock.blockMember.id.eq(post.member.id))
                .where(memberBlock.member.id.eq(1L));

        List<Post> posts = queryFactory
                .selectFrom(timeLine.post)
                .where(timeLine.post.id.in(subQuery1),
                        timeLine.post.id.notIn(subQuery2),
                        keyCheck(scrollRequest))
                .fetch();

        return posts;
    }

//    @Override
//    public List<Post> findPostIdByMemberFromTimeLineSortModify(Member myMember, ScrollRequest scrollRequest) {
//        List<Long> getPostIds = queryFactory.select(timeLine.post.id)
//                .from(timeLine)
//                .leftJoin(memberBlock)
//                .on(timeLine.member.id.eq(memberBlock.member.id),
//                        memberBlock.member.id.eq(myMember.getId()))
//                .where(timeLine.member.eq(myMember),
//                        memberBlock.member.id.isNull(),
//                        keyCheckModify(scrollRequest))
//                .orderBy(timeLine.lastModifiedDate.desc())
//                .limit(ScrollRequest.size)
//                .fetch();
//
//        List<Post> postByCovering = getPostByCovering(getPostIds);
//
//        return postByCovering;
//    }

    @Override
    public List<Long> getNextKey(Member myMember, ScrollRequest scrollRequest) {
        List<Long> postIdForTimeLine = queryFactory.select(timeLine.id)
                .from(timeLine)
                .leftJoin(memberBlock)
                .on(timeLine.member.id.eq(memberBlock.member.id),
                        memberBlock.member.id.eq(myMember.getId()))
                .where(timeLine.member.id.eq(myMember.getId()),
                        memberBlock.member.id.isNull(),
                        keyCheck(scrollRequest))
                .orderBy(timeLine.id.desc())
                .limit(ScrollRequest.size)
                .fetch();

        return postIdForTimeLine;
    }

//    public List<Post> getPostByCovering(List<Long> getPostIds) {
//        List<Post> result = queryFactory.select(post)
//                .from(post)
//                .where(post.id.in(getPostIds))
//                .orderBy(post.id.desc())
//                .fetch();
//        return result;
//    }

    private BooleanExpression keyCheck(ScrollRequest scrollRequest) {
        return scrollRequest.hasKey() ? timeLine.id.lt(scrollRequest.key()) : null;
    }

//    private BooleanExpression keyCheckModify(ScrollRequest scrollRequest) {
//        return scrollRequest.hasKey() ? timeLine.lastModifiedDate.loe(parseKey(scrollRequest)) : null;
//    }

//    private LocalDateTime parseKey(ScrollRequest scrollRequest) {
//        String key = Long.toString(scrollRequest.key());
//
//        StringBuffer str = new StringBuffer().append(key);
//        str.insert(14,".");
//        str.insert(12,":");
//        str.insert(10,":");
//        str.insert(8," ");
//        str.insert(6,"-");
//        str.insert(4,"-");
//
//        LocalDateTime parse = LocalDateTime.parse(str, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSSSS"));
//        return parse;
//    }
}

