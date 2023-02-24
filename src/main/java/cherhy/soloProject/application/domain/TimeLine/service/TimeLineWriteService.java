package cherhy.soloProject.application.domain.TimeLine.service;

import cherhy.soloProject.application.domain.TimeLine.repository.jpa.TimeLineRepository;
import cherhy.soloProject.application.domain.member.repository.jpa.MemberRepository;
import cherhy.soloProject.application.domain.post.repository.jpa.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class TimeLineWriteService {
    private final PostRepository postRepository;
    private final MemberRepository memberRepository;
    private final TimeLineRepository timeLineRepository;
}
