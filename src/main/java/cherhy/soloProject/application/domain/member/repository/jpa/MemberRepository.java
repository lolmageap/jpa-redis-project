package cherhy.soloProject.application.domain.member.repository.jpa;

import cherhy.soloProject.application.domain.member.entity.Member;
import cherhy.soloProject.application.domain.member.repository.mapper.MemberMapper;
import cherhy.soloProject.application.domain.member.repository.querydsl.MemberRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long>, MemberRepositoryCustom, MemberMapper {
    Optional<Member> findByEmail(String email);
    @Query("select m from Member m where m.user_id = :userId")
    Optional<Member> findByUserId(@Param("userId") String userId);

    @Query("select m from Member m where m.id in ( select f.following.id from Follow f where f.follower.id = :memberId) ")
    List<Member> findAllByFollowers(@Param("memberId") Long memberId);

}
