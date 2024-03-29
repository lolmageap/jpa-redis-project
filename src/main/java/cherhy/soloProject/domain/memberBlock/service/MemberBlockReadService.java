package cherhy.soloProject.domain.memberBlock.service;

import cherhy.soloProject.Util.scrollDto.ScrollRequest;
import cherhy.soloProject.exception.MemberBlockException;
import cherhy.soloProject.domain.memberBlock.dto.response.MemberBlockResponse;
import cherhy.soloProject.domain.member.entity.Member;
import cherhy.soloProject.domain.memberBlock.entity.MemberBlock;
import cherhy.soloProject.domain.memberBlock.repository.jpa.MemberBlockRepository;
import cherhy.soloProject.exception.MemberNotFoundException;
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

    // TODO : 내가 차단한 회원인지 조회, 차단 정보 조회
    public Optional<MemberBlock> getBlockMember(Member myMember, Member blockMember) {
        return memberBlockRepository.getBlockMember(myMember, blockMember);
    }

    public void ifIBlock(Member myMember, Member blockedMember) {
        memberBlockRepository.ifIBlock(myMember, blockedMember).ifPresent(m -> {
            throw new MemberBlockException();
        });;
    }

    // TODO : 무한스크롤 차단 목록 조회
    public List<MemberBlockResponse> getMemberBlocks(Member member, ScrollRequest scrollRequest) {
        return memberBlockRepository.getBlockMemberScroll(member,scrollRequest).orElseThrow(MemberNotFoundException::new);
    }

    // TODO : 무한스크롤 다음 키 조회
    public long getNextKey(List<MemberBlockResponse> memberBlockResponseDtos) {
        return memberBlockResponseDtos.stream().mapToLong(v -> v.MemberBlockId())
                .min().orElse(ScrollRequest.NONE_KEY);
    }

}
