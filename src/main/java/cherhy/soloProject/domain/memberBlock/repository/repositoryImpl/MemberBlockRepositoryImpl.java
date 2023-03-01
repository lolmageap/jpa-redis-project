package cherhy.soloProject.domain.memberBlock.repository.repositoryImpl;


import cherhy.soloProject.Util.scrollDto.ScrollRequest;
import cherhy.soloProject.domain.member.entity.Member;
import cherhy.soloProject.domain.memberBlock.dto.response.MemberBlockResponseDto;
import cherhy.soloProject.domain.memberBlock.repository.querydsl.MemberBlockRepositoryCustom;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

import static cherhy.soloProject.domain.member.entity.QMember.*;
import static cherhy.soloProject.domain.memberBlock.entity.QMemberBlock.*;

@Repository
@RequiredArgsConstructor
public class MemberBlockRepositoryImpl implements MemberBlockRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    @Override
    public Optional<List<MemberBlockResponseDto>> getBlockMemberScroll(Member requestMember, ScrollRequest scrollRequest) {
        List<MemberBlockResponseDto> result = queryFactory.select(Projections.constructor(MemberBlockResponseDto.class,
                         memberBlock.id, memberBlock.blockMember.id, memberBlock.blockMember.name, memberBlock.blockMember.email))
                .from(memberBlock)
                .innerJoin(memberBlock.member,member)
                .where(memberBlock.member.eq(requestMember), keyCheck(scrollRequest))
                .orderBy(memberBlock.id.desc())
                .limit(ScrollRequest.size)
                .fetch();

        return Optional.ofNullable(result);
    }

    private BooleanExpression keyCheck(ScrollRequest scrollRequest) {
        return scrollRequest.hasKey() ? memberBlock.id.lt(scrollRequest.key()) : null;
    }
}
