package cherhy.soloProject.application.domain.post.repository.querydsl;

import cherhy.soloProject.Util.scrollDto.ScrollRequest;
import cherhy.soloProject.application.domain.post.dto.PostPhotoDto;
import cherhy.soloProject.application.domain.post.entity.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface PostRepositoryCustom {

    Page<PostPhotoDto> findAllByMemberId(Long memberId, Pageable pageable);
    List<Post> findByMemberIdPostIdDesc(Long memberId, ScrollRequest scrollRequest);
    List<Post> findAllByMemberIdNoKey(Long memberId, ScrollRequest scrollRequest);

}
