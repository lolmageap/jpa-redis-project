package cherhy.soloProject.domain.follow.repository.querydsl;

import cherhy.soloProject.Util.scrollDto.ScrollRequest;
import cherhy.soloProject.domain.follow.dto.response.FollowMemberResponse;
import cherhy.soloProject.domain.member.entity.Member;

import java.util.List;

public interface FollowRepositoryCustom {
    List<FollowMemberResponse> findAllByFollowing(Member member, ScrollRequest scrollRequest);
    List<FollowMemberResponse> findAllByFollower(Member member, ScrollRequest scrollRequest);
}
