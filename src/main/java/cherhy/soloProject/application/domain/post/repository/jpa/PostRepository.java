package cherhy.soloProject.application.domain.post.repository.jpa;

import cherhy.soloProject.application.domain.post.entity.Post;
import cherhy.soloProject.application.domain.post.repository.querydsl.PostRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> , PostRepositoryCustom {

    @Query("SELECT distinct p FROM Post p left join fetch p.photos ph  where p.member.id = :memberId")
    List<Post> findAllByMemberId(@Param("memberId") Long memberId);

}
