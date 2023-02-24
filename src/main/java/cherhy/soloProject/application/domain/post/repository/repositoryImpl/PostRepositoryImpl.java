package cherhy.soloProject.application.domain.post.repository.repositoryImpl;

import cherhy.soloProject.Util.scrollDto.ScrollRequest;
import cherhy.soloProject.application.domain.post.entity.Post;
import cherhy.soloProject.application.domain.post.repository.querydsl.PostRepositoryCustom;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

import static cherhy.soloProject.application.domain.photo.entity.QPhoto.*;
import static cherhy.soloProject.application.domain.post.entity.QPost.*;
import static cherhy.soloProject.application.domain.postBlock.entity.QPostBlock.*;


@Repository
@RequiredArgsConstructor
public class PostRepositoryImpl implements PostRepositoryCustom {

    private final EntityManager em;
    private final JPAQueryFactory queryFactory;

    @Override
    public List<Post> findAllByMemberId(Long memberId, Pageable pageable) {
        return getPosts(memberId, pageable);
    }
    @Override
    public List<Post> findAllByMemberId(Long memberId, Long memberSessionId, Pageable pageable) {
        return getPosts(memberId, memberSessionId, pageable);
    }
    @Override
    public Long findAllByMemberIdCount(Long memberId) {
        return  getTotal(memberId);
    }

    @Override
    public Long findAllByMemberIdCount(Long memberId, Long memberSessionId) {
        return getTotal(memberId, memberSessionId);
    }

    @Override
    public List<Post> findByMemberIdPostIdDesc(Long memberId, ScrollRequest scrollRequest) {
        return getPostsCursor(memberId, scrollRequest);
    }

    @Override
    public List<Post> findByMemberIdPostIdDesc(Long memberId, Long memberSessionId, ScrollRequest scrollRequest) {
        return getPostsCursor(memberId, memberSessionId, scrollRequest);
    }

    private Long getTotal(Long memberId) {
        Long getTotal = queryFactory.select(post.count())
                .from(post)
                .where(post.member.id.eq(memberId))
                .fetchOne();
        return getTotal;
    }

    private Long getTotal(Long memberId, Long memberSessionId) {
        Long getTotal = queryFactory.select(post.count())
                .from(post)
                .leftJoin(postBlock).on(postBlock.post.id.eq(post.id)
                        .and(postBlock.member.id.eq(memberSessionId)))
                .where(post.member.id.eq(memberId).and(postBlock.id.isNull()))
                .fetchOne();
        return getTotal;
    }

    private List<Post> getPosts(Long memberId, Pageable pageable) {
        return queryFactory.select(post).distinct()
                .from(post)
                .leftJoin(photo1).on(photo1.post.eq(post))
                .fetchJoin()
                .where(post.member.id.eq(memberId))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(post.id.desc())
                .fetch();
    }

    private List<Post> getPosts(Long memberId,Long memberSessionId, Pageable pageable) {
        return queryFactory.select(post).distinct()
                .from(post)
                .leftJoin(photo1).on(photo1.post.eq(post))
                .fetchJoin()
                .leftJoin(postBlock).on(postBlock.post.id.eq(post.id)
                        .and(postBlock.member.id.eq(memberSessionId)))
                .fetchJoin()
                .where(post.member.id.eq(memberId).and(postBlock.id.isNull()))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(post.id.desc())
                .fetch();
    }

    private List<Post> getPostsCursor(Long memberId, ScrollRequest scrollRequest) {
        return queryFactory.select(post).distinct()
                .from(post)
                .leftJoin(photo1).on(photo1.post.eq(post))
                .fetchJoin()
                .where(post.member.id.eq(memberId),
                        keyCheck(scrollRequest))
                .limit(scrollRequest.size())
                .orderBy(post.id.desc())
                .fetch();
    }
    private List<Post> getPostsCursor(Long memberId, Long memberSessionId, ScrollRequest scrollRequest) {
        return queryFactory.select(post).distinct()
                .from(post)
                .leftJoin(photo1).on(photo1.post.eq(post))
                .fetchJoin()
                .leftJoin(postBlock).on(postBlock.post.id.eq(post.id)
                        .and(postBlock.member.id.eq(memberSessionId)))
                .fetchJoin()
                .where(post.member.id.eq(memberId),
                        (postBlock.id.isNull()),
                        keyCheck(scrollRequest))
                .limit(scrollRequest.size())
                .orderBy(post.id.desc())
                .fetch();
    }

    private BooleanExpression keyCheck(ScrollRequest scrollRequest) {
        return scrollRequest.hasKey() ? post.id.lt(scrollRequest.key()) : null;
    }

}
