package cherhy.soloProject.domain.TimeLine.service;

import cherhy.soloProject.Util.scrollDto.ScrollRequest;
import cherhy.soloProject.domain.TimeLine.repository.jpa.TimeLineRepository;
import cherhy.soloProject.domain.member.entity.Member;
import cherhy.soloProject.domain.post.entity.Post;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class TimeLineReadService {
    private final TimeLineRepository timeLineRepository;

    public List<Long> getTimeLineNextKey(ScrollRequest scrollRequest, Member member) {
        return timeLineRepository.getNextKey(member, scrollRequest);
    }

    public List<Post> getPostIdByMemberFromTimeLineCursor(Member member, ScrollRequest scrollRequest) {
        return timeLineRepository.findPostIdByMemberFromTimeLine(member, scrollRequest);
    }

    public long getNextKey(ScrollRequest scrollRequest, List<Long> covering) {
        return covering.stream().mapToLong(v -> v).min().orElse(scrollRequest.NONE_KEY);
    }

    public long getNextKeySortModifyDate(ScrollRequest scrollRequest, List<LocalDateTime> covering) {
        return covering.stream().mapToLong(v -> Long.parseLong(v.format(DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSSSS"))))
                .min().orElse(scrollRequest.NONE_KEY);
    }

    public void getTimeLinePost(Member member, ScrollRequest scrollRequest) {
    }
}
