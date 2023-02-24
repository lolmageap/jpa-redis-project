package cherhy.soloProject.application.domain.TimeLine.service;

import cherhy.soloProject.Util.scrollDto.PageScroll;
import cherhy.soloProject.Util.scrollDto.ScrollRequest;
import cherhy.soloProject.application.domain.TimeLine.repository.jpa.TimeLineRepository;
import cherhy.soloProject.application.domain.member.entity.Member;
import cherhy.soloProject.application.domain.member.repository.jpa.MemberRepository;
import cherhy.soloProject.application.domain.post.dto.PostPhotoDto;
import cherhy.soloProject.application.domain.post.entity.Post;
import cherhy.soloProject.application.domain.post.repository.jpa.PostRepository;
import cherhy.soloProject.application.exception.MemberNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class TimeLineReadService {
    private final TimeLineRepository timeLineRepository;

    public List<LocalDateTime> getTimeLineNextKey(ScrollRequest scrollRequest, Member member) {
        return timeLineRepository.getNextKey(member, scrollRequest);
    }

    public List<Post> getPostIdByMemberFromTimeLineCursor(Member member, ScrollRequest scrollRequest) {
        return timeLineRepository.findPostIdByMemberFromTimeLine(member, scrollRequest);
    }

    public long getNextKey(ScrollRequest scrollRequest, List<LocalDateTime> covering) {
        return covering.stream().mapToLong(v -> Long.parseLong(v.format(DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSSSS"))))
                .min().orElse(scrollRequest.NONE_KEY);
    }

}
