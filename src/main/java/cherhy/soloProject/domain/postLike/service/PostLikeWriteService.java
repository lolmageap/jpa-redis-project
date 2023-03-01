package cherhy.soloProject.domain.postLike.service;

import cherhy.soloProject.domain.member.entity.Member;
import cherhy.soloProject.domain.post.entity.Post;
import cherhy.soloProject.domain.postLike.entity.PostLike;
import cherhy.soloProject.domain.postLike.repository.jpa.PostLikeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class PostLikeWriteService {
    private final PostLikeRepository postLikeRepository;

    public void likeOrLikeCancel(ValueOperations<String, String> ops, Member findMember, Post findPost, String formatPost, Optional<PostLike> postLike) {
        if (postLike.isEmpty()){
            //좋아요 테이블에 값이 없으면 좋아요 +1
            if (ops.get(formatPost) == null){
                ops.set(formatPost, Long.toString(findPost.getLikeCount()+1));
            }else {
                ops.increment(formatPost);
            }
            buildPostLike(findMember, findPost);
        }else {
            //좋아요 테이블에 값이 있으면 좋아요 -1
            if (ops.get(formatPost) == null){
                ops.set(formatPost, Long.toString(findPost.getLikeCount()-1));
            }else {
                ops.decrement(formatPost);
            }
            postLikeRepository.delete(postLike.get());
        }
    }

    private PostLike buildPostLike(Member findMember, Post findPost) {
        PostLike buildPostLike = PostLike.builder()
                .member(findMember)
                .post(findPost)
                .build();
        PostLike savePostLike = postLikeRepository.save(buildPostLike);
        return savePostLike;
    }

}
