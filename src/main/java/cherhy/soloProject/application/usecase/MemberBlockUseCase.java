package cherhy.soloProject.application.usecase;

import cherhy.soloProject.Util.scrollDto.PageScroll;
import cherhy.soloProject.Util.scrollDto.ScrollRequest;
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
public class MemberBlockUseCase {

    private final MemberReadService memberReadService;
    private final MemberBlockReadService memberBlockReadService;
    private final MemberBlockWriteService memberBlockWriteService;

    public ResponseEntity blockMember(Long memberId, Long blockMemberId) {
        Member member = memberReadService.getMember(memberId);
        Member blockMember = memberReadService.getMember(blockMemberId);
        Optional<MemberBlock> memberBlock = memberBlockReadService.getBlockMember(member, blockMember);
        memberBlock.ifPresentOrElse(mb -> memberBlockWriteService.unblock(mb),
                () -> memberBlockWriteService.buildMemberBlock(member,blockMember));
        return ResponseEntity.ok(200);
    }

    public PageScroll getBlockMember(Long memberId, ScrollRequest scrollRequest) {
        Member member = memberReadService.getMember(memberId);
        List<MemberBlock> memberBlocks = memberBlockReadService.getMemberBlocks(member, scrollRequest);
        List<MemberBlockResponseDto> memberResponseDtos = memberBlockReadService.changeMemberResponseDto(memberBlocks);
        long nextKey = memberBlockReadService.getNextKey(memberResponseDtos);
        return new PageScroll(scrollRequest.next(nextKey),memberResponseDtos);
    }

}
