package cherhy.soloProject.domain.postBlock.repository.querydsl;


import cherhy.soloProject.Util.scrollDto.ScrollRequest;
import cherhy.soloProject.domain.member.entity.Member;
import cherhy.soloProject.domain.postBlock.entity.PostBlock;

import java.util.List;
import java.util.Optional;

public interface PostBlockRepositoryCustom {

    Optional<List<PostBlock>> findByPostBlockScroll(Member member, ScrollRequest scrollRequest);

}
