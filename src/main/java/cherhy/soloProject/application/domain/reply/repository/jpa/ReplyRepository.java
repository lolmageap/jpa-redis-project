package cherhy.soloProject.application.domain.reply.repository.jpa;

import cherhy.soloProject.application.domain.reply.entity.Reply;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReplyRepository extends JpaRepository<Reply, Long> {

}
