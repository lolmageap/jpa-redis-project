package cherhy.soloProject.application.domain.reply.service;

import cherhy.soloProject.application.domain.member.entity.Member;
import cherhy.soloProject.application.domain.post.entity.Post;
import cherhy.soloProject.application.domain.reply.dto.RequestReplyDto;
import cherhy.soloProject.application.domain.reply.entity.Reply;
import cherhy.soloProject.application.domain.reply.repository.jpa.ReplyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.format.DateTimeFormatter;

import static cherhy.soloProject.application.RedisKey.REPLY_MODIFY_DESC;

@Service
@Transactional
@RequiredArgsConstructor
public class ReplyWriteService {
    private final ReplyRepository replyRepository;

    public Reply save(Reply build) {
        return replyRepository.save(build);
    }

    public Boolean addRedis(ZSetOperations zSetOps, Post findPost, Reply save) {
        String key = String.valueOf(save.getId());
        String postRedis = REPLY_MODIFY_DESC + findPost.getId();
        Long format = formatScore(save);
        Boolean add = zSetOps.add(postRedis, key, format);// sorted set에 입력
        return add;
    }

    public Long formatScore(Reply save) {
        String format = DateTimeFormatter.ofPattern("yyyyMMddhhmmssSSSSS").format(save.getLastModifiedDate());
        return Long.parseLong(format);
    }

    public Reply buildReply(RequestReplyDto reply, Member findMember, Post findPost) {
        return Reply.builder()
                .member(findMember)
                .post(findPost)
                .content(reply.content())
                .build();
    }

}
