package cherhy.soloProject.application.domain.post.service;

import cherhy.soloProject.application.domain.member.entity.Member;
import cherhy.soloProject.application.domain.member.repository.jpa.MemberRepository;
import cherhy.soloProject.application.domain.photo.entity.Photo;
import cherhy.soloProject.application.domain.photo.repository.jpa.PhotoRepository;
import cherhy.soloProject.application.domain.post.dto.PostDto;
import cherhy.soloProject.application.domain.post.entity.Post;
import cherhy.soloProject.application.domain.post.repository.jpa.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;
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
            insertPhoto(postDto, addPost);
        }
    }

    public void modifyPost(PostDto postDto, Long postId){ //게시물 저장
        Post findPost = postRepository.findById(postId).orElseThrow(NoSuchElementException::new);
        Post modify = modify(postDto, findPost);
    }

    //여기서부터 다시
    private Post modify(PostDto postDto, Post findPost) {
        if (postDto.content().isEmpty()){
            findPost.validContent(postDto.content());
            findPost.changeContent(postDto.content());
        }
        if (postDto.title().isEmpty()){
            findPost.validTitle(postDto.title());
            findPost.changeTitle(postDto.title());
        }
        if (postDto.photos().isEmpty()){
            insertPhoto(postDto, findPost); // 이거 수정해야댐 아직 미완
        }
        return findPost;
    }

    private void insertPhoto(PostDto postDto, Post addPost) {
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
