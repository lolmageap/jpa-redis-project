package cherhy.soloProject.domain.follow.repository.mapper;

import cherhy.soloProject.domain.follow.dto.request.FollowQueryDto;
import cherhy.soloProject.domain.follow.dto.response.ResponseFollowMemberDto;

import java.util.List;

public interface FollowMapper {
    // 멤버 아이디 -> 스크롤 키값
    List<ResponseFollowMemberDto> findAllByFollowingCoveringIndexNoKey(FollowQueryDto followQueryDto);
    List<ResponseFollowMemberDto> findAllByFollowingCoveringIndex(FollowQueryDto followQueryDto);
}
