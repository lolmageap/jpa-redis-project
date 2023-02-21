package cherhy.soloProject.application.domain.postBlock.repository.jpa;

import cherhy.soloProject.application.domain.member.entity.Member;
import cherhy.soloProject.application.domain.postBlock.entity.PostBlock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface PostBlockRepository extends JpaRepository<PostBlock, Long> {

    @Query("select p from PostBlock p where p.member.id = :memberId")
    Optional<List<PostBlock>> findByMember(@Param("memberId") Long memberId);
}
