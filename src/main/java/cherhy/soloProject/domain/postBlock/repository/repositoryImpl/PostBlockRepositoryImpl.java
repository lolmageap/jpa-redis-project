package cherhy.soloProject.domain.postBlock.repository.repositoryImpl;

import cherhy.soloProject.Util.scrollDto.ScrollRequest;
import cherhy.soloProject.domain.member.entity.Member;
import cherhy.soloProject.domain.postBlock.entity.PostBlock;
import cherhy.soloProject.domain.postBlock.repository.querydsl.PostBlockRepositoryCustom;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

import static cherhy.soloProject.domain.postBlock.entity.QPostBlock.*;

@Repository
@RequiredArgsConstructor
public class PostBlockRepositoryImpl implements PostBlockRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public Optional<List<PostBlock>> findByPostBlockScroll(Member member, ScrollRequest scrollRequest) {

        List<PostBlock> fetch = queryFactory.select(postBlock)
                .from(postBlock)
                .where(postBlock.member.eq(member), keyCheck(scrollRequest))
                .orderBy(postBlock.post.id.asc())
                .limit(ScrollRequest.size)
                .fetch();

        return Optional.ofNullable(fetch);
    }

    private BooleanExpression keyCheck(ScrollRequest scrollRequest) {
        return scrollRequest.hasKey() ? postBlock.post.id.gt(scrollRequest.key()) : null;
    }

}
