package cherhy.soloProject.application.domain.memberBlock.repository.querydsl;


import cherhy.soloProject.Util.scrollDto.ScrollRequest;
import cherhy.soloProject.application.domain.member.entity.Member;
import cherhy.soloProject.application.domain.member.entity.QMember;
import cherhy.soloProject.application.domain.memberBlock.entity.MemberBlock;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

import static cherhy.soloProject.application.domain.member.entity.QMember.*;
import static cherhy.soloProject.application.domain.memberBlock.entity.QMemberBlock.*;

@Repository
@RequiredArgsConstructor
public class MemberBlockRepositoryImpl implements MemberBlockRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    @Override
    public Optional<List<MemberBlock>> getBlockMemberScroll(Member findMember, ScrollRequest scrollRequest) {
        List<MemberBlock> result = queryFactory.select(memberBlock)
                .from(memberBlock)
                .leftJoin(member)
                .on(memberBlock.member.eq(member))
                .fetchJoin()
                .where(memberBlock.member.eq(findMember), keyCheck(scrollRequest))
                .orderBy(memberBlock.id.desc())
                .limit(ScrollRequest.size)
                .fetch();

        return Optional.ofNullable(result);
    }

    private BooleanExpression keyCheck(ScrollRequest scrollRequest) {
        return scrollRequest.hasKey() ? memberBlock.id.lt(scrollRequest.key()) : null;
    }
}
