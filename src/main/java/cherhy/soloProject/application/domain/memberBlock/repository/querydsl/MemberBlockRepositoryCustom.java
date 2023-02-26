package cherhy.soloProject.application.domain.memberBlock.repository.querydsl;

import cherhy.soloProject.Util.scrollDto.ScrollRequest;
import cherhy.soloProject.application.domain.member.entity.Member;
import cherhy.soloProject.application.domain.memberBlock.entity.MemberBlock;

import java.util.List;
import java.util.Optional;

public interface MemberBlockRepositoryCustom{
    Optional<List<MemberBlock>> getBlockMemberScroll(Member member, ScrollRequest scrollRequest);
}
