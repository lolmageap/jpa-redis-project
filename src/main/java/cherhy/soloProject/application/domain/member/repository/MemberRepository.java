package cherhy.soloProject.application.domain.member.repository;

import cherhy.soloProject.application.domain.member.entity.Member;
import cherhy.soloProject.application.domain.member.repository.mapper.MemberMapper;
import cherhy.soloProject.application.domain.member.repository.querydsl.MemberRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long>, MemberRepositoryCustom, MemberMapper {
    Member findByEmail(String email);

}
