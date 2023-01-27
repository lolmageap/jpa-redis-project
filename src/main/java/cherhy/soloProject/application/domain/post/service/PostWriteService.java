package cherhy.soloProject.application.domain.post.service;

import cherhy.soloProject.application.domain.member.entity.Member;
import cherhy.soloProject.application.domain.member.repository.jpa.MemberRepository;
import cherhy.soloProject.application.domain.photo.entity.Photo;
import cherhy.soloProject.application.domain.photo.repository.jpa.PhotoRepository;
import cherhy.soloProject.application.domain.post.dto.request.PostRequestDto;
import cherhy.soloProject.application.domain.post.entity.Post;
import cherhy.soloProject.application.domain.post.repository.jpa.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;

@Service
@Transactional
@RequiredArgsConstructor
public class PostWriteService {

    private final PostRepository postRepository;
    private final MemberRepository memberRepository;
    private final PhotoRepository photoRepository;


    public String createPost(PostRequestDto postRequestDto){ //게시물 생성
        Member findMember = memberRepository.findById(postRequestDto.memberId())
                .orElseThrow(() -> new NullPointerException("회원정보가 없습니다"));

        Post addPost = buildPost(postRequestDto, findMember);
        postRepository.save(addPost);
        insertPhoto(postRequestDto, addPost);
        return "업로드 성공";
    }

    public String modifyPost(PostRequestDto postRequestDto, Long postId){ //게시물 저장
        Post findPost = postRepository.findById(postId).orElseThrow(
                () -> new NoSuchElementException("게시물이 존재하지 않습니다."));

        modify(postRequestDto, findPost);
        return "변경 성공";
    }


    // ↓ 여기서부턴 비즈니스 로직 ↓
    private Post modify(PostRequestDto postRequestDto, Post findPost) {
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

    private void insertPhoto(PostRequestDto postRequestDto, Post addPost) {

        Post findPost = postRepository.findById(addPost.getId())
                .orElseThrow(() -> new NullPointerException("게시물이 등록되지 않았습니다."));

        if(!postRequestDto.photos().isEmpty()){ //사진 업로드가 없을 경우
            buildPhoto(postRequestDto, findPost);
        }
    }

    private Post buildPost(PostRequestDto postRequestDto, Member findMember) {
        return Post.builder()
                .member(findMember)
                .title(postRequestDto.title())
                .content(postRequestDto.content())
                .build();
    }

    private void buildPhoto(PostRequestDto postRequestDto, Post findPost) {
        for (String photo : postRequestDto.photos()) {
            Photo build = Photo.builder()
                    .post(findPost)
                    .photo(photo)
                    .build();
            photoRepository.save(build);
        }
    }
}
