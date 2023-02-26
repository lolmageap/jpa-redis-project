package cherhy.soloProject.application.domain.memberBlock.dto.response;

import cherhy.soloProject.application.domain.member.entity.Member;

public record MemberBlockResponseDto(Member member, Long MemberBlockId) {

}
