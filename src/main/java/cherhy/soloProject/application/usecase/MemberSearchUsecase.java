package cherhy.soloProject.application.usecase;

import cherhy.soloProject.domain.member.dto.response.MemberSearchResponseDto;
import cherhy.soloProject.domain.member.entity.Member;
import cherhy.soloProject.domain.member.service.MemberReadService;
import cherhy.soloProject.domain.member.service.MemberWriteService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

@Service
@Transactional
@RequiredArgsConstructor
public class MemberSearchUsecase {
    private final MemberReadService memberReadService;
    private final MemberWriteService memberWriteService;
    private final StringRedisTemplate redisTemplate;


    public List<MemberSearchResponseDto> searchMember(String searchName, Long memberId) {
        ZSetOperations<String, String> ops = redisTemplate.opsForZSet();
        List<Member> findMemberList = memberReadService.getMemberList(searchName);
        List<MemberSearchResponseDto> findMembers = memberReadService.changeMemberSearchResponseDto(findMemberList);
        memberWriteService.insertRedisSearchLog(ops, searchName, memberId);
        memberWriteService.insertRedisSearchRanking(ops, searchName);
        return findMembers;
    }

    public Set<String> getSearchHistoryLog(Long memberId) {
        ZSetOperations<String, String> ops = redisTemplate.opsForZSet();
        return memberReadService.getSearchHistoryLog(ops, memberId);
    }

    public Set<String> getHighScoreSearchWord(String searchName) {
        ZSetOperations<String, String> ops = redisTemplate.opsForZSet();
        return memberReadService.getHighScoreSearchWord(ops, searchName);
    }
}
