package cherhy.soloProject.domain.post.service;


import cherhy.soloProject.domain.photo.entity.Photo;
import cherhy.soloProject.domain.post.dto.request.PostRequestDto;
import cherhy.soloProject.domain.post.entity.Post;
import cherhy.soloProject.domain.post.repository.jpa.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;


@Service
@Transactional
@RequiredArgsConstructor
public class PostWriteService {

    private final PostRepository postRepository;

    public Post save(Post post) {
        return postRepository.save(post);
    }

    public Post update(PostRequestDto postRequestDto, Post post) {
        List<Photo> photos = new ArrayList<>(post.getPhotos());
        photos.clear();
        Post updatePost = post.of(postRequestDto, post.getMember());
        updatePost.addId(post.getId());
        return updatePost;
    }


//    // TODO : 좋아요 시
//    public void addPostLikeToRedis(Post savePost) {
//        ValueOperations<String, String> ops = redisTemplate.opsForValue();
//        String formatPost = String.format(POST_LIKE.name() + savePost.getId());
//        ops.set(formatPost, "0");
//    }

}
