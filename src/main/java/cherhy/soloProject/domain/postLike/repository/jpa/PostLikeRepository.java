package cherhy.soloProject.domain.postLike.repository.jpa;

import cherhy.soloProject.domain.postLike.entity.PostLike;
import cherhy.soloProject.domain.postLike.repository.querydsl.PostLikeRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface PostLikeRepository extends JpaRepository<PostLike, Long>, PostLikeRepositoryCustom {

    @Query("select pl from PostLike pl where pl.member.id = :member and pl.post.id = :post")
    Optional<PostLike> findByMemberIdAndPostId(@Param("member") Long memberId, @Param("post") Long postId);

}
