package cherhy.soloProject.application.domain.memberBlock.service;

import cherhy.soloProject.Util.scrollDto.ScrollRequest;
import cherhy.soloProject.application.domain.memberBlock.dto.response.MemberBlockResponseDto;
import cherhy.soloProject.application.domain.member.entity.Member;
import cherhy.soloProject.application.domain.memberBlock.entity.MemberBlock;
import cherhy.soloProject.application.domain.memberBlock.repository.jpa.MemberBlockRepository;
import cherhy.soloProject.application.exception.MemberNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberBlockReadService {

    private final MemberBlockRepository memberBlockRepository;

    public Optional<MemberBlock> getBlockMember(Member member, Member blockMember) {
        return memberBlockRepository.getBlockMember(member, blockMember);
    }

    public List<MemberBlockResponseDto> getMemberBlocks(Member member, ScrollRequest scrollRequest) {
        return memberBlockRepository.getBlockMemberScroll(member,scrollRequest).orElseThrow(MemberNotFoundException::new);
    }

//    public List<MemberBlockResponseDto> changeMemberResponseDto(List<MemberBlock> memberBlocks) {
//        return memberBlocks.stream().map(m -> new MemberBlockResponseDto(m.getBlockMember(), m.getId())).collect(Collectors.toList());
//    }

    public long getNextKey(List<MemberBlockResponseDto> memberBlockResponseDtos) {
        return memberBlockResponseDtos.stream().mapToLong(v -> v.MemberBlockId())
                .min().orElse(ScrollRequest.NONE_KEY);
    }

}
