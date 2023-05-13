package cherhy.soloProject.domain.post.service;


import cherhy.soloProject.domain.member.entity.Member;
import cherhy.soloProject.domain.photo.repository.jpa.PhotoRepository;
import cherhy.soloProject.domain.post.dto.request.PostRequestDto;
import cherhy.soloProject.domain.post.entity.Post;
import cherhy.soloProject.domain.post.repository.jpa.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@Transactional
@RequiredArgsConstructor
public class PostWriteService {

    private final PostRepository postRepository;
    private final PhotoRepository photoRepository;

    // TODO : 수정 및 null validation check
    public Post modify(PostRequestDto postRequestDto, Post findPost) {
        if (!postRequestDto.content().isEmpty()){
            findPost.changeContent(postRequestDto.content());
        }
        if (!postRequestDto.title().isEmpty()){
            findPost.changeTitle(postRequestDto.title());
        }
        if (!postRequestDto.photos().isEmpty()){
            photoRepository.deleteByPostId(findPost.getId());
        }
        return findPost;
    }

    // TODO : 저장
    public Post save(Post post) {
        return postRepository.save(post);
    }

//    // TODO : 좋아요 시
//    public void addPostLikeToRedis(Post savePost) {
//        ValueOperations<String, String> ops = redisTemplate.opsForValue();
//        String formatPost = String.format(POST_LIKE.name() + savePost.getId());
//        ops.set(formatPost, "0");
//    }

}
