package cherhy.soloProject.domain.member.repository.jpa;

import cherhy.soloProject.domain.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findByEmail(String email);
    @Query("select m from Member m where m.userId = :userId")
    Optional<Member> findByUserId(@Param("userId") String userId);

    Optional<List<Member>> findTop3ByNameLikeOrderByIdAsc(String searchMemberName);

//    @Query("select ms.blockMember from Member m inner join m.memberBlocks ms on m.id = ms.member.id where m.id = :memberId")
//    Optional<List<Member>> findAllMemberByBlocked(@Param("memberId") Long memberId);
}
