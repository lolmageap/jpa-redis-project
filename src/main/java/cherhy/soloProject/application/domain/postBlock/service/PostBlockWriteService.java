package cherhy.soloProject.application.domain.postBlock.service;

import cherhy.soloProject.application.domain.member.entity.Member;
import cherhy.soloProject.application.domain.member.repository.jpa.MemberRepository;
import cherhy.soloProject.application.domain.post.entity.Post;
import cherhy.soloProject.application.domain.post.repository.jpa.PostRepository;
import cherhy.soloProject.application.domain.postBlock.dto.request.PostBlockRequestDto;
import cherhy.soloProject.application.domain.postBlock.entity.PostBlock;
import cherhy.soloProject.application.domain.postBlock.repository.jpa.PostBlockRepository;
import cherhy.soloProject.application.exception.MemberNotFoundException;
import cherhy.soloProject.application.exception.PostNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class PostBlockWriteService {
    private final MemberRepository memberRepository;
    private final PostRepository postRepository;
    private final PostBlockRepository postBlockRepository;
    public String blockPost(PostBlockRequestDto postBlockRequestDto) {
        Member member = getMember(postBlockRequestDto);
        Post post = getPost(postBlockRequestDto);
        String message = save(member, post);
        return message;
    }

    private String save(Member member, Post post) {
        PostBlock buildPostBlock = PostBlock.builder()
                .member(member)
                .post(post)
                .build();
        postBlockRepository.save(buildPostBlock);
        return "차단 성공";
    }

    private Post getPost(PostBlockRequestDto postBlockRequestDto) {
        return postRepository.findById(postBlockRequestDto.PostId()).orElseThrow(PostNotFoundException::new);
    }

    private Member getMember(PostBlockRequestDto postBlockRequestDto) {
        return memberRepository.findById(postBlockRequestDto.memberId()).orElseThrow(MemberNotFoundException::new);
    }
}
