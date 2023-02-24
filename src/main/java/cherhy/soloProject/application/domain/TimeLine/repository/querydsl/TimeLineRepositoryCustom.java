package cherhy.soloProject.application.domain.TimeLine.repository.querydsl;

import cherhy.soloProject.Util.scrollDto.ScrollRequest;
import cherhy.soloProject.application.domain.member.entity.Member;
import cherhy.soloProject.application.domain.post.entity.Post;


import java.time.LocalDateTime;
import java.util.List;

public interface TimeLineRepositoryCustom {

    List<Post> findPostIdByMemberFromTimeLine(Member member, ScrollRequest scrollRequest);
    List<LocalDateTime> getNextKey(Member member, ScrollRequest scrollRequest);
}
