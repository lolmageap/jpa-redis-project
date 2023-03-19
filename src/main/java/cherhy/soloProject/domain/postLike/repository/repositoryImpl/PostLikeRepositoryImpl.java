package cherhy.soloProject.domain.postLike.repository.repositoryImpl;

import cherhy.soloProject.Util.scrollDto.ScrollRequest;
import cherhy.soloProject.domain.member.entity.Member;
import cherhy.soloProject.domain.postLike.dto.response.PostLikeResponse;
import cherhy.soloProject.domain.postLike.repository.querydsl.PostLikeRepositoryCustom;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static cherhy.soloProject.domain.postLike.entity.QPostLike.postLike;


@Repository
@RequiredArgsConstructor
public class PostLikeRepositoryImpl implements PostLikeRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<PostLikeResponse> getPostLike(Member member, ScrollRequest scrollRequest) {

        List<PostLikeResponse> result = queryFactory.select(Projections.constructor(PostLikeResponse.class,
                        postLike.id, postLike.post))
                .from(postLike)
                .where(postLike.member.eq(member)
                        , keyCheck(scrollRequest))
                .orderBy(postLike.id.desc())
//                .orderBy(postLike.post.lastModifiedDate.desc())
                .limit(ScrollRequest.size)
                .fetch();
        return result;
    }

    private BooleanExpression keyCheck(ScrollRequest scrollRequest) {
        return scrollRequest.hasKey() ? postLike.id.lt(scrollRequest.key()) : null;
    }

}
