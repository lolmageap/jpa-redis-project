package cherhy.soloProject.application.domain.post.repository.jpa;

import cherhy.soloProject.application.domain.post.entity.TimeLine;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TimeLineRepository extends JpaRepository<TimeLine, Long> {

}
