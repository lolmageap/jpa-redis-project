package cherhy.soloProject.application.domain.postLike.service;

import cherhy.soloProject.application.domain.member.entity.Member;
import cherhy.soloProject.application.domain.post.entity.Post;
import cherhy.soloProject.application.domain.postLike.entity.PostLike;
import cherhy.soloProject.application.domain.postLike.repository.jpa.PostLikeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class PostLikeReadService {

    private final PostLikeRepository postLikeRepository;

    public Optional<PostLike> getMemberIdAndPostId(Member findMember, Post findPost) {
        return postLikeRepository.findByMemberIdAndPostId(findMember.getId(), findPost.getId());
    }

}
