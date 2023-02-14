package cherhy.soloProject.application.domain.follow.service;

import cherhy.soloProject.application.domain.follow.dto.request.FollowMemberDto;
import cherhy.soloProject.application.domain.follow.entity.Follow;
import cherhy.soloProject.application.domain.follow.repository.jpa.FollowRepository;
import cherhy.soloProject.application.domain.member.entity.Member;
import cherhy.soloProject.application.domain.member.repository.jpa.MemberRepository;
import cherhy.soloProject.application.exception.MemberNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class FollowWriteService {

    private final FollowRepository followRepository;
    private final MemberRepository memberRepository;

    public String followMember(FollowMemberDto followMemberDto){
        Member findMember = getMember(followMemberDto.MemberId());
        Member followMember = getMember(followMemberDto.FollowerId());
        String res = followingCheck(findMember, followMember);
        return res;
    }

    private Member getMember(Long memberId) {
        return memberRepository.findById(memberId).orElseThrow(MemberNotFoundException::new);
    }

    private static Follow buildFollow(Member findMember, Member followMember) {
        return Follow.builder()
                .follower(findMember)
                .following(followMember)
                .build();
    }

    private String followingCheck(Member findMember, Member followMember) {
        Optional<Follow> follow = followRepository.followCheck(findMember.getId(), followMember.getId());
        if (follow.isEmpty()){
            Follow buildFollow = buildFollow(findMember, followMember);
            followRepository.save(buildFollow);
            return "팔로우";
        }
        followRepository.delete(follow.get());
        return "언팔로우";
    }

}
