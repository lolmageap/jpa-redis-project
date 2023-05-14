package cherhy.soloProject.domain.post.service;

import cherhy.soloProject.domain.member.entity.Member;
import cherhy.soloProject.domain.member.service.MemberReadService;
import cherhy.soloProject.domain.member.service.MemberWriteService;
import cherhy.soloProject.domain.member.service.MemberWriteServiceTest;
import cherhy.soloProject.domain.photo.entity.Photo;
import cherhy.soloProject.domain.post.dto.request.PostRequestDto;
import cherhy.soloProject.domain.post.entity.Post;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Commit;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
@SpringBootTest
class PostWriteServiceTest {

    @Autowired
    PostWriteService postWriteService;
    @Autowired
    PostReadService postReadService;
    @Autowired
    MemberWriteService memberWriteService;
    @Autowired
    MemberReadService memberReadService;

    @BeforeEach
    @DisplayName("회원 및 게시글 추가")
    void addMemberAndPost(){
        addMember();
        addPost();
    }

    private void addMember(){
        MemberWriteServiceTest memberWriteServiceTest = new MemberWriteServiceTest(memberReadService, memberWriteService);
        memberWriteServiceTest.addMember();
    }

    private Post addPost() {
        Member member = memberReadService.getMember(1L);
        List<String> photos = List.of("one", "two", "three", "four", "five");
        PostRequestDto request = new PostRequestDto("첫번째 게시물입니다", "첫번째 게시물의 내용입니다.", photos);

        Post post = Post.of(request, member);
        return postWriteService.save(post);
    }
    
    @Test
    @DisplayName("게시글 등록")
    void testSavePost(){
        // when
        Post post = postReadService.getPost(1L);

        // then
        Assertions.assertThat(post.getId()).isEqualTo(1L);
        Assertions.assertThat(post.getPhotos().size()).isEqualTo(5);
    }

    @Test
    @DisplayName("게시글 수정")
    void testUpdatePost(){
        // given
        Post post = postReadService.getPost(1L);
        List<String> photos = List.of("1", "2", "3");
        PostRequestDto postRequestDto = new PostRequestDto("바꾼 게시글 입니다.", "내용도 같이 수정했습니다.", photos);

        // when
        Post updatePost = postWriteService.update(postRequestDto, post);

        // then
        Assertions.assertThat(updatePost.getId()).isEqualTo(1L);
        Assertions.assertThat(updatePost.getPhotos().size()).isEqualTo(3);
    }

}