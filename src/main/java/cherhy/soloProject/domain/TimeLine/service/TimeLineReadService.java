package cherhy.soloProject.domain.TimeLine.service;

import cherhy.soloProject.Util.scrollDto.ScrollRequest;
import cherhy.soloProject.domain.TimeLine.repository.jpa.TimeLineRepository;
import cherhy.soloProject.domain.member.entity.Member;
import cherhy.soloProject.domain.post.entity.Post;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class TimeLineReadService {
    private final TimeLineRepository timeLineRepository;

    // TODO : 타임라인에서 게시글 조회
    public List<Post> getTimeLine(Member member, ScrollRequest scrollRequest) {
        return timeLineRepository.getTimeLine(member, scrollRequest);
    }

    // TODO : 타임라인 키 값 조회
    public List<Long> getTimeLineNextKey(ScrollRequest scrollRequest, Member member) {
        return timeLineRepository.getNextKey(member, scrollRequest);
    }

    // TODO : 조회된 키 값 중에서 가장 큰 키값 return, 다음 키값으로 사용하기 위해
    public long getNextKey(ScrollRequest scrollRequest, List<Long> covering) {
        return covering.stream().mapToLong(v -> v).min().orElse(scrollRequest.NONE_KEY);
    }

}
