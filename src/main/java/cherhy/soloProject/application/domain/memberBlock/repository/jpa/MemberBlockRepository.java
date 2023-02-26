package cherhy.soloProject.application.domain.memberBlock.repository.jpa;

import cherhy.soloProject.application.domain.member.entity.Member;
import cherhy.soloProject.application.domain.memberBlock.entity.MemberBlock;
import cherhy.soloProject.application.domain.memberBlock.repository.querydsl.MemberBlockRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface MemberBlockRepository extends JpaRepository<MemberBlock, Long>, MemberBlockRepositoryCustom {

    @Query("select mb from MemberBlock mb where mb.member = :member and mb.blockMember = :blockMember")
    Optional<MemberBlock> getBlockMember(@Param("member") Member member, @Param("blockMember") Member blockMember);

}
