package cherhy.soloProject.application.domain.follow.repository.querydsl;

import cherhy.soloProject.Util.scrollDto.ScrollRequest;
import cherhy.soloProject.application.domain.follow.dto.response.ResponseFollowMemberDto;
import cherhy.soloProject.application.domain.member.entity.Member;

import java.util.List;

public interface FollowRepositoryCustom {
    List<ResponseFollowMemberDto> findAllByFollowing(Member member, ScrollRequest scrollRequest);
    List<ResponseFollowMemberDto> findAllByFollower(Member member, ScrollRequest scrollRequest);
}
