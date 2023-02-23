package cherhy.soloProject.application.domain.post.repository.jpa;

import cherhy.soloProject.application.domain.post.entity.Post;
import cherhy.soloProject.application.domain.post.repository.querydsl.PostRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> , PostRepositoryCustom {
    @Query("SELECT distinct p FROM Post p left join fetch p.photos ph where p.member.id = :memberId")
    List<Post> findAllByMemberId(@Param("memberId") Long memberId);

    @Query("SELECT distinct p FROM Post p " +
            "left join p.photos ph " +
            "left join PostBlock pb " +
            "on p.id = pb.post.id and pb.member.id = :myMemberId " +
            "where p.member.id = :memberId and pb.id is null")
    List<Post> findPostByMemberId(@Param("memberId") Long memberId, @Param("myMemberId") Long myMemberId);

}
