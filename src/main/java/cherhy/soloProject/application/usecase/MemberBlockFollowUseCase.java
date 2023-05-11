package cherhy.soloProject.application.usecase;

import cherhy.soloProject.Util.scrollDto.ScrollResponse;
import cherhy.soloProject.Util.scrollDto.ScrollRequest;
import cherhy.soloProject.domain.follow.service.FollowReadService;
import cherhy.soloProject.domain.follow.service.FollowWriteService;
import cherhy.soloProject.domain.memberBlock.dto.response.MemberBlockResponseDto;
import cherhy.soloProject.domain.member.entity.Member;
import cherhy.soloProject.domain.member.service.MemberReadService;
import cherhy.soloProject.domain.memberBlock.entity.MemberBlock;
import cherhy.soloProject.domain.memberBlock.service.MemberBlockReadService;
import cherhy.soloProject.domain.memberBlock.service.MemberBlockWriteService;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;


@Service
@Transactional
@RequiredArgsConstructor
public class MemberBlockFollowUseCase {

    private final MemberReadService memberReadService;
    private final MemberBlockReadService memberBlockReadService;
    private final MemberBlockWriteService memberBlockWriteService;
    private final FollowReadService followReadService;
    private final FollowWriteService followWriteService;

    public ResponseEntity blockMember(Member member, Long blockMemberId) {
        memberReadService.SameUserCheck(member.getId(), blockMemberId);

        Member blockMember = memberReadService.getMember(blockMemberId);
        Optional<MemberBlock> memberBlock = memberBlockReadService.getBlockMember(member, blockMember);

        memberBlock.ifPresentOrElse(mb -> memberBlockWriteService.unblock(mb),
        () -> {
            MemberBlock memberToBlock = MemberBlock.of(member, blockMember);
            memberBlockWriteService.block(memberToBlock);
            followReadService.getFollowExist(member, blockMember).ifPresent(f -> followWriteService.unfollow(f));
        });
        return ResponseEntity.ok("성공");
    }

    @Cacheable(cacheNames = "memberBlock", key = "#memberId.toString() + '_' + ( #scrollRequest.key() != null ? #scrollRequest.key() : '' )"
            , cacheManager = "cacheManager")
    public ScrollResponse getBlockMember(Member member, ScrollRequest scrollRequest) {
        List<MemberBlockResponseDto> memberBlocks = memberBlockReadService.getMemberBlocks(member, scrollRequest);
        long nextKey = memberBlockReadService.getNextKey(memberBlocks);
        return new ScrollResponse(scrollRequest.next(nextKey),memberBlocks);
    }

//    public List<Long> getBlockMember(Long memberId) {
//        List<Member> blockMember = memberReadService.getBlockMember(memberId);
//        return blockMember.stream().map(m -> m.getId()).collect(Collectors.toList());
//    }


}
