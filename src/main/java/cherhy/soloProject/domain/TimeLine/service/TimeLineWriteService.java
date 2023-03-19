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

    // TODO : 팔로워가 없으면 그냥 게시글 업로드, 팔로워가 있다면 타임라인에 업로드
    public ResponseEntity uploadTimeLine(List<Member> findMembers, Post post) {
        if (findMembers.size() < 1){
            return ResponseEntity.ok("게시글 업로드 성공");
        }else {
            save(post, findMembers);
            return ResponseEntity.ok("타임라인에 업로드 성공");
        }
    }

    // TODO : 한번에 저장
    private void save(Post post, List<Member> findAllByFollower) {
        List<TimeLine> timeLineMember = new ArrayList<>();

        for (Member member : findAllByFollower) {
            TimeLine timeLine = buildTimeLine(post, member);
            timeLineMember.add(timeLine);
        }

        timeLineRepository.saveAll(timeLineMember);
    }

    // TODO : 타임라인 빌드
    private TimeLine buildTimeLine(Post post, Member member) {
        return TimeLine.builder()
                .post(post)
                .member(member)
                .build();
    }

}
