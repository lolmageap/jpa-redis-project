package cherhy.soloProject.application.domain.follow.repository.jpa;

import cherhy.soloProject.application.domain.follow.entity.Follow;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface FollowRepository extends JpaRepository<Follow, Long> {
    @Query("select f from Follow f where f.follower.id = :memberId and f.following.id = :followerId")
    Follow followCheck(@Param("memberId") Long memberId, @Param("followerId") Long followerId);
}
