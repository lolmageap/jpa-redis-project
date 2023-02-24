package cherhy.soloProject.application.domain.TimeLine.repository.jpa;

import cherhy.soloProject.application.domain.TimeLine.entity.TimeLine;
import cherhy.soloProject.application.domain.TimeLine.repository.querydsl.TimeLineRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TimeLineRepository extends JpaRepository<TimeLine, Long> , TimeLineRepositoryCustom {

}
