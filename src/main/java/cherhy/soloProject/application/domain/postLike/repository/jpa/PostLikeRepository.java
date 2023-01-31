package cherhy.soloProject.application.domain.postLike.repository.jpa;

import cherhy.soloProject.application.domain.postLike.entity.PostLike;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostLikeRepository extends JpaRepository<PostLike, Long> {
}
