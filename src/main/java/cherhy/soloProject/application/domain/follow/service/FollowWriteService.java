package cherhy.soloProject.application.domain.follow.service;

import cherhy.soloProject.application.domain.follow.dto.FollowMemberDto;
import cherhy.soloProject.application.domain.follow.entity.Follow;
import cherhy.soloProject.application.domain.follow.repository.jpa.FollowRepository;
import cherhy.soloProject.application.domain.member.entity.Member;
import cherhy.soloProject.application.domain.member.repository.jpa.MemberRepository;
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

    public Boolean followMember(FollowMemberDto followMemberDto){
        Follow follow = followValid(followMemberDto);
        Follow save = followRepository.save(follow);

        if (save != null) return true;

        return false;
    }

    public Boolean unFollowMember(FollowMemberDto followMemberDto){
        Follow follow = unFollowValid(followMemberDto);
        followRepository.delete(follow);

        Optional<Follow> byId = followRepository.findById(follow.getId());

        if (byId.isEmpty()){
            return true;
        }
        return false;
    }

    public Follow unFollowValid(FollowMemberDto followMemberDto){
        var findMember = memberRepository.findById(followMemberDto.MemberId());
        var followMember = memberRepository.findById(followMemberDto.FollowerId());

        Follow check = followingCheck(findMember, followMember);

        if (check == null){
            throw new IllegalArgumentException("팔로잉이 되어있지않습니다");
        }

        return check;
    }

    private Follow followValid(FollowMemberDto followMemberDto) {
        var findMember = memberRepository.findById(followMemberDto.MemberId());
        var followMember = memberRepository.findById(followMemberDto.FollowerId());

        Follow check = followingCheck(findMember, followMember);

        if (check != null){
            throw new IllegalArgumentException("이미 팔로잉이 되어있습니다");
        }

        Follow buildFollow = Follow.builder()
                .follower(findMember.get())
                .following(followMember.get())
                .build();

        return buildFollow;
    }

    private Follow followingCheck(Optional<Member> findMember, Optional<Member> followMember) {
        if (findMember.isEmpty() || followMember.isEmpty()){
            throw new IllegalArgumentException("존재하지 않는 유저입니다");
        }

        Follow followCheck = followRepository.followCheck(findMember.get().getId(), followMember.get().getId());

        return followCheck;
    }




}
