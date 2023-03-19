package cherhy.soloProject.domain.reply.service;

import cherhy.soloProject.domain.member.entity.Member;
import cherhy.soloProject.domain.post.entity.Post;
import cherhy.soloProject.domain.reply.dto.RequestReplyDto;
import cherhy.soloProject.domain.reply.entity.Reply;
import cherhy.soloProject.domain.reply.repository.jpa.ReplyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.format.DateTimeFormatter;

import static cherhy.soloProject.application.key.RedisKey.REPLY_MODIFY_DESC;

@Service
@Transactional
@RequiredArgsConstructor
public class ReplyWriteService {
    private final ReplyRepository replyRepository;

    // TODO : 댓글 저장
    public Reply save(Reply build) {
        return replyRepository.save(build);
    }

    // TODO : 레디스에 저장한 댓글 id와 score = "날짜" 입력, 날짜 오름차순 내림차순으로 조회하기 위해
    public Boolean addRedis(ZSetOperations zSetOps, Post findPost, Reply save) {
        String key = String.valueOf(save.getId());
        String postRedis = REPLY_MODIFY_DESC.name() + findPost.getId();
        Long format = formatScore(save);
        Boolean add = zSetOps.add(postRedis, key, format);// sorted set에 입력
        return add;
    }

    // TODO : score = "날짜" 포맷
    public Long formatScore(Reply save) {
        String format = DateTimeFormatter.ofPattern("yyyyMMddhhmmssSSSSS").format(save.getLastModifiedDate());
        return Long.parseLong(format);
    }

    // TODO : 댓글 빌드
    public Reply buildReply(RequestReplyDto reply, Member findMember, Post findPost) {
        return Reply.builder()
                .member(findMember)
                .post(findPost)
                .content(reply.content())
                .build();
    }

}
