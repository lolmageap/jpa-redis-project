package cherhy.soloProject.domain.member.repository;

import cherhy.soloProject.domain.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member , Long> {

}
