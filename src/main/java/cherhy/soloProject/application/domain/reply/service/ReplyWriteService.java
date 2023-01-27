package cherhy.soloProject.application.domain.reply.service;

import cherhy.soloProject.application.domain.member.entity.Member;
import cherhy.soloProject.application.domain.member.repository.jpa.MemberRepository;
import cherhy.soloProject.application.domain.post.entity.Post;
import cherhy.soloProject.application.domain.post.repository.jpa.PostRepository;
import cherhy.soloProject.application.domain.reply.dto.RequestReplyDto;
import cherhy.soloProject.application.domain.reply.entity.Reply;
import cherhy.soloProject.application.domain.reply.repository.jpa.ReplyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class ReplyWriteService {
    private final ReplyRepository replyRepository;
    private final MemberRepository memberRepository;
    private final PostRepository postRepository;

    public void setReply(RequestReplyDto reply){
        Member findMember = getMember(reply);
        Post findPost = getPost(reply);
        Reply buildReply = buildReply(reply, findMember, findPost);
        replyRepository.save(buildReply);
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
