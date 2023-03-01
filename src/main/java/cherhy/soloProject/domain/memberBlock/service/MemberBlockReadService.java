package cherhy.soloProject.domain.memberBlock.service;

import cherhy.soloProject.Util.scrollDto.ScrollRequest;
import cherhy.soloProject.domain.memberBlock.dto.response.MemberBlockResponseDto;
import cherhy.soloProject.domain.member.entity.Member;
import cherhy.soloProject.domain.memberBlock.entity.MemberBlock;
import cherhy.soloProject.domain.memberBlock.repository.jpa.MemberBlockRepository;
import cherhy.soloProject.application.exception.MemberNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberBlockReadService {

    private final MemberBlockRepository memberBlockRepository;

    public Optional<MemberBlock> getBlockMember(Member myMember, Member blockMember) {
        return memberBlockRepository.getBlockMember(myMember, blockMember);
    }
    public Optional<MemberBlock> ifIBlock(Member myMember, Member blockedMember) {
        Optional<MemberBlock> memberBlock = memberBlockRepository.ifIBlock(myMember, blockedMember);
        return memberBlock;
    }

    public List<MemberBlockResponseDto> getMemberBlocks(Member member, ScrollRequest scrollRequest) {
        return memberBlockRepository.getBlockMemberScroll(member,scrollRequest).orElseThrow(MemberNotFoundException::new);
    }

    public long getNextKey(List<MemberBlockResponseDto> memberBlockResponseDtos) {
        return memberBlockResponseDtos.stream().mapToLong(v -> v.MemberBlockId())
                .min().orElse(ScrollRequest.NONE_KEY);
    }

}
