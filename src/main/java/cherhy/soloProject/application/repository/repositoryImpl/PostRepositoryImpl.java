package cherhy.soloProject.application.repository.repositoryImpl;

import cherhy.soloProject.Util.CursorRequest;
import cherhy.soloProject.application.domain.dto.PostPhotoDto;
import cherhy.soloProject.application.domain.dto.QPostPhotoDto;
import cherhy.soloProject.application.domain.entity.Post;
import cherhy.soloProject.application.repository.querydsl.PostRepositoryCustom;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.stream.Collectors;

import static cherhy.soloProject.application.domain.entity.QPhoto.*;
import static cherhy.soloProject.application.domain.entity.QPost.post;

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
    public List<PostPhotoDto> findAllByMemberIdNoKey(Long memberId, CursorRequest cursorRequest) {
        return getPostsCursorNoKey(memberId, cursorRequest);
    }
    @Override
    public List<PostPhotoDto> findByMemberIdPostIdDesc(Long memberId, CursorRequest cursorRequest) {
        return getPostsCursor(memberId, cursorRequest);
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

    private List<PostPhotoDto> getPostsCursorNoKey(Long memberId, CursorRequest cursorRequest) {
        List<Post> fetch = queryFactory.select(post).distinct()
                .from(post)
                .leftJoin(photo1).on(photo1.post.eq(post))
                .fetchJoin()
                .where(post.member.id.eq(memberId))
                .limit(cursorRequest.size())
                .orderBy(post.id.desc())
                .fetch();

        List<PostPhotoDto> collect = getPostPhotoDtos(fetch);
        return collect;
    }

    private List<PostPhotoDto> getPostsCursor(Long memberId, CursorRequest cursorRequest) {
        List<Post> fetch = queryFactory.select(post).distinct()
                .from(post)
                .leftJoin(photo1).on(photo1.post.eq(post))
                .fetchJoin()
                .where(
                        post.member.id.eq(memberId)
                                .and(post.id.loe(cursorRequest.key())))
                .limit(cursorRequest.size())
                .orderBy(post.id.desc())
                .fetch();

        List<PostPhotoDto> collect = getPostPhotoDtos(fetch);
        return collect;
    }

    private List<PostPhotoDto> getPostPhotoDtos(List<Post> fetch) {
        List<PostPhotoDto> collect = fetch.stream()
                .map(p -> new PostPhotoDto(p.getId(), p.getMember().getId(), p.getTitle(), p.getContent(), p.getPhotos()))
                .collect(Collectors.toList());
        return collect;
    }

}
