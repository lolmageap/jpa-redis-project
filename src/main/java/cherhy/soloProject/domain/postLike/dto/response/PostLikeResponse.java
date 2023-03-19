package cherhy.soloProject.domain.postLike.dto.response;

import cherhy.soloProject.domain.post.entity.Post;

public record PostLikeResponse(Long postLikeId, Post post) {
}
