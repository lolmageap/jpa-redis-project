package cherhy.soloProject.domain.TimeLine.repository.querydsl;

import cherhy.soloProject.Util.scrollDto.ScrollRequest;
import cherhy.soloProject.domain.member.entity.Member;
import cherhy.soloProject.domain.post.entity.Post;


import java.util.List;

public interface TimeLineRepositoryCustom {

    List<Post> getTimeLine(Member member, ScrollRequest scrollRequest); // id순
//    List<Post> findPostIdByMemberFromTimeLineSortModify(Member member, ScrollRequest scrollRequest); // 날짜순
    List<Long> getNextKey(Member member, ScrollRequest scrollRequest);
}
