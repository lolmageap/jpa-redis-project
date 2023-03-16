package cherhy.soloProject.domain.TimeLine.dto;

import cherhy.soloProject.domain.post.entity.Post;

public record TimeLineResponse(Long timeLineId, Post post) {
}
