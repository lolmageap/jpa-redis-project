package cherhy.soloProject.application.repository.querydsl;

import cherhy.soloProject.application.domain.dto.PostPhotoDto;
import cherhy.soloProject.application.domain.entity.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PostRepositoryCustom {

    Page<PostPhotoDto> findAllByMemberId(@Param("memberId") Long memberId, Pageable pageable);

}
