package cherhy.soloProject.domain.follow.repository.repositoryImpl;

import cherhy.soloProject.Util.scrollDto.ScrollRequest;
import cherhy.soloProject.domain.follow.dto.response.FollowMemberResponse;
import cherhy.soloProject.domain.follow.repository.querydsl.FollowRepositoryCustom;
import cherhy.soloProject.domain.member.entity.Member;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;

import java.util.List;

import static cherhy.soloProject.domain.follow.entity.QFollow.follow;
import static cherhy.soloProject.domain.member.entity.QMember.member;

@Repository
@RequiredArgsConstructor
public class FollowRepositoryImpl implements FollowRepositoryCustom {

    private final EntityManager em;
    private final JPAQueryFactory queryFactory;

    @Override
    public List<FollowMemberResponse> findAllByFollowing(Member requestMember, ScrollRequest scrollRequest) {
        return findFollowing(requestMember, scrollRequest);
    }

    @Override
    public List<FollowMemberResponse> findAllByFollower(Member requestMember, ScrollRequest scrollRequest) {
        return findFollower(requestMember, scrollRequest);
    }

    private List<FollowMemberResponse> findFollowing(Member requestMember, ScrollRequest scrollRequest) {
        return queryFactory
                .select(Projections.constructor(FollowMemberResponse.class,
                                member.id, follow.id, member.name, member.email))
                .from(follow)
                .innerJoin(follow.following, member)
                .where(follow.follower.eq(requestMember) , (keyCheck(scrollRequest)))
                .orderBy(follow.id.desc())
                .limit(ScrollRequest.size)
                .fetch();
    }

    private List<FollowMemberResponse> findFollower(Member requestMember, ScrollRequest scrollRequest) {
        return queryFactory
                .select(Projections.constructor(FollowMemberResponse.class,
                        member.id, follow.id, member.name, member.email))
                .from(follow)
                .innerJoin(follow.follower, member)
                .where(follow.following.eq(requestMember), (keyCheck(scrollRequest)))
                .orderBy(follow.id.desc())
                .limit(ScrollRequest.size)
                .fetch();
    }
    private BooleanExpression keyCheck(ScrollRequest scrollRequest) {
        return scrollRequest.hasKey() ? follow.id.lt(scrollRequest.key()) : null;
    }

}
