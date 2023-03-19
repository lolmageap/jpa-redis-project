package cherhy.soloProject.domain.follow.repository.jpa;


import cherhy.soloProject.domain.follow.entity.Follow;
import cherhy.soloProject.domain.follow.repository.querydsl.FollowRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface FollowRepository extends JpaRepository<Follow, Long>, FollowRepositoryCustom {
    @Query("select f from Follow f where f.follower.id = :memberId and f.following.id = :followerId")
    Optional<Follow> followCheck(@Param("memberId") Long memberId, @Param("followerId") Long followerId);

}
