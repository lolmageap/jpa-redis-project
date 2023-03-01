package cherhy.soloProject.domain.memberBlock.repository.jpa;

import cherhy.soloProject.domain.member.entity.Member;
import cherhy.soloProject.domain.memberBlock.entity.MemberBlock;
import cherhy.soloProject.domain.memberBlock.repository.querydsl.MemberBlockRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface MemberBlockRepository extends JpaRepository<MemberBlock, Long>, MemberBlockRepositoryCustom {

    @Query("select mb from MemberBlock mb where mb.member = :myMember and mb.blockMember = :blockMember")
    Optional<MemberBlock> getBlockMember(@Param("myMember") Member myMember, @Param("blockMember") Member blockMember);
    @Query("select mb from MemberBlock mb where mb.member = :blockedMember and mb.blockMember = :myMember")
    Optional<MemberBlock> ifIBlock(@Param("myMember") Member myMember, @Param("blockedMember") Member blockedMember);

}
