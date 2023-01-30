package cherhy.soloProject.application.domain.reply.repository.repositoryImpl;

import cherhy.soloProject.Util.scrollDto.ScrollRequest;
import cherhy.soloProject.application.domain.reply.dto.response.ResponseReplyDto;
import cherhy.soloProject.application.domain.reply.entity.Reply;
import cherhy.soloProject.application.domain.reply.repository.querydsl.ReplyRepositoryCustom;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

import static cherhy.soloProject.application.domain.reply.entity.QReply.*;

@Repository
@RequiredArgsConstructor
public class ReplyRepositoryImpl implements ReplyRepositoryCustom {

    private final EntityManager em;
    private final JPAQueryFactory queryFactory;


//    @Override
//    public List<ResponseReplyDto> findByPostIdScrollNoKey(Long postId, ScrollRequest scrollRequest) {
//        return queryFactory.select(Projections.constructor(ResponseReplyDto.class,
//                        reply.member.id, reply.member.name, reply.post.id, reply.content))
//                .from(reply)
//                .innerJoin(reply.post)
//                .on(reply.post.id.eq(postId))
//                .innerJoin(reply.member)
//                .orderBy(reply.lastModifiedDate.desc())
//                .fetch();
//    }

}
