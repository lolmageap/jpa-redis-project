package cherhy.soloProject.application.service.postService;

import cherhy.soloProject.application.domain.dto.PostDto;
import cherhy.soloProject.application.domain.entity.Member;
import cherhy.soloProject.application.domain.entity.Photo;
import cherhy.soloProject.application.domain.entity.Post;
import cherhy.soloProject.application.repository.jpa.MemberRepository;
import cherhy.soloProject.application.repository.jpa.PhotoRepository;
import cherhy.soloProject.application.repository.jpa.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class PostWriteService {

    private final PostRepository postRepository;
    private final MemberRepository memberRepository;
    private final PhotoRepository photoRepository;


    public void save(PostDto postDto){ //게시물 저장
        Optional<Member> findMember = memberRepository.findById(postDto.memberId());
        if(!findMember.isEmpty()){
            Post addPost = buildPost(postDto, findMember);
            postRepository.save(addPost);
            photoCheck(postDto, addPost);
        }
    }



















    /////module

    private void photoCheck(PostDto postDto, Post addPost) {
        if(!postDto.photos().isEmpty()){
            Post findPost = postRepository.findById(addPost.getId()).get();
            for (String photo : postDto.photos()) {
                Photo build = Photo.builder().post(findPost).photo(photo).build();
                photoRepository.save(build);
            }
        }
    }

    private Post buildPost(PostDto postDto, Optional<Member> findMember) {
        return Post.builder()
                .member(findMember.get())
                .title(postDto.title())
                .content(postDto.content())
                .build();
    }


}
