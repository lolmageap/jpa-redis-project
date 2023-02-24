package cherhy.soloProject.application.domain.postLike.service;

import cherhy.soloProject.application.domain.member.entity.Member;
import cherhy.soloProject.application.domain.member.repository.jpa.MemberRepository;
import cherhy.soloProject.application.domain.post.entity.Post;
import cherhy.soloProject.application.domain.post.repository.jpa.PostRepository;
import cherhy.soloProject.application.domain.postLike.dto.PostLikeDto;
import cherhy.soloProject.application.domain.postLike.entity.PostLike;
import cherhy.soloProject.application.domain.postLike.repository.jpa.PostLikeRepository;
import cherhy.soloProject.application.exception.MemberNotFoundException;
import cherhy.soloProject.application.exception.PostNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.*;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class PostLikeWriteService {
    private final PostLikeRepository postLikeRepository;

    public String likeOrLikeCancel(ValueOperations<String, String> ops, Member findMember, Post findPost, String formatPost, Optional<PostLike> postLike) {
        if (postLike.isEmpty()){
            //좋아요 테이블에 값이 없으면 좋아요 +1
            if (ops.get(formatPost) == null){
                ops.set(formatPost, Long.toString(findPost.getLikeCount()+1));
            }else {
                ops.increment(formatPost);
            }
            buildPostLike(findMember, findPost);
            return "좋아요";
        }else {
            //좋아요 테이블에 값이 있으면 좋아요 -1
            if (ops.get(formatPost) == null){
                ops.set(formatPost, Long.toString(findPost.getLikeCount()-1));
            }else {
                ops.decrement(formatPost);
            }
            postLikeRepository.delete(postLike.get());
            return "좋아요 취소";
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
