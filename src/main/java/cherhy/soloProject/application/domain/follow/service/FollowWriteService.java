package cherhy.soloProject.application.domain.follow.service;

import cherhy.soloProject.application.domain.follow.dto.request.FollowMemberDto;
import cherhy.soloProject.application.domain.follow.entity.Follow;
import cherhy.soloProject.application.domain.follow.repository.jpa.FollowRepository;
import cherhy.soloProject.application.domain.member.entity.Member;
import cherhy.soloProject.application.domain.member.repository.jpa.MemberRepository;
import cherhy.soloProject.application.exception.MemberNotFoundException;
import cherhy.soloProject.application.exception.NoFollowerException;
import cherhy.soloProject.application.exception.NotFollowException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class FollowWriteService {

    private final FollowRepository followRepository;
    private final MemberRepository memberRepository;

    public Boolean followMember(FollowMemberDto followMemberDto){
        Follow follow = followValid(followMemberDto);
        followRepository.save(follow);
        return true;
    }

    public Boolean unFollowMember(FollowMemberDto followMemberDto){
        Follow follow = unFollowValid(followMemberDto);
        followRepository.delete(follow);

        getFollower(follow);
        return true;
    }

    private Follow getFollower(Follow follow) {
        return followRepository.findById(follow.getId()).orElseThrow(NoFollowerException::new);
    }

    public Follow unFollowValid(FollowMemberDto followMemberDto){
        Member findMember = getMember(followMemberDto.MemberId());
        Member followMember = getMember(followMemberDto.FollowerId());
        Follow check = unFollowingCheck(findMember, followMember);
        return check;
    }

    private Member getMember(Long memberId) {
        return memberRepository.findById(memberId).orElseThrow(MemberNotFoundException::new);
    }

    private Follow followValid(FollowMemberDto followMemberDto) {
        Member findMember = getMember(followMemberDto.MemberId());
        Member followMember = getMember(followMemberDto.FollowerId());
        followingCheck(findMember, followMember); 
        
        Follow buildFollow = Follow.builder()
                .follower(findMember)
                .following(followMember)
                .build();

        return buildFollow;
    }

    private Follow followingCheck(Member findMember, Member followMember) {
        return followRepository.followCheck(findMember.getId(), followMember.getId())
                .orElseThrow(NotFollowException::new);
    }  // 팔로워가 아니어야함
    
    private Follow unFollowingCheck(Member findMember, Member followMember) {
        return followRepository.followCheck(findMember.getId(), followMember.getId())
                .orElseThrow(NotFollowException::new);
    } // 팔로워가 맞아야함

}
