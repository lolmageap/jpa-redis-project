package cherhy.soloProject.domain.postLike.repository.querydsl;


import cherhy.soloProject.Util.scrollDto.ScrollRequest;
import cherhy.soloProject.domain.member.entity.Member;
import cherhy.soloProject.domain.post.entity.Post;
import cherhy.soloProject.domain.postLike.dto.response.PostLikeResponse;

import java.util.List;

public interface PostLikeRepositoryCustom {

    List<PostLikeResponse> getPostLike(Member member, ScrollRequest scrollRequest);

}
