package cherhy.soloProject.domain.postLike.service;

import cherhy.soloProject.domain.member.entity.Member;
import cherhy.soloProject.domain.post.entity.Post;
import cherhy.soloProject.domain.postLike.entity.PostLike;
import cherhy.soloProject.domain.postLike.repository.jpa.PostLikeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class PostLikeReadService {

    private final PostLikeRepository postLikeRepository;

    public Optional<PostLike> getMemberIdAndPostId(Member findMember, Post findPost) {
        return postLikeRepository.findByMemberIdAndPostId(findMember.getId(), findPost.getId());
    }

}
