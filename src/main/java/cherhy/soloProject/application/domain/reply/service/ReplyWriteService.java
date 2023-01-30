package cherhy.soloProject.application.domain.reply.service;

import cherhy.soloProject.application.domain.member.entity.Member;
import cherhy.soloProject.application.domain.member.repository.jpa.MemberRepository;
import cherhy.soloProject.application.domain.post.entity.Post;
import cherhy.soloProject.application.domain.post.repository.jpa.PostRepository;
import cherhy.soloProject.application.domain.reply.dto.RequestReplyDto;
import cherhy.soloProject.application.domain.reply.entity.Reply;
import cherhy.soloProject.application.domain.reply.repository.jpa.ReplyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.format.DateTimeFormatter;

@Service
@Transactional
@RequiredArgsConstructor
public class ReplyWriteService {
    private final ReplyRepository replyRepository;
    private final MemberRepository memberRepository;
    private final PostRepository postRepository;
    private final StringRedisTemplate stringRedisTemplate;

    public Boolean setReply(RequestReplyDto reply){
        ZSetOperations zSetOps = stringRedisTemplate.opsForZSet(); //Sorted Set 선언
        Member findMember = getMember(reply);
        Post findPost = getPost(reply);
        Reply build = buildReply(reply, findMember, findPost);
        Reply save = replyRepository.save(build);
        return addRedis(zSetOps, findPost, save);
    }

    private Boolean addRedis(ZSetOperations zSetOps, Post findPost, Reply save) {
        String key = String.valueOf(save.getId());
        String postRedis = "postReplyOrderByLastModifyDate" + findPost.getId();
        Long format = formatScore(save);
        Boolean add = zSetOps.add(postRedis, key, format);// sorted set에 입력
        return add;
    }

    private Long formatScore(Reply save) {
        String format = DateTimeFormatter.ofPattern("yyyyMMddhhmmssSSSSS").format(save.getLastModifiedDate());
        return Long.parseLong(format);
    }

    private Reply buildReply(RequestReplyDto reply, Member findMember, Post findPost) {
        return Reply.builder()
                .member(findMember)
                .post(findPost)
                .content(reply.content())
                .build();
    }

    private Member getMember(RequestReplyDto reply) {
        return memberRepository.findById(reply.memberId())
                .orElseThrow(() -> new NullPointerException("회원이 존재하지 않습니다"));
    }

    private Post getPost(RequestReplyDto reply) {
        return postRepository.findById(reply.postId())
                .orElseThrow(() -> new NullPointerException("게시물이 존재하지 않습니다"));
    }

}
