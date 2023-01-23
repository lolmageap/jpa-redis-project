package cherhy.soloProject.application.repository.querydsl;

import cherhy.soloProject.application.domain.dto.MemberDto;
import cherhy.soloProject.application.domain.entity.Member;


public interface MemberRepositoryCustom {
    Member createNewMember(MemberDto dto);
    Member findByMemberName(Long id);
}
