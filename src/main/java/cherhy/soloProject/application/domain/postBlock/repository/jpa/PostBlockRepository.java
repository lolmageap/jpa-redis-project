package cherhy.soloProject.application.domain.postBlock.repository.jpa;

import cherhy.soloProject.application.domain.postBlock.entity.PostBlock;
import cherhy.soloProject.application.domain.postBlock.repository.querydsl.PostBlockRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PostBlockRepository extends JpaRepository<PostBlock, Long>, PostBlockRepositoryCustom {
    Optional<List<PostBlock>> findByMemberIdOrderByPostAsc(Long memberId);
    Optional<PostBlock> findByMemberIdAndPostId(Long memberId, Long postId);
}
