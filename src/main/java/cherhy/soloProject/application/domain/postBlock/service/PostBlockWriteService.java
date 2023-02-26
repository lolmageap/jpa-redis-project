package cherhy.soloProject.application.domain.postBlock.service;

import cherhy.soloProject.application.domain.member.entity.Member;
import cherhy.soloProject.application.domain.post.entity.Post;
import cherhy.soloProject.application.domain.postBlock.entity.PostBlock;
import cherhy.soloProject.application.domain.postBlock.repository.jpa.PostBlockRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class PostBlockWriteService {
    private final PostBlockRepository postBlockRepository;

    public Optional<PostBlock> getPostBlockByMemberIdAndPostId(Long memberId, Long postId) {
        return postBlockRepository.findByMemberIdAndPostId(memberId, postId);
    }

    public void block(Member member, Post post) {
        PostBlock buildPostBlock = PostBlock.builder()
                .member(member)
                .post(post)
                .build();

        postBlockRepository.save(buildPostBlock);
    }

    public void unblock(PostBlock postBlock) {
        postBlockRepository.delete(postBlock);
    }
}
