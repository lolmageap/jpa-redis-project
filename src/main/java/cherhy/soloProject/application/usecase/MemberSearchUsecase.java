package cherhy.soloProject.application.usecase;

import cherhy.soloProject.domain.member.dto.response.MemberSearchResponse;
import cherhy.soloProject.domain.member.entity.Member;
import cherhy.soloProject.domain.member.service.MemberReadService;
import cherhy.soloProject.domain.member.service.MemberWriteService;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
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

    @Cacheable(cacheNames = "SearchMember", key = "#memberId + #searchName", cacheManager = "cacheManager")
    public List<MemberSearchResponse> getSearchMember(String searchName, Member member) {
        ZSetOperations<String, String> ops = redisTemplate.opsForZSet();
        List<Member> findMemberList = memberReadService.getMembers(searchName);
        List<MemberSearchResponse> findMembers = MemberSearchResponse.from(findMemberList);
        memberWriteService.insertRedisSearchLog(ops, searchName, member.getId());
        memberWriteService.insertRedisSearchRanking(ops, searchName);
        return findMembers;
    }

    public Set<String> getSearchHistoryLog(Member member) {
        ZSetOperations<String, String> ops = redisTemplate.opsForZSet();
        return memberReadService.getSearchHistoryLog(ops, member.getId());
    }

    public Set<String> getHighScoreSearchWord(String searchName) {
        ZSetOperations<String, String> ops = redisTemplate.opsForZSet();
        return memberReadService.getHighScoreSearchWord(ops, searchName);
    }
}
