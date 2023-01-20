package cherhy.soloProject.application.domain.member.repository.querydsl;

import cherhy.soloProject.application.domain.member.dto.MemberDto;
import cherhy.soloProject.application.domain.member.entity.Member;


public interface MemberRepositoryCustom {
    Member createNewMember(MemberDto dto);
    Member findByMemberName(Long id);
}
