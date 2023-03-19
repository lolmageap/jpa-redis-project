package cherhy.soloProject.domain.postLike.service;

import cherhy.soloProject.Util.scrollDto.ScrollRequest;
import cherhy.soloProject.domain.member.entity.Member;
import cherhy.soloProject.domain.post.entity.Post;
import cherhy.soloProject.domain.postLike.dto.response.PostLikeResponse;
import cherhy.soloProject.domain.postLike.entity.PostLike;
import cherhy.soloProject.domain.postLike.repository.jpa.PostLikeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class PostLikeReadService {

    private final PostLikeRepository postLikeRepository;

    // TODO : 무한스크롤 좋아요한 게시물 조회
    public List<PostLikeResponse> getPostLike(Member member, ScrollRequest scrollRequest) {
        return postLikeRepository.getPostLike(member, scrollRequest);
    }

    // TODO : 회원 Id와 게시글 Id로 좋아요한 게시물 조회, 좋아요 했을 경우 좋아요 취소를 위해
    public Optional<PostLike> getMemberIdAndPostId(Member findMember, Post findPost) {
        return postLikeRepository.findByMemberIdAndPostId(findMember.getId(), findPost.getId());
    }

    // TODO : 무한스크롤 다음 키 조회
    public long getNextKey(List<PostLikeResponse> findPosts) {
        return findPosts.stream().mapToLong(v -> v.postLikeId())
                .min().orElse(ScrollRequest.NONE_KEY);
    }

    // TODO : Post로 변환
    public List<Post> changePost(List<PostLikeResponse> posts) {
        List<Post> result = posts.stream().map(p -> p.post()).collect(Collectors.toList());
        return result;
    }

}
