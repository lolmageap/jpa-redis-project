package cherhy.soloProject.application.domain.post.repository.repositoryImpl;

import cherhy.soloProject.Util.scrollDto.ScrollRequest;
import cherhy.soloProject.application.domain.post.dto.PostPhotoDto;
import cherhy.soloProject.application.domain.post.entity.Post;
import cherhy.soloProject.application.domain.post.repository.querydsl.PostRepositoryCustom;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.stream.Collectors;

import static cherhy.soloProject.application.domain.photo.entity.QPhoto.*;
import static cherhy.soloProject.application.domain.post.entity.QPost.*;


@Repository
@RequiredArgsConstructor
public class PostRepositoryImpl implements PostRepositoryCustom {

    private final EntityManager em;
    private final JPAQueryFactory queryFactory;

    @Override
    public Page<PostPhotoDto> findAllByMemberId(Long memberId, Pageable pageable) {
        List<PostPhotoDto> content = getPosts(memberId, pageable);
        Long total = getTotal(memberId);
        return new PageImpl<>(content, pageable, total);
    }
    @Override
    public List<Post> findAllByMemberIdNoKey(Long memberId, ScrollRequest scrollRequest) {
        return getPostsCursorNoKey(memberId, scrollRequest);
    }
    @Override
    public List<Post> findByMemberIdPostIdDesc(Long memberId, ScrollRequest scrollRequest) {
        return getPostsCursor(memberId, scrollRequest);
    }


    private Long getTotal(Long memberId) {
        Long getTotal = queryFactory.select(post.count())
                .from(post)
                .where(post.member.id.eq(memberId))
                .fetchOne();
        return getTotal;
    }

    private List<PostPhotoDto> getPosts(Long memberId, Pageable pageable) {
        List<Post> fetch = queryFactory.select(post).distinct()
                .from(post)
                .leftJoin(photo1).on(photo1.post.eq(post))
                .fetchJoin()
                .where(post.member.id.eq(memberId))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(post.id.desc())
                .fetch();

        List<PostPhotoDto> collect = getPostPhotoDtos(fetch);
        return collect;
    }

    private List<Post> getPostsCursorNoKey(Long memberId, ScrollRequest scrollRequest) {
        return queryFactory.select(post).distinct()
                .from(post)
                .leftJoin(photo1).on(photo1.post.eq(post))
                .fetchJoin()
                .where(post.member.id.eq(memberId))
                .limit(scrollRequest.size())
                .orderBy(post.id.desc())
                .fetch();
    }

    private List<Post> getPostsCursor(Long memberId, ScrollRequest scrollRequest) {
        return queryFactory.select(post).distinct()
                .from(post)
                .leftJoin(photo1).on(photo1.post.eq(post))
                .fetchJoin()
                .where(
                        post.member.id.eq(memberId)
                                .and(post.id.lt(scrollRequest.key())))
                .limit(scrollRequest.size())
                .orderBy(post.id.desc())
                .fetch();
    }

    private List<PostPhotoDto> getPostPhotoDtos(List<Post> fetch) {
        List<PostPhotoDto> collect = fetch.stream()
                .map(p -> new PostPhotoDto(p))
                .collect(Collectors.toList());
        return collect;
    }

}
