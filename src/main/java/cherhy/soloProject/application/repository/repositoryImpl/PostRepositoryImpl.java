package cherhy.soloProject.application.repository.repositoryImpl;

import cherhy.soloProject.application.domain.dto.PostPhotoDto;
import cherhy.soloProject.application.domain.dto.QPostPhotoPageDto;
import cherhy.soloProject.application.repository.querydsl.PostRepositoryCustom;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;

import static cherhy.soloProject.application.domain.entity.QMember.member;
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
        Long total = getTotal(memberId, pageable);
        return new PageImpl<PostPhotoDto>(content, pageable, total);
    }



    private Long getTotal(Long memberId, Pageable pageable) {
        Long getTotal = queryFactory.select(post.count())
                .from(post)
                .fetchJoin()
                .join(post.photos, photo1)
                .where(post.member.id.eq(memberId))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetchOne();
        return getTotal;
    }

    //여기서부터 다시!!
    private List<PostPhotoDto> getPosts(Long memberId, Pageable pageable) {
        List<PostPhotoDto> fetch = queryFactory.select(new QPostPhotoPageDto(
                post.id.as("post_id"), post.member.id.as("memberId"), post.title, post.content, ArrayList<photo1>
                ))
                .from(post)
                .fetchJoin()
                .join(post.photos, photo1)
                .where(post.member.id.eq(memberId))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();
        return fetch;
    }
}
