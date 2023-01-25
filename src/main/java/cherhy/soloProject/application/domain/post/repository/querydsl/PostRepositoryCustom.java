package cherhy.soloProject.application.domain.post.repository.querydsl;

import cherhy.soloProject.Util.ScrollRequest;
import cherhy.soloProject.application.domain.post.dto.PostPhotoDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface PostRepositoryCustom {

    Page<PostPhotoDto> findAllByMemberId(Long memberId, Pageable pageable);
    List<PostPhotoDto> findByMemberIdPostIdDesc(Long memberId, ScrollRequest scrollRequest);
    List<PostPhotoDto> findAllByMemberIdNoKey(Long memberId, ScrollRequest scrollRequest);

}
