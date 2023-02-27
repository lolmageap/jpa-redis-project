package cherhy.soloProject.application.usecase;

import cherhy.soloProject.Util.scrollDto.ScrollResponse;
import cherhy.soloProject.Util.scrollDto.ScrollRequest;
import cherhy.soloProject.application.domain.follow.service.FollowReadService;
import cherhy.soloProject.application.domain.follow.service.FollowWriteService;
import cherhy.soloProject.application.domain.memberBlock.dto.response.MemberBlockResponseDto;
import cherhy.soloProject.application.domain.member.entity.Member;
import cherhy.soloProject.application.domain.member.service.MemberReadService;
import cherhy.soloProject.application.domain.memberBlock.entity.MemberBlock;
import cherhy.soloProject.application.domain.memberBlock.service.MemberBlockReadService;
import cherhy.soloProject.application.domain.memberBlock.service.MemberBlockWriteService;
import lombok.RequiredArgsConstructor;
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

    public ResponseEntity blockMember(Long memberId, Long blockMemberId) {
        memberReadService.SameUserCheck(memberId, blockMemberId);

        Member member = memberReadService.getMember(memberId);
        Member blockMember = memberReadService.getMember(blockMemberId);
        Optional<MemberBlock> memberBlock = memberBlockReadService.getBlockMember(member, blockMember);

        memberBlock.ifPresentOrElse(mb -> memberBlockWriteService.unblock(mb),
        () -> {
            memberBlockWriteService.buildMemberBlock(member,blockMember);
            followReadService.getFollowExist(member, blockMember).ifPresent(f -> followWriteService.unfollow(f));
        });
        return ResponseEntity.ok(200);
    }

    public ScrollResponse getBlockMember(Long memberId, ScrollRequest scrollRequest) {
        Member member = memberReadService.getMember(memberId);
        List<MemberBlockResponseDto> memberBlocks = memberBlockReadService.getMemberBlocks(member, scrollRequest);
        long nextKey = memberBlockReadService.getNextKey(memberBlocks);
        return new ScrollResponse(scrollRequest.next(nextKey),memberBlocks);
    }


}
