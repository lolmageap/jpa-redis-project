package cherhy.soloProject.domain.follow.repository.mapper;

import cherhy.soloProject.domain.follow.dto.request.FollowQueryRequest;
import cherhy.soloProject.domain.follow.dto.response.FollowMemberResponse;

import java.util.List;

public interface FollowMapper {
    // 멤버 아이디 -> 스크롤 키값
    List<FollowMemberResponse> findAllByFollowingCoveringIndexNoKey(FollowQueryRequest followQueryDto);
    List<FollowMemberResponse> findAllByFollowingCoveringIndex(FollowQueryRequest followQueryDto);
}
