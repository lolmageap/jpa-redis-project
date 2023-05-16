package cherhy.soloProject.domain.memberBlock.repository.querydsl;

import cherhy.soloProject.Util.scrollDto.ScrollRequest;
import cherhy.soloProject.domain.member.entity.Member;
import cherhy.soloProject.domain.memberBlock.dto.response.MemberBlockResponse;


import java.util.List;
import java.util.Optional;

public interface MemberBlockRepositoryCustom{
    Optional<List<MemberBlockResponse>> getBlockMemberScroll(Member member, ScrollRequest scrollRequest);
}
