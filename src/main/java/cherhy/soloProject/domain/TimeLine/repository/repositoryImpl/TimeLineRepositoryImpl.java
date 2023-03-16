package cherhy.soloProject.domain.TimeLine.repository.repositoryImpl;

import cherhy.soloProject.Util.scrollDto.ScrollRequest;
import cherhy.soloProject.domain.member.entity.Member;
import cherhy.soloProject.domain.post.entity.Post;
import cherhy.soloProject.domain.TimeLine.repository.querydsl.TimeLineRepositoryCustom;
import cherhy.soloProject.domain.postBlock.entity.QPostBlock;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static cherhy.soloProject.domain.memberBlock.entity.QMemberBlock.*;
import static cherhy.soloProject.domain.post.entity.QPost.post;
import static cherhy.soloProject.domain.TimeLine.entity.QTimeLine.*;
import static cherhy.soloProject.domain.postBlock.entity.QPostBlock.*;

@Repository
@RequiredArgsConstructor
public class TimeLineRepositoryImpl implements TimeLineRepositoryCustom {
    private final JPAQueryFactory queryFactory;

//    select a.* from
//            (select * from timeline t where member_id = 1) as a
//    left join
//            (select p.post_id from member_block mb left join post p
//                    on mb.block_member_member_id = p.member_id
//                    where mb.member_member_id = 1) as b
//    on a.post_id = b.post_id
//    where b.post_id is null;

    @Override
    public List<Post> findPostIdByMemberFromTimeLine(Member myMember, ScrollRequest scrollRequest) {

        List<Post> posts = queryFactory.select(timeLine.post)
                .from(timeLine)
                .leftJoin(memberBlock)
                .on(timeLine.member.id.eq(memberBlock.member.id),
                        memberBlock.member.id.eq(myMember.getId()))
                .on(timeLine.member.id.eq(postBlock.member.id),
                        postBlock.member.id.eq(myMember.getId()))
                .where(timeLine.member.id.eq(myMember.getId()),
                        memberBlock.member.id.isNull(),
                        postBlock.member.id.isNull(),
                        keyCheck(scrollRequest))
                .orderBy(timeLine.id.desc())
                .limit(ScrollRequest.size)
                .fetch();

//        List<Post> postByCovering = getPostByCovering(getPostIds);

        return posts;
    }

    @Override
    public List<Post> findPostIdByMemberFromTimeLineSortModify(Member myMember, ScrollRequest scrollRequest) {
        List<Long> getPostIds = queryFactory.select(timeLine.post.id)
                .from(timeLine)
                .leftJoin(memberBlock)
                .on(timeLine.member.id.eq(memberBlock.member.id),
                        memberBlock.member.id.eq(myMember.getId()))
                .where(timeLine.member.eq(myMember),
                        memberBlock.member.id.isNull(),
                        keyCheckModify(scrollRequest))
                .orderBy(timeLine.lastModifiedDate.desc())
                .limit(ScrollRequest.size)
                .fetch();

        List<Post> postByCovering = getPostByCovering(getPostIds);

        return postByCovering;
    }

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

    public List<Post> getPostByCovering(List<Long> getPostIds) {
        List<Post> result = queryFactory.select(post)
                .from(post)
                .where(post.id.in(getPostIds))
                .orderBy(post.id.desc())
                .fetch();
        return result;
    }

    private BooleanExpression keyCheck(ScrollRequest scrollRequest) {
        return scrollRequest.hasKey() ? timeLine.id.lt(scrollRequest.key()) : null;
    }

    private BooleanExpression keyCheckModify(ScrollRequest scrollRequest) {
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

