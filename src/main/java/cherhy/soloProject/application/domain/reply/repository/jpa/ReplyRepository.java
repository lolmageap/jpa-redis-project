package cherhy.soloProject.application.domain.reply.repository.jpa;

import cherhy.soloProject.application.domain.reply.entity.Reply;
import cherhy.soloProject.application.domain.reply.repository.querydsl.ReplyRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ReplyRepository extends JpaRepository<Reply, Long> , ReplyRepositoryCustom {
    @Query("select r from Reply r where r.post.id = :postId and r.id in :sortedKeys")
    List<Reply> findByPostIdScroll(@Param("postId") Long postId,@Param("sortedKeys") List<Long> sortedKeys);

}
