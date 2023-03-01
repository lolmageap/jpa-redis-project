package cherhy.soloProject.domain.TimeLine.service;

import cherhy.soloProject.domain.TimeLine.entity.TimeLine;
import cherhy.soloProject.domain.TimeLine.repository.jpa.TimeLineRepository;
import cherhy.soloProject.domain.member.entity.Member;
import cherhy.soloProject.domain.member.repository.jpa.MemberRepository;
import cherhy.soloProject.domain.post.entity.Post;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class TimeLineWriteService {
    private final MemberRepository memberRepository;
    private final TimeLineRepository timeLineRepository;

    public ResponseEntity insertTimeLineValue(Member findMember, Post post) {
        List<Member> findAllByFollower = memberRepository.findAllByFollowers(findMember.getId());
        return createTimeLine(post, findAllByFollower);
    }

    private ResponseEntity createTimeLine(Post post, List<Member> findAllByFollower) {
        if (findAllByFollower.size() < 1){
            return ResponseEntity.ok(200);
        }else {
            uploadTimeLine(post, findAllByFollower);
            return ResponseEntity.ok("타임라인에 업로드 성공");
        }
    }

    private void uploadTimeLine(Post post, List<Member> findAllByFollower) {
        List<TimeLine> timeLineMember = new ArrayList<>();

        for (Member member : findAllByFollower) {
            TimeLine timeLine = buildTimeLine(post, member);
            timeLineMember.add(timeLine);
        }

        timeLineRepository.saveAll(timeLineMember);
    }


    private TimeLine buildTimeLine(Post post, Member member) {
        return TimeLine.builder()
                .post(post)
                .member(member)
                .build();
    }

}
