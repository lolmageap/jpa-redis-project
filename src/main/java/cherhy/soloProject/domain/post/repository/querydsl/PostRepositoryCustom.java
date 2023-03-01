package cherhy.soloProject.domain.post.repository.querydsl;

import cherhy.soloProject.Util.scrollDto.ScrollRequest;
import cherhy.soloProject.domain.post.entity.Post;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface PostRepositoryCustom {

    List<Post> findAllByMemberId(Long memberId, Pageable pageable);
    List<Post> findAllByMemberId(Long memberId, Long memberSessionId, Pageable pageable);
    Long findAllByMemberIdCount(Long memberId);
    Long findAllByMemberIdCount(Long memberId, Long memberSessionId);
    List<Post> findByMemberIdPostIdDesc(Long memberId, ScrollRequest scrollRequest);
    List<Post> findByMemberIdPostIdDesc(Long memberId, Long memberSessionId, ScrollRequest scrollRequest);

}
