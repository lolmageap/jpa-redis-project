package cherhy.soloProject.domain.post.service;


import cherhy.soloProject.domain.member.entity.Member;
import cherhy.soloProject.domain.photo.entity.Photo;
import cherhy.soloProject.domain.photo.repository.jpa.PhotoRepository;
import cherhy.soloProject.domain.post.dto.request.PostRequestDto;
import cherhy.soloProject.domain.post.entity.Post;
import cherhy.soloProject.domain.post.repository.jpa.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@Transactional
@RequiredArgsConstructor
public class PostWriteService {

    private final PostRepository postRepository;
    private final PostReadService postReadService;
    private final PhotoRepository photoRepository;
    private final StringRedisTemplate redisTemplate;

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
            insertPhoto(postRequestDto, findPost);
        }
        return findPost;
    }

    // TODO : 사진 등록 및 수정
    private void insertPhoto(PostRequestDto postRequestDto, Post addPost) {
        Post findPost = postReadService.getPost(addPost.getId());
        if(!postRequestDto.photos().isEmpty()){ //사진 업로드가 없을 경우
            buildPhoto(postRequestDto, findPost);
        }
    }

    // TODO : 게시글 빌드
    public Post buildPost(PostRequestDto postRequestDto, Member findMember) {
        Post buildPost = Post.builder()
                .member(findMember)
                .title(postRequestDto.title())
                .content(postRequestDto.content())
                .build();

        Post savePost = save(buildPost);
        insertPhoto(postRequestDto, savePost);
        return savePost;
    }

    // TODO : 사진 빌드
    private void buildPhoto(PostRequestDto postRequestDto, Post findPost) {
        for (String photo : postRequestDto.photos()) {
            Photo build = Photo.builder()
                    .post(findPost)
                    .photo(photo)
                    .build();
            photoRepository.save(build);
        }
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
