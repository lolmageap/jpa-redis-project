package cherhy.soloProject.application.domain.memberBlock.repository.querydsl;

import cherhy.soloProject.Util.scrollDto.ScrollRequest;
import cherhy.soloProject.application.domain.member.entity.Member;
import cherhy.soloProject.application.domain.memberBlock.dto.response.MemberBlockResponseDto;


import java.util.List;
import java.util.Optional;

public interface MemberBlockRepositoryCustom{
    Optional<List<MemberBlockResponseDto>> getBlockMemberScroll(Member member, ScrollRequest scrollRequest);
}
