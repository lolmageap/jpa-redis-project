package cherhy.soloProject.application.repository.querydsl;

import cherhy.soloProject.Util.CursorRequest;
import cherhy.soloProject.application.domain.dto.PostPhotoDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface PostRepositoryCustom {

    Page<PostPhotoDto> findAllByMemberId( Long memberId, Pageable pageable);
    List<PostPhotoDto> findByMemberIdPostIdDesc(Long memberId, CursorRequest cursorRequest);
    List<PostPhotoDto> findAllByMemberIdNoKey(Long memberId, CursorRequest cursorRequest);

}
