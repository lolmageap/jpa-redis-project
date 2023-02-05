package cherhy.soloProject.application.domain.post.repository.jpa;

import cherhy.soloProject.application.domain.member.entity.Member;
import cherhy.soloProject.application.domain.post.entity.TimeLine;
import cherhy.soloProject.application.domain.post.repository.querydsl.TimeLineRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TimeLineRepository extends JpaRepository<TimeLine, Long> , TimeLineRepositoryCustom {

}
