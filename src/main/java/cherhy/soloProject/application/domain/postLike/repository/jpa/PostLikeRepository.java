package cherhy.soloProject.application.domain.postLike.repository.jpa;

import cherhy.soloProject.application.domain.member.entity.Member;
import cherhy.soloProject.application.domain.post.entity.Post;
import cherhy.soloProject.application.domain.postLike.entity.PostLike;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface PostLikeRepository extends JpaRepository<PostLike, Long> {
    @Query("select pl from PostLike pl where pl.member.id = :member and pl.post.id = :post")
    Optional<PostLike> findByMemberIdAndPostId(@Param("member") Long memberId, @Param("post") Long postId);
}
